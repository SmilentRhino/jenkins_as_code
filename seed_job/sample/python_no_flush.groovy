#!/usr/bin/env groovy
node('master'){
    stage("Print") {
        git url: 'https://github.com/SmilentRhino/jenkins_test.git'
        sh "ls"
        sh "/usr/bin/python python_scripts/python_no_flush.py"
    }
}
