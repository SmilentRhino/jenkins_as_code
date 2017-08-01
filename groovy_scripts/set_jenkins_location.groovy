import jenkins.model.*
def jenkins_url = 'http://192.168.33.41:8080/'
def admin_address = 'zmzsghhz@163.com'

origin_admin_address = jenkins.model.JenkinsLocationConfiguration.get().getAdminAddress()
if (origin_admin_address != admin_address) {
    println 'Original admin address is :' + origin_admin_address
    println 'Now change to ' + admin_address
    jenkins.model.JenkinsLocationConfiguration.get().setAdminAddress(admin_address)
} else {
    println 'Admin address is already ' + admin_address + ', no need to change'
}

origin_jenkins_url = jenkins.model.JenkinsLocationConfiguration.get().getUrl()
if (origin_jenkins_url != jenkins_url) {
    println 'Original jenkins url is :' + origin_jenkins_url
    println 'Now change to ' + jenkins_url
    jenkins.model.JenkinsLocationConfiguration.get().setUrl(jenkins_url)
} else {
    println 'Jenkins url is already ' + jenkins_url  + ', no need to change'
}

//Check if change successfully


if (jenkins.model.JenkinsLocationConfiguration.get().getAdminAddress() != admin_address) {
    println 'Change admin address failed, please check'
}

if (jenkins.model.JenkinsLocationConfiguration.get().getUrl() != jenkins_url) {
    println 'Change jenkins url failed, please check'
}
