pipeline {
    agent any
    stages {
        stage('Clonde') {
            steps {
                git credentialsId: '33e35b80-2db3-4f65-ba2f-621628b51904', url: 'https://github.com/SmilentRhino/op_mac.git'
                echo 'Clone..'
            }        
        }
        stage('Build') {
            steps {
                sh 'ls'
            }
        }
        stage('Test') {
            environment {
                AN_ACCESS_KEY = credentials('bff24bf8-a05c-4bc7-8b36-b89fc0f80d8a')
            }
            steps {
                sh 'printenv'
                sh '/usr/bin/python test_password.py'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
