#!/bin/bash
################################################################################
# Prvisioning script to deploy the demo on an OpenShift environment            #
################################################################################
function usage() {
    echo
    echo "Usage:"
    echo " $0 [command] [demo-name] [options]"
    echo " $0 --help"
    echo
    echo "Example:"
    echo " $0 deploy --maven-mirror-url http://nexus.repo.com/content/groups/public/ --project-suffix s40d"
    echo
    echo "COMMANDS:"
    echo "   deploy                   Set up the demo projects and deploy demo apps"
    echo "   delete                   Clean up and remove demo projects and objects"
    echo "   verify                   Verify the demo is deployed correctly"
    echo "   idle                     Make all demo servies idle"
    echo
    echo "DEMOS:"
    echo "   travel-agency            Travel Agency demo with all services deployed"
    echo
    echo "OPTIONS:"
    echo "   --user [username]         The admin user for the demo projects. mandatory if logged in as system:admin"
    echo "   --maven-mirror-url [url]  Use the given Maven repository for builds. If not specifid, a Nexus container is deployed in the demo"
    echo "   --project-suffix [suffix] Suffix to be added to demo project names e.g. ci-SUFFIX. If empty, user will be used as suffix"
    echo "   --ephemeral               Deploy demo without persistent storage"
    echo "   --run-verify              Run verify after provisioning"
    echo
}

ARG_USERNAME=
ARG_PROJECT_SUFFIX=
ARG_MAVEN_MIRROR_URL=
ARG_EPHEMERAL=false
ARG_COMMAND=
ARG_RUN_VERIFY=false
ARG_DEMO=

while :; do
    case $1 in
        deploy)
            ARG_COMMAND=deploy
            if [ -n "$2" ]; then
                ARG_DEMO=$2
                shift
            fi
            ;;
        delete)
            ARG_COMMAND=delete
            if [ -n "$2" ]; then
                ARG_DEMO=$2
                shift
            fi
            ;;
        verify)
            ARG_COMMAND=verify
            if [ -n "$2" ]; then
                ARG_DEMO=$2
                shift
            fi
            ;;
        idle)
            ARG_COMMAND=idle
            if [ -n "$2" ]; then
                ARG_DEMO=$2
                shift
            fi
            ;;
        --user)
            if [ -n "$2" ]; then
                ARG_USERNAME=$2
                shift
            else
                printf 'ERROR: "--user" requires a non-empty value.\n' >&2
                usage
                exit 255
            fi
            ;;
        --maven-mirror-url)
            if [ -n "$2" ]; then
                ARG_MAVEN_MIRROR_URL=$2
                shift
            else
                printf 'ERROR: "--maven-mirror-url" requires a non-empty value.\n' >&2
                usage
                exit 255
            fi
            ;;
        --project-suffix)
            if [ -n "$2" ]; then
                ARG_PROJECT_SUFFIX=$2
                shift
            else
                printf 'ERROR: "--project-suffix" requires a non-empty value.\n' >&2
                usage
                exit 255
            fi
            ;;
        --minimal)
            printf 'WARNING: --minimal is deprecated. Specify the demo name to deploy a subset of pods.\n' >&2
            usage
            exit 255
            ;;
        --ephemeral)
            ARG_EPHEMERAL=true
            ;;
        --run-verify)
            ARG_RUN_VERIFY=true
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        --)
            shift
            break
            ;;
        -?*)
            printf 'WARN: Unknown option (ignored): %s\n' "$1" >&2
            shift
            ;;
        *)               # Default case: If no more options then break out of the loop.
            break
    esac

    shift
done

################################################################################
# CONFIGURATION                                                                #
################################################################################
LOGGEDIN_USER=$(oc whoami)
OPENSHIFT_USER=${ARG_USERNAME:-$LOGGEDIN_USER}

# project
PRJ_SUFFIX=${ARG_PROJECT_SUFFIX:-`echo $OPENSHIFT_USER | sed -e 's/[-@].*//g'`}
PRJ_CI=ci-$PRJ_SUFFIX
PRJ_TRAVEL_AGENCY_PROD=travel-agency-prod-$PRJ_SUFFIX

# config
GITHUB_ACCOUNT=${GITHUB_ACCOUNT:-jbossdemocentral}
GITHUB_REF=${GITHUB_REF:-openshift-build}
GITHUB_URI=https://github.com/$GITHUB_ACCOUNT/bpms-travel-agency-demo.git

