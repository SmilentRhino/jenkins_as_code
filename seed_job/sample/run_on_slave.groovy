#!/usr/bin/env groovy
node('slave'){
    stage("Print") {
        echo "HELLO WORLD" 
    }
    stage("Print") {
        node('master') {
            echo "HELLO WORLD" 
        }
    }
    stage("Print") {
        node('slave') {
            echo "HELLO WORLD" 
        }
    }
}
