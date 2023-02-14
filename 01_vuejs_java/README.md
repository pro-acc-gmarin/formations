## Stack technique

- [Java](https://www.java.com/fr/) - language pour le backend.
- [Tomcat](https://tomcat.apache.org/) - serveur applicatif et moteur de servlets.
- [Maven](https://maven.apache.org/) - outil d'automatisation de gestion de dépendances.

## Côté serveur

### Déploiement sur un tomcat local

- Automatiser le déploiement du war dans un tomcat en local. (powershell ou plugin maven pour les versions tomcat7&8)
- Mettre en place le remote debugging entre votre IDE et votre tomcat.

### Hexagone

- On va faire en sorte d'appliquer le [Domain Driven Development](https://www.baeldung.com/hexagonal-architecture-ddd-spring) afin de découpler totalement les objets métiers.
	- Pour chaque ressource créer 3 packages : application, domain et infrastructure
		- application : va contenir les controllers
		- domain : va contenir l'objet métier, et les différents ports
			- api : une interface de services et une implémentation de ce service qui utilisera l'interface de la spi
			- spi : une interface pour la persistence.
		- infrastructure : va contenir la logique de persistence
			
	
### Controllers

- Mettre en place une servlet par resource et un affichage standard pour chaque méthode HTTP.
- Mettre en place une servlet faisant office de [Front controller pattern](https://www.baeldung.com/java-front-controller-pattern)

> Utiliser l'annotation @WebServlet ou la configuration dans le fichier web.xml afin de spécifier les url-patterns de chaque servlet.
> La servlet "front controller" devra être chargée en première et donc il faudra spécifier le paramètre "loadOnStartup".

- Créer une collection Postman pour requêter chaque méthode HTTP de chaque servlet.

- Pour gérer les payloads des requêtes et la construction de vues des objets métiers on mettra en place le [DTO pattern](https://www.baeldung.com/java-dto-pattern) en utilisant une librairie de mapping comme [MapStruct](https://www.baeldung.com/mapstruct).

### Connexion à la base de donnée

-   [**Connection à la base de donnée par DataSource**](https://www.codejava.net/servers/tomcat/configuring-jndi-datasource-for-database-connection-pooling-in-tomcat) :
    -   Définir une DataSource pour tomcat dans le context.xml.
    -   Définir la référence à cette resource dans le web.xml.
	
### Liquibase

-   Pour gérer les modifications de la base de donnée, mettez en place [liquibase](https://docs.liquibase.com/tools-integrations/maven/workflows/creating-liquibase-projects-with-maven-postgresql.html) pour gérer les mises à jour et rollback.  

### Repository

- Dans le package infrastructure, créer une implémentation de la SPI pour servir de [DAO](https://www.baeldung.com/java-dao-pattern)
- Utiliser l'objet de connection de votre DataSource par injection via @Resource.
- Gérer les transactions par la désactivation de l'autoCommit pour le faire à point nommé et la réalisation d'un rollback en cas d'erreur.

### Authentification

-   Mettre en place par le biais du [Chain of Responsability pattern](https://www.baeldung.com/chain-of-responsibility-pattern) deux filtres :
    -   AuthenticationFilter : qui va vérifier si l'utilisateur est valide.
    -   AuthorizationFilter : qui va vérifier si l'utilisateur a accès à ce qu'il demande.
    -   On passera les informations dans la session pour le moment.
- Stateless :
    -   Mise en place d'une authentification par le biais d'un [token JWT](https://medium.com/@deshankalupahana/verify-jwt-token-with-servlet-filter-5648cd57ab26):
        -   On vérifie l'authenticité du token. 
        -   On ne passe plus par la session, mais incorporant des claims spécifiques au sein du token qui passera par la requête HTTP.
    
### Sécurité
-   Faites un filtre [CORS](https://www.baeldung.com/cs/cors-preflight-requests) en tête de chaîne. 
-   Passer [Tomcat en https](http://www.trialdatasolutions.com/tds/howto/selfsignedcertificate.jsp) en utilisant un certificat auto signé.
    -   Optionel : voir du côté de [Caddy](https://caddyserver.com/docs/) pour le [https](https://medium.com/@devahmedshendy/traditional-setup-run-local-development-over-https-using-caddy-964884e75232)  

### [**Négociation de contenu**](https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation)

-   Mettre en place un filtre de négociation de contenu en se basant sur le header Accept.
	
### [**Gestion du cache**](https://medium.com/@s.ramanathan/browser-caching-719ccc484c73)

-   Mettre en place un filtre qui va contrôlé les headers Last-Modified et If-Modified-Since.
-   Mettre en place un eTag et le header If-None-Match en remplacement du point précédent.