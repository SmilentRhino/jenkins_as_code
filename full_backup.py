#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
Backup mobilejenkins jenkins configuration for blackboard
'''

from __future__ import print_function
import os
import sys
import shutil
import datetime

def print_info(jenkins_home,
               backup_home,
               collected_info):
    '''
    Print info and do the backup
    '''
    print('Jenkins Home: {0}'.format(jenkins_home))
    [not_backup, dir_to_create, link_to_create, dir_to_copy, file_to_copy] = collected_info
# List ignore files
    print('Following {0} files are not in backup:'.format(len(not_backup)))
    print('\t'.join(not_backup))

# Create Dirs
    print('Create {0} dirs'.format(len(dir_to_create)))
    for dir_path in dir_to_create:
        print(os.path.join(backup_home, *dir_path))
        os.mkdir(os.path.join(backup_home, *dir_path))

# Create links
    print('Create {0} links:'.format(len(link_to_create)))
    for link in link_to_create:
        print(link)
        os.symlink(link[0], os.path.join(backup_home, link[1]))

# Copy dirs
    print('Copy {0} dirs:'.format(len(dir_to_copy)))
    for dir_path in dir_to_copy:
        print(os.path.join(backup_home, *dir_path))
        if not os.path.exists(os.path.join(jenkins_home, *dir_path)):
            print('Fatal error, dir not exists: {0}'.format(dir_path))
            sys.exit(1)
        shutil.copytree(os.path.join(jenkins_home, *dir_path),
                        os.path.join(backup_home, *dir_path))



# Copy files
    print('Copy {0} files:'.format(len(file_to_copy)))
    for file_path in file_to_copy:
        print(os.path.join(backup_home, *file_path))
        if not os.path.exists(os.path.join(jenkins_home, *file_path)):
            print('Fatal error, file not exists: {0}'.format(dir_path))
            sys.exit(1)
        shutil.copy(os.path.join(jenkins_home, *file_path),
                    os.path.join(backup_home, *file_path))

def create_pid():
    '''
    Create pid to avoid parallel run
    '''
    if os.path.exists('./full_backup.pid'):
        print('Fatal error, backup process already exists.')
        print('PID created at {0}'.format(os.path.getctime('./full_backup.pid')))
        sys.exit(1)
    with open('./full_backup.pid', 'w') as pid:
        pid.write(str(os.getpid()))

def rm_pid():
    '''
    Remove pid after successfull run
    '''
    if not os.path.exists('./full_backup.pid'):
        print('Fatal error, backup pid is missing.')
        sys.exit(1)
    else:
        os.remove('./full_backup.pid')

def main():
    '''
    Backup and archive
    '''
#    expected_entries = ['.aws',
#                        'users']
    jenkins_home = '/var/lib/jenkins'
    backup_home = os.path.join(jenkins_home, 'jenkins_backup')
    dir_to_create = []
    link_to_create = []
    dir_to_copy = []
    file_to_copy = []
    not_backup = []

    create_pid()
# Create new backup directory
    new_backup = os.path.join(backup_home, datetime.datetime.now().strftime('FULL-%Y-%m-%d_%H-%M'))
    if os.path.exists(new_backup):
        print('Error: Backup dir {0} already exists.'.format(new_backup))
        sys.exit(1)
    else:
        print('Create new backup {0}'.format(new_backup))
        os.mkdir(new_backup)

# Check if jenkins home changes
#    current_entries = os.listdir(jenkins_home)
#    print('\t'.join(current_entries))
#    mis_entries = set(expected_entries).symmetric_difference(current_entries)
#    if mis_entries:
#        print('Unexpected or missed entries:', end='')
#        print('\t'.join(mis_entries))

# Backup none-job files info collect
    for entry in os.listdir(jenkins_home):
        if entry.endswith('.log') or entry.endswith('.tmp'):
            not_backup.append(entry)
        elif entry not in ['caches', 'logs', 'jobs', 'jenkins_backup']:
            src_path = os.path.join(jenkins_home, entry)
            if os.path.islink(src_path):
                link_to_create.append([os.readlink(src_path), entry])
                #os.symlink(os.readlink(src_path), dst_path)
            elif os.path.isfile(src_path):
                file_to_copy.append([entry])
                #shutil.copy(src_path, dst_path)
            elif os.path.isdir(src_path) and not os.path.islink(src_path):
                dir_to_copy.append([entry])
                #shutil.copytree(src_path, dst_path)
            else:
                print('Unkown entry type, {0}'.format(entry))

# Backup job config info collect
    dir_to_create.append(['jobs'])
    for jenkins_job in os.listdir(os.path.join(jenkins_home, 'jobs')):
        dir_to_create.append(['jobs', jenkins_job])
        file_to_copy.append(['jobs', jenkins_job, 'config.xml'])
        if 'branches' in os.listdir(os.path.join(jenkins_home, 'jobs', jenkins_job)):
            dir_to_create.append(['jobs', jenkins_job, 'branches'])
            dir_to_copy.append(['jobs', jenkins_job, 'template'])
            for branch in os.listdir(os.path.join(jenkins_home, 'jobs', jenkins_job, 'branches')):
                dir_to_create.append(['jobs', jenkins_job, 'branches', branch])
                file_to_copy.append(['jobs', jenkins_job, 'branches', branch, 'config.xml'])

    print_info(jenkins_home, new_backup,
               [not_backup, dir_to_create, link_to_create, dir_to_copy, file_to_copy])
    rm_pid()

if __name__ == "__main__":
    main()
