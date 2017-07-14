#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
Backup mobilejenkins jenkins configuration for blackboard
'''

from __future__ import print_function
import os
import sys
import json
import requests
from requests.compat import urljoin


def create_pid():
    '''
    Create pid to avoid parallel run
    '''
    pid_path = sys.argv[0] + '.pid'
    if os.path.exists(pid_path):
        print('Fatal error, {0} is already running.'.format(pid_path))
        print('PID created at {0}'.format(os.path.getctime(pid_path)))
        sys.exit(1)
    with open(pid_path, 'w') as pid:
        pid.write(str(os.getpid()))

def rm_pid():
    '''
    Remove pid after successfull run
    '''

    pid_path = sys.argv[0] + '.pid'
    if not os.path.exists(pid_path):
        print('Fatal error, process pid is missing.')
        sys.exit(1)
    else:
        os.remove(pid_path)

def get_cause(build_url, session):
    '''
    Get the cause of current build
    '''
    build_api = urljoin(build_url, 'api/json')
    build_detail = session.get(build_api)
    build_detail = json.loads(build_detail.text)
    build_causes = []
    if 'actions' in build_detail and build_detail['actions']:
        for action in build_detail['actions']:
            if 'causes' in action and action['causes']:
                build_causes = action['causes']
    return build_causes

def get_upstream_path(build_causes):
    '''
    Get all upstream build path
    '''
    upstream_builds = []
    upstream_info = ["upstreamBuild",
                     "upstreamProject",
                     "upstreamUrl",
                     "shortDescription"]
    user_info = ["shortDescription",
                 "userId",
                 "userName"]
    for build_cause in build_causes:
        if set(upstream_info).issubset(build_cause):
            build_path = os.path.join(build_cause['upstreamProject'],
                                      str(build_cause['upstreamBuild']))
            upstream_builds.append(build_path)
        elif set(user_info).issubset(build_cause):
            pass
        else:
            pass
    return upstream_builds

def main():
    '''
    Get jenkins downstream build cause
    '''
    build_url = os.getenv("BUILD_URL")
    jenkins_user = os.getenv("ADMIN_USER")
    jenkins_token = os.getenv("ADMIN_TOKEN")
    if not all([build_url, jenkins_user, jenkins_token]):
        print("Please provide url, user and token!")
        sys.exit(1)
    create_pid()
    session = requests.Session()
    session.auth = (jenkins_user, jenkins_token)
    build_causes = get_cause(build_url, session)
    if build_causes:
        upstream_builds = get_upstream_path(build_causes)
        print(upstream_builds)
    rm_pid()

if __name__ == "__main__":
    main()
