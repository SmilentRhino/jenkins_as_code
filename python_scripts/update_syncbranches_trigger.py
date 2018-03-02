#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
Update multibranch sync trigger
'''
import os
import re

def update_sync_trigger():
    '''
    Modify xml configure directly to update multi branches sync trigger.
    Should reload jobs after this.
    Don't forget to use git to track changes, and rollback when !!!surprise!!!.
    '''
    jenkins_home = '/var/lib/jenkins'
    jobs_root = os.path.join(jenkins_home, 'jobs')
    os.chdir(jenkins_home)
    os.getcwd()
    change_list = [
        ["Multibranch-JOB-1", ""],
        ["Multibranch-JOB-2", ""],
        ["Multibranch-JOB-3", ""]]

    for i in change_list:
        proj_name = i[0]
        old_spec = '(.*?)'
        old_pattern = '<syncBranchesTrigger>\n    <spec>' + \
                      old_spec + \
                      '</spec>\n  </syncBranchesTrigger>\n'
        new_spec = ''
        new_pattern = '<syncBranchesTrigger>\n    <spec>' + \
                      new_spec + \
                      '</spec>\n  </syncBranchesTrigger>\n'
        proj_path = os.path.join(jobs_root, proj_name)
        if os.path.exists(proj_path):
            config_path = os.path.join(proj_path, 'config.xml')
            if os.path.exists(config_path):
                print config_path
                old_config = ''
                with open(config_path) as config_file:
                    old_config = config_file.read()
                print 'old_config'
                #print old_config
                match = re.search(old_pattern, old_config)
                if match:
                    print 'There is match'
                    print match.group(0)
                    old_pattern = match.group(0)
                else:
                    print 'Fatal Error, no match'
                new_config = old_config.replace(old_pattern, new_pattern)
                print 'new_config'
                #print new_config
                if new_config:
                    pass
                    with open(config_path, 'w') as config_file:
                        config_file.write(new_config)
    #    break

if __name__ == "__main__":
    update_sync_trigger()
