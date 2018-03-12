node('master'){
    stage('Init') {
            echo 'Go go go'
            sh 'pwd'
            sh 'ls'
            sh 'cat Jenkinsfile'
            sh 'git branch'
            jobDsl sandbox: true, targets: 'seed_job/seed_of_seed.groovy'
    }
}
