package Model;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Multicast extends MulticastSocket {

    private Thread listen;

    public Multicast(int port) throws IOException {
        super(port);
    }

    public void joinGroup(String group) throws IOException {
        this.joinGroup(InetAddress.getByName(group));
    }

    private Multicast me() {
        return this;
    }

    public void startListening() {
        listen = new Thread() {

            @Override
            public void run() {
                while (true) {
                    DatagramPacket packet = new DatagramPacket(new byte[256], 256);
                    try {
                        me().receive(packet);
                    } catch (IOException ex) {

                    }
                    String msg = new String(packet.getData(), 0, packet.getLength());
                    JSONParser parser = new JSONParser();
                    JSONObject obj = null;
                    try {
                        obj = (JSONObject) parser.parse(msg);
                    } catch (Exception ex) {

                    }
                    MessageHandler.handle(obj);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        listen.start();
    }

    public void stopListening() {
        me().listen.interrupt();
        try {
            me().listen.join();
        } catch (InterruptedException ex) {
        }
    }

    private void broadcast(String multicastMessage) throws SocketException, IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buf = multicastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("224.0.0.255"), 5544);
        socket.send(packet);
        socket.close();
    }

    public void discover() throws SocketException, IOException {
        JSONObject discover = new JSONObject();
        discover.put("cmd", "discover");

        this.broadcast(discover.toString());
    }

    private static class MessageHandler {

        public static void handle(JSONObject obj) {
            if (obj.get("cmd").equals("connect")) {
                connect();
            }
        }

        private static void connect() {
        }

    }
}