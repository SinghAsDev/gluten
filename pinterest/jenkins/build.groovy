pipeline {
    agent any
    environment {  
        IS_JENKINS_BUILD = "true"
    } 
    stages {
        stage ('Build image') {
            parallel {
                // stage('linux/aarch64') {
                //     agent {
                //         label "arm-worker"
                //     }
                //     steps {
                //         echo "Not ready to build aarch64 image"
                //         checkout([
                //             $class: 'GitSCM',
                //             branches: [[name: params.VELOX_BRANCH]],
                //             extensions: [
                //                     [$class: 'RelativeTargetDirectory', relativeTargetDir: 'ep/build-velox/build/velox_ep'],
                //                     [$class: 'CloneOption', depth: 100, honorRefspec: true, noTags: true, reference: '', shallow: true, timeout: 30]
                //             ],
                //             userRemoteConfigs: [[ url: 'org-86262824@github.com:pinternal/velox.git', credentialsId: '145755']]
                //         ])
                //         sh "DOCKER_BUILDKIT=1 docker build --no-cache --file pinterest/jenkins/aarch64/Dockerfile --output out ."
                //         script {
                //             def files = findFiles(glob: '**/gluten-velox-bundle-spark3*')
                //             for (file in files) {
                //                 def fullPath = file.getPath()  
                //                 def parentDir = new File(fullPath).parent  
                //                 def fullName = file.getName()  
                //                 def fileName = fullName.substring(0, fullName.lastIndexOf('.'))  
                //                 def fileExt = fullName.substring(fullName.lastIndexOf('.'))  
                //                 def newFileName = parentDir + '/' + fileName + '_' + env.BUILD_NUMBER + fileExt  
                //                 sh "mv ${fullPath} ${newFileName}"  
                //             }
                //         }
                //         sh "aws s3 cp ${env.WORKSPACE}/out s3://datausers/jenkins/gluten/jars/aarch64/ --recursive --exclude '*' --include 'gluten-velox-bundle-spark3*.jar'"
                //     }
                // }
                stage ('linux/amd64 - Ubuntu 20.04') {
                    agent {
                        label "amd-worker"
                    }
                    steps {
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: params.VELOX_BRANCH]],
                            extensions: [
                                    [$class: 'RelativeTargetDirectory', relativeTargetDir: 'ep/build-velox/build/velox_ep'],
                                    [$class: 'CloneOption', depth: 100, honorRefspec: true, noTags: true, reference: '', shallow: true, timeout: 30]
                            ],
                            userRemoteConfigs: [[ url: 'org-86262824@github.com:pinternal/velox.git', credentialsId: '145755']]
                        ])
                        sh "DOCKER_BUILDKIT=1 docker build --no-cache --file pinterest/jenkins/amd64/Ubuntu20/Dockerfile --output out ."
                        script {
                            def files = findFiles(glob: '**/gluten-velox-bundle-spark3*')
                            for (file in files) {
                                def fullPath = file.getPath()  
                                def parentDir = new File(fullPath).parent  
                                def fullName = file.getName()  
                                def fileName = fullName.substring(0, fullName.lastIndexOf('.'))  
                                def fileExt = fullName.substring(fullName.lastIndexOf('.'))  
                                def newFileName = parentDir + '/' + fileName + '_' + env.BUILD_NUMBER + fileExt  
                                sh "mv ${fullPath} ${newFileName}"  
                            }
                        }
                        sh "aws s3 cp ${env.WORKSPACE}/out s3://datausers/jenkins/gluten/jars/amd64/ --recursive --exclude '*' --include 'gluten-velox-bundle-spark3*.jar'"
                    }
                }
                // stage ('linux/amd64 - Ubuntu 18.04') {
                //     agent {
                //         label "amd-worker"
                //     }
                //     steps {
                //         checkout([
                //             $class: 'GitSCM',
                //             branches: [[name: params.VELOX_BRANCH]],
                //             extensions: [
                //                     [$class: 'RelativeTargetDirectory', relativeTargetDir: 'ep/build-velox/build/velox_ep'],
                //                     [$class: 'CloneOption', depth: 100, honorRefspec: true, noTags: true, reference: '', shallow: true, timeout: 30]
                //             ],
                //             userRemoteConfigs: [[ url: 'org-86262824@github.com:pinternal/velox.git', credentialsId: '145755']]
                //         ])
                //         sh "DOCKER_BUILDKIT=1 docker build --no-cache --file pinterest/jenkins/amd64/Ubuntu18/Dockerfile --output out ."
                //         script {
                //             def files = findFiles(glob: '**/gluten-velox-bundle-spark3*')
                //             for (file in files) {
                //                 def fullPath = file.getPath()  
                //                 def parentDir = new File(fullPath).parent  
                //                 def fullName = file.getName()  
                //                 def fileName = fullName.substring(0, fullName.lastIndexOf('.'))  
                //                 def fileExt = fullName.substring(fullName.lastIndexOf('.'))  
                //                 def newFileName = parentDir + '/' + fileName + '_' + env.BUILD_NUMBER + fileExt  
                //                 sh "mv ${fullPath} ${newFileName}"  
                //             }
                //         }
                //         sh "aws s3 cp ${env.WORKSPACE}/out s3://datausers/jenkins/gluten/jars/amd64/--recursive --exclude '*' --include 'gluten-velox-bundle-spark3*.jar'"
                //     }
                // }
            }
        }
    }
    post {
        // Clean after build
        always {
            cleanWs()
        }
    }
}
