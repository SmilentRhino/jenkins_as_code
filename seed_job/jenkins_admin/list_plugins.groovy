import jenkins.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
def plugin_list = []
def instance = Jenkins.getInstance()
println ('Detail')
println ('-----------------------------------------------------')
println ('-----------------------------------------------------')

instance.pluginManager.plugins.each { plugin -> 
    println ("${plugin.getDisplayName()} (${plugin.getShortName()}): ${plugin.getVersion()}");
    plugin_list.add(["${plugin.getShortName()}", "${plugin.getVersion()}"]);
}
println (plugin_list)
println ('-----------------------------------------------------')
println ('-----------------------------------------------------')
println ('Yaml')
println ('-----------------------------------------------------')
println ('-----------------------------------------------------')

plugin_list.each{
  plugin -> 
  println ("${plugin[0]}:${plugin[1]}")
}
println ('-----------------------------------------------------')
println ('-----------------------------------------------------')
println ('Json')
println ('-----------------------------------------------------')
println ('-----------------------------------------------------')
println JsonOutput.prettyPrint(JsonOutput.toJson(plugin_list))
