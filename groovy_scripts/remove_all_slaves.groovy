import jenkins.model.*
Jenkins.getInstance().getNodes().each{my_node ->
    Jenkins.getInstance().removeNode(my_node)
}
