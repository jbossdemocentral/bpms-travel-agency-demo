#!/bin/sh
DEMO="Travel Agency Demo"
AUTHORS="Niraj Patel, Shepherd Chengeta,"
AUTHORS2="Andrew Block, Eric D. Schabell, Duncan Doyle"
PROJECT="git@github.com:jbossdemocentral/bpms-travel-agency-demo.git"
PRODUCT="JBoss BPM Suite"
VERSION=6.4
TARGET=./target
JBOSS_HOME=$TARGET/jboss-eap-7.0
SERVER_DIR=$JBOSS_HOME/standalone/deployments/
SERVER_CONF=$JBOSS_HOME/standalone/configuration/
SERVER_BIN=$JBOSS_HOME/bin
SRC_DIR=./installs
SUPPORT_DIR=./support
PRJ_DIR=./projects
BPMS=jboss-bpmsuite-6.4.0.GA-deployable-eap7.x.zip
EAP=jboss-eap-7.0.0-installer.jar
#EAP_PATCH=jboss-eap-6.4.7-patch.zip
PROJECT_GIT_REPO=https://github.com/jbossdemocentral/bpms-travel-agency-demo-repo
PROJECT_GIT_BRANCH=master
PROJECT_GIT_DIR=./support/demo_project_git
OFFLINE_MODE=false

# wipe screen.
clear

function usage {
      echo "Usage: init.sh [args...]"
      echo "where args include:"
      echo "    -o              run this script in offline mode. The project's Git repo will not be downloaded. Instead a cached version will be used if available."
      echo "    -h              prints this help."
}

#Parse the params
while getopts "oh" opt; do
  case $opt in
    o)
      OFFLINE_MODE=true
      ;;
    h)
      usage
      exit 0
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
  esac
done

echo
echo "##################################################################"
echo "##                                                              ##"
echo "##  Setting up the ${DEMO}                           ##"
echo "##                                                              ##"
echo "##                                                              ##"
echo "##     ####  ####   #   #      ### #   # ##### ##### #####      ##"
echo "##     #   # #   # # # # #    #    #   #   #     #   #          ##"
echo "##     ####  ####  #  #  #     ##  #   #   #     #   ###        ##"
echo "##     #   # #     #     #       # #   #   #     #   #          ##"
echo "##     ####  #     #     #    ###  ##### #####   #   #####      ##"
echo "##                                                              ##"
echo "##                                                              ##"
echo "##  brought to you by,                                          ##"
echo "##               ${AUTHORS}    		##"
echo "##               ${AUTHORS2}	##"
echo "##                                                              ##"
echo "##  ${PROJECT} ##"
echo "##                                                              ##"
echo "##################################################################"
echo

command -v mvn -q >/dev/null 2>&1 || { echo >&2 "Maven is required but not installed yet... aborting."; exit 1; }

# make some checks first before proceeding.
if [ -r $SRC_DIR/$EAP ] || [ -L $SRC_DIR/$EAP ]; then
	echo Product sources are present...
	echo
else
	echo Need to download $EAP package from the Customer Portal
	echo and place it in the $SRC_DIR directory to proceed...
	echo
	exit
fi

#if [ -r $SRC_DIR/$EAP_PATCH ] || [ -L $SRC_DIR/$EAP_PATCH ]; then
#	echo Product patches are present...
#	echo
#else
#	echo Need to download $EAP_PATCH package from the Customer Portal
#	echo and place it in the $SRC_DIR directory to proceed...
#	echo
#	exit
#fi

if [ -r $SRC_DIR/$BPMS ] || [ -L $SRC_DIR/$BPMS ]; then
		echo Product sources are present...
		echo
else
		echo Need to download $BPMS package from the Customer Portal
		echo and place it in the $SRC_DIR directory to proceed...
		echo
		exit
fi

# Remove the old JBoss instance, if it exists.
if [ -x $JBOSS_HOME ]; then
		echo "  - existing JBoss product install removed..."
		echo
		rm -rf target
fi

# Run installers.
echo "JBoss EAP installer running now..."
echo
java -jar $SRC_DIR/$EAP $SUPPORT_DIR/installation-eap -variablefile $SUPPORT_DIR/installation-eap.variables

if [ $? -ne 0 ]; then
	echo
	echo Error occurred during JBoss EAP installation!
	exit
fi

#echo
#echo "Applying JBoss EAP 6.4.7 patch now..."
#echo
#$JBOSS_HOME/bin/jboss-cli.sh --command="patch apply $SRC_DIR/$EAP_PATCH --override-modules"
#
#if [ $? -ne 0 ]; then
#	echo
#	echo Error occurred during JBoss EAP patching!
#	exit
#fi

echo
echo Deploying JBoss BPM Suite now...
echo
unzip -qo $SRC_DIR/$BPMS -d $TARGET
if [ $? -ne 0 ]; then
	echo Error occurred during $PRODUCT installation!
	exit
fi

echo
echo "  - enabling demo accounts role setup in application-roles.properties file..."
echo
$JBOSS_HOME/bin/add-user.sh -a -r ApplicationRealm -u bpmsAdmin -p bpmsuite1! -ro analyst,admin,user,manager,taskuser,reviewerrole,employeebookingrole,kie-server,rest-all --silent
$JBOSS_HOME/bin/add-user.sh -a -r ApplicationRealm -u erics -p bpmsuite1! -ro analyst,admin,user,manager,taskuser,reviewerrole,employeebookingrole,kie-server,rest-all --silent

