#!/usr/bin/env groovy
node('master'){
    stage("Print") {
        echo "STRING_PARAM: ${STRING_PARAM}" 
        build job: 'string_parameter_job', parameters: [string(name: 'STRING_PARAM', value: 'default_value')]
    }
}
