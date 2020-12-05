package model.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;

/**
 * Represents interface of Client and describes how the client sends information back and forth
 * between the server.
 */
public interface ClientInterface {

    /**
     * This method is called when the client receives a message.  When the client receives that
     * message, it will parse the function being called and send the parameters to the corresponding
     * method.
     *
     * @param functionObject is the Json Object that represents a function message from the server.
     */
    void checkMethod(JsonArray functionObject) throws IOException;

    /**
     * This method is called if the function Object from the server is the start function. It takes
     * in the JsonArray of parameters and responds to the function. Since the response for the
     * function is void, the function returns void.
     *
     * @param parameters a JsonArray that represents all of the parameters sent with this function
     */
    void startTournament(JsonArray parameters);

    /**
     * This method is called if the function Object from the server is the play-as function. It takes
     * in the JsonArray of parameters and responds to the function. Since the response for the
     * function is void, the function returns void.
     *
     * This function assumes the parameters to be a single String that represents a color.
     *
     * @param parameters a JsonArray that represents all of the parameters sent with this function
     */
    void playingAs(JsonArray parameters);

    /**
     * This function assumes the parameters to be between 1 and 3 Strings that represents a color.
     * It takes in the JsonArray of parameters and responds to the function. Since the response for
     * the function is void, the function returns void.
     *
     * @param parameters a JsonArray that represents all of the parameters sent to this function
     */
    void playingWith(JsonArray parameters);

    /**
     * This method is called if the function Object from the server is the setup function. It takes
     * in the JsonArray of parameters and responds to the function. This function returns the
     * JsonArray that is sent back to the server.
     *
     * @param parameters a JsonArray that represents all of the parameters sent with this function
     * @return JsonArray that represents a Position as specified in the Fish Specifications.
     * @throws IOException if communication is cut short due to a timeout
     */
    JsonArray setUp(JsonArray parameters) throws IOException;

    /**
     * This method is called if the function Object from the server is the take-turn function. It
     * takes in the JsonArray of parameters and responds to the function. This function returns the
     * JsonArray that is sent back to the server.
     *
     * @param parameters a JsonArray that represents all of the parameters sent with this function
     * @return JsonArray that represents an Action as specified in the Fish Specifications.
     * @throws IOException if communication is cut short due to a timeout
     */
    JsonArray takeTurn(JsonArray parameters) throws IOException;

    /**
     * This method is called if the function Object from the server is the end function. It takes in
     * the JsonArray of parameters and responds to the function.
     *
     * @param parameters a JsonArray that represents all of the parameters sent with this function
     */
    void endTournament(JsonArray parameters) throws IOException;

    /**
     * This method is called after the remote Player is created to give the server their user ID.
     * The
     *
     * @return String that is equivalent to that sent name
     */
    String sendName() throws IOException;

}
