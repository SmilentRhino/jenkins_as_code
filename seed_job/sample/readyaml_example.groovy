#!/usr/bin/env groovy
node('master'){
    scm {
        git {
            remote {
                url('https://github.com/SmilentRhino/jenkins_as_code.git')
            }
            branch('feature/refactor')
        }
    }
    stage("Print") {
        datas = readYaml file: 'seed_job/files/users.yml'
        echo "HELLO WORLD" 
    }
}
