node('master'){
    stage('Checkout'){
        git branch: 'feature/refactor', url: 'https://github.com/SmilentRhino/jenkins_as_code.git'
    }
    stage('UpdateSeedDSL') {
            echo 'Go go go'
            sh 'pwd'
            sh 'ls'
            sh 'cat Jenkinsfile'
            sh 'ls seed_job'
            sh 'git branch'
            jobDsl sandbox: true, targets: 'seed_job/seed_of_seed.groovy'
    }
    stage('ExecuteDSL'){
        build 'seed_dsl'
    }
}
