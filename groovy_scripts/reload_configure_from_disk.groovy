Jenkins.instance.getAllItems().each { my_item ->
  //println my_item
  if (my_item.class == com.github.mjdetullio.jenkins.plugins.multibranch.FreeStyleMultiBranchProject){
    //println 'TEMP'
    println my_item.getFullName()
    //my_item.save()
    my_item.doReload()
  }
}
