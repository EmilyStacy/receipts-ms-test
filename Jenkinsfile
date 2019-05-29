pipeline {
    libraries { lib 'SharedLibrary' }

    environment {
        http_proxy='http://inetgw.aa.com:9093'
        https_proxy='http://inetgw.aa.com:9093'

        pcfAppName='receipts-ms'
        PCF_ID = credentials('PCF_DEVTEST_KEY')
        PCF_STAGE_PROD_ID = credentials('PCF_STAGE_PROD_KEY')

        PCF_URL='api.system.depaas.qcorpaa.aa.com'
        PCF_TEST_DOMAIN='apps.depaas.qcorpaa.aa.com'
        PCF_STAGE_URL='api.system.sepaas.aa.com'
        PCF_STAGE_DOMAIN='apps.sepaas.aa.com'
        NOTIFYUSERS="DL_eTDS_TeamReceipts@aa.com@aa.com"
        PCF_PROD_URL='api.system.ppepaas.aa.com'
        PCF_PROD_DOMAIN='apps.ppepaas.aa.com'
        PCF_SPACE='Dev'
        PCF_STAGE_SPACE='Stage'
        PCF_PROD_SPACE='Production'
        PCF_ORG ='eTDS'
        CF_HOME="${WORKSPACE}"
        DEPLOY_DETAILS = "<BR>DEPLOY DETAILS: "
        PCF_BLUE= "Temp-FVT-API"
        PCF_GREEN= "FVT-API"
        //SLACK_TOKEN = credentials('SlackToken')
        SLACK_CHANNEL = 'receipts-msg'
        BUILD_DETAILS = "BUILD DETAILS: ${env.JOB_NAME} #${env.BUILD_NUMBER} - ${BUILD_URL} "
        JOB_CAUSES = edtUtil.getCauses()

        slackChannel='receipts-msg'
        cfKeepRollback=0
        nexusCredentials=credentials('Nexus3upload')
        jarPath='./target/${pcfAppName}-*.jar'
        GIT_REPO = "AA-CustTech-Fly/receipts-ms"
        GITHUB = credentials('Jing-GHE-Receipts')
        SONAR_API_KEY = credentials('SONAR_API_KEY')
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
                sh "mvn -s .settings.xml clean install -DskipTests=false"

            }

        }

        stage('Unit Tests') {
            steps {
                sh "mvn -s .settings.xml test"
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage ('sonar code scan') {
            when {
                branch 'master'
            }
            steps {
                sh """ mvn -s .settings.xml sonar:sonar"""
            }
        }

        stage ('sonar code review') {
            when {
                changeRequest()
            }
            steps {
                sh """
                    mvn --settings .settings.xml \
                    sonar:sonar \
                    -Dsonar.login=$SONAR_API_KEY \
                    -Dsonar.password="" \
                    -Dsonar.analysis.mode=preview \
                    -Dsonar.github.pullRequest=${env.CHANGE_ID} \
                    -Dsonar.github.repository=$GIT_REPO \
                    -Dsonar.github.login=$GITHUB_USR \
                    -Dsonar.github.oauth=$GITHUB_PSW \
                    -Dsonar.github.endpoint=https://ghe.aa.com/api/v3/ \
                """
            }
        }

        stage('coverage') {
            steps {
                sh "mvn -s .settings.xml jacoco:check"
                jacoco()
            }
        }

        stage('dev') {
            steps {
                sh "cf login -a $PCF_URL -u $PCF_ID_USR -p $PCF_ID_PSW -o $PCF_ORG -s $PCF_SPACE"
                sh "cf push receipts-ms-dev -f manifest.yml"
            }
            post {
                success {
                    script {
                        createChangeRequest(
                            appName: "Receipts",      	        //Application name based on what is shown in Archer
                            appVersion: "1.0.0",			    //Version number of the Application deployed to Production
                            team: "Receipts",             	    //Cherwell Team Name
                            location: "DFW",                    //Location of the Datacenter where the Production Application resides
                            requestingEmployeeId: "00854495",   //Default Requestor and Owner of the Change Ticket
                            finalDisposition: "Successful",     //Set to Successful if Production succeeded, or Failed if attempt failed 
                            description: "Receipts MS", 
                            cherwellInstance: "stage" 
                        )
                    }
                }
            }
        }

        stage('stage') {
        	when {
        		branch 'master'
        	}
            steps {
                sh "cf login -a '$PCF_PROD_URL' -u '$PCF_STAGE_PROD_ID_USR' -p '$PCF_STAGE_PROD_ID_PSW' -o '$PCF_ORG' -s '$PCF_STAGE_SPACE'"
                sh "cf push receipts-ms-stagep-green -f manifest.yml"
                //sh "cf push receipts-ms-stagep-green -f manifest.yml"
            }

        }

        stage('prod') {
            when {
                branch 'master'
            }
            steps {
                sh "cf login -a $PCF_PROD_URL -u $PCF_STAGE_PROD_ID_USR -p $PCF_STAGE_PROD_ID_PSW -o $PCF_ORG -s $PCF_PROD_SPACE"
                sh "cf push receipts-ms-prodp-green -f manifest.yml"
            }
            post {
                success {
                    script {
                        createChangeRequest(
                            appName: "Receipts",      	        //Application name based on what is shown in Archer
                            appVersion: "1.0.0",			    //Version number of the Application deployed to Production
                            team: "Receipts",             	    //Cherwell Team Name
                            location: "DFW",                    //Location of the Datacenter where the Production Application resides
                            requestingEmployeeId: "00854495",   //Default Requestor and Owner of the Change Ticket
                            finalDisposition: "Successful",     //Set to Successful if Production succeeded, or Failed if attempt failed 
                            description: "Receipts MS", 
                        )
                    }
                }
            }
        }
    }
}
