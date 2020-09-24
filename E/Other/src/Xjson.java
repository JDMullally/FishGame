import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;

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

    private String[] args;

    /*
     * Constructor takes in args
     */
    protected Xjson(String[] args) {
        this.args = args;
    }

    /*
     * Entry point for the application
     */
    public static void main(String[] args) {
        new Xjson(args);
    }

    /*
     * Parses JSON and returns a  result
     */
    protected String[] xjson() {
        int count = 0;
        int argCounter = 0;
        JsonArray array = new JsonArray();
        while (argCounter < args.length) {
            String s = args[argCounter];
            argCounter++;

            if(!s.isEmpty()) {
                // parse json
                JsonReader reader = new JsonReader(new StringReader(s));
                reader.setLenient(true);
                try {
                    while (reader.hasNext()) {
                        JsonToken nextToken = reader.peek();
                        if (JsonToken.NUMBER.equals(nextToken)) {
                            array.add(reader.nextLong());
                            count++;
                        } else if (JsonToken.BOOLEAN.equals(nextToken)) {
                            array.add(reader.nextBoolean());
                            count++;
                        } else if (JsonToken.STRING.equals(nextToken)) {
                            array.add(reader.nextString());
                            count++;
                        } else if (JsonToken.NULL.equals(nextToken)) {
                            count++;
                            array.add("NULL"); // Can't add a null value to JsonArray, but can add string null
                            reader.nextNull();
                        } else if (JsonToken.BEGIN_OBJECT.equals(nextToken)) {
                            JsonObject built = buildObj(reader);
                            count++;
                            array.add(built);
                        } else if (JsonToken.BEGIN_ARRAY.equals(nextToken)) {
                            JsonArray built = buildArr(reader);
                            count++;
                            array.add(built);
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // print json object
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("count", new JsonPrimitive(count));
        jsonObject.add("seq", array);

        // print json array
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(count));
        for (int i = array.size() - 1; i >= 0; i--) {
            jsonArray.add(array.get(i));
        }

        return new String[] {jsonObject.toString(), jsonArray.toString()};
    }

    private static JsonObject buildObj(JsonReader reader) {
        JsonObject build = new JsonObject();
        try {
            while (reader.hasNext()) {
                if (JsonToken.BEGIN_OBJECT.equals(reader.peek())) {
                    reader.beginObject();
                } else if (JsonToken.NAME.equals(reader.peek())) {
                    String n = reader.nextName();
                    if (JsonToken.BOOLEAN.equals(reader.peek())) {
                        Boolean b = reader.nextBoolean();
                        build.addProperty(n, b);
                    } else if (JsonToken.NUMBER.equals(reader.peek())) {
                        Integer i = reader.nextInt();
                        build.addProperty(n, i);
                    } else if (JsonToken.STRING.equals(reader.peek())) {
                        String s = reader.nextString();
                        build.addProperty(n, s);
                    } else if (JsonToken.BEGIN_OBJECT.equals(reader.peek())) {
                        JsonElement obj = buildObj(reader);
                        build.add(n,obj);
                    } else if (JsonToken.BEGIN_ARRAY.equals(reader.peek())) {
                        JsonElement arr = buildArr(reader);
                        build.add(n, arr);
                    }
                } else if (JsonToken.END_OBJECT.equals(reader.peek())) {
                    reader.endObject();
                    return build;
                } else {
                    break;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return build;
    }

    private static JsonArray buildArr(JsonReader reader) throws IOException {
        JsonArray build = new JsonArray();
        try {
            while (reader.hasNext()) {
                if (JsonToken.BEGIN_ARRAY.equals(reader.peek())) {
                    reader.beginArray();
                }
                else if (JsonToken.BOOLEAN.equals(reader.peek())) {
                    Boolean b = reader.nextBoolean();
                    build.add(b);
                } else if (JsonToken.NUMBER.equals(reader.peek())) {
                    Integer i = reader.nextInt();
                    build.add(i);
                } else if (JsonToken.STRING.equals(reader.peek())) {
                    String s = reader.nextString();
                    build.add(s);
                } else if (JsonToken.BEGIN_OBJECT.equals(reader.peek())) {
                    JsonElement obj = buildObj(reader);
                    build.add(obj);
                } else if (JsonToken.BEGIN_ARRAY.equals(reader.peek())) {
                    JsonElement arr = buildArr(reader);
                    build.add(arr);
                }
                else if (JsonToken.END_ARRAY.equals(reader.peek())) {
                    reader.endArray();
                    return build;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Oops");
        }
        return build;
    }
}
