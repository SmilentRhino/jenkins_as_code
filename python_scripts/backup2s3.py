# -*- coding: utf-8 -*-

'''
This script is to upload jenkins backup file to s3
'''

from __future__ import print_function
import os.path
import re
import boto3

def backup2s3():
    '''
    Upload jenkins backup to S3
    Backup name example: FULL-2017-07-24_22-34.tgz
    '''
    default_profile = 'dev'
    default_bucket = 'mobile-jenkins-backup'
    jenkins_backup_home = '/var/lib/jenkins/jenkins_backup/'
    os.chdir(jenkins_backup_home)
    print('Current dir: {0}'.format(os.getcwd()))
    latest_backup = ''

    for entry_name in os.listdir('.'):
        if re.match(r'FULL-\d{4}-\d{2}-\d{2}_\d{2}-\d{2}.tgz', entry_name):
            print("{0} is the latest local backup tgz".format(entry_name))
            latest_backup = entry_name
    #return latest_backup

    if latest_backup:
        session = boto3.session.Session(profile_name=default_profile)
        s3_client = session.client('s3')
        current_backups = s3_client.list_objects(Bucket=default_bucket)
        backup_names = [s3_obj['Key'] for s3_obj in current_backups['Contents']]
        if latest_backup not in backup_names:
            print("Uploading {0}".format(latest_backup))
            s3_client.upload_file(latest_backup, default_bucket, latest_backup)
        else:
            print("Backup {0} already exists,".format(latest_backup) +
                  "this is not the latest backup.")

if __name__ == '__main__':
    backup2s3()
