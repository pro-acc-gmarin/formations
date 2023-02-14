## Stack technique

Pour pouvoir faire une application basique voici la stack technique nécessaire :

- [PHP](https://www.php.net/) - language de script pour le backend.
- [HTML](https://www.w3schools.com/html/default.asp) - language de balise pour structurer les pages web.
- [CSS](https://www.w3schools.com/css/default.asp) - language pour gérer le style des éléments HTML.
- [JS](https://www.w3schools.com/js/default.asp) - language de script pour gérer le dynamisme des pages web.
- [VueJS](https://vuejs.org/) - framework javascript
- [Bootstrap](https://getbootstrap.com/docs/5.1/getting-started/introduction/) - framework css

## Gestionnaire de tâches

Une fois que vous avez un environnement de développement fonctionnel, vous allez créer une application de gestion de tâches basique.

### Base de données

-   **Créez un modèle de base de donnée pour gérer des tablesaux, des tâches et des utilisateurs.**
    -   Sachant qu'une tâche a un titre, une description, une date d'échéance, un status et peut être liée à d'autres tâches.
    -   Sachant qu'un tableau peut avoir plusieurs tâches et un titre.
    -   Sachant qu'un utilisateur peut avoir un nom, prénom, un email, un mot de passe et un rôle (Standard, Administrateur).
-   **Fournissez un fichier .sql pour pouvoir l'initialiser.**

### Interface utilisateur

-   **Créez un formulaire de connexion**.
-   **Pour les utilisateurs** :
    - Créez un formulaire de création et de mise à jour d'un profil utilisateur.
    - Créez une page d'affichage du profil utilisateur avec la posibilité de le mettre à jour.
- **Pour les tableaux** :
    - Créez un formulaire de création et mise à jour de tâches.
    - Créez un menu de choix des tableaux disponibles.
    - Créez une interface d'affichage des différentes tâches. (Draft, TODO, Blocked, OnGoing, Complete) 
- **Pour les tâches** :
    - Créez un formulaire de création et mise à jour de tâches.
    - Implémentez la possibilité de supprimer une tâche.
- **Pour gérer les utilisateurs** :
    - Créez une interface de modification des utilisateurs (y compris mot de passe) avec la possibilité de valider leurs comptes.
    - Cette interface ne sera utilisable que par un utilisateur administrateur.
- **Validation des entrées utilisateurs** :
    -   Contrôlez la validité des entrées de l'utilisateurs et l'affichage de messages d'erreurs appropriés.  

### Interface utilisateur - Framework

-   Intégrez et utilisez **Bootstrap** pour gérer le style de votre interface.
-   Intégrez et utilisez **VueJs** pour découper votre interface en Vue.
    -   Le but ici c'est de pouvoir résumer un tableau à une balise <tableau id="##"></tableau>. (d'autres paramètres peuvent être nécessaires)
    -   Mettez en place le plus de tests possibles.

### Côté serveur

-   **Base de donnée** :
    - Créez un script dédié à la connexion.
    - Créez un script PHP par actions CRUD pour chaque table de la base.
        > Sécurité
        > N'utilisez que des requêtes préparées pour éviter les injections SQL.
        > Echappez et mettez en place une validation des entrées côté UI pour éviter les failles XSS.
        > Stockez les mots de passes de manière sécurisée (chiffrés) pour évitez les attaques par dictionnaire.
-   **Authentification** :
    -   Vérifier la validité de l'utilisateur :
        -   la validité de son compte
        -   son login et mot de passe
-   **Permission** :
    -   Selon le rôle de l'utilisateur connecté affichez correctement les différentes fonctionnalités utilisables.
- **Lien avec l'UI** :
    -   Faites en sorte que les différents formulaires fassent les bonnes requêtes HTTP vers les bons scripts PHP. 