import groovy.json.JsonSlurper
json_slurper = new JsonSlurper()
def result = [:]
Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  println it.fullName;
  //println it
  //println it.getScmCheckoutStrategy()
  //println it.getScm().getType(//)
  if (it.getScm().class == hudson.plugins.git.GitSCM) {
      //println it.getTriggers()
    it.getScm().getUserRemoteConfigs().each { myuser_remote_config ->
      println myuser_remote_config.getUrl()
    }
  }
  if (it.getTriggers()) {
    
    it.getTriggers().each { k,v ->
      //v.getScmpoll_spec()
      //println k
      //println v
      println v.getSpec()
    }
  }
//  }
}


import groovy.json.JsonSlurper
import groovy.json.JsonOutput
json_slurper = new JsonSlurper()
def result = [:]
Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  println it.fullName;
  result[it.fullName] = ['repos':[],'triggers':[]]
  if (it.getScm().class == hudson.plugins.git.GitSCM) {
      //println it.getTriggers()
    it.getScm().getUserRemoteConfigs().each { myuser_remote_config ->
      println myuser_remote_config.getUrl()
      result[it.fullName]['repos'] << myuser_remote_config.getUrl()
    }
  }
  if (it.getTriggers()) {

    it.getTriggers().each { k,v ->
      //v.getScmpoll_spec()
      println k
      if (v.class == hudson.triggers.SCMTrigger) {
        result[it.fullName]['triggers'] << v.getSpec()
      }
    }
  }
//  }
}
JsonOutput.toJson(result)
