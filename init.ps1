Param(
    [switch]$h,
    [switch]$o
)

Write-Host "Installing Travel Agency Demo"

# wipe screen
Clear-Host

$PROJECT_HOME = $PSScriptRoot
$DEMO="Travel Agency Demo"
$AUTHORS="Nirja Patel, Shepherd Chengeta,"
$AUTHORS2="Andrew Block, Eric D. Schabell, Duncan Doyle"
$PROJECT="git@github.com:jbossdemocentral/bpms-travel-agency-demo.git"
$PRODUCT="JBoss BPM Suite"
$VERSION="6.3"
$JBOSS_HOME="$PROJECT_HOME\target\jboss-bpmsuite-$VERSION"
$SERVER_DIR="$JBOSS_HOME\standalone\deployments\"
$SERVER_CONF="$JBOSS_HOME\standalone\configuration\"
$SERVER_BIN="$JBOSS_HOME\bin"
$SRC_DIR="$PROJECT_HOME\installs"
$SUPPORT_DIR="$PROJECT_HOME\support"
$PRJ_DIR="$PROJECT_HOME\projects"
$BPMS="jboss-bpmsuite-6.3.0.GA-installer.jar"
$EAP="jboss-eap-6.4.0-installer.jar"
$EAP_PATCH="jboss-eap-6.4.7-patch.zip"
$PROJECT_GIT_REPO="https://github.com/jbossdemocentral/bpms-travel0-agency-demo-repo"
$PROJECT_GIT_DIR="$PROJECT_HOME\support\demo_project_git"
$OFFLINE_MODE="false"

If ($h) {
	Write-Host "Usage: init.ps1 [args...]"
    Write-Host "where args include:"
    Write-Host "    -o              run this script in offline mode. The project's Git repo will not be downloaded. Instead a cached version will be used if available."
    Write-Host "    -h              prints this help."
	exit
}

Write-Host "#################################################################"
Write-Host "##                                                             ##"
Write-Host "##  Setting up the %DEMO%                          ##"
Write-Host "##                                                             ##"
Write-Host "##                                                             ##"
Write-Host "##     ####  ####   #   #      ### #   # ##### ##### #####     ##"
Write-Host "##     #   # #   # # # # #    #    #   #   #     #   #         ##"
Write-Host "##     ####  ####  #  #  #     ##  #   #   #     #   ###       ##"
Write-Host "##     #   # #     #     #       # #   #   #     #   #         ##"
Write-Host "##     ####  #     #     #    ###  ##### #####   #   #####     ##"
Write-Host "##                                                             ##"
Write-Host "##                                                             ##"
Write-Host "##  brought to you by,                                         ##"
Write-Host "##                     %AUTHORS%           ##"
Write-Host "##                       %AUTHORS2%          ##"
Write-Host "##                                                             ##"
Write-Host "##  %PROJECT%##"
Write-Host "##                                                             ##"
Write-Host "#################################################################`n"


#Test whether Maven is available.
if ((Get-Command "mvn" -ErrorAction SilentlyContinue) -eq $null)
{
   Write-Host "Maven is required but not installed yet... aborting.`n"
   exit
}

If (Test-Path "$SRC_DIR\$EAP") {
	Write-Host "Product sources are present...`n"
} Else {
	Write-Host "Need to download $EAP package from the Customer Support Portal"
	Write-Host "and place it in the $SRC_DIR directory to proceed...`n"
	exit
}

If (Test-Path "$SRC_DIR\$EAP_PATCH") {
	Write-Host "Product patches are present...`n"
} Else {
	Write-Host "Need to download $EAP_PATCH package from the Customer Support Portal"
	Write-Host "and place it in the $SRC_DIR directory to proceed...`n"
	exit
}

If (Test-Path "$SRC_DIR\$BPMS") {
	Write-Host "Product sources are present...`n"
} Else {
	Write-Host "Need to download $BPMS package from the Customer Support Portal"
	Write-Host "and place it in the $SRC_DIR directory to proceed...`n"
	exit
}

#Test whether Java is available.
if ((Get-Command "java.exe" -ErrorAction SilentlyContinue) -eq $null)
{
   Write-Host "The 'java' command is required but not available. Please install Java and add it to your PATH.`n"
   exit
}

