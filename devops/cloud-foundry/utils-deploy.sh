#!/bin/bash

# Cloud Foundry Deploy Utils
# Version: 0.1.1
# Author: Eric Swanson <eric.ca.swanson@aa.com>

ERR_LOGIN=9001

ERR_APP_CREATE=9101
ERR_APP_RENAME=9102
ERR_APP_PUSH=9103
ERR_APP_START=9104
ERR_APP_RESTAGE=9105
ERR_APP_STOP=9106
ERR_APP_DELETE=9107

ERR_ROUTE_CREATE=9110
ERR_ROUTE_MAP=9111
ERR_ROUTE_UNMAP=9112
ERR_ROUTE_DELETE=9113

export CF_HOME=.

# Environments like Local and Jenkins may require different proxies
# For no proxy, use empty quotes: setHttpProxy ""
function setHttpProxy() {
    httpProxy=$1

    export http_proxy=$httpProxy
    export https_proxy=$httpProxy

    echo "http_proxy: $http_proxy"
    echo "https_proxy: $https_proxy"
}

# CF Login *Assumes to use an "apikey"
# See "Bluemix API Keys" as an example:
# https://console.bluemix.net/iam/#/apikeys
function login() {
  echo $CF_HOME
    apiUrl=$1
    apiKeyUsr=$2
    apiKeyPsw=$3 #passcode if dedicated
    org=$4
    space=$5
    

echo $apiUrl $apiKey $org $space

	echo "Before public login"

    cfOutput=$(cf login -a "$apiUrl" -o "$org" -s "$space" -u "$apiKeyUsr" -p "$apiKeyPsw")
    echo "After public login" $cfOutput
    
    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_LOGIN
    fi
}

# Determine if an app already exists
# Returns "0" if the app does not exist or "1" if it does exist
function getAppExists() {
    appName=$1
    appNotFoundIndicator="not found"
    exists=1 # assume apps exist (easier to test for not found)

    [[ $(cf app "$appName" 2>&1) =~ "$appNotFoundIndicator" ]] && exists=0

    echo $exists
}

# Determine if a route already exists
# Returns "0" if the route does not exist or "1" if it does exist
function getRouteExists() {
    appName=$1
    domain=$2
    routeFoundIndicator="does exist"
    exists=0

    [[ $(cf check-route "$appName" "$domain") =~ $routeFoundIndicator ]] && exists=1

    echo $exists
}

# Maps an application to a route
function mapRoute() {
    appName=$1
    domain=$2
    route=$3

    cfOutput=$(cf map-route "$appName" "$domain" --hostname "$route")

    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_ROUTE_MAP
    fi
}

# Unmaps an application from a route
function unmapRoute() {
    appName=$1
    domain=$2
    route=$3

    cfOutput=$(cf unmap-route "$appName" "$domain" --hostname "$route")

    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_ROUTE_UNMAP
    fi
}

# Creates a new route
function createRoute() {
    space=$1
    appName=$2
    domain=$3

    cfOutput=$(cf create-route "$space" "$domain" --hostname "$appName")

    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_ROUTE_CREATE
    fi
}

# Deletes an existing route
function deleteRoute() {
    domain=$1
    route=$2

    cfOutput=$(cf delete-route "$domain" --hostname "$route" -f)

    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_ROUTE_DELETE
    fi
}

# Deletes an existing app forcefully and optionally
# deletes the routes associated with the app if "deleteRoutes" is set to "1".
# e.g. deleteApp "my-app" 1
function deleteApp() {
    appName=$1
    deleteRoutes=$2

    if [ $deleteRoutes -eq 0 ] ; then
        cfOutput=$(cf delete "$appName" -f)

        echo "$cfOutput"

        failureIndicator="FAILED"
        failed=0

        [[ $cfOutput =~ $failureIndicator ]] && failed=1

        if [ $failed -eq 1 ] ; then
            exit $ERR_APP_DELETE
        fi
    fi

    if [ $deleteRoutes -eq 1 ] ; then
        cfOutput=$(cf delete "$appName" -f -r)

        echo "$cfOutput"

        failureIndicator="FAILED"
        failed=0

        [[ $cfOutput =~ $failureIndicator ]] && failed=1

        if [ $failed -eq 1 ] ; then
            exit $ERR_APP_DELETE
        fi
    fi
}

