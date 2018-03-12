import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.security.*
import hudson.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import hudson.model.User
user_list = []
User.getAll().each { user ->
  println user
  user_list.add(['name': "${user.getId()}", 'pass': 'changeme'])
}

println JsonOutput.prettyPrint(JsonOutput.toJson("${user_list}"))
//println hudson.model.User.current()
//println  hudson.model.User.getById('unknown', false).delete()
//User.getById('getId')
