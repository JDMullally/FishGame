import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The program consumes an arbitrarily long sequence of JSON values from STDIN.
 * When the input stream ends, it outputs two JSON values to STDOUT:
 *
 * the first one is a JSON object with two fields: count and seq.
 * The value of the first field is the number of JSON values read;
 * the value of the second field is a JSON list of all JSON values read in order.
 *
 * the second one is a JSON list whose first element is the count of JSON values read and the
 * remainder is the sequence of JSON values read in reverse order.
 */
public class Xjson {

    public static void main(String[] args) {
        xjson(args);
    }

    public static void xjson(String[] args) {
        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);

        int count = 0;
        ArrayList<String> seq = new ArrayList<>();
        while (scanner.hasNext()) {
            String s = scanner.next();
            if (!s.isEmpty()) {
                seq.add(s);
                count++;
            }
        }

        XJsonObject xJsonObject = new XJsonObject(count, seq);
        System.out.println(gson.toJson(xJsonObject));

        Collections.reverse(seq); // reverses seq
        seq.add(0, Integer.toString(count)); // add count to front of seq list
        System.out.println(gson.toJson(seq));
    }
}
