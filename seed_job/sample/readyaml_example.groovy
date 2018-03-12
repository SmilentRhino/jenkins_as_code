#!/usr/bin/env groovy
node('master'){
    stage('CheckOut'){
        checkout([$class: 'GitSCM', branches: [[name: 'feature/refactor']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/SmilentRhino/jenkins_as_code.git']]])
    }

    stage("Print") {
        datas = readYaml file: 'seed_job/files/users.yml'
        echo "HELLO WORLD" 
    }
}
