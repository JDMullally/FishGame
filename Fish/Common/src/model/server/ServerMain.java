package model.server;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        int port = -1;
        /*
        try {
            port = Integer.getInteger(args[0]);
        } catch (Exception e) {
            System.out.println("usage: ./xserver <port number>");
            System.exit(0);
        }*/
        Server server = new SignUpServer(5056);
        System.out.println("start sign up");
        server.signUp();
        System.out.println("successfully signed up");
        server.sendToTournamentManager();
    }

}