# maven
#MAVEN_MIRROR_URL=${ARG_MAVEN_MIRROR_URL:-http://nexus.$PRJ_CI.svc.cluster.local:8081/content/groups/public}
MAVEN_MIRROR_URL=${ARG_MAVEN_MIRROR_URL:-http://nexus:8081/content/groups/public}
MAVEN_MIRROR_URL_SVC=${ARG_MAVEN_MIRROR_URL:-http://nexus.$PRJ_CI.svc.cluster.local:8081/content/groups/public}

GOGS_USER=developer
GOGS_PASSWORD=developer
GOGS_ADMIN_USER=team
GOGS_ADMIN_PASSWORD=team

WEBHOOK_SECRET=UfW7gQ6Jx4

################################################################################
# DEMO MATRIX                                                                  #
################################################################################
case $ARG_DEMO in
    travel-agency)
	   # No need to set anything here anymore.
	;;
    *)
        echo "ERROR: Invalid demo name: \"$ARG_DEMO\""
        usage
        exit 255
        ;;
esac

################################################################################
# FUNCTIONS                                                                    #
################################################################################

function print_info() {
  echo_header "Configuration"

  OPENSHIFT_MASTER=$(oc status | head -1 | sed 's#.*\(https://[^ ]*\)#\1#g') # must run after projects are created

  echo "Demo name:           $ARG_DEMO"
  echo "OpenShift master:    $OPENSHIFT_MASTER"
  echo "Current user:        $LOGGEDIN_USER"
  echo "Ephemeral:           $ARG_EPHEMERAL"
  echo "Project suffix:      $PRJ_SUFFIX"
  echo "GitHub repo:         https://github.com/$GITHUB_ACCOUNT/bpms-travel-agency-demo"
  echo "GitHub branch/tag:   $GITHUB_REF"
  echo "Gogs url:            http://$GOGS_ROUTE"
  echo "Gogs admin user:     $GOGS_ADMIN_USER"
  echo "Gogs admin pwd:      $GOGS_ADMIN_PASSWORD"
  echo "Gogs user:           $GOGS_USER"
  echo "Gogs pwd:            $GOGS_PASSWORD"
  echo "Gogs webhook secret: $WEBHOOK_SECRET"
  echo "Maven mirror url:    $MAVEN_MIRROR_URL"
}

# waits while the condition is true until it becomes false or it times out
function wait_while_empty() {
  local _NAME=$1
  local _TIMEOUT=$(($2/5))
  local _CONDITION=$3

  echo "Waiting for $_NAME to be ready..."
  local x=1
  while [ -z "$(eval ${_CONDITION})" ]
  do
    echo "."
    sleep 5
    x=$(( $x + 1 ))
    if [ $x -gt $_TIMEOUT ]
    then
      echo "$_NAME still not ready, I GIVE UP!"
      exit 255
    fi
  done

  echo "$_NAME is ready."
}

function remove_storage_claim() {
  local _DC=$1
  local _VOLUME_NAME=$2
  local _CLAIM_NAME=$3
  local _PROJECT=$4
  oc volumes dc/$_DC --name=$_VOLUME_NAME --add -t emptyDir --overwrite -n $_PROJECT
  oc delete pvc $_CLAIM_NAME -n $_PROJECT
}

function configure_project_permissions() {
  _PROJECTS=$@
  for project in $_PROJECTS
  do
    oc adm policy add-role-to-group admin system:serviceaccounts:$PRJ_CI -n $project >/dev/null
    oc adm policy add-role-to-group admin system:serviceaccounts:$project -n $project >/dev/null
    # This role is required to allow the default user in the application project to pull images from the CI project.
    #oc adm policy add-role-to-user system:image-puller system:serviceaccount:${PRJ_TRAVEL_AGENCY_PROD}:default --namespace=$PRJ_CI

  done

  if [ $LOGGEDIN_USER == 'system:admin' ] ; then
    for project in $_PROJECTS
    do
      oc adm policy add-role-to-user admin $ARG_USERNAME -n $project
      oc annotate --overwrite namespace $project demo=demo1-$PRJ_SUFFIX demo=demo-modern-arch-$PRJ_SUFFIX
    done
    oc adm pod-network join-projects --to=$PRJ_CI $_PROJECTS >/dev/null 2>&1
  fi

  # Hack to extract domain name when it's not determine in
  # advanced e.g. <user>-<project>.4s23.cluster
  oc create route edge testroute --service=testsvc --port=80 -n $PRJ_CI >/dev/null
  DOMAIN=$(oc get route testroute -o template --template='{{.spec.host}}' -n $PRJ_CI | sed "s/testroute-$PRJ_CI.//g")
  GOGS_ROUTE="gogs-$PRJ_CI.$DOMAIN"
  oc delete route testroute -n $PRJ_CI >/dev/null
}

