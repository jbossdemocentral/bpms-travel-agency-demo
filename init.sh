#!/bin/sh 
DEMO="Travel Agency Demo"
AUTHORS="Niraj Patel, Shepherd Chengeta,"
AUTHORS2="Andrew Block, Eric D. Schabell"
PROJECT="git@github.com:jbossdemocentral/bpms-travel-agency-demo.git"
PRODUCT="JBoss BPM Suite"
VERSION=6.2
JBOSS_HOME=./target/jboss-bpmsuite-$VERSION
SERVER_DIR=$JBOSS_HOME/standalone/deployments/
SERVER_CONF=$JBOSS_HOME/standalone/configuration/
SERVER_BIN=$JBOSS_HOME/bin
SRC_DIR=./installs
SUPPORT_DIR=./support
PRJ_DIR=./projects
BPMS=jboss-bpmsuite-6.2.0.GA-installer.jar
EAP=jboss-eap-6.4.0-installer.jar
EAP_PATCH=jboss-eap-6.4.4-patch.zip


# wipe screen.
clear 

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
echo "##                     ${AUTHORS}          ##"
echo "##                       ${AUTHORS2}         ##"
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

if [ -r $SRC_DIR/$EAP_PATCH ] || [ -L $SRC_DIR/$EAP_PATCH ]; then
	echo Product patches are present...
	echo
else
	echo Need to download $EAP_PATCH package from the Customer Portal 
	echo and place it in the $SRC_DIR directory to proceed...
	echo
	exit
fi

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

echo
echo "Applying JBoss EAP 6.4.4 patch now..."
echo
$JBOSS_HOME/bin/jboss-cli.sh --command="patch apply $SRC_DIR/$EAP_PATCH --override-modules"

if [ $? -ne 0 ]; then
	echo
	echo Error occurred during JBoss EAP patching!
	exit
fi

echo
echo JBoss BPM Suite installer running now...
echo
java -jar $SRC_DIR/$BPMS $SUPPORT_DIR/installation-bpms -variablefile $SUPPORT_DIR/installation-bpms.variables

if [ $? -ne 0 ]; then
	echo Error occurred during $PRODUCT installation!
	exit
fi

echo
echo "  - enabling demo accounts role setup in application-roles.properties file..."
echo
cp $SUPPORT_DIR/application-roles.properties $SERVER_CONF

echo "  - setting up demo projects..."
echo
cp -r $SUPPORT_DIR/bpm-suite-demo-niogit $SERVER_BIN/.niogit

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
echo "========================================================================"
echo "=                                                                      ="
echo "=  You can now start the $PRODUCT with:                         ="
echo "=                                                                      ="
echo "=   $SERVER_BIN/standalone.sh                      ="
echo "=                                                                      ="
echo "=  Login into business central at:                                     ="
echo "=                                                                      ="
echo "=    http://localhost:8080/business-central  (u:erics / p:bpmsuite1!)  ="
echo "=                                                                      ="
echo "=  See README.md for general details to run the various demo cases.    ="
echo "=                                                                      ="
echo "=  $PRODUCT $VERSION $DEMO Setup Complete.              ="
echo "=                                                                      ="
echo "========================================================================"