if ((Get-Command "javac.exe" -ErrorAction SilentlyContinue) -eq $null)
{
   Write-Host "The 'javac' command is required but not available. Please install Java and add it to your PATH.`n"
   exit
}

# Remove the old installation if it exists
If (Test-Path "$JBOSS_HOME") {
	Write-Host "Removing existing installation.`n"
	# The "\\?\" prefix is a trick to get around the 256 path-length limit in Windows.
	# If we don't do this, the Remove-Item command fails when it tries to delete files with a name longer than 256 characters.
	Remove-Item "\\?\$JBOSS_HOME" -Force -Recurse
	# The command above does not seem to work reliably, so trying this alternative instead.
	#Get-ChildItem -Path "$JBOSS_HOME\\*" -Recurse | Remove-Item -Force -Recurse
	#Remove-Item $JBOSS_HOME
}

#Run installers.
Write-Host "EAP installer running now...`n"
$argList = "-jar $SRC_DIR\$EAP $SUPPORT_DIR\installation-eap -variablefile $SUPPORT_DIR\installation-eap.variables"
$process = (Start-Process -FilePath java.exe -ArgumentList $argList -Wait -PassThru)
Write-Host "Process finished with return code: " $process.ExitCode
Write-Host "`n"

If ($process.ExitCode -ne 0) {
	Write-Error "Error installing JBoss EAP."
	exit
}

Write-Host "Applying JBoss EAP patch now...`n"
$argList = '--command="patch apply ' + "$SRC_DIR\$EAP_PATCH" + ' --override-all"'
$patchProcess = (Start-Process -FilePath "$JBOSS_HOME\bin\jboss-cli.bat" -ArgumentList $argList -Wait -PassThru)
Write-Host "Process finished with return code: " $patchProcess.ExitCode
Write-Host ""

If ($patchProcess.ExitCode -ne 0) {
	Write-Error "Error occurred during JBoss EAP patch installation."
	exit
}

Write-Host "JBoss EAP patch applied succesfully!`n"

Write-Host "JBoss BPM Suite installer running now..."
$argList = "-jar $SRC_DIR\$BPMS $SUPPORT_DIR\installation-bpms -variablefile $SUPPORT_DIR\installation-bpms.variables"
$bpmsProcess = (Start-Process -FilePath java.exe -ArgumentList $argList -Wait -PassThru)
Write-Host "Process finished with return code: " $bpmsProcess.ExitCode
Write-Host ""

If ($bpmsProcess.ExitCode -ne 0) {
	Write-Error "Error occurred during JBoss BPM Suite installation."
	exit
}

Write-Host "- enabling demo accounts role setup in application-roles.properties file...`n"
Copy-Item "$SUPPORT_DIR\application-roles.properties" $SERVER_CONF -force

Write-Host "- Setting up demo projects...`n"
New-Item -ItemType directory -Path "$SERVER_BIN\.niogit\" | Out-Null

################################# Begin setup demo projects ##########################################

Write-Host "- Setting up demo projects...`n"
New-Item -ItemType directory -Path "$SERVER_BIN\.niogit\" | Out-Null
Copy-Item "$SUPPORT_DIR\bpm-suite-demo-niogit\*" "$SERVER_BIN\.niogit\" -force -recurse

