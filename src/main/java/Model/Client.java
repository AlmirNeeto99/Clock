package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(Socket s) throws IOException {
        this.client = s;
        this.out = new ObjectOutputStream(this.client.getOutputStream());
        this.in = new ObjectInputStream(this.client.getInputStream());
    }

    @Override
    public void run() {
        while (true) {
            String message = null;

            try {
                message = (String) in.readObject();
                if (message != null) {
                    System.out.println(message);
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Oops...");
            }
        }
    }

    public void makeRequest(String message) throws IOException {
        out.writeObject(message);
        out.close();
    }

    public void close() {
        this.close();
    }
}