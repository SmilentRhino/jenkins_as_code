#!/usr/bin/env groovy
node('master'){
    stage("Print") {
        echo "CREDENTIAL_PARAM_ID: ${CREDENTIAL_PARAM_ID}" 
        withCredentials([usernamePassword(credentialsId: "${CREDENTIAL_PARAM_ID}",
                     usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            echo "$env.USERNAME"
            echo "$env.PASSWORD"
        }
        withCredentials([usernamePassword(credentialsId: "${CREDENTIAL_PARAM_ID}",
                             usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD'),
                         string(credentialsId: "${CREDENTIAL_PARAM_ID}",
                             variable: 'SAME_CREDENTIAL'),]) {
            sh 'echo $PASSWORD'
            echo "${env.SAME_CREDENTIAL}"
        }
    }
}
