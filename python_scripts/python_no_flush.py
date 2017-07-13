#!/user/bin/env
# -*- coding: utf-8 -*-
'''
print without flush, for jenkins
'''

import time

print 'Hello from the beginning'
time.sleep(30)
print 'Hello after halt a minute'
time.sleep(60)
print 'Another Hello after another minute'
