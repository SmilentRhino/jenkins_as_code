import hudson.model.*
import jenkins.model.*
import jenkins.triggers.SCMTriggerItem;
import hudson.plugins.git.*

Jenkins.instance.getItems().each { my_item ->
  if (my_item instanceof com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject) {
        multi_branches_job = my_item
        if (multi_branches_job instanceof com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject) {
            println 'Current branches!'
            current_branches = multi_branches_job.getItems()
            new_branches = current_branches
            if (new_branches) {
                println 'Currently we have following branches.'
                println new_branches
                println "Update job branches options"
                new_branches.each { new_branch ->
                    new_branch.getSCMs().each { scm ->
                        if (scm instanceof GitSCM) {
                            scm.getRepositories().each { repository->
                                repository.getURIs().each { url ->
                                    println url
                                    scm.getBranches().each { branchSpec ->
                                        println branchSpec.getName()
                                        println repository.getName() + "/" + branchSpec.getName()
                                        println branchSpec.matches(repository.getName() + "/" + branchSpec.getName())
                                        if (!branchSpec.matches(repository.getName() + "/" + branchSpec.getName())) {
                                            branchSpec.setName(repository.getName() + "/" + branchSpec.getName())
                                            println 'New branch options:' + branchSpec.getName()

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                println 'No new branches.'
            }
            return
        } else {
            println "This job is not multi-branches-job"
            throw new RuntimeException("")
        }
  }
}
