package model.server;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import model.state.IGameState;
import model.tree.Action;
import model.tree.PlayerInterface;

public class Client {

    protected Socket socket;
    private InputStream inStream;
    private OutputStream outStream;
    private PlayerInterface playerInterface;
    private int age;

    public Client(Socket clientSocket, int age) throws IOException {
        this.socket = clientSocket;
        this.inStream = socket.getInputStream();
        this.outStream = socket.getOutputStream();
        this.age = age;
    }

    public void start(boolean start) { }

    public void playingAs(Color color) { }

    public void playingWith(List<Color> otherPlayers) { }

    public Action setUp(IGameState state) {
        return null;
    }

    public Action takeTurn(IGameState state) {
        return null;
    }

    public void end(boolean win) { }

    public int getAge() {
        return this.age;
    }

    public String getID() {
        return "Remote Player";
    }
}
