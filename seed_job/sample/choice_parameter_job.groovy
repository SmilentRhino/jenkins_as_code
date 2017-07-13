#!/usr/bin/env groovy
node('master'){
    stage("Print") {
        echo "CHOICE_PARAM: ${CHOICE_PARAM}" 
    }
}
