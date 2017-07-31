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

def to_add_credentials_text = '''\
{
    "user_password_cred": [
        {
            "id": "test_user_password",
            "description": "",
            "username": "myusername",
            "password": "mypassword",
            "scope": "GLOBAL"
        },
        {
            "id": "test_user_password1",
            "description": "",
            "username": "myusername",
            "password": "mypassword",
            "scope": "GLOBAL"
        }
    ],
    "basic_ssh_user_private_key_cred": [
        {
            "impl": "BasicSSHUserPrivateKey.DirectEntryPrivateKeySource",
            "id": "my_pri_key",
            "username": "myusername",
            "passphrase": "mypassphrase",
            "privatekeys": [
                "-----BEGIN RSA PRIVATE KEY-----
Proc-Type: 4,ENCRYPTED
DEK-Info: AES-128-CBC,0304825BE8B23CA9398FF45176C48AFC

Y8kdqSTIvXfpkrkJPhMDmSpHSdtkKuMyQE8lw6G5nax3XQKXQkqaEAPKOj5fF7vk
ZSTcltvPET/TknPbtWaeIPeinWFM99Pt7ebs/47nbz0vp4Ug8xBKT+PLDqMeGOs+
jtWbapwbWh4EVJZtvv6JMGFXpl/psNPTpDT6ug/A0vP2y0PNBMQCZiyZ2xsDaR/D
8/3jEG1bTxaO5h9ZV4L+yyGKZQcuPfRNjaruQKqqAq0StIaRVPLbGk55TxqszA4d
WhIoEllrlSTbclWBNlxLaBIH7tvB5L2Und9o2y2hhfpvO91UXKnLdO8jR3jZ9mnp
yAih6fRVunfPD6oWbTkU/5OxzVJcGcnB9sBl4T4tQBND5mf9ng7oWJFTjZ+i7JWb
cjThEe0GmCYm4727xZB2I9ssNKLLReRNaf26TDWQ0AV13+rVuZuAwQ90BCImqseF
YONTs9Qu2ZyVi/PRkHhnLQ51XFWakWLjydFsHxyt4LeRFgYod1OSuJaHo6b6yJDk
jDNlE+5SrcPdpuHXQjbolQPXxNl6Btlz6V5FYfvHge2x7vv+xN5XX5s6EGC4gINU
aoTCYcT2qaW5BKgR3a8Qyeeg9RB//9MgdhaxHwv2fcfuUx0isy62uUr8VV1F3oOX
JiEamaFyX9Kw62yZ9WBg4kpiIO2HWAHkFJiOwEL5FW0IdIzBmH8ctdRyE88/AqRn
pWuhgRal0f/C4G5JVpDKEudGqOqn821jVGjC8mXsh2tbuJNKNGVQU9AgSlzqk0QW
ll3ZS388U8oPf5t11zXN44p3BtfW2i9Ar1Fci+JCI3uYWwWF0n0hjCOkpRdL/Z7K
PDUtnRIHYAAqpgOstiU7JYHQPpwaSJMAtDui60t1Rd8NpMqaB+Ea8KOp34soSxI8
PEHcebMH4+/k82QMcQyL0Y19Mxrk3sTPXFyQq6sOORvj2CdOxoNF/o7qig5sNEwJ
Drh57jCL/IJGrvMo0NKcEPBhK7DcKrFzJT0UHS6Cr8d+kraXPLsfMi9SiMya1l9e
yejpPA1uDO7FTT/5TU03c82bqGJK9x+yTm/G7WBGSs65RZ60Bspl4zOxCiArcTbV
PKXZdTNBthV3IXJt94WRxmDudnGmtsw16yc2ZlLDvvwt3NF3Q4TlBsw5AhdzVKlh
XyAVChEKeJBoIdqlTR9gOs2PJpYHS8FuUyzrW3G+N0SpQG8awOVhNuqeVNKojGTU
ztzIPrPaCFsktzw1vKsFptrZG3jbbk46X+HQn4R35/JCA7P4+YRXWERJ0TF3VdYE
lZoDgWMJKe/0HPvyqiU9bCX0HvfBRtPTixC8FePdtwgYkGIAhHO1woufzRk+D560
cWeSzOpsSYRlG2CfTKYmRjLwYRP2+oaii8EuMvt0SGZcDYA8uv9x5oguGF86oiik
E8Il4CN0QtppNUAnPjfdHVjOxrtoVkkcAA7CCLQGUwPX1ofXjOjPZ+Whpw/cLQOQ
plOQHLKpI4sbVTgoJhJi5wm+EdY9ADwArwMulWOIG/f8pkFP7pmDvy2lwbalLuvf
Vvh00SE0ojhUfqw1xQOeKQMkgpIuccH+2PFcGTIFazEeQIgfyns4bPV163eu5Z4Z
-----END RSA PRIVATE KEY-----"
            ],
            "description": "",
            "scope": "GLOBAL"
        },
        {
            "impl": "BasicSSHUserPrivateKey.DirectEntryPrivateKeySource",
            "id": "my_pri_key1",
            "username": "myusername",
            "passphrase": "mypassphrase",
            "privatekeys": [
                ""
            ],
            "description": "",
            "scope": "GLOBAL"
        }
    ]
}
'''
def jsonSlurper = new JsonSlurper()
to_add_credentials = jsonSlurper.parseText(to_add_credentials_text)

