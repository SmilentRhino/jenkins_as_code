pipeline {
    agent any

    stages {
        stage('Build') {
            environment {
                AN_ACCESS_KEY = credentials('my-prefined-secret-text')
            }
            steps {
                echo 'Building..'
                sh 'printenv'
                sh 'python test_password.py'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
