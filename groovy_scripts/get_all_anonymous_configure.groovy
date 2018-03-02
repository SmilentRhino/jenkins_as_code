import hudson.model.Item.*
Jenkins.instance.getItems().each { my_item ->
  //println my_item.getFullName()
  //println my_item.getACL().getClass()
  if (my_item.getACL() instanceof hudson.security.SidACL) {
      //println my_item.getFullName()
    my_item.properties.each { my_property_des,my_property ->
      if (my_property instanceof hudson.security.AuthorizationMatrixProperty) {
          //println my_property
          if (my_property.hasPermission(hudson.security.ACL.ANONYMOUS_USERNAME, hudson.model.Item.CONFIGURE)) {
              println my_item.getFullName()
          }
      }
       if (my_property instanceof hudson.security.SidACL) {
          //println my_property
          if (my_property.hasPermission(hudson.security.ACL.ANONYMOUS, hudson.model.Item.CONFIGURE)) {
              println my_item.getFullName()
          }
        }
    }
      //println my_item.getACL().hasPermission(hudson.security.ACL.ANONYMOUS,hudson.security.Permission.CONFIGURE)
  }
  
}
