import jenkins.model.*
import hudson.model.User
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.domains.*
/*
Currently only support system credentials with global domain
*/
/Get credientials.json
def credentials_list_path = build.workspace.toString() + '/seed_job/files/sys_credentials.json'
def inputFile = new File("$credentials_list_path")
def expected_credentials = new JsonSlurper().parse(inputFile)

current_credentials = SystemCredentialsProvider.getDomainCredentialsMap()

def cred_exists(cred_domain, id){
    current_credentials.each{ domain, cred->
        if (domain==cred_domain && cred.getId()==id){
            return true
        }
    }
    return false
}

expected_credentials.each{ expected_cred->
    def cred_domain = ''
    def cred_scope = ''
    def cred_type = ''
    def cred = ''
    if (expected_cred.domain == 'global'){
            cred_domain = Domain.global()
            if cred_exists(cred_domian, expected_cred.id){
                println "Credential $expected_cred.id exists"
            }
            if (expected_cred.scope == 'global'){
                cred_scope = CredentialsScope.GLOBAL
            }
            else if (expected_cred.scope == 'system'){
                cred_scope = CredentialsScope.SYSTEM
            }
            else if (expected_cred.scope == 'user'){
                cred_scope = CredentialsScope.USER
            }
            else{
                println "Unsupported credential scope"
                break
            }
            if (expected_cred.type == 'username_password') {
                cred = UsernamePasswordCredentialsImpl(scope=expected.cred.scop,
                           id=expected_cred.id,
                           description=expected_cred.description,
                           username=expected_cred.username,
                           password=expected_cred.password)
            }
            else if (expected_cred.type == 'username_priv_key'){
                break 
            }
            else if (expected_cred.type == 'username_secret_text'){
                break
            }
            else{
                println 'Unsupported credential type'
            }
        }
        SystemCredentialsProvider.getInstance().getStore().addCredentials(cred_domain, cred)
    }
    else{
        println ("Domain other than global not supported now")
        break
    }
}

