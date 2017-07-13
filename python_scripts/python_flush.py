#!/user/bin/env
# -*- coding: utf-8 -*-
'''
print with flush, for jenkins test
'''

import time
import sys

print 'Hello from the beginning'
sys.stdout.flush()
time.sleep(30)
print 'Hello after halt a minute'
sys.stdout.flush()
time.sleep(60)
print 'Another Hello after another minute'
sys.stdout.flush()
