#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
Check jenkins home structure, if changed, we should manually decide if backup is
needed
'''

import os
import sys
from __future__ import print_function

def main():
    expected_entries = []
    current_entries = []
    jenkins_home = '/var/lib/jenkins'
    entry_list = os.listdir(jenkins_home)
    for entry in entry_list:
        entry_path = os.path.join(jenkins_home, entry)
        if not os.path.islink(entry_path):
            current_entries.append(entry)
    print('Current entries:', end='')
    print(current_entries)
    mis_entries =  set(expected_entries).symmetric_difference(current_entries):
        if mis_entries:
            print('Unexpected or missed entries:', end='')
            print(mis_entires)
            sys.exit(1)
        else:
            sys.exit(0)

if __name__ == "__main__":
    main()
