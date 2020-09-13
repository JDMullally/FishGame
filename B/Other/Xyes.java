package Other;

/*
 * a shell-executable command-line program that echos the command line arguments to standard output
 */
public class Xyes {
    /*
     * Accepts command line arguments to print
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        textline(args);
    }

    public static void textline(String[] args) {
        String str = "hello world";
        boolean limit = false;

        if (args.length > 0) {
            if (args[0].equals("-limit")) {
                limit = true;
                str = String.join(" ", args).substring(args[0].length()).trim(); // removes "-limit" from str
            } else {
                str = String.join(" ", args).trim();
            }
        }

        int counter = 0;
        while (!limit || counter < 20) {
            System.out.println(str);
            counter++;
        }
    }
}
