pipeline {

    agent any

       tools {
        maven "MY_MAVEN"
    }

    stages {
        stage ('Clone Repository'){
            steps {
                echo 'downloading github project...'
                git branch: 'master', credentialsId: '1234', url: 'https://github.com/Rufus100Procent/MusicApplicationBetaTesting.git'
            }
        }
        stage('clean') {
            steps {
                  sh 'pwd'
                  sh 'mvn clean'
            }
        }
        stage('build') {
            steps {
                echo 'building...'
                sh 'mvn test-compile '
                echo 'finished building'
            }
        }
        stage('test'){
            steps {
                echo 'starting test.....'
                sh 'mvn surefire:test'
                echo 'finished test'
                script {
                    def testResults = findFiles(glob: 'build/reports/**/*.xml')
                    for(xml in testResults) {
                        touch xml.getPath()
                    }
            }
        }
        stage('packing into war') {
            steps {

                echo 'ROOT.war is ready to be used'
            }
        }
        stage('Deploy') {
            steps {
                sh 'pwd'
                sh 'cp ./target/ROOT.war /artifacts'
                echo 'ROOT.war is now inside tomcat'
            }
        }
    }
    post {
        always {
            echo 'Pipeline completed'
        }
        success {
             echo 'The pipeline was successful'
            }
        failure {
            sh 'Pipeline failed'
        }

    }
}
