

# Vérifier si Chocolatey est déjà installé
if (!(Get-Command choco -errorAction SilentlyContinue)) {
    # Installer Chocolatey si ce n'est pas déjà fait
    Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
}

# Define the list of choices
$IDE_CHOICES = @("1 - IntelliJ IDEA", "2 - Eclipse", "3 - Visual Studio Code")

Write-Host "IDE Installation Menu"
Write-Host $IDE_CHOICES
# Read the user's choice
$IDE_SELECTED = Read-Host "Please select the IDE you want to install:"

if ($IDE_SELECTED -eq "1") {
  # Check if IntelliJ IDEA is already installed
  if (!(Get-Command "intellij" -ErrorAction SilentlyContinue)) {
    # Install IntelliJ IDEA with Chocolatey
    choco install intellijidea-community --version=2022.2.2 -y
  } else {
    Write-Host "IntelliJ IDEA is already installed."
  }
}

if ($IDE_SELECTED -eq "2") {
  # Check if Eclipse is already installed
  if (!(Get-Command "eclipse" -ErrorAction SilentlyContinue)) {
    # Install Eclipse with Chocolatey
    choco install eclipse --version=4.26
  } else {
    Write-Host "Eclipse is already installed."
  }
}

if ($IDE_SELECTED -eq "3") {
  # Check if Visual Studio Code is already installed
  if (!(Get-Command "code" -ErrorAction SilentlyContinue)) {
    # Install Visual Studio Code with Chocolatey
    choco install vscode --version=1.75.1
  } else {
    Write-Host "Visual Studio Code is already installed."
  }
}

# Define the list of choices
$STACK_CHOICES = @("1 - PHP", "2 - JAVA", "3 - ANGULAR")

Write-Host "Stack Installation Menu"
Write-Host $STACK_CHOICES
# Read the user's choice
$STACK_SELECTED = Read-Host "Please select the stack you want to install:"

if ($STACK_SELECTED -eq "1") {
	# Define the list of choices
	$PHP_INSTALL_CHOICES = @("1 - PHP+MySQL+Apache", "2 - WAMP")
	
	Write-Host "PHP Installation Menu"
	Write-Host $PHP_INSTALL_CHOICES
	# Read the user's choice
	$PHP_INSTALL_SELECTED = Read-Host "Please select the PHP installation you want to :"
	
	if ($PHP_INSTALL_SELECTED -eq "1") {
		# Vérifier si PHP est déjà installé
		if (!(Get-Command php -errorAction SilentlyContinue)) {
			# Installer PHP avec Chocolatey si ce n'est pas déjà fait
			choco install php --version=8.2.2
		}

		# Vérifier si Apache est déjà installé
		if (!(Get-Command httpd -errorAction SilentlyContinue)) {
			# Installer Apache avec Chocolatey si ce n'est pas déjà fait
			choco install apache24
		}

		# Vérifier si MySQL est déjà installé
		if (!(Get-Command mysql -errorAction SilentlyContinue)) {
			# Installer MySQL avec Chocolatey si ce n'est pas déjà fait
			choco install mysql --version=8.0.31
		}
		Write-Host "PHP development environment configured successfully!"
	}
	if ($PHP_INSTALL_SELECTED -eq "2") {
		# Check if Wamp is already installed
		if (!(Get-Command wampmanager -ErrorAction SilentlyContinue)) {
		  # Install Wamp with Chocolatey
		  choco install wamp-server --version=3.3.0 -y
		}
		Write-Host "WAMP configured successfully!"
	}
}

if ($STACK_SELECTED -eq "2") {
	# Verifying if Java is installed
	if (!(Get-Command java -ErrorAction SilentlyContinue)) {
	  # Installing Java JDK with Chocolatey
	  choco install -y oraclejdk --version=17.0.2
	}

	# Verifying if Tomcat is installed
	if (!(Get-Command tomcat -ErrorAction SilentlyContinue)) {
	  # Installing Tomcat with Chocolatey
	  choco install -y Tomcat --version=9.0.70
	}

	# Verifying if Maven is installed
	if (!(Get-Command mvn -ErrorAction SilentlyContinue)) {
	  # Installing Maven with Chocolatey
	  choco install -y maven --version=3.9.0
	}
	Write-Host "Java development environment configured successfully!"
}

if ($STACK_SELECTED -eq "3") {
	# Install Node.js if not installed
	if (!(Get-Command node -ErrorAction SilentlyContinue)) {
	  choco install nodejs --version=19.6.0 -y
	}

	# Install Angular CLI if not installed
	if (!(Get-Command ng -ErrorAction SilentlyContinue)) {
	  npm install -g @angular/cli
	}
	Write-Host "Angular development environment configured successfully!"
}

