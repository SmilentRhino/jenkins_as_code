import jenkins.model.*
import hudson.model.User
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

org.acegisecurity.Authentication auth = User.current().impersonate()
Credentials cre = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,java.util.UUID.randomUUID().toString(), "description", "user", "password")
def credentials_store = jenkins.model.Jenkins.instance.getExtensionList(
        'com.cloudbees.plugins.credentials.UserCredentialsProvider'
        )
credentials_store[0].getStore(User.current()).getDomains()
credentials_store[0].getStore(User.current()).addCredentials(Domain.global(), cre)

