#!/bin/bash

FILE=../Test/1-in.json
if ! test -f "$FILE"; then
    echo "$FILE does not exist."
fi

java -jar ../../xjson.jar < "../Test/1-in.json" > "../Test/1-out.json"
