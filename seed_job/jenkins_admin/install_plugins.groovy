import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def plugin_list_path = build.workspace.toString() + '/seed_job/files/plugins.json'
println ("$plugin_list_path")
def inputFile = new File("$plugin_list_path")
def plugin_list = new JsonSlurper().parse(inputFile)
//def instance = Jenkins.getInstance()
println (plugin_list)

