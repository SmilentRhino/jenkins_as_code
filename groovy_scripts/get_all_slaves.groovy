import jenkins.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
def slave_list = []

Jenkins.getInstance().getNodes().each { jenkins_slave ->
    def slave_map = [:]
    println jenkins_slave.class
    println 'Start get jenkins slave info'
    slave_map['node_name'] = jenkins_slave.getNodeName()
    slave_map['root_path'] = jenkins_slave.getRemoteFS()
    slave_map['mode'] = jenkins_slave.getMode().getName()
    slave_map['num_executors'] = jenkins_slave.getNumExecutors()
    slave_map['label'] = jenkins_slave.getLabelString()
    println JsonOutput.toJson(slave_map)
  if (jenkins_slave.getRetentionStrategy().class == hudson.slaves.RetentionStrategy.Always) {
    slave_map['retention_strategy'] = 'hudson.slaves.RetentionStrategy.Always'
  } else {
    println 'Unrecognized retention strategy'
  }
    node_properties = jenkins_slave.getNodeProperties()
  if (node_properties) {
    node_properties.each {node_property ->
      if (node_property.class == hudson.slaves.EnvironmentVariablesNodeProperty) {
          env_vars = [:]
          node_property.getEnvVars().each { k,v ->
              println k
              println v
              env_vars[k] = v
          }
          slave_map['env_vars'] = env_vars
          println JsonOutput.toJson(slave_map)
      } else {
          println 'Unkown property'
      }
    }
  }
  if ( jenkins_slave.class == hudson.slaves.DumbSlave) {
    slave_launcher = jenkins_slave.getLauncher()
    if (slave_launcher.class == hudson.plugins.sshslaves.SSHLauncher) {
        ssh_launcher = [:]
        println 'SSH Launcher'
/*
ssh-slaves pre 1.15 use no verification
*/
        if (Jenkins.getInstance().getPluginManager().getPlugin('ssh-slaves').getVersionNumber() > new hudson.util.VersionNumber('1.15')){
            ssh_hostkey_verification_strategy = slave_launcher.getSshHostKeyVerificationStrategy().class.toString()
            ssh_launcher["ssh_hostkey_verification_strategy"]=ssh_hostkey_verification_strategy
        }
        ssh_launcher["launcher_timeout"]=slave_launcher.getLaunchTimeoutSeconds()
        ssh_launcher["retry_wait_time"] = slave_launcher.getRetryWaitTime()
        ssh_launcher["host_port"] = slave_launcher.getPort()
        ssh_launcher["suffix_start_slave_cmd"] = slave_launcher.getSuffixStartSlaveCmd()
        ssh_launcher["prefix_start_slave_cmd"] = slave_launcher.getPrefixStartSlaveCmd()
        ssh_launcher["host_ip"] = slave_launcher.getHost()
        ssh_launcher["max_retries"] = slave_launcher.getMaxNumRetries()
        ssh_launcher["jvm_opts"] = slave_launcher.getJvmOptions()
        ssh_launcher["java_path"] = slave_launcher.getJavaPath()
        ssh_launcher["credentials_id"] = slave_launcher.getCredentialsId()
        slave_map['ssh_launcher'] = ssh_launcher
        println 'Slave map'
    } else if (slave_launcher.class == hudson.slaves.JNLPLauncher) {
        jnlp_launcher = [:]
        println 'JNLP Launcher'
        slave_map['jnlp_launcher'] = jnlp_launcher
    }
  } else {
    println 'This slave is not dumbslave'
  }
  slave_list.add(slave_map)
}
println JsonOutput.toJson(slave_list)
