import hudson.triggers.SCMTrigger
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
json_slurper = new JsonSlurper()

def count = 0
change_list_json = '[["azhang_test", "H 3 * * *"]]'
change_list = json_slurper.parseText(change_list_json)
change_list.each { my_change ->
    println my_change[0]
    count += 1
    println count   
  new_trigger = new hudson.triggers.SCMTrigger(my_change[1])
  Jenkins.instance.getItem(my_change[0]).getTemplate().addTrigger(new_trigger)
}
