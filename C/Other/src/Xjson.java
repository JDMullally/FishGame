import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
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

    public static void main(String[] args) throws IOException {
        xjson(args);
    }

    public static void xjson(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int count = 0;
        JsonArray array = new JsonArray();
        while (scanner.hasNext()) {
            String s = scanner.next();

            if (s.charAt(0) == '{' || s.charAt(0) == '[') {
                JsonReader reader = new JsonReader(new StringReader(s));
                reader.setLenient(true);
                array.add(JsonParser.parseReader(reader));
                count++;
            }

            // parse json
            JsonReader reader = new JsonReader(new StringReader(s));
            reader.setLenient(true);
            try {
                while (reader.hasNext()) {
                    if (JsonToken.NUMBER.equals(reader.peek())) {
                        array.add(reader.nextLong());
                        count++;
                    } else if (JsonToken.BOOLEAN.equals(reader.peek())) {
                        array.add(reader.nextBoolean());
                        count++;
                    } else if (JsonToken.STRING.equals(reader.peek())) {
                        array.add(reader.nextString());
                        count++;
                    } else {
                        System.out.println(reader.peek());
                        break;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        scanner.close();

        // print json object
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("count", new JsonPrimitive(count));
        jsonObject.add("seq", array);
        System.out.println(jsonObject.toString());

        // print json array
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(count));
        for (int i = array.size() - 1; i >= 0; i--) {
            jsonArray.add(array.get(i));
        }
        System.out.println(jsonArray.toString());
    }
}
