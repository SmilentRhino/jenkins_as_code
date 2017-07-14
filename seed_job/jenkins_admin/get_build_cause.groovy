#!/usr/bin/env groovy
node('CD'){
    stage("clone") {
        git url: 'https://github.com/SmilentRhino/jenkins_test.git'
        echo 'Cloning...'
    }
    stage('Get build Cause') {
        withCredentials([usernamePassword(credentialsId: "${ADMIN_CREDENTIAL_ID}",
                     usernameVariable: 'ADMIN_USER', passwordVariable: 'ADMIN_TOKEN')]) {
            sh '/home/ubuntu/pyenv/versions/mylatest_v2/bin/python python_scripts/get_build_cause.py'
        }
    }
}
