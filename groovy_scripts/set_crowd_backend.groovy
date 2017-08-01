import jenkins.model.*
import hudson.security.*
import de.theit.jenkins.crowd.*
hudson.security.SecurityRealm.all()
def instance = Jenkins.getInstance()
def realm = new CrowdSecurityRealm('http://crowd.alexrhino.net/crowd/','12345','123456','',false,20000,false,'crowd.token_key','',false,null,null,null,null,null,null,null)
instance.setSecurityRealm(realm)
instance.save()
