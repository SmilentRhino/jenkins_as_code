import jenkins.model.*
import hudson.model.User
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.*
/*
Currently only support system credentials with global domain
*/
def all_credentials = [:]
def sys_cred_store() {
    def credential_store = [:]
    def credential_providers = com.cloudbees.plugins.credentials.CredentialsProvider.all()
    credential_providers.each { credential_provider ->
        switch (credential_provider.class) {
            case com.cloudbees.plugins.credentials.SystemCredentialsProvider.ProviderImpl:
                println 'SystemCredentialsProvider'
                credential_store = credential_provider.getStore(Jenkins.instance)
                store_domains = credential_store.getDomains()
//stor_doman.isGlobal() doesn't work before credential plugin 2.0
                store_domains.each { store_domain ->
                    if (store_domain == Domain.global()) {
                        println 'Default domain is global'
                    } else {
                        println 'Fatal Error, unsupported credential domain in system credentials provider'
                    }
                }
                break
            case com.cloudbees.plugins.credentials.UserCredentialsProvider:
                println 'UserCredentialsProvider'
                println 'Skip...'
                break
/*
If FolderCredentialsProvider doesn't exist, groovy will throw exception, so I put it at the end.
*/
            case com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider:
                println 'FolderCredentialsProvider'
                println 'Skip...'
                break
            default:
                println 'Fatal Error, unsupported credential provider!!!'
        }
    }
    return credential_store
}

def get_sys_global_credential(com.cloudbees.plugins.credentials.CredentialsStore credential_store) {
    system_global_credentials = [:]
    credentials = credential_store.getCredentials(com.cloudbees.plugins.credentials.domains.Domain.global())
    credentials.each { credential ->
        switch (credential.class) {
            case com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl:
                if (!('user_password_cred' in system_global_credentials)) {
                    system_global_credentials['user_password_cred'] = []
                }
                def user_password_cred = [:]
                user_password_cred['id'] = credential.id
                user_password_cred['description'] = credential.description
                user_password_cred['username'] = credential.getUsername()
                if (credential.getPassword()) {
                    user_password_cred['password'] = credential.getPassword().getPlainText()
                } else {
                    user_password_cred['password'] = credential.getPassword()
                }
                user_password_cred['scope'] = credential.getScope().toString()
                system_global_credentials['user_password_cred'].add(user_password_cred)
                break
            case com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey:
                if (!('basic_ssh_user_private_key_cred' in system_global_credentials)) {
                    system_global_credentials['basic_ssh_user_private_key_cred'] = []
                }
                def basic_ssh_user_private_key_cred = [:]
                def private_key_src = credential.getPrivateKeySource()
                if ( private_key_src.class == com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey.DirectEntryPrivateKeySource) {
                    basic_ssh_user_private_key_cred['impl'] = 'BasicSSHUserPrivateKey.DirectEntryPrivateKeySource'
                } else {
                    println 'Fatal Error: Unsupported private key source!!!'
                }
                basic_ssh_user_private_key_cred['id'] = credential.id
                basic_ssh_user_private_key_cred['username'] = credential.getUsername()
                if (credential.getPassphrase()) {
                    basic_ssh_user_private_key_cred['passphrase'] = credential.getPassphrase().getPlainText()
                } else {
                    basic_ssh_user_private_key_cred['passphrase'] = credential.getPassphrase()
                }
                basic_ssh_user_private_key_cred['privatekeys'] = credential.getPrivateKeys()
                basic_ssh_user_private_key_cred['description'] = credential.description
                basic_ssh_user_private_key_cred['scope'] = credential.getScope().toString()
                system_global_credentials['basic_ssh_user_private_key_cred'].add(basic_ssh_user_private_key_cred)
                break
            default:
                println 'Fatal Error, unsupported credential type!!!'
        }
    }
    return system_global_credentials
}

//println JsonOutput.toJson(all_credentials)
credential_store = sys_cred_store()
//println credential_store.class
system_global_credentials = get_sys_global_credential(credential_store)

println JsonOutput.prettyPrint(JsonOutput.toJson(system_global_credentials))
