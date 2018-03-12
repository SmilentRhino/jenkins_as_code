import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.security.*
import hudson.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def user_list_path = build.workspace.toString() + '/seed_job/files/users.json'
println ("$user_list_path")
def inputFile = new File("$user_list_path")
def user_list = new JsonSlurper().parse(inputFile)
println (user_list)
def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
user_list.each{ user->
    println ("Ensure ${plugin[0]} ...")
    hudsonRealm.createAccount("$user.name","$user.pass")
}
instance.setSecurityRealm(hudsonRealm)
instance.save()

