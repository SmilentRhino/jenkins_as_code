#!/usr/bin/env groovy
node('master'){
    stage("Print") {
        echo "STRING_PARAM: ${STRING_PARAM}" 
    }
}
