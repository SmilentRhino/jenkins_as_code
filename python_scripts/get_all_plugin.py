#!/usr/bin/env python
'''
Get all plugin from jenkins api
'''
import json
from jenkinsapi.jenkins import Jenkins

def get_server_instance():
    '''
    Get jenkins server obj
    '''
    jenkins_url = 'http://jenkins.alexrhino.net:8080'
    server = Jenkins(jenkins_url, username='smilentrhino', password='IWontTellU')
    return server

def get_plugin_details():
    # Refer Example #1 for definition of function 'get_server_instance'
    server = get_server_instance()
    all_plugins = {'default_plugins':[]}
    for plugin in server.get_plugins().values():
        print "Short Name:%s" % (plugin.shortName)
        print "Long Name:%s" % (plugin.longName)
        print "Version:%s" % (plugin.version)
        print "URL:%s" % (plugin.url)
        print "Active:%s" % (plugin.active)
        print "Enabled:%s" % (plugin.enabled)
        all_plugins['default_plugins'].append(
            { 'name': plugin.shortName, 'version' : plugin.version}
        )
    print json.dumps(all_plugins)
    with open('bb_mobile_jenkins_all_plugins','w') as f:
        json.dump(all_plugins, f)
    return all_plugins


if __name__ == '__main__':
    print get_server_instance().version
    get_plugin_details()
