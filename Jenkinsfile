node('master'){
    stage('Init') {
            echo 'Go go go'
            sh 'pwd'
            sh 'ls'
            sh 'cat Jenkinsfile'
            sh 'ls seed_job'
            sh 'git branch'
            jobDsl sandbox: true, targets: 'seed_job/seed_of_seed.groovy'
    }
}
