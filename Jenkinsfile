pipeline {
    libraries { lib 'SharedLibrary' }

    environment {
        http_proxy='http://inetgw.aa.com:9093'
        https_proxy='http://inetgw.aa.com:9093'

        pcfAppName='receipts-ms'
        deployDevAppName="$pcfAppName" + "${BRANCH_NAME == 'master' ? '' : "-" + BRANCH_NAME.replaceAll('_','-')}" + "-dev"
        
        PCF_DEVTEST_ID = credentials('PCF_DEVTEST_KEY')
        PCF_STAGE_PROD_ID = credentials('PCF_STAGE_PROD_KEY')

        PCF_DEV_TEST_URL='api.system.depaas.qcorpaa.aa.com'
        PCF_DEVTEST_DOMAIN='apps.depaas.qcorpaa.aa.com'
        PCF_PRODP_URL='api.system.ppepaas.aa.com'
        PCF_PRODC_URL='api.system.cpepaas.aa.com'
        PCF_PRODP_DOMAIN='apps.ppepaas.aa.com'
        PCF_PRODC_DOMAIN='apps.cpepaas.aa.com'
        PCF_GTM_DOMAIN='apps.aa.com'
        PCF_DEV_SPACE='Dev'
        PCF_STAGE_SPACE='Stage'
        PCF_PROD_SPACE='Production'
        PCF_ORG ='eTDS'
        CF_HOME="${WORKSPACE}"
        // NOTIFYUSERS="DL_eTDS_TeamReceipts@aa.com"
        // PCF_BLUE= "Temp-FVT-API"
        // PCF_GREEN= "FVT-API"
        // SLACK_TOKEN = credentials('SlackToken')
        // SLACK_CHANNEL = 'receipts-msg'
        // slackChannel='receipts-msg'
        // BUILD_DETAILS = "BUILD DETAILS: ${env.JOB_NAME} #${env.BUILD_NUMBER} - ${BUILD_URL} "
        // DEPLOY_DETAILS = "<BR>DEPLOY DETAILS: "
        // JOB_CAUSES = edtUtil.getCauses()

        cfKeepRollback=0
        nexusCredentials=credentials('Nexus3upload')
        jarPath='./target/${pcfAppName}-*.jar'
        GIT_REPO = "AA-CustTech-Fly/receipts-ms"
        GITHUB = credentials('Jing-GHE-Receipts')
        GIT_URL = scm.getUserRemoteConfigs()[0].getUrl()
        SONARQUBE_API_KEY = credentials('RECEIPTS_SONARQUBE_API_KEY')
        SONARQUBE_PROJECT_KEY = 'tr.receipts-ms'
        SONARQUBE_PROJECT_NAME = 'tr.receipts-ms'
	pom = readMavenPom file: 'pom.xml'
    }
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    agent {
        label 'Builder'
    }

    stages {
        stage('loadXml') {
            steps {
                // Credential has to exist in Jenkins already.  The function will replace the user and password in the settings.xml template and create the .settings.xml locally for use
                script {
                    loadSettingsXml("$nexusCredentials_USR","$nexusCredentials_PSW")
                }

            }

        }

        stage('build') {
            steps {
                sh "mvn -Dskip.unit.tests=true -s .settings.xml clean install"

            }

        }

        stage('unit tests') {
            steps {
                sh "mvn -Dskip.unit.tests=false -s .settings.xml test -Punit-test"
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('coverage') {
            steps {
                sh "mvn -Dskip.integration.tests=true -s .settings.xml verify -P coverage jacoco:check"
                jacoco()
            }
        }

        stage ('sonar code scan') {
    		steps {
                sh "mvn -s .settings.xml test verify surefire-report:report-only sonar:sonar -Dsonar.host.url=${env.SonarQubeOSS} -Dsonar.projectKey=${SONARQUBE_PROJECT_KEY} -Dsonar.projectName=${SONARQUBE_PROJECT_NAME} -Dsonar.login=${SONARQUBE_API_KEY} -Dsonar.password="
            }
	   }

       stage('coverity setup') {
            when {
                branch 'master'
            }
            steps {
                 script {
                      coverityScan(
                            APP_NAME: 'receipts-ms',
                            CONFIG: ['java'],
                            COV_ENV: 'prod',
                            NOTIFYUSERS: "DL_Ticketing_Receipts"
                      )
                 }
            }
        }

        stage('deploy dev') {
            when {
                // if it is a branch and not a PR
                allOf {
                    not {
                        changeRequest()
                    }
                }
            }
            steps {
                sh "cf login -a $PCF_DEV_TEST_URL -u $PCF_DEVTEST_ID_USR -p $PCF_DEVTEST_ID_PSW -o $PCF_ORG -s $PCF_DEV_SPACE"

                sh """
                    chmod u+x ./devops/epaas/deploy.sh
                    ./devops/epaas/deploy.sh ${PCF_DEV_TEST_URL} $PCF_DEVTEST_ID_USR $PCF_DEVTEST_ID_PSW ${PCF_ORG} ${PCF_DEV_SPACE} ${PCF_DEVTEST_DOMAIN} ${deployDevAppName} ${jarPath} ${cfKeepRollback} ${http_proxy} manifest-dev.yml
                  """                  
            }
        }

        stage('integration tests') {
            when {
                // if it is a branch and not a PR
                allOf {
                    not {
                        changeRequest()
                    }
                }
            }
            steps {
                sh """
                    mvn -s .settings.xml verify -Pintegration-tests -Dcucumber.options='--tags @TicketAndFees' -Dbranch.application.url='https://'${deployDevAppName}.${PCF_DEVTEST_DOMAIN}
                  """
                  
                publishHTML target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'target/cucumberReport',
                    reportFiles: 'index.html',
                    reportName: 'Cucumber Rpt'
                ]
            }
        }

        
//        stage ('job:bff-e2e') {
//            when {
//                branch 'master'
//            }
//            
//            steps {
//                build job: 'AA-DevOps/AA-CustTech-Fly/AA-CT-Fly-Receipts/CrossApps/bff-e2e'
//            }
//        }

        stage('deploy stage') {
            when {
                branch 'master'
            }

            steps {
                sh "cf login -a '$PCF_PRODC_URL' -u '$PCF_STAGE_PROD_ID_USR' -p '$PCF_STAGE_PROD_ID_PSW' -o '$PCF_ORG' -s '$PCF_STAGE_SPACE'"
                sh """
                    chmod u+x ./devops/epaas/deploy.sh
                    ./devops/epaas/deploy.sh ${PCF_PRODC_URL} $PCF_STAGE_PROD_ID_USR $PCF_STAGE_PROD_ID_PSW ${PCF_ORG} ${PCF_STAGE_SPACE} ${PCF_PRODC_DOMAIN} ${pcfAppName}-stage ${jarPath} ${cfKeepRollback} ${http_proxy} manifest-stage.yml ${PCF_GTM_DOMAIN}
                 """
                sh "cf login -a '$PCF_PRODP_URL' -u '$PCF_STAGE_PROD_ID_USR' -p '$PCF_STAGE_PROD_ID_PSW' -o '$PCF_ORG' -s '$PCF_STAGE_SPACE'"
                sh """
                    chmod u+x ./devops/epaas/deploy.sh
                    ./devops/epaas/deploy.sh ${PCF_PRODP_URL} $PCF_STAGE_PROD_ID_USR $PCF_STAGE_PROD_ID_PSW ${PCF_ORG} ${PCF_STAGE_SPACE} ${PCF_PRODP_DOMAIN} ${pcfAppName}-stage ${jarPath} ${cfKeepRollback} ${http_proxy} manifest-stage.yml ${PCF_GTM_DOMAIN}
                 """
            }
                    }

        stage('deploy prod') {
            when {
                branch 'master'
            }

            steps {
                sh "cf login -a $PCF_PRODC_URL -u $PCF_STAGE_PROD_ID_USR -p $PCF_STAGE_PROD_ID_PSW -o $PCF_ORG -s $PCF_PROD_SPACE"
                sh """
                    chmod u+x ./devops/epaas/deploy.sh
                    ./devops/epaas/deploy.sh ${PCF_PRODC_URL} $PCF_STAGE_PROD_ID_USR $PCF_STAGE_PROD_ID_PSW ${PCF_ORG} ${PCF_PROD_SPACE} ${PCF_PRODC_DOMAIN} ${pcfAppName} ${jarPath} ${cfKeepRollback} ${http_proxy} manifest-prod.yml ${PCF_GTM_DOMAIN}
                 """
                sh "cf login -a $PCF_PRODP_URL -u $PCF_STAGE_PROD_ID_USR -p $PCF_STAGE_PROD_ID_PSW -o $PCF_ORG -s $PCF_PROD_SPACE"
                sh """
                    chmod u+x ./devops/epaas/deploy.sh
                    ./devops/epaas/deploy.sh ${PCF_PRODP_URL} $PCF_STAGE_PROD_ID_USR $PCF_STAGE_PROD_ID_PSW ${PCF_ORG} ${PCF_PROD_SPACE} ${PCF_PRODP_DOMAIN} ${pcfAppName} ${jarPath} ${cfKeepRollback} ${http_proxy} manifest-prod.yml ${PCF_GTM_DOMAIN}
                 """
            }

            post {
                success {
                    script {
                    	POM_VERSION = pom.version.replace('-SNAPSHOT', '')

                        createChangeRequest(
                                appName: "Receipts",      	        //Application name based on what is shown in Archer
                                appVersion: "${POM_VERSION}",       //Version number of the Application deployed to Production
                                team: "Fly - Ancillaries Receipts", //Cherwell Team Name
                                location: "DFW",                    //Location of the Datacenter where the Production Application resides
                                requestingEmployeeId: "00854495",   //Default Requestor and Owner of the Change Ticket
                                finalDisposition: "Successful",     //Set to Successful if Production succeeded, or Failed if attempt failed
                                description: "Receipts MS",
                                cherwellInstance: "prod"
                        )
                    }
                }
            }
        }
        
        stage('tag build') {
            when{
                branch 'master'
            }
            steps {
                echo "*****Tag the Deployment*****"
                script {
                    SCM_URL = "$GIT_URL".trim().minus("https://")
                    
                    APPLICATION_VERSION = pom.version.replace('SNAPSHOT', BUILD_NUMBER)
                }
                               
                sh "git tag ${pcfAppName}-$APPLICATION_VERSION"
                sh "git push https://$GITHUB_USR:$GITHUB_PSW@$SCM_URL $pcfAppName-$APPLICATION_VERSION"
            }
        }
        
    }
}
