import jenkins.model.*
Jenkins.instance.getItems().each { my_item ->
  if (my_item instanceof com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject) {
      println my_item
  }
}
