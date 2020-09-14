# B - Command Line

xyes is a shell-executable command-line program that echos the command line arguments to standard
output. The purpose is to to get to know our partners with a tiny on-line assignment and to learn
to live up very rudimentary specifications.

### Example

```
$  ./xyes [-limit] [arg1 arg2 ...argN]
```

Which turns all command line arguments into a string and concatenates those strings, separated by 
a single ASCII blank space. The program displays this final string an infinite number of times to
STDOUT, or 20 times if the first command line argument is "–limit". If there are no command line 
arguments to form the output string, the program uses "hello world" instead.

### Testing

Go to the `B/Other` directory and run:

```
$  ./test.sh
```