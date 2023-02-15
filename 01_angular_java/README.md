## Stack technique

- [Java](https://www.java.com/fr/) - language pour le backend.
- [Tomcat](https://tomcat.apache.org/) - serveur applicatif et moteur de servlets.
- [Maven](https://maven.apache.org/) - outil d'automatisation de gestion de dépendances.

## Côté serveur

-   [**Connection à la base de donnée par DataSource**](https://www.codejava.net/servers/tomcat/configuring-jndi-datasource-for-database-connection-pooling-in-tomcat) :
    -   Définir une DataSource pour tomcat dans le context.xml.
    -   Définir la référence à cette resource dans le web.xml.
-   **Liquibase**
    -   Pour gérer les modifications de la base de donnée, mettez en place [liquibase](https://docs.liquibase.com/tools-integrations/maven/workflows/creating-liquibase-projects-with-maven-postgresql.html) pour gérer les mises à jour et rollback.   
-   **Controller** :
    -   Mettre en place pour chaque resource les servlets et le traitement des bonnes méthodes HTTPs :
        -   Pour venir remplacer les scripts CRUD du PHP.
    - [Front controller pattern](https://www.baeldung.com/java-front-controller-pattern) à mettre en place pour n'avoir qu'un seul point d'entrée pour le frontend.
    - [DTO pattern](https://www.baeldung.com/java-dto-pattern) à mettre en place afin de renvoyer et de construire des vues de notre objet métier (domaine) sans affecter ce dernier :
        -   Il faudra pouvoir mapper les différents objets (DTO) via l'utilisation de [MapStruct](https://www.baeldung.com/mapstruct).  
-   **Business** :
    -   Les business doivent être des classes singletons (voir [Singleton pattern](https://www.baeldung.com/java-singleton))
    -   Les business gèrent le code métier et font le lien avec les repository par des classes [DAOs](https://www.baeldung.com/java-dao-pattern) qui les utilise.
-   **Repository** :
    -   Classe singleton qui implémente toutes les opérations côté base de donnée par des requêtes préparées.
    -   Récupérer par injection la DataSource via l'annotation @Resource dans vos services.
    -   Utiliser l'objet de connection de votre DataSource.
    -   Gérer les transactions par la désactivation de l'autoCommit pour le faire à point nommé et la réalisation d'un rollback en cas d'erreur.
-   **Authentification** :
    -   Mettre en place par le biais du [Chain of Responsability pattern](https://www.baeldung.com/chain-of-responsibility-pattern) deux filtres :
        -   AuthenticationFilter : qui va vérifier si l'utilisateur est valide.
        -   AuthorizationFilter : qui va vérifier si l'utilisateur a accès à ce qu'il demande.
        -   On passera les informations dans la session pour le moment.
    - Stateless :
        -   Mise en place d'une authentification par le biais d'un [token JWT](https://medium.com/@deshankalupahana/verify-jwt-token-with-servlet-filter-5648cd57ab26):
            -   On vérifie l'authenticité du token. 
            -   On ne passe plus par la session, mais incorporant des claims spécifiques au sein du token qui passera par la requête HTTP.
-   **Sécurité**:
    -   Faites un filtre [CORS](https://www.baeldung.com/cs/cors-preflight-requests) en tête de chaîne. 
    -   Passer [Tomcat en https](http://www.trialdatasolutions.com/tds/howto/selfsignedcertificate.jsp) en utilisant un certificat auto signé.
        -   Optionel : voir du côté de [Caddy](https://caddyserver.com/docs/) pour le [https](https://medium.com/@devahmedshendy/traditional-setup-run-local-development-over-https-using-caddy-964884e75232)  
-   [**Négociation de contenu**](https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation):
    -   Mettre en place un filtre de négociation de contenu en se basant sur le header Accept.
-   [**Gestion du cache**](https://medium.com/@s.ramanathan/browser-caching-719ccc484c73)
    -   Mettre en place un filtre qui va contrôlé les headers Last-Modified et If-Modified-Since.
    -   Mettre en place un eTag et le header If-None-Match en remplacement du point précédent.