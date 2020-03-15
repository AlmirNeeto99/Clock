package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Client implements Runnable {

    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private boolean isOn = true;

    public Client(Socket s) throws IOException {
        this.client = s;
        this.out = new ObjectOutputStream(this.client.getOutputStream());
        this.in = new ObjectInputStream(this.client.getInputStream());
    }

    @Override
    public void run() {
        while (isOn) {
            String message = null;
            try {
                message = (String) in.readObject();
                if (message != null) {
                    handleRequest(message);
                }
            } catch (IOException | ClassNotFoundException ex) {
                //ex.printStackTrace();
            }
        }
    }

    public void close() {
        this.isOn = false;
    }

    public abstract void handleRequest(String message);

    public void sendResponse(String message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
}