# Create Project
function create_projects() {
  echo_header "Creating project..."

  echo "Creating project $PRJ_CI"
  oc new-project $PRJ_CI --display-name='CI/CD' --description='CI/CD Components (Jenkins, Gogs, etc)' >/dev/null

  echo "Creating project $PRJ_TRAVEL_AGENCY_PROD"
  oc new-project $PRJ_TRAVEL_AGENCY_PROD --display-name='Travel Agency PROD' --description='Travel Agency Production Environment' >/dev/null
}

# Deploy Nexus
function deploy_nexus() {
  if [ -z "$ARG_MAVEN_MIRROR_URL" ] ; then # no maven mirror specified
    local _TEMPLATE="https://raw.githubusercontent.com/OpenShiftDemos/nexus/master/nexus2-persistent-template.yaml"
    if [ "$ARG_EPHEMERAL" = true ] ; then
      _TEMPLATE="https://raw.githubusercontent.com/OpenShiftDemos/nexus/master/nexus2-template.yaml"
    fi

    echo_header "Deploying Sonatype Nexus repository manager..."
    echo "Using template $_TEMPLATE"
    oc process -f $_TEMPLATE -n $PRJ_CI | oc create -f - -n $PRJ_CI
    sleep 5
    oc set resources dc/nexus --limits=cpu=1,memory=2Gi --requests=cpu=200m,memory=1Gi -n $PRJ_CI
  else
    echo_header "Using existng Maven mirror: $ARG_MAVEN_MIRROR_URL"
  fi
}

# Wait till Nexus is ready
function wait_for_nexus_to_be_ready() {
  if [ -z "$ARG_MAVEN_MIRROR_URL" ] ; then # no maven mirror specified
    wait_while_empty "Nexus" 600 "oc get ep nexus -o yaml -n $PRJ_CI | grep '\- addresses:'"
  fi
}

