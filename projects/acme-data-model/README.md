PLEASE NOTE: In the current demo, you do not need to do anything as we have set up your demo environment for you already when you ran/run the ./init.sh (init.bat) file

Preparing Data Objects for BPMS
================================
In order to invoke webservices successfully in BPMS, you will need to import classes that are identical to those used by the web service. This is why I created the acme-data-model project. The process to prepare the data objects is described here. Please note, it is expected that in future versions, this process will not be necessary. Once that is the case, I will update the project to reflect that change. 

Step 1
-------
With your Web Services server (i.e. where you have the flight and hotel webservices) running, open JBDS. Version of JBDS tested in this demo is 7.1.1.
 

Step 2
--------
Create a new maven project which will be packaged as a JAR file. You can name it anything you want. In this case, I named it "acme-data-model"

Step 3
-------
Add a kmodule.xml file into the META-INF folder. The META-INF folder typically is in the src/main/resources folder therefore if it is not already in the project, create it by right clicking on the src/main/resources then adding a folder and name it META-INF.

The kmodule.xml file itself only needs to contain one line in it... You can copy and paste the contents of the file below

    <kmodule xmlns="http://jboss.org/kie/6.0.0/kmodule" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>  

Save the file and project.

Step 4
-------
Inside the project you created in Step 3, create a new Web Service Client

- right click on the /src/main/java folder

- in the menu that pops up, navigate to New --> Other --> Web Service --> Web Service Client and click Next

- In the Service definition, paste the FLIGHT web service link (e.g. http://localhost:8080/acme-flight-service-1.0/AcmeDemoInterface?wsdl)

- In the Configuration, select your Server Runtime, Web Service Runtime and Client Project (these typically will be selected by default).. If you get a JBoss WS runtime error then set the "JBoss WS Runtime" preferences in Window --> Preferences --> Web Services --> JBoss WS Runtime Preferences to point to the JBOSS_HOME folder of your jboss application server

- Click Next

- Leave the Package Name empty and click Finish

- Two packages will be created (one of them should have then *.clientsample)

 
Repeat the steps above for the Hotel web service link: http://localhost:8080/acme-hotel-service-1.0/HotelWS?wsdl

Step 5
-------
From the generated classes, add the java.io.Serializable interface to the classes listed below

com.jboss.soap.service.acmedemo.Flight
    // i.e by changing  
    public class Flight  
    //to  
    public class Flight implements Serializable  

 

com.jboss.soap.service.acmedemo.FlightRequest
    // i.e by changing  
    public class FlightRequest  
    //to  
    public class FlightRequest implements Serializable  

 

acme.service.soap.hotelws.Resort
    // i.e by changing  
    public class Resort  
    //to  
    public class Resort implements Serializable  

 

acme.service.soap.hotelws.HotelRequest
    // i.e by changing  
    public class HotelRequest  
    //to  
    public class HotelRequest implements Serializable  

 

This is necessary to serialize and allow the classes to be moved over the wire.


Step 6: Build and deploy
------------------------
You could build the project by right clicking on the project name and selecting "Run As" --> "maven Install".. If your maven build is successful then it should create a .jar file in target folder of your project.

Then deploy the .jar to the following location:

$JBOSS_BPMS_SERVER_HOME/standalone/deployments/business-central.war/WEB-INF/lib


Step 7:
-------
Restart your BPMS server

You are now ready to work in BPMS.

PLEASE NOTE: In the current demo, you do not need to do anything as we have set this up already for you when you ran/run the ./init.sh (init.bat) file
