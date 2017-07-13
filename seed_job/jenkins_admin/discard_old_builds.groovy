#!/usr/bin/env groovy
node('CD'){
    stage("clone") {
        git url: 'https://github.com/SmilentRhino/jenkins_test.git'
        echo 'Cloning...'
    }
    stage('Scan and del') {
        sh 'printenv'
        withCredentials([usernamePassword(credentialsId: "${ADMIN_CREDENTIAL_ID}",
                     usernameVariable: 'ADMIN_USER', passwordVariable: 'ADMIN_TOKEN')]) {
            sh 'printenv'
            sh '/home/ubuntu/pyenv/versions/mylatest_v2/bin/python python_scripts/discard_older_builds.py'
        }
    }
}