# Deploy Gogs
function deploy_gogs() {
  echo_header "Deploying Gogs git server..."

  local _TEMPLATE="https://raw.githubusercontent.com/OpenShiftDemos/gogs-openshift-docker/master/openshift/gogs-persistent-template.yaml"
  if [ "$ARG_EPHEMERAL" = true ] ; then
    _TEMPLATE="https://raw.githubusercontent.com/OpenShiftDemos/gogs-openshift-docker/master/openshift/gogs-template.yaml"
  fi

  local _DB_USER=gogs
  local _DB_PASSWORD=gogs
  local _DB_NAME=gogs
  local _GITHUB_REPO="https://github.com/$GITHUB_ACCOUNT/bpms-travel-agency-demo.git"

  echo "Using template $_TEMPLATE"
  oc process -f $_TEMPLATE --param=HOSTNAME=$GOGS_ROUTE --param=GOGS_VERSION=0.9.113 --param=DATABASE_USER=$_DB_USER --param=DATABASE_PASSWORD=$_DB_PASSWORD --param=DATABASE_NAME=$_DB_NAME --param=SKIP_TLS_VERIFY=true -n $PRJ_CI | oc create -f - -n $PRJ_CI

  sleep 5

  # wait for Gogs to be ready
  wait_while_empty "Gogs PostgreSQL" 600 "oc get ep gogs-postgresql -o yaml -n $PRJ_CI | grep '\- addresses:'"
  wait_while_empty "Gogs" 600 "oc get ep gogs -o yaml -n $PRJ_CI | grep '\- addresses:'"

  sleep 20

  # add admin user
  _RETURN=$(curl -o /dev/null -sL --post302 -w "%{http_code}" http://$GOGS_ROUTE/user/sign_up \
    --form user_name=$GOGS_ADMIN_USER \
    --form password=$GOGS_ADMIN_PASSWORD \
    --form retype=$GOGS_ADMIN_PASSWORD \
    --form email=$GOGS_ADMIN_USER@gogs.com)
  sleep 5

  # import project  GitHub repo
  import_repo_into_gogs "$_GITHUB_REPO" 1 "bpms-travel-agency-demo"
  import_repo_into_gogs "https://github.com/jbossdemocentral/bpms-travel-agency-demo-repo" 1 "bpms-travel-agency-demo-repo"

  # import Flight and Hotel Service Deployment projects. Required for our binary builds of these services in an EAP container.
  import_repo_into_gogs "https://github.com/DuncanDoyle/bpms-travel-agency-demo-flight-service-ocp.git" 1 "flight-service-ocp"
  import_repo_into_gogs "https://github.com/DuncanDoyle/bpms-travel-agency-demo-hotel-service-ocp.git" 1 "hotel-service-ocp"

#  read -r -d '' _DATA_JSON << EOM
# {
#  "clone_addr": "$_GITHUB_REPO",
#  "uid": 1,
#  "repo_name": "coolstore-microservice"
#}
#EOM

#  _RETURN=$(curl -o /dev/null -sL -w "%{http_code}" -H "Content-Type: application/json" -d "$_DATA_JSON" -u $GOGS_ADMIN_USER:$GOGS_ADMIN_PASSWORD -X POST http://$GOGS_ROUTE/api/v1/repos/migrate)
#  if [ $_RETURN != "201" ] && [ $_RETURN != "200" ] ; then
#    echo "WARNING: Failed (http code $_RETURN) to import GitHub repo $_REPO to Gogs"
#  else
#    echo "CoolStore GitHub repo imported to Gogs"
#  fi

  # create user
  read -r -d '' _DATA_JSON << EOM
{
    "login_name": "$GOGS_USER",
    "username": "$GOGS_USER",
    "email": "$GOGS_USER@gogs.com",
    "password": "$GOGS_PASSWORD"
}
EOM
  _RETURN=$(curl -o /dev/null -sL -w "%{http_code}" -H "Content-Type: application/json" -d "$_DATA_JSON" -u $GOGS_ADMIN_USER:$GOGS_ADMIN_PASSWORD -X POST http://$GOGS_ROUTE/api/v1/admin/users)
  if [ $_RETURN != "201" ] && [ $_RETURN != "200" ] ; then
    echo "WARNING: Failed (http code $_RETURN) to create user $GOGS_USER"
  else
    echo "Gogs user created: $GOGS_USER"
  fi

  sleep 2

  # import tag to master
#  local _CLONE_DIR=/tmp/$(date +%s)-coolstore-microservice
#  rm -rf $_CLONE_DIR && \
#      git clone http://$GOGS_ROUTE/$GOGS_ADMIN_USER/coolstore-microservice.git $_CLONE_DIR && \
#      cd $_CLONE_DIR && \
#      git branch -m master master-old && \
#      git checkout $GITHUB_REF && \
#      git branch -m $GITHUB_REF master && \
#      git push -f http://$GOGS_ADMIN_USER:$GOGS_ADMIN_PASSWORD@$GOGS_ROUTE/$GOGS_ADMIN_USER/coolstore-microservice.git master && \
#      rm -rf $_CLONE_DIR
}

function import_repo_into_gogs() {
  local _REPO=$1
  local _UID=$2
  local _REPO_NAME=$3

  # import project  GitHub repo
  read -r -d '' _DATA_JSON << EOM
{
  "clone_addr": "$_REPO",
  "uid": $_UID,
  "repo_name": "$_REPO_NAME"
}
EOM

  _RETURN=$(curl -o /dev/null -sL -w "%{http_code}" -H "Content-Type: application/json" -d "$_DATA_JSON" -u $GOGS_ADMIN_USER:$GOGS_ADMIN_PASSWORD -X POST http://$GOGS_ROUTE/api/v1/repos/migrate)
  if [ $_RETURN != "201" ] && [ $_RETURN != "200" ] ; then
    echo "WARNING: Failed (http code $_RETURN) to import GitHub repo $_REPO to Gogs"
  else
    echo "$REPO_NAME GitHub repo imported to Gogs"
  fi
}


# Deploy Jenkins
function deploy_jenkins() {
  echo_header "Deploying Jenkins..."

  if [ "$ARG_EPHEMERAL" = true ] ; then
    oc new-app jenkins-ephemeral -l app=jenkins -p MEMORY_LIMIT=1Gi -n $PRJ_CI
  else
    oc new-app jenkins-persistent -l app=jenkins -p MEMORY_LIMIT=1Gi -n $PRJ_CI
  fi

  sleep 2
  oc set resources dc/jenkins --limits=cpu=1,memory=2Gi --requests=cpu=200m,memory=1Gi -n $PRJ_CI
}

# Deploy Jenkins Slave with Maven configuration.
function deploy_jenkins_maven_slave() {
  echo_header "Deploying Jenkins Slaves..."
  oc process -f http://$GOGS_ROUTE/team/bpms-travel-agency-demo/raw/$GITHUB_REF/support/openshift/jenkins-maven-slave-template.yaml -p DOCKERFILE_REPOSITORY="http://gogs:3000/team/bpms-travel-agency-demo" -p DOCKERFILE_REF="openshift-build" -p DOCKERFILE_CONTEXT=support/openshift/jenkins-maven-slave -n $PRJ_CI | oc create -f - -n $PRJ_CI
}

# TODO: Do we need this function?
function remove_storage_if_ephemeral() {
  local _PROJECT=$1
  if [ "$ARG_EPHEMERAL" = true ] ; then
    # TODO: Remove storage claim of Travel Agency project.
    #remove_storage_claim inventory-postgresql inventory-postgresql-data inventory-postgresql-pv $_PROJECT
    #remove_storage_claim catalog-mongodb mongodb-data mongodb-data-pv $_PROJECT
  fi
}

function deploy_build() {
  local _TEMPLATE="http://$GOGS_ROUTE/team/bpms-travel-agency-demo/raw/$GITHUB_REF/support/openshift/bpms-travel-agency-build.yaml"

  echo_header "Deploying BuildConfig and ImageStreams..."
  oc process -f _TEMPLATE -p APPLICATION_NAME=flight-service -p GIT_URI=http://gogs:3000/team/flight-service-ocp -p MAVEN_MIRROR_URL=$MAVEN_MIRROR_URL -n $PRJ_CI | oc create -f - -n $PRJ_CI
  oc process -f _TEMPLATE -p APPLICATION_NAME=hotel-service -p GIT_URI=http://gogs:3000/team/hotel-service-ocp -p MAVEN_MIRROR_URL=$MAVEN_MIRROR_URL -n $PRJ_CI | oc create -f - -n $PRJ_CI

  # build images

  #for buildconfig in flight-service hotel-service
  #do
  #  oc start-build $buildconfig -n $PRJ_CI
  #  wait_while_empty "$buildconfig build" 180 "oc get builds -n $PRJ_TRAVEL_AGENCY_PROD | grep $buildconfig | grep Running"
  #  sleep 10
  #done

}

function deploy_deploy() {
  local _TEMPLATE="http://$GOGS_ROUTE/team/bpms-travel-agency-demo/raw/$GITHUB_REF/support/openshift/bpms-travel-agency-deploy.yaml"

  echo_header "Deploying DeploymentConfig, Service and Routes..."
  oc process -f _TEMPLATE -p APPLICATION_NAME=flight-service -p APPLICATION_ENV=prod -p IS_NAME=flight-service -p IS_VERSION=latest -p IS_NAMESPACE=$PRJ_CI -n $PRJ_TRAVEL_AGENCY_PROD | oc create -f - -n $PRJ_TRAVEL_AGENCY_PROD
  oc process -f _TEMPLATE -p APPLICATION_NAME=hotel-service -p APPLICATION_ENV=prod -p IS_NAME=hotel-service -p IS_VERSION=latest -p IS_NAMESPACE=$PRJ_CI -n $PRJ_TRAVEL_AGENCY_PROD | oc create -f - -n $PRJ_TRAVEL_AGENCY_PROD

}

function deploy_bpms_ips() {

  echo_header "Deploying JBoss BPM Suite - Intelligent Process Server..."

  #We need to create the service account for the IPS
  oc create serviceaccount processserver-service-account -n $PRJ_TRAVEL_AGENCY_PROD
  oc policy add-role-to-user view system:serviceaccount:$PRJ_TRAVEL_AGENCY_PROD:processserver-service-account
  #And the secret
  oc create secret generic processserver-app-secret --from-file=jgroups.jceks --from-file=keystore.jks -n $PRJ_TRAVEL_AGENCY_PROD
  oc secret add sa/processserver-service-account secret/processserver-app-secret -n $PRJ_TRAVEL_AGENCY_PROD

  #Create the application.
  oc new-app --template=processserver63-postgresql-s2i -p APPLICATION_NAME="bpms-travel-agency-process" -p KIE_SERVER_USER="kieserver" -p KIE_SERVER_PASSWORD="kieserver@01" -p SOURCE_REPOSITORY_URL="http://gogs.$PRJ_CI.svc.cluster.local:3000/team/bpms-travel-agency-demo-repo.git" -p SOURCE_REPOSITORY_REF=fix-maven-build -p CONTEXT_DIR=specialtripsagencyproject -p KIE_CONTAINER_DEPLOYMENT="travel_agency_container=org.specialtripsagency:specialtripsagencyproject:2.0.1" -p MAVEN_MIRROR_URL=$MAVEN_MIRROR_URL_SVC -n $PRJ_TRAVEL_AGENCY_PROD

}

function wait_for_builds_to_complete() {
  # wait for builds
  for buildconfig in coolstore-gw web-ui inventory cart catalog pricing
  do
    wait_while_empty "$buildconfig image" 600 "oc get builds -n $PRJ_COOLSTORE_PROD | grep $buildconfig | grep -v Running"
    sleep 10
  done

  # verify successful builds
  for buildconfig in coolstore-gw web-ui inventory cart catalog pricing
  do
    if [ -z "$(oc get builds -n $PRJ_COOLSTORE_PROD | grep $buildconfig | grep Complete)" ]; then
      echo "ERROR: Build $buildconfig did not complete successfully"
      exit 255
    fi
  done
}

function deploy_pipeline() {
  echo_header "Configuring CI/CD..."

  local _PIPELINE_NAME=bpms-travel-agency-pipeline
  local _TEMPLATE=https://raw.githubusercontent.com/$GITHUB_ACCOUNT/bpms-travel-agency-demo/$GITHUB_REF/support/openshift/bpms-travel-agency-pipeline.yaml

  # TODO: Parameterize our pipeline template.
  #oc process -f $_TEMPLATE --param=PIPELINE_NAME=$_PIPELINE_NAME --param=DEV_PROJECT=$PRJ_INVENTORY --param=TEST_PROJECT=$PRJ_COOLSTORE_TEST --param=PROD_PROJECT=$PRJ_COOLSTORE_PROD --param=GENERIC_WEBHOOK_SECRET=$WEBHOOK_SECRET -n $PRJ_CI | oc create -f - -n $PRJ_CI
  oc process -f $_TEMPLATE | oc create -f - -n $PRJ_CI

  # TODO: Implement a webhook to trigger the pipeline.
  # configure webhook to trigger pipeline
#  read -r -d '' _DATA_JSON << EOM
# {
#  "type": "gogs",
#  "config": {
#    "url": "https://$OPENSHIFT_MASTER/oapi/v1/namespaces/$PRJ_CI/buildconfigs/$_PIPELINE_NAME/webhooks/$WEBHOOK_SECRET/generic",
#    "content_type": "json"
#  },
#  "events": [
#    "push"
#  ],
#  "active": true
#}
#EOM


#  _RETURN=$(curl -o /dev/null -sL -w "%{http_code}" -H "Content-Type: application/json" -d "$_DATA_JSON" -u $GOGS_ADMIN_USER:$GOGS_ADMIN_PASSWORD -X POST http://$GOGS_ROUTE/api/v1/repos/$GOGS_ADMIN_USER/coolstore-microservice/hooks)
#  if [ $_RETURN != "201" ] && [ $_RETURN != "200" ] ; then
#   echo "WARNING: Failed (http code $_RETURN) to configure webhook on Gogs"
#  fi
}

function start_pipeline() {
  oc start-build bpms-travel-agency-pipeline -n $PRJ_CI
}

function verify_build_and_deployments() {
  echo_header "Verifying build and deployments"
  # verify builds in CI
  local _BUILDS_FAILED=false
  for buildconfig in flight-service-binary hotel-service-binary
  do
    if [ -n "$(oc get builds -n $PRJ_CI | grep $buildconfig | grep Failed)" ] && [ -z "$(oc get builds -n $PRJ_CI | grep $buildconfig | grep Complete)" ]; then
      _BUILDS_FAILED=true
      echo "WARNING: Build $project/$buildconfig has failed..."
      #oc start-build $buildconfig -n $PRJ_CI --wait
    fi
  done

  # verify builds in Travel Agency
  local _BUILDS_FAILED=false
  for buildconfig in optaplanner-employee-rostering
  do
    if [ -n "$(oc get builds -n $PRJ_TRAVEL_AGENCY_PROD | grep $buildconfig | grep Failed)" ] && [ -z "$(oc get builds -n $PRJ_TRAVEL_AGENCY_PROD| grep $buildconfig | grep Complete)" ]; then
      _BUILDS_FAILED=true
      echo "WARNING: Build $project/$buildconfig has failed..."
    fi
  done

  # verify deployments
  for project in $PRJ
  do
    local _DC=
    for dc in $(oc get dc -n $project -o=custom-columns=:.metadata.name,:.status.replicas); do
      if [ $dc = 0 ] && [ -z "$(oc get pods -n $project | grep "$dc-[0-9]\+-deploy" | grep Running)" ] ; then
        echo "WARNING: Deployment $project/$_DC in project $project is not complete. Starting a new deployment..."
        oc rollout cancel dc/$_DC -n $project >/dev/null
        sleep 5
        oc rollout latest dc/$_DC -n $project
        oc rollout status dc/$_DC -n $project
      fi
      _DC=$dc
    done
  done
}

function make_idle() {
  echo_header "Idling Services"
  oc idle -n $PRJ --all
}

# GPTE convention
function set_default_project() {
  if [ $LOGGEDIN_USER == 'system:admin' ] ; then
    oc project default >/dev/null
  fi
}

function echo_header() {
  echo
  echo "########################################################################"
  echo $1
  echo "########################################################################"
}

################################################################################
# MAIN: DEPLOY DEMO                                                            #
################################################################################

if [ "$LOGGEDIN_USER" == 'system:admin' ] && [ -z "$ARG_USERNAME" ] ; then
  # for verify and delete, --project-suffix is enough
  if [ "$ARG_COMMAND" == "delete" ] || [ "$ARG_COMMAND" == "verify" ] && [ -z "$ARG_PROJECT_SUFFIX" ]; then
    echo "--user or --project-suffix must be provided when running $ARG_COMMAND as 'system:admin'"
    exit 255
  # deploy command
  elif [ "$ARG_COMMAND" != "delete" ] && [ "$ARG_COMMAND" != "verify" ] ; then
    echo "--user must be provided when running $ARG_COMMAND as 'system:admin'"
    exit 255
  fi
fi

pushd ~ >/dev/null
START=`date +%s`

echo_header "BPM Suite Travel Agency Demo ($(date))"

case "$ARG_COMMAND" in
    delete)
        echo "Delete BPM Suite Travel Agecncy demo ($ARG_DEMO)..."
        oc delete project $PRJ_TRAVEL_AGENCY_PROD $PRJ_CI
        ;;

    verify)
        echo "Verifying BPM Suite Travel Agency demo ($ARG_DEMO)..."
        print_info
        verify_build_and_deployments
        ;;

    idle)
        echo "Idling BPM Suite Travel Agency demo ($ARG_DEMO)..."
        print_info
        make_idle
        ;;

    deploy)
        echo "Deploying BPM Suite Travel Agency demo ($ARG_DEMO)..."

#       create_projects
        configure_project_permissions $PRJ_TRAVEL_AGENCY_PROD

        print_info
#        deploy_nexus
#        wait_for_nexus_to_be_ready
#     	 deploy_gogs

#	       deploy_jenkins
#	       deploy_jenkins_maven_slave
#        deploy_pipeline
#        deploy_build
#	       deploy_deploy
         start_pipeline
#	       deploy_bpms_ips


        if [ "$ARG_RUN_VERIFY" = true ] ; then
          echo "Waiting for deployments to finish..."
          sleep 30
          verify_build_and_deployments
        fi
        ;;

    *)
        echo "Invalid command specified: '$ARG_COMMAND'"
        usage
        ;;
esac

set_default_project
popd >/dev/null

END=`date +%s`
echo
echo "Provisioning done! (Completed in $(( ($END - $START)/60 )) min $(( ($END - $START)%60 )) sec)"
