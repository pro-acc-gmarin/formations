## Stack technique

- [Angular](https://angular.io/) - framework javascript.
- [Bootstrap](https://getbootstrap.com/docs/5.1/getting-started/introduction/) - framework css

## Frontend

-   **Migration vers Angular**
    -   Faites en sorte d'avoir un [environnement de développement](https://medium.com/geekculture/angular-a-beginners-guide-f8cd9ee7325) viable pour Angular.
    -   **Modules**
        -   Chaque page peux être considérée comme un [module Angular](https://medium.com/@josce.james7/an-introduction-to-angular-modules-c26d441e42fa), faites le découpage.
    -   **Composants**
        -   Chaque partie de l'interface peux être considérée comme un [composant](https://medium.com/yavar/angular-components-1e940f3f889e), faites le découpage.
            -   On se basera sur le principe de [smart et dumb component](https://medium.com/generic-ui/an-enterprise-approach-to-the-smart-and-dumb-components-pattern-in-angular-37b6000f91de) pour la structure des modules.
        -   Pour le transit d'informations entre les composant, il faut utiliser [@Input et @Output](https://medium.com/@jaydeepvpatil225/component-communication-in-angular-34a4c2ca93d4).
    -   **Services**   
        -   Toutes les requêtes HTTPs précédemment établies devront être gérer par des [services](https://medium.com/@redin.gaetan/angular-for-everyone-chapter-4-services-61beaa998a86) et traités au niveau du smart component.
    -   **Authentification**
        -   Mettre en place un [AuthGuard](https://medium.com/@ryanchenkie_40935/angular-authentication-using-route-guards-bf7a4ca13ae3) pour gérer les accès aux différentes routes.   