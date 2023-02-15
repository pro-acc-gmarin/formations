Param(
  [Parameter(Mandatory=$true, Position=0)]
  [string]$TOMCAT_SERVICE_NAME,
  
  [Parameter(Mandatory=$true, Position=1)]
  [string]$CONTEXT_PATH,
  
  [Parameter(Mandatory=$true, Position=2)]
  [string]$CATALINA_PATH
)

set JPDA_ADDRESS=8000
set JPDA_TRANSPORT=dt_socket
# Commande pour lancer Tomcat avec JPDA
cd $CATALINA_PATH
$START_COMMAND = ".\catalina.bat jpda start"
$STOP_COMMAND = ".\shutdown.bat"

# Ajout des options JPDA à la commande

# Exécution de la commande
Invoke-Expression $STOP_COMMAND
# Boucle pour attendre que le serveur précédent soit correctement arrêté
while (Test-NetConnection -ComputerName localhost -Port 8005 -InformationLevel Quiet) {
    Start-Sleep -s 1
}

# Lancement du nouveau serveur
Invoke-Expression $START_COMMAND

# Boucle pour attendre que le nouveau serveur soit correctement démarré
while (!(Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet)) {
    Start-Sleep -s 1
}

Start-Process "http://localhost:8080/$CONTEXT_PATH"