import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.security.*
import hudson.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.cloudbees.plugins.credentials.CredentialsProvider
//Get users.json 
def crowd2_path = build.workspace.toString() + '/seed_job/files/crowd2.json'
def inputFile = new File("$crowd2_path")
def crowd2_conf = new JsonSlurper().parse(inputFile)
println (crowd2_conf)

//Get current users
def instance = Jenkins.getInstance()

if (instance.getSecurityRealm().getClass() == de.theit.jenkins.crowd.CrowdSecurityRealm){
    crowdRealm = instance.getSecurityRealm() 
}else{
    crowdRealm = new de.theit.jenkins.crowd.CrowdSecurityRealm(
        url = crowd2_conf.url,
        applicationName = crowd2_conf.applicationName,
        password = crowd2_conf.password,
        group = crowd2_conf.group,
        nestedGroups = crowd2_conf.nestedGroups,
        sessionValidationInterval = crowd2_conf.sessionValidationInterval,
        useSSO = crowd2_conf.useSSO,
        cookieDomain = crowd2_conf.cookieDomain,
        cookieTokenkey = crowd2_conf.cookieTokenkey,
        useProxy = crowd2_conf.useProxy,
        httpProxyHost = crowd2_conf.httpProxyHost,
        httpProxyPort = crowd2_conf.httpProxyPort,
        httpProxyUsername = crowd2_conf.httpProxyUsername,
        httpProxyPassword = crowd2_conf.httpProxyPassword,
        socketTimeout = crowd2_conf.socketTimeout,
        httpTimeout = crowd2_conf.httpTimeout,
        httpMaxConnections = crowd2_conf.httpMaxConnections
    ) 
}

if (instance.getSecurityRealm().getClass() == de.theit.jenkins.crowd.CrowdSecurityRealm){
    //Doing nothing
}else{
    instance.setSecurityRealm(crowdRealm)
}
instance.save()
