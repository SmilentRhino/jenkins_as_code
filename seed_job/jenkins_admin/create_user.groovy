import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.security.*
import hudson.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.cloudbees.plugins.credentials.CredentialsProvider
//Get users.json 
def user_list_path = build.workspace.toString() + '/seed_job/files/users.json'
println ("$user_list_path")
def inputFile = new File("$user_list_path")
def user_list = new JsonSlurper().parse(inputFile)
println (user_list)

//Get current users
def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
def allUsers = hudsonRealm.getAllUsers()
def userIdList = []
allUsers.each{ user->
    userIdList.add(user.getId())
}
println ("$userIdList")
println ("$allUsers")

//Check if user property has changed
boolean CheckChanges() {
    //Not implement yet
}

//Ensure user present or absent
user_list.each{ user->
    if (user.name in userIdList) {
        if (user?.state == 'present') {
            println ("$user.name already exists, skip creating...")
            CheckChanges()
        }
        else{
            println ("Removing $user.name")
            hudsonRealm.getUser("$user.name").delete()
        }
    } else{
        if (user?.state == 'present') {
            println ("Creating user $user.name...")
            hudsonRealm.createAccount("$user.name","$user.pass")
        }
        else{
            //Doing nothing
        }
    }
}
instance.setSecurityRealm(hudsonRealm)

//Create auth strategy
auth_strategy = instance.authorizationStrategy
def CreateAuthStrategy(sid, matrix){
    if (matrix?.overall?.administer) {
        auth_strategy.add(Hudson.ADMINISTER, sid)
    }
    if (matrix?.overall?.read) {
        auth_strategy.add(Hudson.READ, sid)
    }
    if (matrix?.credential?.create) {
        println ('YEYEYE')
        auth_strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.CREATE, sid)
    }else{
        println ('NONONO')
    }
    if (matrix?.credential?.delete) {
        auth_strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.DELETE, sid)
    }
    if (matrix?.credential?.manage_domains) {
        auth_strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.MANAGE_DOMAINS, sid)
    }
    if (matrix?.credential?.update) {
        auth_strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.UPDATE, sid)
    }
    if (matrix?.credential?.view) {
        auth_strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.VIEW, sid)
    }
    if (matrix?.agent?.build) {
        auth_strategy.add(hudson.model.Computer.BUILD, sid)
    }
    if (matrix?.agent?.configure) {
        auth_strategy.add(hudson.model.Computer.CONFIGURE, sid)
    }
    if (matrix?.agent?.connect) {
        auth_strategy.add(hudson.model.Computer.CONNECT, sid)
    }
    if (matrix?.agent?.create) {
        auth_strategy.add(hudson.model.Computer.CREATE, sid)
    }
    if (matrix?.agent?.delete) {
        auth_strategy.add(hudson.model.Computer.DELETE, sid)
    }
    if (matrix?.agent?.disconnect) {
        auth_strategy.add(hudson.model.Computer.DISCONNECT, sid)
    }
    if (matrix?.job?.build) {
        auth_strategy.add(hudson.model.Item.BUILD, sid)
    }
    if (matrix?.job?.cancel) {
        auth_strategy.add(hudson.model.Item.CANCEL, sid)
    }
    if (matrix?.job?.configure) {
        auth_strategy.add(hudson.model.Item.CONFIGURE, sid)
    }
    if (matrix?.job?.create) {
        auth_strategy.add(Permission.fromId('hudson.model.Item.Create'), sid)
    }

    if (matrix?.job?.delete) {
        auth_strategy.add(hudson.model.Item.DELETE, sid)
    }
    if (matrix?.job?.discover) {
        auth_strategy.add(hudson.model.Item.DISCOVER, sid)
    }
    if (matrix?.job?.move) {
        auth_strategy.add(Permission.fromId('hudson.model.Item.Move'), sid)
    }
    if (matrix?.job?.read) {
        auth_strategy.add(hudson.model.Item.READ, sid)
    }
    if (matrix?.job?.workspace) {
        auth_strategy.add(hudson.model.Item.WORKSPACE, sid)
    }
    if (matrix?.run?.delete) {
        auth_strategy.add(Permission.fromId('hudson.model.Run.Delete'), sid)
    }
    if (matrix?.run?.replay) {
        auth_strategy.add(Permission.fromId('hudson.model.Run.Replay'), sid)
    }
    if (matrix?.run?.update) {
        auth_strategy.add(Permission.fromId('hudson.model.Run.Update'), sid)
    }
    if (matrix?.view?.configure) {
        auth_strategy.add(Permission.fromId('hudson.model.View.Configure'), sid)
    }
    if (matrix?.view?.create) {
        auth_strategy.add(Permission.fromId('hudson.model.View.Create'), sid)
    }
    if (matrix?.view?.delete) {
        auth_strategy.add(Permission.fromId('hudson.model.View.Delete'), sid)
    }
    if (matrix?.view?.read) {
        auth_strategy.add(Permission.fromId('hudson.model.View.Read'), sid)
    }
    if (matrix?.scm?.tag) {
        auth_strategy.add(Permission.fromId('hudson.scm.SCM.Tag'), sid)
    }
}

//Set global matrix authorization strategy
user_list.each{ user->
    if (user.name in auth_strategy.getAllSIDs()) {
        if (user?.matrix?.state == 'present') {
            println ("$user.name already exists in matrix, skip creating...")
            //CheckChanges()
            CreateAuthStrategy(user.name, user.matrix)
        }
        else{
            println ("Removing $user.name")
        }
    } else{
        if (user?.matrix?.state == 'present') {
            println ("Creating user $user.name...")
            //hudsonRealm.createAccount("$user.name","$user.pass")
            CreateAuthStrategy(user.name, user.matrix)
        }
        else{
            //Doing nothing
        }
    }
}

instance.save()



