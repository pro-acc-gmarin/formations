Param(
  [Parameter(Mandatory=$true, Position=0)]
  [string]$TOMCAT_WEBAPP_PATH,

  [Parameter(Mandatory=$true, Position=1)]
  [string]$WAR_FILE_PATH
)

Write-Host "Deploying application to Tomcat ..."
$WAR_FILE_NAME = Split-Path -Path $WAR_FILE_PATH -Leaf
$DESTINATION_PATH = Join-Path $TOMCAT_WEBAPP_PATH $WAR_FILE_NAME
Write-Host "Copy $WAR_FILE_PATH to $DESTINATION_PATH"
Copy-Item $WAR_FILE_PATH $DESTINATION_PATH -Force

Write-Host "Done."