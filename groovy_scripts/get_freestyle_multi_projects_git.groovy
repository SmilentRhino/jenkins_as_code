import hudson.model.*
import jenkins.model.*
import jenkins.triggers.SCMTriggerItem;
import hudson.plugins.git.*

Jenkins.instance.getItems().each { my_item ->
    if (my_item instanceof com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject) {
        multi_branches_job = my_item
        if (multi_branches_job instanceof com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject) {
            //println 'Current branches!'
            print multi_branches_job.getFullName() + ',\t'
            multi_branches_job.getSCMSources().each { my_scm_src ->
                if (my_scm_src.getClass() == jenkins.plugins.git.GitSCMSource) {
                    my_scm_src.getRemoteConfigs().each { my_remote_config ->
                        println my_remote_config.getUrl() + '\t'
                    }
                } else {
                      println 'Not gitscmsource'
                }
            }
        } else {
            println "This job is not multi-branches-job"
            throw new RuntimeException("")
        }
    }
}