# Renames an app
function renameApp() {
    oldAppName=$1
    newAppName=$2

    cfOutput=$(cf rename "$oldAppName" "$newAppName")

    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_APP_RENAME
    fi
}

# Pushes an app with optional auto-start
# Set "startOnPush" to "1" to start the application after successful deployment.
# e.g. pushApp "my-app" "./devops/epaas/manifest.yml" 1
function pushApp() {
    appName=$1
    manifestPath=$2
    startOnPush=$3

    if [ $startOnPush -eq 0 ] ; then
        cfOutput=$(cf push "$appName" -f "$manifestPath" --hostname "$appName" --no-start)

        echo "$cfOutput"

        failureIndicator="FAILED"
        failed=0

        [[ $cfOutput =~ $failureIndicator ]] && failed=1

        if [ $failed -eq 1 ] ; then
            exit $ERR_APP_PUSH
        fi
    fi

    if [ $startOnPush -eq 1 ] ; then
        cfOutput=$(cf push "$appName" -f "$manifestPath" --hostname "$appName")

        echo "$cfOutput"

        failureIndicator="FAILED"
        failed=0

        [[ $cfOutput =~ $failureIndicator ]] && failed=1

        if [ $failed -eq 1 ] ; then
            exit $ERR_APP_PUSH
        fi
    fi
}

#parse epaas manifest file for build pack
function getBuildPack() {
  manifestFile=$1
  bp=$(grep "buildpack" $manifestFile)
  bp=${bp#*:}
  eval "$2=$(echo $bp)"
}

#parse epaas manifest file for number of instances
function getAppInstances() {
  manifestFile=$1
  ni=$(grep "instances" $manifestFile)
  ni=${ni#*:}
  eval "$2=$(echo $ni)"
}

# Pushes an app with a file and optional auto-start
# Set "startOnPush" to "1" to start the application after successful deployment.
# e.g. pushApp "my-app" "./devops/epaas/manifest.yml" "./target/my-app-0.0.1-SNAPSHOT.jar" 1
function pushAppFile() {
    appName=$1
    manifestPath=$2
    filePath=$3
    startOnPush=$4
    cfAppDomain=$5

    #cf version 6.25 on Jenkins doesn't handle external build pack in manifest file well for dedicated epaas
    getBuildPack $manifestPath buildPack

    if [ $startOnPush -eq 0 ] ; then
        echo "cf push $appName -b $buildPack -f $manifestPath  -p $filePath -d $cfAppDomain --hostname $appName --no-start"
        cfOutput=$(cf push "$appName" -b "$buildPack" -f "$manifestPath" -p "$filePath" -d "$cfAppDomain" --hostname "$appName" --no-start)

        echo "$cfOutput"

        failureIndicator="FAILED"
        failed=0

        [[ $cfOutput =~ $failureIndicator ]] && failed=1

        if [ $failed -eq 1 ] ; then
            exit $ERR_APP_PUSH
        fi
    fi

    if [ $startOnPush -eq 1 ] ; then
        echo "cf push $appName -b $buildPack -f $manifestPath  -p $filePath -d $cfAppDomain --hostname $appName"
        cfOutput=$(cf push "$appName" -b "$buildPack" -f "$manifestPath" -p "$filePath" -d "$cfAppDomain" --hostname "$appName")

        echo "$cfOutput"

        failureIndicator="FAILED"
        failed=0

        [[ $cfOutput =~ $failureIndicator ]] && failed=1

        if [ $failed -eq 1 ] ; then
            exit $ERR_APP_PUSH
        fi
    fi
}

# Sets an app environment variable
function setAppEnv() {
    appName=$1
    envKey=$2
    envValue=$3

    cf set-env "$appName" "$envKey" "$envValue"
}

# Restages an app
# Useful after "setAppEnv" is called
function restageApp() {
    appName=$1

    cfOutput=$(cf restage "$appName")

    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_APP_RESTAGE
    fi
}

# Starts an app
function startApp() {
    appName=$1

    cfOutput=$(cf start "$appName")

    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_APP_START
    fi
}

# Stops an app
function stopApp() {
    appName=$1

    cfOutput=$(cf stop "$appName")

    echo "$cfOutput"

    failureIndicator="FAILED"
    failed=0

    [[ $cfOutput =~ $failureIndicator ]] && failed=1

    if [ $failed -eq 1 ] ; then
        exit $ERR_APP_STOP
    fi
}
