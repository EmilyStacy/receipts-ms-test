#!/bin/bash

# get current script path
scriptDir=$(dirname $0)

functionsPath="$scriptDir/../cloud-foundry/utils-deploy.sh"

# import CF scripts
source $functionsPath

# retrieve script arguments
cfApiUrl=$1
cfApiUsr=$2
cfApiPwd=$3
cfOrg=$4
cfSpace=$5
cfAppDomain=$6
cfAppName=$7
jarPath=$8
cfKeepRollback=$9
httpProxy=${10}
manifestFileName=${11} # only the file name
custDomain=${12}

# set proxy to empty if not set
if [ -z "$httpProxy" ]; then
    httpProxy=""
fi

manifestPath="./devops/epaas/${manifestFileName}"

# default route to app
cfAppRoute="${cfAppName}.${cfAppDomain}"

# track if app route exists
cfAppRouteExists=0 # assume routes do not exist (easier to test for found)


# track staging app name, nv for "new version"
cfAppStageName="${cfAppName}-nv"

# track rollback app name
cfAppRollbackName="${cfAppName}-rollback"
# track if rollback exists
cfAppRollbackExists=1 # assume apps exist (easier to test for not found)

setHttpProxy "$httpProxy"

#login "$cfApiUrl" "$cfApiKey" "$cfOrg" "$cfSpace"

# check if any deployments for app, staging, rollback exist
cfAppExists=$(getAppExists "$cfAppName")
cfAppStageExists=$(getAppExists "$cfAppStageName")
cfAppRollbackExists=$(getAppExists "$cfAppRollbackName")

# check if app route exists
cfAppRouteExists=$(getRouteExists "$cfAppName" "$cfAppDomain")
#check if common route (among datacenters) exists
cfCommonAppRouteExists=0
if [ ! -z "$custDomain" ]
then
    cfCommonAppRouteExists=$(getRouteExists "$cfAppName" "$custDomain")
fi

if [ $cfAppStageExists -eq 1 ] ; then
    echo 'Stage exists: Deleting existing staged application'


    # remote the route before deleting the app
    cfStageAppRouteExists=$(getRouteExists "$cfAppStageName" "$cfAppDomain")

    if [ $cfStageAppRouteExists -eq 1 ]
    then
      unmapRoute "$cfAppStageName" "$cfAppDomain" "$cfAppStageName"
      deleteRoute "$cfAppDomain" "$cfAppStageName"
    fi
    deleteApp "$cfAppStageName" 0
fi

# create app route if not exists
# TIP: the app route is created prior to deploying the app to
#      1) 'reserve' it ASAP
#      2) We want to start the app independently instead of
#         auto-creating the route when we push
if [ $cfAppRouteExists -eq 0 ] ; then
    echo 'Missing: creating app route'
    createRoute "$cfSpace" "$cfAppName" "$cfAppDomain"
fi

if [ $cfCommonAppRouteExists -eq 0 -a ! -z "$custDomain" ] ; then
    echo 'Missing common header route: creating app common header route'
    createRoute "$cfSpace" "$cfAppName" "$custDomain"
fi

echo "Staging application $cfAppStageName ($jarPath)"
# do not auto-start the app (startOnPush)
# TIP: not auto-starting the app allows us time to prepare the app
#      before it's made visible on any route.
#      For example, setting env vars and restaging.
startOnPush=1
pushAppFile "$cfAppStageName" "$manifestPath" "$jarPath" $startOnPush $cfAppDomain

# If your application requires environment variables (very likely will), set them here...
# TIP: always restage the app after environment variables are set
# setAppEnv $cfAppName some.env.key "SomeEnvValue"

# TIP: always restage the app after environment variables are set
# restageApp $cfAppName

#echo 'Starting the app as staging'
#startApp "$cfAppStageName"

echo 'Map new staging app to app main route'
# If app was already deployed, BOTH the main app and
# the new staging app point to the main route. This
# offers a zero downtime swap, but also offers a very
# temporary state of some users potentially going to
# old and new. If this is not desireable, consider
# using a GTM to manage visible routes independent of CF.
mapRoute "$cfAppStageName" "$cfAppDomain" "$cfAppName"

if [ ! -z "$custDomain" ]
then
  mapRoute "$cfAppStageName" "$custDomain" "$cfAppName"
fi

# App already deployed! Rename existing app to rollback
# NOTE: Existing app routes are unmodified during rename
if [ $cfAppExists -eq 1 ] ; then

    if [ $cfAppRollbackExists -eq 1 ] ; then
        echo 'Rollback exists: Deleting existing rollback'

        # remote the route before deleting the app
        unmapRoute "$cfAppRollbackName" "$cfAppDomain" "$cfAppName"
        if [ ! -z "$custDomain" ]
        then
            unmapRoute "$cfAppRollbackName" "$custDomain" "$cfAppName"
        fi

        deleteApp "$cfAppRollbackName" 0
    fi

    echo 'Existing: Renaming existing app for rollback (existing routes unmodified)'

    # renaming the existing version of the app to rollback
    renameApp "$cfAppName" "$cfAppRollbackName"
fi

echo 'Production rollout: Renaming staging app'
# rename to production (existing app was already named -rollback)
renameApp "$cfAppStageName" "$cfAppName"

# remove staging route
unmapRoute "$cfAppName" "$cfAppDomain" "$cfAppStageName"
# delete staging route
deleteRoute "$cfAppDomain" "$cfAppStageName"

if [ $cfAppExists -eq 1 ] ; then
    echo 'Disconnecting and stopping rollback'
    # remove prod route from -rollback
    unmapRoute "$cfAppRollbackName" "$cfAppDomain" "$cfAppName"
    if [ ! -z "$custDomain" ]
    then
      unmapRoute "$cfAppRollbackName" "$custDomain" "$cfAppName"
    fi


    # delete the -rollback app if not needed
    if [ $cfKeepRollback -eq 0 ] ; then
        deleteApp "$cfAppRollbackName" 1
    fi

    # stop the -rollback app if we're keeping it
    # TIP: The app's -rollback instance will now
    #      be available to rollback a release.
    if [ $cfKeepRollback -eq 1 ] ; then
        stopApp "$cfAppRollbackName"
    fi
fi
