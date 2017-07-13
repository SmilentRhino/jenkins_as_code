#!/usr/bin/env groovy
node('master'){
    stage("Print") {
        echo "HELLO WORLD" 
    }
}
