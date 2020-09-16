#!/bin/bash

FILE=../Test/1-in.json
if ! test -f "$FILE"; then
    echo "$FILE does not exist."
fi

IFS=$'\r\n' GLOBIGNORE='*' command eval  'args=($(cat ../Test/1-in.json))'

java -jar ../Other/xjson.jar "${args[@]}" > ../Test/1-out.json