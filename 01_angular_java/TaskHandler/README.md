### Stack technique

- [Java](https://www.java.com/fr/) - language pour le backend.
- [Tomcat](https://tomcat.apache.org/) - serveur applicatif et moteur de servlets.
- [Maven](https://maven.apache.org/) - outil d'automatisation de gestion de dépendances.

### 00 - Initialisation du projet

---
#### Sécurité

-   Faites un filtre [CORS](https://www.baeldung.com/cs/cors-preflight-requests) en tête de chaîne.

#### Initialisation de la base de données

- **Resource User** : un UUID, un prénom, un nom de famille, un mail, un booléen d'approbation, un role.
- **Resource Task** : un UUID, l'UUID de l'auteur, un titre, une description, une date de création, une liste de tâches liées.
- **Resource Board** : un UUID, l'UUID du propriétaire, un titre, une description, une liste de tâches liées.

#### Déploiement sur un tomcat local

- Automatiser le déploiement du war dans un tomcat en local. (powershell ou plugin maven pour les versions tomcat7&8)
- Mettre en place le remote debugging entre votre IDE et votre tomcat.

#### Hexagone

- On va faire en sorte d'appliquer le [Domain Driven Development](https://www.baeldung.com/hexagonal-architecture-ddd-spring) afin de découpler totalement les objets métiers.
    - Pour chaque ressource créer 3 packages : application, domain et infrastructure
        - **application** : va contenir les controllers
        - **domain** : va contenir l'objet métier, et les différents ports
            - api : une interface de services et une implémentation de ce service qui utilisera l'interface de la spi
            - spi : une interface pour la persistence.
        - **infrastructure** : va contenir la logique de persistence


#### Controllers

- Mettre en place une servlet par resource et un affichage standard pour chaque méthode HTTP.
- Mettre en place une servlet faisant office de [Front controller pattern](https://www.baeldung.com/java-front-controller-pattern)

> Utiliser l'annotation @WebServlet ou la configuration dans le fichier web.xml afin de spécifier les url-patterns de chaque servlet.
> 
> Note : Pour que le "front controller" soit le seul à capter les URLs, il faut déclarer les autres servlets dans le web.xml sans mapping.
> Le "front controller" redirigera la requête en utilisant leurs noms.
> 
> La servlet "front controller" devra être chargée en première et donc il faudra spécifier le paramètre "loadOnStartup".

- Créer une collection Postman pour requêter chaque méthode HTTP de chaque servlet.


### 01 - CRUD

---

#### Connexion à la base de donnée

##### Tomcat

-   [**Connection à la base de donnée par DataSource**](https://www.codejava.net/servers/tomcat/configuring-jndi-datasource-for-database-connection-pooling-in-tomcat) :
    -   Définir une DataSource pour tomcat dans le server.xml
    ~~~ xml  
    <Resource name="jdbc/<global_resource_name>" 
        auth="Container" 
        type="javax.sql.DataSource"   
        driverClassName="com.mysql.jdbc.Driver"  
        url="jdbc:mysql://localhost:3306/<database_name>"
        username="<username>"  
        password="<password>"
        maxTotal="100"
        maxIdle="30"
        maxWaitMillis="10000" />
    ~~~ 
    - Définir une resource surveillée dans le context.xml de tomcat:
    ~~~ xml  
    <WatchedResource>META-INF/context.xml</WatchedResource>
    ~~~ 
    - Créer un fichier context.xml que vos placerez dans le dossier "webapps/META-INF" de votre projet :
    ~~~ xml  
    <?xml version="1.0" encoding="UTF-8"?>
    <Context>
      <ResourceLink name="jdbc/<context_resource_name>"
                  global="jdbc/<global_resource_name>"
                  auth="Container"
                  type="javax.sql.DataSource" />
    </Context>
    ~~~ 

##### Infrastructure

###### Repository

- Pour chaque resource, créer un package "repository" dans l'infrastructure.
- Dans ce package "repository", créer un package "spi" qui contiendra l'interface de votre repository.
- Finalement créer l'implémentation de cette spi, et en propriétée de classe déclarer un objet DataSource qui sera alimenté par le constructeur du repository :
```
Context ctx = new InitialContext();
Context initCtx  = (Context) ctx.lookup("java:/comp/env");
this.dataSource = (DataSource) initCtx.lookup("jdbc/<context_resource_name>");
```

---

#### Persistence

> Lombok : Utiliser lombok pour faciliter la génération des différentes méthodes de classes. (Constructeur, Setter/Getter, Hash, ToString, etc..)
>
> MapStruct : Utiliser la librairie MapStruct pour la création des mappers.
> 
> Intellij : Pour activer la génération des annotations : aller dans Settings > Annotation Processor et activer l'option.

- Ajout des dépendances dans le pom.xml:
~~~xml 
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.26</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.mapstruct</groupId>
      <artifactId>mapstruct</artifactId>
      <version>${org.mapstruct.version}</version>
    </dependency>
~~~
- Ajout dans la configuration du compiler maven :
~~~xml  
<annotationProcessorPaths>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${org.projectlombok.version}</version>
    </path>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${org.mapstruct.version}</version>
    </path>
</annotationProcessorPaths>
~~~
     
##### Domain

- Pour chaque resource créer un package "data" et "ports" dans le domain.
- Dans le package "data", créer votre objet métier en adéquation avec votre base de données.
- Dans le package "ports", créer deux packages "api" et "spi" :
  - Dans le premier, créer une interface d'un service CRUD.
  - Dans le second, créer une interface d'un repository CRUD. Il servira à l'infrastructure pour l'implémentation de son DAO.
  - Finalement créer l'implémentation du service CRUD qui fait appel à l'interface du repository CRUD.

##### Infrastructure

###### Entity

- Pour chaque resource, créer un package "entity" dans l'infrastructure.
- Dans ce package "entity", créer un pojo à l'identique de votre domaine.
- Cette classe vous servira de DTO entre le domain et l'infrastructure.

###### Mapper

- Pour chaque resource, créer un package "mapper" dans l'infrastructure.
- Dans ce package "mapper", créer une classe annotée @Mapper (MapStruct) qui définit:
  - une méthode pour passer de l'entité au domain.
  - une méthode pour passer du domain à l'entité.
  - une méthode pour passer d'une liste d'entités à une liste de domain.

###### [DAO pattern](https://www.baeldung.com/java-dao-pattern)

>  Securité : [Preventing SQL Injection Attack With Java Prepared Statement](https://medium.com/swlh/preventing-sql-injection-attack-with-java-prepared-statement-259611281e4d)

- Compléter les méthodes CRUD des repository en utilisant des requêtes paramétrées :
  - Les DAOs ne manipulent que des entités.
  - Pour rendre la maintenance plus simple des requêtes SQL on peut créer une annotation de méthode personnalisée :
    - Pour récupérer la valeur de l'annotation on utilisera la réflection java :
    ```
    String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    Method currentMethod = enclosingClass.getDeclaredMethod(methodName);
    <custom_annotation_name> annotation = currentMethod.getAnnotation(<custom_annotation_name>.class);
    ```
    - Créer un package "utils" (si ce n'est pas déjà fait), puis un package "annotations". Faites votre implémentation dans ce dernier.

###### [Repository pattern](https://www.baeldung.com/java-dao-vs-repository)

- Pour chaque resource, créer un package "adapter" dans l'infrastructure.
- Dans ce package "adapter", créer une implémentation de la SPI du domain pour servir de repository qui utilisera le DAO et le mapper précédemment créés.

##### Application

###### [DTO pattern](https://www.baeldung.com/java-dto-pattern)

> Sécurité : [How to use Java DTOs to stay secure](https://snyk.io/blog/how-to-use-java-dtos/)

- Pour chaque resource, créer un package "dto" dans l'application.
- Dans ce package "dto", créer 
  - un pojo "InDto" pour la récupération des payload de requêtes ne contenant pas son identifiant technique.
  - un pojo "OutDto" pour le renvoie des données dans la réponse ne contenant pas les données sensibles comme les mots de passe par exemple.
- Ces classes vous serviront de DTO entre l'application et le service du domain.

### 02 - Maintenabilité

#### Liquibase
