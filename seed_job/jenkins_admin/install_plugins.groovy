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
println (plugin_list)
println plugin_list.getClass()
def current_plugins = [:]
def mismatch_plugins = []
def new_installed_plugins = []
Jenkins.instance.pluginManager.plugins.each { plugin ->
    println ("${plugin.getDisplayName()} ${plugin.getShortName()}: ${plugin.getVersion()}")
    current_plugins[plugin.getShortName()] = plugin.getVersion()
}
plugin_list.each{ name, version->
        if (current_plugins?."$name") {
            if (current_plugins?."$name" == version) {
                println ("Plugin $name:$version exists as expected...")
            }
            else {
                println ("Plugin $name exists, version not match")
                println ("Expected $version")
                println ("We have ${current_plugins."$name"}")
                mismatch_plugins.add("$name")
            }
        }
        else {
            println ("Plugin $name:$version not exists, installing...")
            Jenkins.instance.updateCenter.getPlugin("$name").deploy()
            new_installed_plugins.add("$name")
        }
}

println '-----------------------------------------'
println 'New installed plugins'
println new_installed_plugins
println '-----------------------------------------'
println 'Version mis match plugins'
println '-----------------------------------------'
println mismatch_plugins
println '-----------------------------------------'
