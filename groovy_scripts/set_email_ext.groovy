import jenkins.model.*
def default_smtp_server = 'smpt.163.com'

origin_smtp_server = Jenkins.getInstance().getPublisher('ExtendedEmailPublisher').getSmtpServer()
if (origin_smtp_server != default_smtp_server) {
    println 'Change smtp_server from ' + origin_smtp_server  + 'to ' + default_smtp_server 
    Jenkins.getInstance().getPublisher('ExtendedEmailPublisher').setSmtpServer(default_smtp_server)
} else {
    println 'Email-ext smtp server is already ' + origin_smtp_server
}
if (Jenkins.getInstance().getPublisher('ExtendedEmailPublisher').getSmtpServer() != default_smtp_server) {
    println 'Change email-ext smtp server failed, please check'
}
