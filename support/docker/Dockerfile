# Use jbossdemocentral/developer as the base
FROM jbossdemocentral/developer

# Maintainer details
MAINTAINER Andrew Block <andy.block@gmail.com>

# Environment Variables 
ENV BPMS_HOME /opt/jboss/bpms/jboss-eap-6.4
ENV BPMS_VERSION_MAJOR 6
ENV BPMS_VERSION_MINOR 3
ENV BPMS_VERSION_MICRO 0
ENV BPMS_VERSION_PATCH GA

ENV EAP_VERSION_MAJOR 6
ENV EAP_VERSION_MINOR 4
ENV EAP_VERSION_MICRO 0
ENV EAP_VERSION_PATCH 7

# ADD Installation Files
COPY support/installation-bpms support/installation-eap support/installation-bpms.variables support/installation-eap.variables installs/jboss-bpmsuite-$BPMS_VERSION_MAJOR.$BPMS_VERSION_MINOR.$BPMS_VERSION_MICRO.$BPMS_VERSION_PATCH-installer.jar installs/jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_MICRO-installer.jar installs/jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_PATCH-patch.zip /opt/jboss/

# Update Permissions on Installers
USER root
RUN chown 1000:1000 /opt/jboss/jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_MICRO-installer.jar /opt/jboss/jboss-bpmsuite-$BPMS_VERSION_MAJOR.$BPMS_VERSION_MINOR.$BPMS_VERSION_MICRO.$BPMS_VERSION_PATCH-installer.jar 
USER 1000

# Prepare and run installer and cleanup installation components
RUN sed -i "s:<installpath>.*</installpath>:<installpath>$BPMS_HOME</installpath>:" /opt/jboss/installation-eap \
    && sed -i "s:<installpath>.*</installpath>:<installpath>$BPMS_HOME</installpath>:" /opt/jboss/installation-bpms \
	&& java -jar /opt/jboss/jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_MICRO-installer.jar  /opt/jboss/installation-eap -variablefile /opt/jboss/installation-eap.variables \
    && $BPMS_HOME/bin/jboss-cli.sh --command="patch apply /opt/jboss/jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_PATCH-patch.zip --override-all" \
    && java -jar /opt/jboss/jboss-bpmsuite-$BPMS_VERSION_MAJOR.$BPMS_VERSION_MINOR.$BPMS_VERSION_MICRO.$BPMS_VERSION_PATCH-installer.jar  /opt/jboss/installation-bpms -variablefile /opt/jboss/installation-bpms.variables \
    && rm -rf /opt/jboss/jboss-bpmsuite-$BPMS_VERSION_MAJOR.$BPMS_VERSION_MINOR.$BPMS_VERSION_MICRO.$BPMS_VERSION_PATCH-installer.jar /opt/jboss/jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_MICRO-installer.jar /opt/jboss/jboss-eap-$EAP_VERSION_MAJOR.$EAP_VERSION_MINOR.$EAP_VERSION_PATCH-patch.zip /opt/jboss/installation-bpms /opt/jboss/installation-bpms.variables /opt/jboss/installation-eap /opt/jboss/installation-eap.variables $BPMS_HOME/standalone/configuration/standalone_xml_history/

# Copy demo and support files
COPY support/bpm-suite-demo-niogit $BPMS_HOME/bin/.niogit
COPY projects /opt/jboss/bpms-projects
COPY support/userinfo.properties $BPMS_HOME/standalone/deployments/business-central.war/WEB-INF/classes/
COPY support/application-roles.properties support/standalone.xml $BPMS_HOME/standalone/configuration/

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
CMD ["/opt/jboss/bpms/jboss-eap-6.4/bin/standalone.sh","-c","standalone.xml","-b", "0.0.0.0","-bmanagement","0.0.0.0"]
