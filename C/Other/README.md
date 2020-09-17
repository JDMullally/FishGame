# C - Command Line

xjson consumes an arbitrarily long sequence of JSON values from STDIN. When the input stream ends, it outputs two JSON values to STDOUT:

### Example
Go to the C/Other/Task directory  and  run

```
$  ./xjson [arg1 arg2 ...argN]
```
Which reads well formed JSON values from STDIN and outputs two JSON values to STDOUT

- the first one is a JSON object with two fields: count and seq. The value of the first field is the number of JSON values read; the value of the second field is a JSON list of all JSON values read in order.

- the second one is a JSON list whose first element is the count of JSON values read and the remainder is the sequence of JSON values read in reverse order.

### Testing
Go to the C/Other/Test directory and run

```
$  ./test.sh
```