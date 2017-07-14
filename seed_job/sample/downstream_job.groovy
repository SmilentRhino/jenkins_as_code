#!/usr/bin/env groovy
node('master'){
    stage("Print") {
        build job: 'string_parameter_job', parameters: [string(name: 'STRING_PARAM', value: 'default_value')]
    }
}
