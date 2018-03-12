import jenkins.model.*

Jenkins.instance.items.findAll { job ->
    job.delete()
}
