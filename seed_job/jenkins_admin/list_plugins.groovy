import jenkins.model.*
def instance = Jenkins.getInstance()
instance.pluginManager.plugins.each{
  plugin -> 
    println ("${plugin.getDisplayName()} (${plugin.getShortName()}): ${plugin.getVersion()}")
}

println ('Split line')
println ('-----------------------------------------------------')
println ('-----------------------------------------------------')

instance.pluginManager.plugins.each{
  plugin -> 
      println ("${plugin.getShortName()}: ${plugin.getVersion()}")
}
