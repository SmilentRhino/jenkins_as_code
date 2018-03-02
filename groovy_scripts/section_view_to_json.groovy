import groovy.json.JsonOutput
json_output = new groovy.json.JsonOutput()
def all_section_view = []
Jenkins.instance.getViews().each { my_view ->
  if (my_view instanceof hudson.plugins.sectioned_view.SectionedView) {
    section_view_map = [:]
    println my_view.name
    section_view_map['name'] = my_view.getViewName()
    section_view_map['listviews'] = []
    println my_view.getSections().each { my_section ->
      if (my_section instanceof hudson.plugins.sectioned_view.ListViewSection) {
        def list_view_map = [:]
        println my_section.getName()
        list_view_map['name'] = my_section.getName()
        println my_section.getIncludeRegex()
        list_view_map['regex'] = my_section.getIncludeRegex()
        println my_section.getJobFilters()
        list_view_map['filter'] = my_section.getJobFilters()
        list_view_map['job_names'] = []
        my_section.getItems(Jenkins.instance).each { my_item ->
          list_view_map['job_names'] << my_item.getFullName()
        }
        section_view_map['listviews'] << list_view_map

      } else {
          println 'Unsupported section'
      }
      
    }
    all_section_view.add(section_view_map)
  }
}
//println all_section_view
println json_output.toJson(all_section_view)
