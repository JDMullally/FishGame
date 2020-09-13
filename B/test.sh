#!/bin/bash


bash -c '(sleep 1; kill $$) & exec java -jar ./other/xyes.jar test' > ./other/outputfiles/out1.txt
filename='./other/outputfiles/out1.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -gt 20 ];
then
  echo 'Test 1 Successful';
else
  echo "Test 1 Failed"
fi

bash -c 'java -jar ./other/xyes.jar -limit finite test' > ./other/outputfiles/out2.txt
filename='./other/outputfiles/out2.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -eq 20 ];
then
  echo 'Test 2 Successful';
else
  echo "Test 2 Failed"
fi

bash -c 'java -jar ./other/xyes.jar -limit -limit' > ./other/outputfiles/out3.txt
filename='./other/outputfiles/out3.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -eq 20 ];
then
  echo 'Test 3 Successful';
else
  echo "Test 3 Failed"
fi


bash -c '(sleep 1; kill $$) & exec java -jar ./other/xyes.jar -limit-limit' > ./other/outputfiles/out4.txt
filename='./other/outputfiles/out4.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -gt 20 ];
then
  echo 'Test 4 Successful';
else
  echo "Test 4 Failed"
fi

bash -c '(sleep 1; kill $$) & exec java -jar ./other/xyes.jar' > ./other/outputfiles/out5.txt
filename='./other/outputfiles/out5.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -gt 20 ];
then
  echo 'Test 5 Successful';
else
  echo "Test 5 Failed"
fi