echo "  - setting up demo projects..."
echo
# Copy the default (internal) BPMSuite repo's.
cp -r $SUPPORT_DIR/bpm-suite-demo-niogit $SERVER_BIN/.niogit
# Copy the demo project repo.
if ! $OFFLINE_MODE
then
  # Not in offline mode, so downloading the latest repo. We first download the repo in a temp dir and we only delete the old, cached repo, when the download is succesful.
  echo "  - cloning the project's Git repo from: $PROJECT_GIT_REPO"
  echo
#  rm -rf ./target/temp && git clone --bare $PROJECT_GIT_REPO ./target/temp/bpms-specialtripsagency.git || { echo; echo >&2 "Error cloning the project's Git repo. If there is no Internet connection available, please run this script in 'offline-mode' ('-o') to use a previously downloaded and cached version of the project's Git repo... Aborting"; echo; exit 1; }
  rm -rf ./target/temp && git clone -b $PROJECT_GIT_BRANCH --single-branch $PROJECT_GIT_REPO ./target/temp/bpms-specialtripsagency.git || { echo; echo >&2 "Error cloning the project's Git repo. If there is no Internet connection available, please run this script in 'offline-mode' ('-o') to use a previously downloaded and cached version of the project's Git repo... Aborting"; echo; exit 1; }
  pushd ./target/temp/bpms-specialtripsagency.git
  # rename the checked-out branch to master.
  echo "Renaming cloned branch '$PROJECT_GIT_BRANCH' to 'master'."
  git branch -m $PROJECT_GIT_BRANCH master
  popd

  echo "  - replacing cached project git repo: $PROJECT_GIT_DIR/bpms-specialtripsagency.git"
  echo
#  rm -rf $PROJECT_GIT_DIR/bpms-specialtripsagency.git && mkdir -p $PROJECT_GIT_DIR && cp -R target/temp/bpms-specialtripsagency.git $PROJECT_GIT_DIR/bpms-specialtripsagency.git && rm -rf ./target/temp
  # Make a bare clone of the Git repo.
  rm -rf $PROJECT_GIT_DIR/bpms-specialtripsagency.git && mkdir -p $PROJECT_GIT_DIR && git clone --bare target/temp/bpms-specialtripsagency.git $PROJECT_GIT_DIR/bpms-specialtripsagency.git && rm -rf ./target/temp
else
  echo "  - running in offline-mode, using cached project's Git repo."
  echo
  if [ ! -d "$PROJECT_GIT_DIR" ]
  then
    echo "No project Git repo found. Please run the script without the 'offline' ('-o') option to automatically download the required Git repository!"
    echo
    exit 1
  fi
fi
# Copy the repo to the JBoss BPMSuite installation directory.
rm -rf $JBOSS_HOME/bin/.niogit/specialtripsagency.git && cp -R $PROJECT_GIT_DIR/bpms-specialtripsagency.git $SERVER_BIN/.niogit/specialtripsagency.git

echo "  - setting up web services..."
echo
mvn clean install -f $PRJ_DIR/pom.xml
cp -r $PRJ_DIR/acme-demo-flight-service/target/acme-flight-service-1.0.war $SERVER_DIR
cp -r $PRJ_DIR/acme-demo-hotel-service/target/acme-hotel-service-1.0.war $SERVER_DIR

echo
echo "  - adding acmeDataModel-1.0.jar to business-central.war/WEB-INF/lib"
cp -r $PRJ_DIR/acme-data-model/target/acmeDataModel-1.0.jar $SERVER_DIR/business-central.war/WEB-INF/lib

echo
echo "  - deploying external-client-ui-form-1.0.war to EAP deployments directory"
cp -r $PRJ_DIR/external-client-ui-form/target/external-client-ui-form-1.0.war $SERVER_DIR/

echo
echo "  - setting up standalone.xml configuration adjustments..."
echo
cp $SUPPORT_DIR/standalone.xml $SERVER_CONF

echo "  - setup email task notification users..."
echo
cp $SUPPORT_DIR/userinfo.properties $SERVER_DIR/business-central.war/WEB-INF/classes/

echo "  - making sure standalone.sh for server is executable..."
echo
chmod u+x $JBOSS_HOME/bin/standalone.sh

# Optional: uncomment this to install mock data for BPM Suite.
#
#echo - setting up mock bpm dashboard data...
#cp $SUPPORT_DIR/1000_jbpm_demo_h2.sql $SERVER_DIR/dashbuilder.war/WEB-INF/etc/sql
#echo

echo
echo "============================================================================"
echo "=                                                                          ="
echo "=  You can now start the $PRODUCT with:                             ="
echo "=                                                                          ="
echo "=   $SERVER_BIN/standalone.sh                               ="
echo "=                                                                          ="
echo "=  Login into business central at:                                         ="
echo "=                                                                          ="
echo "=    http://localhost:8080/business-central  (u:bpmsAdmin / p:bpmsuite1!)  ="
echo "=                                                                          ="
echo "=  See README.md for general details to run the various demo cases.        ="
echo "=                                                                          ="
echo "=  $PRODUCT $VERSION $DEMO Setup Complete.                  ="
echo "=                                                                          ="
echo "============================================================================"
