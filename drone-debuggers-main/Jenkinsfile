pipeline {
    agent any
    environment {
        SCANNER_HOME = tool name: 'sonarqube-scanner-6.0.0.4432', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
    }

    tools {
          jdk 'jdk-22'
          maven 'maven-3.9.8'
          nodejs 'nodejs-20.15.0'
    }

    stages {
        stage('Build') {
            parallel {
                stage('Backend') {
                    stages {
                        stage('Set Version') {
                            steps {
                                dir('backend') {
                                    script {
                                        def newVersion = BRANCH_NAME.replaceAll(/[^a-zA-Z0-9]/, '-')
                                        sh "mvn versions:set -DgenerateBackupPoms=false -DnewVersion=0.0.0-$newVersion"
                                    }
                                }
                            }
                        }
                        stage('Build') {
                            steps {
                                dir('backend') {
                                    sh "mvn -Dmaven.test.failure.ignore=true clean package"
                                }
                            }
                            post {
                                success {
                                    dir('backend') {
                                        junit '**/target/surefire-reports/TEST-*.xml'
                                        archiveArtifacts 'target/*.jar'
                                    }
                                }
                            }
                        }
                    }
                }
                stage('Frontend') {
                    stages {
                        stage('Set Version') {
                            steps {
                                dir('frontend') {
                                    script {
                                        def newVersion = BRANCH_NAME.replaceAll(/[^a-zA-Z0-9]/, '-')
                                        sh "npm --allow-same-version verison 0.0.0-$newVersion"
                                    }
                                }
                            }
                        }
                        stage('Build') {
                            steps {
                                dir('frontend') {
                                    sh """
                                        npm install
                                        npm run build-prod
                                        zip -r app.zip dist
                                    """
                                }
                            }
                            post {
                                success {
                                    dir('frontend') {
                                        archiveArtifacts 'app.zip'
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('SonarQube') {
            steps {
                withSonarQubeEnv('Calypso Binar SonarQube Server') {
                    script {
                        if (env.CHANGE_ID) {
                            echo "Building pull request. PR number: ${env.CHANGE_ID}, source branch: ${env.CHANGE_BRANCH}, target branch: ${env.CHANGE_TARGET}"
                            sh "${SCANNER_HOME}/bin/sonar-scanner -Dsonar.pullrequest.key=${env.CHANGE_ID} -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH} -Dsonar.pullrequest.base=${env.CHANGE_TARGET} -Dsonar.qualitygate.wait=true"
                        } else {
                            echo "Building regular branch ${env.BRANCH_NAME}."
                            sh "${SCANNER_HOME}/bin/sonar-scanner -Dsonar.branch.name=${env.BRANCH_NAME} -Dsonar.qualitygate.wait=true"
                        }
                    }
                }
            }
        }
    }
    post {
        failure {
            emailext body: '''$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS:
Check console output at $BUILD_URL to view the results.''', recipientProviders: [culprits(), brokenBuildSuspects(), brokenTestsSuspects()], subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!'
        }
    }
}