If (! $o) {
  # Not in offline mode, so downloading the latest repo. We first download the repo in a temp dir and we only delete the old, cached repo, when the download is succesful.
  Write-Host "  - cloning the project's Git repo from: $PROJECT_GIT_REPO`n"
  If (Test-Path "$PROJECT_HOME\target\temp") {
	Remove-Item "$PROJECT_HOME\target\temp" -Force -Recurse
  }
  $argList = "clone --bare $PROJECT_GIT_REPO $PROJECT_HOME\target\temp\brms-coolstore-repo.git"
  $gitProcess = (Start-Process -FilePath "git" -ArgumentList $argList -Wait -PassThru -NoNewWindow)
  If ($gitProcess.ExitCode -ne 0) {
		Write-Host "Error cloning the project's Git repo. If there is no Internet connection available, please run this script in 'offline-mode' ('-o') to use a previously downloaded and cached version of the project's Git repo... Aborting"
		exit 1
  }
  Write-Host ""
  Write-Host "  - replacing cached project git repo: $PROJECT_GIT_DIR/brms-coolstore-repo.git`n"
  If (Test-Path "$PROJECT_GIT_DIR") {
	Remove-Item "$PROJECT_GIT_DIR" -Force -Recurse
  }
  New-Item -ItemType directory -Path "$PROJECT_GIT_DIR"
  Copy-Item "$PROJECT_HOME\target\temp\brms-coolstore-repo.git" "$PROJECT_GIT_DIR\brms-coolstore-repo.git" -Force -Recurse
  Remove-Item "$PROJECT_HOME\target\temp" -Force -Recurse
} else {
  Write-Host "  - running in offline-mode, using cached project's Git repo.`n"

  If (-Not (Test-Path "$PROJECT_GIT_DIR\brms-coolstore-repo.git")) {
    Write-Host "No project Git repo found. Please run the script without the 'offline' ('-o') option to automatically download the required Git repository!`n"
    exit 1
  }
}
# Copy the repo to the JBoss BPMSuite installation directory.
Remove-Item "$JBOSS_HOME\bin\.niogit\coolstore-demo.git" -Force -Recurse
Copy-Item "$PROJECT_GIT_DIR\brms-coolstore-repo.git" "$SERVER_BIN\.niogit\coolstore-demo.git" -force -recurse

################################# End setup demo projects ##########################################



Write-Host "- setting up web services...`n"
$argList = "clean install -f $PRJ_DIR\pom.xml"
$mvnProcess = (Start-Process -FilePath mvn -ArgumentList $argList -Wait -PassThru)
Write-Host "Process finished with return code: " $mvnProcess.ExitCode
Write-Host ""
Copy-Item "$PRJ_DIR\acme-demo-flight-service\target\acme-flight-service-1.0.war" "$SERVER_DIR"
Copy-Item "$PRJ_DIR\acme-demo-hotel-service\target\acme-hotel-service-1.0.war" "$SERVER_DIR"

Write-Host "- adding acmeDataModel-1.0.jar to business-central.war/WEB-INF/lib`n"
Copy-Item "$PRJ_DIR\acme-data-model\target\acmeDataModel-1.0.jar" "$SERVER_DIR\business-central.war\WEB-INF\lib" -force

Write-Host "- deploying external-client-ui-form-1.0.war to EAP deployments directory`n"
Copy-Item "$PRJ_DIR\external-client-ui-form\target\external-client-ui-form-1.0.war" "$SERVER_DIR%" -force

Write-Host "- setting up standalone.xml configuration adjustments...`n"
Copy-Item "$SUPPORT_DIR\standalone.xml" "$SERVER_CONF" -force

Write-Host "- setup email task notification user...`n"
Copy-Item "$SUPPORT_DIR\userinfo.properties" "$SERVER_DIR\business-central.war\WEB-INF\classes\" -force

# Optional: uncomment this to install mock data for BPM Suite
#
# Write-Host "- setting up mock bpm dashboard data...`n"
# Copy-Item "$SUPPORT_DIR\1000_jbpm_demo_h2.sql" "$SERVER_DIR\dashbuilder.war\WEB-INF\etc\sql"

Write-Host "========================================================================"
Write-Host "=                                                                      ="
Write-Host "=  You can now start the $PRODUCT with:                         ="
Write-Host "=                                                                      ="
Write-Host "=   $SERVER_BIN/standalone.sh                      ="
Write-Host "=                                                                      ="
Write-Host "=  Login into business central at:                                     ="
Write-Host "=                                                                      ="
Write-Host "=    http://localhost:8080/business-central  (u:erics / p:bpmsuite1!)  ="
Write-Host "=                                                                      ="
Write-Host "=  See README.md for general details to run the various demo cases.    ="
Write-Host "=                                                                      ="
Write-Host "=  $PRODUCT $VERSION $DEMO Setup Complete.              ="
Write-Host "=                                                                      ="
Write-Host "========================================================================"
