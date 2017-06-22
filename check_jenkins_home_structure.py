#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
Just a script to test jenkins build
'''

import os
import sys
jenkins_home = '/var/lib/jenkins'
print(os.listdir(jenkins_home))
sys.exit(1)
