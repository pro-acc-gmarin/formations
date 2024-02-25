Param(
    [Parameter(Mandatory=$true, Position=1)]
    [string]$CONTEXT_PATH,

    [Parameter(Mandatory=$true, Position=2)]
    [string]$CATALINA_PATH
)

Write-Host "Définition des variables d'environnement pour JPDA"
set JPDA_ADDRESS=8000
set JPDA_TRANSPORT=dt_socket

Write-Host "Changement de répertoire vers : $CATALINA_PATH"
cd $CATALINA_PATH

$START_COMMAND = ".\catalina.bat jpda start"
$STOP_COMMAND = ".\shutdown.bat"

# Exécution de la commande d'arrêt
Write-Host "Arret de Tomcat..."
Invoke-Expression $STOP_COMMAND

Write-Host "Attente de l'arret complet de Tomcat..."
# Boucle pour attendre que le serveur précédent soit correctement arrêté
while (Test-NetConnection -ComputerName localhost -Port 8005 -InformationLevel Quiet) {
    Start-Sleep -s 1
}

Write-Host "Tomcat est arrete. Demarrage de Tomcat avec JPDA..."
# Lancement du nouveau serveur
Invoke-Expression $START_COMMAND

Write-Host "Attente du demarrage complet de Tomcat..."
# Boucle pour attendre que le nouveau serveur soit correctement démarré
while (!(Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet)) {
    Start-Sleep -s 1
}

Write-Host "Tomcat demarre. Ouverture de $CONTEXT_PATH dans le navigateur..."
Start-Process "http://localhost:8080/$CONTEXT_PATH"
