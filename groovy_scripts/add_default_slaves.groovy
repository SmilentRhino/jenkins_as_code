import jenkins.model.*
import hudson.model.*
import hudson.slaves.DumbSlave
import hudson.plugins.sshslaves.SSHLauncher
import hudson.slaves.RetentionStrategy.Always
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

//Get current slave first
def slave_list = []
Jenkins.getInstance().getNodes().each { jenkins_slave ->
    def slave_map = [:]
//    println jenkins_slave.class
 //   println 'Start get jenkins slave info'
    slave_map['node_name'] = jenkins_slave.getNodeName()
    slave_map['root_path'] = jenkins_slave.getRemoteFS()
    slave_map['mode'] = jenkins_slave.getMode().getName()
    slave_map['num_executors'] = jenkins_slave.getNumExecutors()
    slave_map['label'] = jenkins_slave.getLabelString()
//    println JsonOutput.toJson(slave_map)
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
  //            println k
   //           println v
              env_vars[k] = v
          }
          slave_map['env_vars'] = env_vars
//          println JsonOutput.toJson(slave_map)
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
//        println 'Slave map'
    } else if (slave_launcher.class == hudson.slaves.JNLPLauncher) {
        jnlp_launcher = [:]
 //       println 'JNLP Launcher'
        slave_map['jnlp_launcher'] = jnlp_launcher
    }
  } else {
    println 'This slave is not dumbslave'
  }
  slave_list.add(slave_map)
}

//For windows server, backslash should be four
slaves_to_add_text = '''\
[
    {
        "label": "label1",
        "mode": "NORMAL",
        "node_name": "192.168.1.1",
        "num_executors": 8,
        "retention_strategy": "hudson.slaves.RetentionStrategy.Always",
        "remotefs": "/home/ubuntu",
        "ssh_launcher": {
            "credentials_id": "my_pri_key1",
            "host_ip": "192.168.1.1",
            "host_port": 22,
            "java_path": "",
            "jvm_opts": "",
            "launcher_timeout": null,
            "max_retries": 0,
            "prefix_start_slave_cmd": "",
            "retry_wait_time": 0,
            "suffix_start_slave_cmd": ""
        }
    },
    {
        "env_vars": {
            "PYTHON_HOME": "/usr/local/bin/python",
            "APPIUM_HOME": "/usr/local/bin/appium",
            "GEM_HOME": "/Users/admin/.rvm/rubies/ruby-2.2.4/bin/gem",
            "RUBY_HOME": "/Users/admin/.rvm/rubies/ruby-2.2.4/bin/ruby"
        },
        "label": "ios",
        "mode": "NORMAL",
        "node_name": "mac_192.168.1.2",
        "num_executors": 1,
        "retention_strategy": "hudson.slaves.RetentionStrategy.Always",
        "remotefs": "/Users/admin/Jenkins",
        "ssh_launcher": {
            "credentials_id": "test_user_password",
            "host_ip": "192.168.1.2",
            "host_port": 22,
            "java_path": "/usr/bin/java",
            "jvm_opts": "",
            "launcher_timeout": null,
            "max_retries": 0,
            "prefix_start_slave_cmd": "",
            "retry_wait_time": 0,
            "suffix_start_slave_cmd": ""
        }
    },
    {
        "jnlp_launcher": {},
        "label": "mac_192.168.1.3",
        "mode": "NORMAL",
        "node_name": "mac_192.168.1.3",
        "num_executors": 1,
        "retention_strategy": "hudson.slaves.RetentionStrategy.Always",
        "remotefs": "/Users/admin/Jenkins"
    },
    {
        "jnlp_launcher": {},
        "label": "Windows",
        "mode": "NORMAL",
        "node_name": "windows10_192.168.1.4",
        "num_executors": 1,
        "retention_strategy": "hudson.slaves.RetentionStrategy.Always",
        "remotefs": "c:\\\\jenkins"
    }
]
'''
ssh_slave_hosts = []
jnlp_node_names = []
slave_list.each { current_slave ->
    if ('ssh_launcher' in current_slave) {
        ssh_slave_hosts << current_slave['ssh_launcher'].host_ip
    } else if('jnlp_launcher' in current_slave.keySet()) {
        jnlp_node_names << current_slave['node_name']
    } else {
        println 'Fatal Error, unsupported launcher!!!'
        println current_slave
    }
}
def jsonSlurper = new JsonSlurper()
slaves_to_add = jsonSlurper.parseText(slaves_to_add_text)
slaves_to_add.each { slave_to_add ->
//    def know_host_verify  = new hudson.plugins.sshslaves.verifiers.KnownHostsFileKeyVerificationStrategy() 
    def new_launcher
    if ('ssh_launcher' in slave_to_add) {
        if (slave_to_add['ssh_launcher'].host_ip in ssh_slave_hosts) {
            println 'SSH slave hosts ' + slave_to_add['ssh_launcher'].host_ip + ' already exists, skipping...'
        } else {
            new_launcher = new hudson.plugins.sshslaves.SSHLauncher(slave_to_add['ssh_launcher'].host_ip,
                                                                    slave_to_add['ssh_launcher'].host_port,
                                                                    slave_to_add['ssh_launcher'].credentials_id,
                                                                    slave_to_add['ssh_launcher'].jvm_opts,
                                                                    slave_to_add['ssh_launcher'].java_path,
                                                                    slave_to_add['ssh_launcher'].prefix_start_slave_cmd,
                                                                    slave_to_add['ssh_launcher'].suffix_start_slave_cmd,
                                                                    slave_to_add['ssh_launcher'].launcher_timeout,
                                                                    slave_to_add['ssh_launcher'].max_retries,
                                                                    slave_to_add['ssh_launcher'].retry_wait_time)
 //                                                                   know_host_verify)
                

            env_entry_list = []
            if ('env_vars' in slave_to_add) {
                slave_to_add['env_vars'].each { k,v -> 
                    new_entry = new hudson.slaves.EnvironmentVariablesNodeProperty.Entry(k,v)
                    env_entry_list.add(new_entry)
                }
            }
            node_properties = new LinkedList()
            if (env_entry_list) {
                env_vars_node_property = new hudson.slaves.EnvironmentVariablesNodeProperty(env_entry_list)
                node_properties.add(env_vars_node_property)
            }
            def new_slave = new DumbSlave(slave_to_add['node_name'],
                                      slave_to_add['node_description'],
                                      slave_to_add['remotefs'],
                                      slave_to_add['num_executors'].toString(),
                                      hudson.model.Node.Mode.NORMAL,
                                      slave_to_add['label'],
                                      new_launcher,
                                      new hudson.slaves.RetentionStrategy.Always(),
                                      node_properties)
            Jenkins.getInstance().addNode(new_slave)
        }
    } else if ('jnlp_launcher' in slave_to_add.keySet()) {
//        println 'Jnlp Laucher'
        if (slave_to_add['node_name'] in jnlp_node_names) {
            println 'Jnlp node ' + slave_to_add['node_name'] + ' already exists!'
        } else {
            new_launcher = new hudson.slaves.JNLPLauncher()
            def new_slave = new DumbSlave(slave_to_add['node_name'],
                                      slave_to_add['node_description'],
                                      slave_to_add['remotefs'],
                                      slave_to_add['num_executors'].toString(),
                                      hudson.model.Node.Mode.NORMAL,
                                      slave_to_add['label'],
                                      new_launcher,
                                      new hudson.slaves.RetentionStrategy.Always(),
                                      new LinkedList())
            Jenkins.getInstance().addNode(new_slave)
        }

    } else {
        println 'Unsupported launcher type'
        println slave_to_add
    }
}
