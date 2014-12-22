External Client UI Form
=======================
This project covers a use case when customer wants to start a business process in BPMS using a REST call externally. 

This project uses a simple JSP and Servlet combination to build a front end. The form you see when you access the following URL is a simple JSP page which gathers information/requirements about a customer and submits it to BPMS via a REST call so that BPMS can kick start the process.

http://<VM IP Address>:8080/external-client-ui-form-1.0/ (is you are accessing from outside the server)
or
http://localhost:8080/external-client-ui-form-1.0/ (if you are accessing from within the server)


Build and deploy
----------------
You could build the project by right clicking on the project name and selecting "Run As" --> "maven Install".. If your maven build is successful then it should create a .jar file in target folder of your project. This is done automatically if you installed the Travel Agency demo project using the provided installation scripts.

Then deploy the .jar to the following location:

$JBOSS_BPMS_SERVER_HOME/standalone/deployments/

