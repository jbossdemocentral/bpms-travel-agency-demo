# Use jbossdemocentral/developer as the base
FROM jbossdemocentral/developer

# Maintainer details
MAINTAINER Andrew Block <andy.block@gmail.com>

# Environment Variables
ENV BPMS_HOME /opt/jboss/bpms/jboss-eap-7.0
ENV BPMS_VERSION_MAJOR 6
ENV BPMS_VERSION_MINOR 4
ENV BPMS_VERSION_MICRO 0
ENV BPMS_VERSION_PATCH GA

ENV EAP_VERSION_MAJOR 7
ENV EAP_VERSION_MINOR 0
ENV EAP_VERSION_MICRO 0
#ENV EAP_VERSION_PATCH 7

ENV EAP_INSTALLER=jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_MICRO-installer.jar
ENV BPMS_DEPLOYABLE=jboss-bpmsuite-$BPMS_VERSION_MAJOR.$BPMS_VERSION_MINOR.$BPMS_VERSION_MICRO.$BPMS_VERSION_PATCH-deployable-eap7.x.zip

# ADD Installation Files
COPY support/installation-eap support/installation-eap.variables installs/$BPMS_DEPLOYABLE installs/$EAP_INSTALLER /opt/jboss/

# Update Permissions on Installers
USER root
RUN chown 1000:1000 /opt/jboss/$EAP_INSTALLER /opt/jboss/$BPMS_DEPLOYABLE
USER 1000

# Prepare and run installer and cleanup installation components
RUN sed -i "s:<installpath>.*</installpath>:<installpath>$BPMS_HOME</installpath>:" /opt/jboss/installation-eap \
    && java -jar /opt/jboss/$EAP_INSTALLER  /opt/jboss/installation-eap -variablefile /opt/jboss/installation-eap.variables \
    #&& $BPMS_HOME/bin/jboss-cli.sh --command="patch apply /opt/jboss/jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_PATCH-patch.zip --override-all" \
    && unzip -qo /opt/jboss/$BPMS_DEPLOYABLE  -d $BPMS_HOME/.. \
    && rm -rf /opt/jboss/$BPMS_DEPLOYABLE /opt/jboss/$EAP_INSTALLER /opt/jboss/installation-eap /opt/jboss/installation-eap.variables $BPMS_HOME/standalone/configuration/standalone_xml_history/

# Copy demo and support files
COPY support/bpm-suite-demo-niogit $BPMS_HOME/bin/.niogit
# Clone the BPM Suite Travel Agency repository.
USER root
RUN rm -rf $BPMS_HOME/bin/.niogit/specialtripsagency.git && git clone --bare https://github.com/jbossdemocentral/bpms-travel-agency-demo-repo.git $BPMS_HOME/bin/.niogit/specialtripsagency.git
USER 1000

# Create users and add support files
RUN $BPMS_HOME/bin/add-user.sh -a -r ApplicationRealm -u bpmsAdmin -p bpmsuite1! -ro analyst,admin,user,manager,taskuser,reviewerrole,employeebookingrole,kie-server,rest-all --silent \
  && $BPMS_HOME/bin/add-user.sh -a -r ApplicationRealm -u erics -p bpmsuite1! -ro analyst,admin,user,manager,taskuser,reviewerrole,employeebookingrole,kie-server,rest-all --silent
COPY support/standalone.xml $BPMS_HOME/standalone/configuration/
COPY support/userinfo.properties $BPMS_HOME/standalone/deployments/business-central.war/WEB-INF/classes/
COPY projects /opt/jboss/bpms-projects

# Swtich back to root user to perform build and cleanup
USER root

# Run Demo Maven build and cleanup
RUN mvn clean install -f /opt/jboss/bpms-projects/pom.xml \
  && cp -r /opt/jboss/bpms-projects/acme-demo-flight-service/target/acme-flight-service-1.0.war $BPMS_HOME/standalone/deployments/ \
  && cp -r /opt/jboss/bpms-projects/acme-demo-hotel-service/target/acme-hotel-service-1.0.war $BPMS_HOME/standalone/deployments/ \
  && cp -r /opt/jboss/bpms-projects/acme-data-model/target/acmeDataModel-1.0.jar $BPMS_HOME/standalone/deployments/business-central.war/WEB-INF/lib/ \
  && cp -r /opt/jboss/bpms-projects/external-client-ui-form/target/external-client-ui-form-1.0.war $BPMS_HOME/standalone/deployments/ \
  && chown -R 1000:1000 $BPMS_HOME/bin/.niogit $BPMS_HOME/standalone/configuration/application-roles.properties $BPMS_HOME/standalone/configuration/standalone.xml  $BPMS_HOME/standalone/deployments/business-central.war/WEB-INF/classes/userinfo.properties $BPMS_HOME/standalone/deployments/acme-flight-service-1.0.war $BPMS_HOME/standalone/deployments/acme-hotel-service-1.0.war $BPMS_HOME/standalone/deployments/business-central.war/WEB-INF/lib/acmeDataModel-1.0.jar $BPMS_HOME/standalone/deployments/external-client-ui-form-1.0.war \
  && rm -rf ~/.m2/repository /opt/jboss/bpms-projects

# Run as JBoss
USER 1000

# Expose Ports
EXPOSE 9990 9999 8080

# Run BPMS
CMD ["/opt/jboss/bpms/jboss-eap-7.0/bin/standalone.sh","-c","standalone.xml","-b", "0.0.0.0","-bmanagement","0.0.0.0"]
