#!/bin/bash

bash -c 'mkdir -p ../outputfiles'

bash -c '(sleep 1; kill $$) & exec java -jar ../xyes.jar test' > ../outputfiles/out1.txt
filename='../outputfiles/out1.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -gt 20 ];
then
  echo 'Test 1 Successful';
else
  echo "Test 1 Failed"
fi

bash -c 'java -jar ../xyes.jar -limit finite test' > ../outputfiles/out2.txt
filename='../outputfiles/out2.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -eq 20 ];
then
  echo 'Test 2 Successful';
else
  echo "Test 2 Failed"
fi

bash -c 'java -jar ../xyes.jar -limit -limit' > ../outputfiles/out3.txt
filename='../outputfiles/out3.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -eq 20 ];
then
  echo 'Test 3 Successful';
else
  echo "Test 3 Failed"
fi


bash -c '(sleep 1; kill $$) & exec java -jar ../xyes.jar -limit-limit' > ../outputfiles/out4.txt
filename='../outputfiles/out4.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -gt 20 ];
then
  echo 'Test 4 Successful';
else
  echo "Test 4 Failed"
fi

bash -c '(sleep 1; kill $$) & exec java -jar ../xyes.jar' > ../outputfiles/out5.txt
filename='../outputfiles/out5.txt'
count_lines=$(cat $filename | wc -l)
if [ $count_lines -gt 20 ];
then
  echo 'Test 5 Successful';
else
  echo "Test 5 Failed"
fi
