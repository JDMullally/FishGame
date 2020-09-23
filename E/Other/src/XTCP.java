import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class XTCP  {

    private ServerSocket server;
    private Socket socket;

    /*
     * Constructor
     */
    private XTCP(int port) throws IOException {
        List<String> json = this.read(port);
        List<String> result = this.parseJSON(json);
        this.write(result);
    }

    /*
     * Entry point for the application
     *
     * @param args program arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            new XTCP(4567);
        } else if (args.length > 1) {
            System.out.println("usage: ./xtcp natural-number");
            System.exit(0);
        } else {
            try {
                new XTCP(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                System.out.println("usage: ./xtcp natural-number");
                System.exit(0);
            }
        }
    }

    /*
     * Creates a socket with the specified port and reads JSON values from the input side of a TCP connection
     */
    private List<String> read(int port) throws IOException {
        try {
            this.server = new ServerSocket(port);
            this.server.setSoTimeout(3000); // sets connection timeout to 3 seconds
            this.socket = this.server.accept();

            // read json from client
            Scanner scanner = new Scanner(this.socket.getInputStream());
            List<String> list = new ArrayList<>();
            while (scanner.hasNext()) {
                list.add(scanner.next());
            }

            return list;
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException("Timeout: client didn't connect to server in 3 seconds");
        }
    }

    /*
     * Parses the JSON values and returns a result
     */
    private List<String> parseJSON(List<String> json) throws IOException {
        // file path to execute xjson
        String filePath = new File(".").getCanonicalPath();
        filePath = filePath.substring(0, filePath.length() - 1) + "C/xjson";

        // execute xjson with json arguments
        ProcessBuilder processBuilder = new ProcessBuilder(filePath);
        Process process = processBuilder.start();

        // writes & reads from xjson
        OutputStream stdin = process.getOutputStream();
        InputStream stdout = process.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        // writes json to xjson
        for (String j : json) {
            writer.write(j);
            writer.flush();
        }
        writer.close();

        // reads json from xjson
        Scanner scanner = new Scanner(stdout);
        List<String> result = new ArrayList<>();
        while (scanner.hasNext()) {
            String s = scanner.next();
            System.out.println(s);
            result.add(s);
        }
        process.destroy(); // terminate xjson

        return result;
    }

    /*
     * Writes the JSON result to the client
     */
    private void write(List<String> result) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        for (String s : result) {
            writer.write(s + "\n");
        }
        writer.flush();
        writer.close();
        this.socket.close();
        this.server.close();
    }

}