job('seed_dsl') {
    steps {
        dsl {
            text(readFileFromWorkspace('seed_job.groovy'))
            removeAction('DELETE')
        }
    }
}
