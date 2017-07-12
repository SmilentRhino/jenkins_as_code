#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
Backup mobilejenkins jenkins configuration for blackboard
'''

from __future__ import print_function
import os
import sys
import time
from operator import itemgetter
import json
import requests


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


def filter_builds(all_builds, time_delta=2592000, keep_count=10):
    '''
    Filter builds which is older than $time_gap seconds, default 60 days
    but keep at least $keep_count pieces of each kind of builds, default 10
    Here kind we mean, SUCCESS, FAIL and ABORTED
    '''
    all_to_discard = []
    now = time.time()
    build_counts = {'suc_seq': 0,
                    'fail_seq': 0,
                    'abort_seq': 0}
    for jobs_type, jobs_detail  in all_builds.items():
        print('Jobs_type {0}'.format(jobs_type))
        for job_name, job_builds in jobs_detail.items():
            if 'allBuilds' not in job_builds:
                print('No builds for job {0}'.format(job_name))
                continue
            builds = sorted(job_builds['allBuilds'],
                            key=itemgetter('timestamp'),
                            reverse=True)
            for build in builds:
                if build['result'] == 'ABORTED':
                    build_counts['abort_seq'] += 1
                    seq = build_counts['abort_seq']
                elif build['result'] == 'SUCCESS':
                    build_counts['suc_seq'] += 1
                    seq = build_counts['suc_seq']
                else:
                    build_counts['fail_seq'] += 1
                    seq = build_counts['fail_seq']
                if seq > keep_count and not build['keepLog']:
                    if now - build['timestamp']/1000.0 > time_delta:
                        all_to_discard.append(build)
    with open('all_to_discard', 'w') as discard_log:
        json.dump(all_to_discard, discard_log)
    return all_to_discard


def get_builds_no_branches(session, jenkins_jobs):
    '''
    Get builds for jobs with no branches
    '''
    no_branches_jobs_detail = {}
    branches_jobs = []
    for job_info in jenkins_jobs:
        print(job_info['name'])
        job_api_url = requests.compat.urljoin(job_info['url'], 'api/json')
        build_filter = {'tree':'allBuilds[number,url,timestamp,result,keepLog]'}
        builds = session.get(job_api_url,
                             params=build_filter)
        builds = json.loads(builds.text)
        if not builds:
            print('{0} has braches.'.format(job_info['name']))
            branches_jobs.append(job_info)
        no_branches_jobs_detail[job_info['name']] = builds
        time.sleep(10)
    return no_branches_jobs_detail, branches_jobs

def get_builds_branches(session, branches_jobs):
    '''
    Get builds for jobs with branches
    '''
    branches_jobs_detail = {}
    for job_info in branches_jobs:
        job_api_url = requests.compat.urljoin(job_info['url'], 'api/json')
        job_view = session.get(job_api_url,
                               params={'tree':'views[jobs[name,url]]'})
        job_view = json.loads(job_view.text)
        branch_jobs = job_view['views'][0]['jobs']
        for sub_job in branch_jobs:
            sub_job_name = job_info['name'] + '/' + sub_job['name']
            print(sub_job_name)
            job_api_url = requests.compat.urljoin(sub_job['url'], 'api/json')
            build_filter = {'tree':
                            'allBuilds[number,url,timestamp,result,keepLog]'}
            builds = session.get(job_api_url,
                                 params=build_filter)
            builds = json.loads(builds.text)
            branches_jobs_detail[sub_job_name] = builds
        time.sleep(10)
    return branches_jobs_detail

def scan_older_builds(jenkins_url, session):
    '''
    Get all builds for all jobs
    '''
    all_builds = {}

# initial session and get all jobs names
    jenkins_api_url = requests.compat.urljoin(jenkins_url, 'api/json')
    basic_info = session.get(jenkins_api_url)
    jenkins_jobs = json.loads(basic_info.text)['jobs']

# get builds info for each job
    if jenkins_jobs:
        (no_branches_jobs_detail,
         branches_jobs) = get_builds_no_branches(session, jenkins_jobs)
        #with open('branches_jobs', 'r') as f:
        #    branches_jobs = json.load(f)
        branches_jobs_detail = get_builds_branches(session, branches_jobs)
        all_builds['branch_jobs'] = branches_jobs_detail
        all_builds['no_branch_jobs'] = no_branches_jobs_detail
    else:
        print('Fatal error, no jobs info from jenkins.')
    with open('all_builds', 'w') as all_builds_log:
        json.dump(all_builds, all_builds_log)

    return all_builds


def del_older_builds(builds_list):
    '''
    Delete useless buiilds
    '''
    for build in builds_list:
        print(build)

def main():
    '''
    Backup and archive
    '''
    jenkins_url = os.getenv("TO_DISCARD_URL")
    jenkins_user = os.getenv("ADMIN_USER")
    jenkins_token = os.getenv("ADMIN_TOKEN")
    if not all([jenkins_url, jenkins_user, jenkins_token]):
        print("Please provide url, user and token!")
        sys.exit(1)
    create_pid()
    session = requests.Session()
    session.auth = (jenkins_user, jenkins_token)
    all_builds = scan_older_builds(jenkins_url, session)
    #with open('all_builds', 'r') as f:
    #    all_builds = json.load(f)
    all_to_discard = filter_builds(all_builds,
                                   time_delta=2592000,
                                   keep_count=10)
    del_older_builds(all_to_discard)
    rm_pid()

if __name__ == "__main__":
    main()
