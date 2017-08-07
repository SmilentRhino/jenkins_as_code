import jenkins.model.*
//getSyncBranchesTrigger() is deprecated after version 0.3
Jenkins.instance.getAllItems().each { my_item ->
  //println my_item
  if (my_item.class == com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject){
    //println 'TEMP'
    print my_item.getFullName() + '\t'  + my_item.getSyncBranchesTrigger().class + '\t' + my_item.getSyncBranchesTrigger().getSpec() + '\t'
    my_item.getSCMSources().each { my_scm_src ->
      //print my_scm_src
      if (my_scm_src.getClass() == jenkins.plugins.git.GitSCMSource) {
        my_scm_src.getRemoteConfigs().each { my_remote_config ->
          print my_remote_config.getUrl() + '\t'
        }
      } else {
        println 'Not gitscmsource'
      }
    }
        my_item.getTemplate().triggers.each { k,v ->
      print k.class.toString() + '\t' + v.getSpec() + '\t'
    }
    println ''
    //println my_item.template.getAllActions()+
    //println my_item.template.getItems()
    //println my_item.template.getScm()
    //println 'TEMP_OVER'
  }
}
