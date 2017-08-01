import jenkins.model.*
def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl,
        Jenkins.instance,
        null,
        null)
for (c in creds) {
    println(c.id + ": " + c.description)
    println c.getUsername()
    println c.getPassword().getPlainText()
    println c.getDescriptor()
}
