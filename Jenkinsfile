node('master'){
    stage('Init') {
            echo 'Go go go'
            sh 'pwd'
            sh 'ls'
            sh 'cat Jenkinsfile'
            jobDsl targets: 'seed_job/seed_job.groovy'
    }
}
