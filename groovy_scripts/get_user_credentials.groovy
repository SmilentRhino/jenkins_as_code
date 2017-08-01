import jenkins.model.*
import hudson.model.User

println 'Current User:' + User.current().getId()
def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl,
        Jenkins.instance,
        User.current().impersonate(),
        null)
for (c in creds) {
    println(c.id + ": " + c.description)
    println c.getUsername()
    println c.getPassword()
    println c.getDescriptor()
}
