import jenkins.model.*
import hudson.model.User
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import hudson.slaves.DumbSlave
import hudson.plugins.sshslaves.SSHLauncher
import hudson.plugins.sshslaves.verifiers.*

/*
Currently only support system credentials with global domain
*/
//Get credientials.json
def credentials_list_path = build.workspace.toString() + '/seed_job/files/nodes.json'
def inputFile = new File("$credentials_list_path")
def expected_nodes = new JsonSlurper().parse(inputFile)

expected_nodes.each{ expected_node->
    node = Jenkins.instance.getNode(expected_node.name)
    if (node) {
        println ("Node $node.getNodeName() exists")
    }
    else{
        def node_launcher = ''
        def node_verify_strategy = ''
        if (expected_node?.launch_method?.type == "ssh_launcher") {
            switch (expected_node?.launch_method.hostkey_verify) {
                case 'known_hosts_file':
                    node_verify_strategy = new KnownHostsFileKeyVerificationStrategy() 
                case 'manually_provided':
                    println 'Manual host key verify strategy not supported in groovy'
                    return
                case 'manually_trusted':
                    println 'Manual host key verify strategy not supported in groovy'
                    return
                case 'non_verify':
                    node_verify_strategy = new NonVerifyingKeyVerificationStrategy() 
                default:
                    println 'Unknown host key verify strategy'
                    return
            }
            node_launcher = new SSHLauncher(
                host=expected_node.host,
                port=expected_node.port,
                credentialsId=expected_node.credential_id,
                jvmOptions=expected_node.jvm_options,
                javaPath=expected_node.java_path
                prefixStartSlaveCmd=expected_node.prefix_start_slave_cmd,
                suffixStartSlaveCmd=expected_node.suffix_start_slave_cmd,
                launchTimeoutSeconds=expected_node.connection_timeout,
                maxNumRetries=expected_node.maximum_number_of_retries,
                retryWaitTime=expected_node.seconds_between_retries,
                sshHostKeyVerificationStrategy= node_verify_strategy
            )
        }
        else{
            return
        }
        node = new DumbSlave(name=expected_node.name,
            remoteFS=expected_node.name.remote_root_dir,
            launcher=node_launcher)
        Jenkins.instance.addNode(node)
        Jenkins.instance.save()
    }
}

