@ECHO OFF
setlocal

set PROJECT_HOME=%~dp0
set DEMO=Travel Agency Demo
set AUTHORS=Nirja Patel, Shepherd Chengeta,
set AUTHORS2=Andrew Block, Eric D. Schabell
set PROJECT=git@github.com:jbossdemocentral/bpms-travel-agency-demo.git
set PRODUCT=JBoss BPM Suite
set JBOSS_HOME=%PROJECT_HOME%target\jboss-eap-6.1
set SERVER_DIR=%JBOSS_HOME%\standalone\deployments\
set SERVER_CONF=%JBOSS_HOME%\standalone\configuration\
set SERVER_BIN=%JBOSS_HOME%\bin
set SRC_DIR=%PROJECT_HOME%installs
set SUPPORT_DIR=%PROJECT_HOME%support
set PRJ_DIR=%PROJECT_HOME%projects
set BPMS=jboss-bpms-installer-6.0.3.GA-redhat-1.jar
set VERSION=6.0.3

REM wipe screen.
cls

echo.
echo #################################################################
echo ##                                                             ##   
echo ##  Setting up the %DEMO%                          ##
echo ##                                                             ##   
echo ##                                                             ##   
echo ##     ####  ####   #   #      ### #   # ##### ##### #####     ##
echo ##     #   # #   # # # # #    #    #   #   #     #   #         ##
echo ##     ####  ####  #  #  #     ##  #   #   #     #   ###       ##
echo ##     #   # #     #     #       # #   #   #     #   #         ##
echo ##     ####  #     #     #    ###  ##### #####   #   #####     ##
echo ##                                                             ##   
echo ##                                                             ##   
echo ##  brought to you by,                                         ##   
echo ##                     %AUTHORS%           ##
echo ##                       %AUTHORS2%          ##
echo ##                                                             ##   
echo ##  %PROJECT%##
echo ##                                                             ##   
echo #################################################################
echo.

REM make some checks first before proceeding.	
if exist %SRC_DIR%\%BPMS% (
        echo Product sources are present...
        echo.
) else (
        echo Need to download %BPMS% package from the Customer Support Portal
        echo and place it in the %SRC_DIR% directory to proceed...
        echo.
        GOTO :EOF
)

REM Move the old JBoss instance, if it exists, to the OLD position.
if exist %JBOSS_HOME% (
         echo - existing JBoss product install removed...
         echo.
         rmdir /s /q target"
 )

REM Run installer.
echo Product installer running now...
echo.
call java -jar %SRC_DIR%/%BPMS% %SUPPORT_DIR%\installation-bpms -variablefile %SUPPORT_DIR%\installation-bpms.variables

if not "%ERRORLEVEL%" == "0" (
	echo Error Occurred During %PRODUCT% Installation!
	echo.
	GOTO :EOF
)

echo - setting up demo projects...
echo.

echo - enabling demo accounts role setup in application-roles.properties file...
echo.
xcopy /Y /Q "%SUPPORT_DIR%\application-roles.properties" "%SERVER_CONF%"
echo. 

mkdir "%SERVER_BIN%\.niogit\"
xcopy /Y /Q /S "%SUPPORT_DIR%\bpm-suite-demo-niogit\*" "%SERVER_BIN%\.niogit\"
echo. 

echo.
echo - setting up web services...
echo.
call mvn clean install -f %PRJ_DIR%/pom.xml
xcopy /Y /Q "%PRJ_DIR%\acme-demo-flight-service\target\acme-flight-service-1.0.war" "%SERVER_DIR%"
xcopy /Y /Q "%PRJ_DIR%\acme-demo-hotel-service\target\acme-hotel-service-1.0.war" "%SERVER_DIR%"

echo.
echo - adding acmeDataModel-1.0.jar to business-central.war/WEB-INF/lib
xcopy /Y /Q %PRJ_DIR%\acme-data-model\target\acmeDataModel-1.0.jar %SERVER_DIR%\business-central.war\WEB-INF\lib

echo.
echo - deploying external-client-ui-form-1.0.war to EAP deployments directory
xcopy /Y /Q %PRJ_DIR%\external-client-ui-form\target\external-client-ui-form-1.0.war %SERVER_DIR%

echo.
echo - setting up standalone.xml configuration adjustments...
echo.
xcopy /Y /Q "%SUPPORT_DIR%\standalone.xml" "%SERVER_CONF%"
echo.

echo.
echo - updating the CustomWorkItemHandler.conf file to use the appropriate email server...
xcopy /Y /Q %SUPPORT_DIR%\CustomWorkItemHandlers.conf %SERVER_DIR%\business-central.war\WEB-INF\classes\META-INF\

REM Optional: uncomment this to install mock data for BPM Suite
REM
REM echo - setting up mock bpm dashboard data...
REM echo.
REM xcopy /Y /Q "%SUPPORT_DIR%\1000_jbpm_demo_h2.sql" "%SERVER_DIR%\dashbuilder.war\WEB-INF\etc\sql"
REM echo.

echo.
echo ========================================================================
echo =                                                                      =
echo =  You can now start the %PRODUCT% with:                         =
echo =                                                                      =
echo =   %SERVER_BIN%\standalone.bat                           =
echo =                                                                      =
echo =  Login into business central at:                                     =
echo =                                                                      =
echo =    http://localhost:8080/business-central  [u:erics / p:bpmsuite1!]  =
echo =                                                                      =
echo =  See README.md for general details to run the various demo cases.    =
echo =                                                                      =
echo =  %PRODUCT% %VERSION% %DEMO% Setup Complete.            =
echo =                                                                      =
echo ========================================================================
echo.
