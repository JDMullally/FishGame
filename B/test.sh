#!/bin/bash

bash -c '(sleep 1; kill $$) & exec java -jar ./other/xyes.jar infinite test' > out.log
echo 'Infinite Test Successful'

bash -c 'java -jar ./other/xyes.jar -limit finite test' > out.log
echo 'Limit 20 Test Successful'

bash -c 'java -jar ./other/xyes.jar -limit -limit -limit' > out.log
echo 'Duplicate Command Test Successful'

bash -c '(sleep 1; kill $$) & exec java -jar ./other/xyes.jar' > out.log
echo 'No Input Test Successful'
