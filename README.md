# Formation
## Consultant full-stack

## Conception d'application web - Introduction

### Installation de l'environnement

L'installation se fera par le biais du gestionnaire de paquet Windows : [chocolatey](https://chocolatey.org/), pour des raisons d'automatisation.
Vous pouvez exécuter en mode administrateur le script se situant sur le dépôt GIT : 
> script/00_script_installation_basic_stack.ps1 

Si vous préférez exécuter chaque partie du script indépendamment :
#### Chocolatey installation
```sh
# Check if Chocolatey is already installed
if (!(Get-Command choco -errorAction SilentlyContinue)) {
    # Install Chocolatey
    Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
}
```
#### PHP installation
```sh
# Check if PHP is already installed
if (!(Get-Command php -errorAction SilentlyContinue)) {
    # Install PHP with Chocolatey
    choco install php --version 7.4.12
}
```
#### Apache installation
```sh
# Check if Apache is already installed
if (!(Get-Command httpd -errorAction SilentlyContinue)) {
    # Install Apache with Chocolatey
    choco install apache24
}
```
#### MySQL installation
```sh
# Check if MySQL is already installed
if (!(Get-Command mysql -errorAction SilentlyContinue)) {
    # Install Wamp with Chocolatey
    choco install mysql --version 8.0.24
}
```
#### WAMP installation (en alternative à PHP+Apache+MySQL)
```sh
# Check if Wamp is already installed
if (!(Get-Command wampmanager -ErrorAction SilentlyContinue)) {
	# Install Wamp with Chocolatey
	choco install wamp-server --version=3.3.0 -y
}
```
#### Eclipse installation
```sh
# Check if Eclipse is already installed
  if (!(Get-Command "eclipse" -ErrorAction SilentlyContinue)) {
    # Install Eclipse with Chocolatey
    choco install eclipse --version=4.26
  } else {
    Write-Host "Eclipse is already installed."
  }
```
#### IntelliJ IDEA installation
```sh
# Check if IntelliJ IDEA is already installed
  if (!(Get-Command "intellij" -ErrorAction SilentlyContinue)) {
    # Install IntelliJ IDEA with Chocolatey
    choco install intellijidea-community --version=2022.2.2 -y
  } else {
    Write-Host "IntelliJ IDEA is already installed."
  }
```
#### Visual Studio Code installation
```sh
# Check if Visual Studio Code is already installed
  if (!(Get-Command "code" -ErrorAction SilentlyContinue)) {
    # Install Visual Studio Code with Chocolatey
    choco install vscode --version=1.75.1
  } else {
    Write-Host "Visual Studio Code is already installed."
  }
```
#### DBeaver installation
```sh
# Check if DBeaver is already installed
if (!(Get-Command "dbeaver" -ErrorAction SilentlyContinue)) {
    # Install DBeaver with Chocolatey
    choco install dbeaver --version=22.3.2 --confirm
}
```
#### JDK installation
```sh
# Verifying if Java is installed
	if (!(Get-Command java -ErrorAction SilentlyContinue)) {
	  # Installing Java JDK with Chocolatey
	  choco install -y oraclejdk --version=17.0.2
	}
```
#### Tomcat installation
```sh
	# Verifying if Tomcat is installed
	if (!(Get-Command tomcat -ErrorAction SilentlyContinue)) {
	  # Installing Tomcat with Chocolatey
	  choco install -y Tomcat --version=9.0.70
	}
```
#### Maven installation
```sh
	# Verifying if Maven is installed
	if (!(Get-Command mvn -ErrorAction SilentlyContinue)) {
	  # Installing Maven with Chocolatey
	  choco install -y maven --version=3.9.0
	}
```
### Configuration
#### DBeaver configuration avec MySQL
Une fois DBeaver installé, ouvrez l'application et cliquez sur le bouton "Nouvelle connexion" dans le coin supérieur gauche de l'interface.

Sélectionnez "MySQL" dans la liste déroulante de types de bases de données.

Entrez les informations de connexion à votre base de données MySQL. Vous pouvez trouver l'hôte et le port de votre base de données en ouvrant le fichier de configuration my.ini ou en utilisant la commande suivante :
```sh
mysql -h [host] -P [port] -u [username] -p
```
Cliquez sur le bouton "Tester la connexion" pour vous assurer que les informations sont correctes.

#### PHP + Apache + MySQL configuration
Une fois ces paquets installés, vous pouvez configurer Apache pour utiliser PHP en modifiant le fichier de configuration d'Apache pour inclure les lignes suivantes :
```sh
LoadModule php7_module "c:/php/php7apache2_4.dll"
AddHandler application/x-httpd-php .php
PHPIniDir "C:/php"
```
Il vous suffit de redémarrer Apache pour que les modifications prennent effet.

Pour configurer MySQL, vous pouvez utiliser la commande suivante :
```sh
mysql_install_db
```
Ensuite, vous pouvez démarrer le service MySQL avec la commande suivante :
```sh
net start mysql
```
Vous pouvez vérifier que MySQL est en cours d'exécution en utilisant la commande suivante :
```
mysql -u root
```