def all_credentials = [:]
def sys_cred_store() {
    def credential_store = [:]
    def credential_providers = com.cloudbees.plugins.credentials.CredentialsProvider.all()
    credential_providers.each { credential_provider ->
        switch (credential_provider.class) {
            case com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider:
                println 'FolderCredentialsProvider'
                println 'Skip...'
                break
            case com.cloudbees.plugins.credentials.SystemCredentialsProvider.ProviderImpl:
                println 'SystemCredentialsProvider'
                credential_store = credential_provider.getStore(Jenkins.instance)
                store_domains = credential_store.getDomains()
                store_domains.each { store_domain ->
                    if (store_domain.isGlobal()) {
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
                user_password_cred['password'] = credential.getPassword().getPlainText()
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
                basic_ssh_user_private_key_cred['passphrase'] = credential.getPassphrase().getPlainText()
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

if ('basic_ssh_user_private_key_cred' in to_add_credentials) {
    def current_cred_ids = []
    if ('basic_ssh_user_private_key_cred' in system_global_credentials) {
        system_global_credentials['basic_ssh_user_private_key_cred'].each {k -> current_cred_ids << k['id']}
    } 
    to_add_credentials['basic_ssh_user_private_key_cred'].each { basic_ssh_user_private_key_cred ->
        if (basic_ssh_user_private_key_cred['id'] in current_cred_ids) { 
            println "Fatal Error" + basic_ssh_user_private_key_cred['id'] + "already exists!!!"
        } else {
            private_key_src = new com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(system_global_credentials.private_keys)
            cre = new com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey(CredentialsScope.GLOBAL,
                                                                                               basic_ssh_user_private_key_cred.id,
                                                                                               basic_ssh_user_private_key_cred.username,
                                                                                               basic_ssh_user_private_key_cred.private_key_src,
                                                                                               basic_ssh_user_private_key_cred.passphrase,
                                                                                               basic_ssh_user_private_key_cred.description)
            add_suc = SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), cre)
            if (add_suc) {
                println 'New credential added.'
            } else {
                println 'Fail to add new credential.'
            }
        }
    }
}

if ('user_password_cred' in to_add_credentials) {
    def current_cred_ids = []
    if ('user_password_cred' in system_global_credentials) {
        system_global_credentials['user_password_cred'].each {k -> current_cred_ids << k['id']}
    } 
    to_add_credentials['user_password_cred'].each { user_password_cred ->
        if (user_password_cred['id'] in current_cred_ids) { 
            println "Fatal Error" + user_password_cred['id'] + "already exists!!!"
        } else {
            Credentials cre = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,
                                                                                user_password_cred.id,
                                                                                user_password_cred.description,
                                                                                user_password_cred.username,
                                                                                user_password_cred.password)
            add_suc = SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), cre)
            if (add_suc) {
                println 'New credential added.'
            } else {
                println 'Fail to add new credential.'
            }
        }
    }
}


