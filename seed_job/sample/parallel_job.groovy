#!/usr/bin/env groovy
node('master'){
    stage("Print") {
        def my_jobs = [:]
        my_jobs['1'] = { build job: 'string_parameter_job', parameters: [string(name: 'STRING_PARAM', value: '1')] }
        my_jobs['2'] = { build job: 'string_parameter_job', parameters: [string(name: 'STRING_PARAM', value: '2')] }
        parallel my_jobs
    }
}
