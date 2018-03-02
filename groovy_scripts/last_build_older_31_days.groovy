import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import java.text.SimpleDateFormat
Calendar calendar = new GregorianCalendar();
Date trialTime = new Date();
calendar.setTime(trialTime);
def result = [:]
Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  if (it.disabled) {
    println it.fullName + ' is Disabled'
  }
  else {
    //println it.fullName;
    mybuild = it.getLastBuild()
    if (mybuild) {
      //println mybuild.timestamp
      my_gap = calendar.getTime().getTime() - mybuild.timestamp.getTime().getTime()
      if(my_gap > 2678400) {
          println it.fullName + 'is older than 31 days'
      }
    }
  }
}
