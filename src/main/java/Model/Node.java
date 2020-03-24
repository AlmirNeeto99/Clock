package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Node extends ServerSocket {

    private Thread server_listening;
    private boolean isServer = false;
    private ArrayList<Thread> clients;
    private Clock clock;

    public Node(int port, Clock c) throws IOException {
        super(port);
        this.clock = c;
    }

    public void att_clock(int hours, int minutes, int seconds){
        clock.setHour(hours);
        clock.setMinute(minutes);
        clock.setSecond(seconds);
    }

    public void start_server() {
        clients = new ArrayList();
        isServer = true;
        server_listening = new Thread() {
            @Override
            public void run() {
                while (isServer) {
                    Socket s;
                    try {
                        s = accept();
                        ClientHandler h = new ClientHandler(s);
                        Thread t = new Thread(h);
                        clients.add(t);
                        t.start();
                    } catch (IOException e) {
                    }
                }
            }
        };
        server_listening.start();
    }

    public void stop_server() {
        for (Thread thread : clients) {
            thread.interrupt();
        }
        clients = null;
        isServer = false;
        server_listening.interrupt();
        server_listening = null;
    }

    public String makeRequest(String host, int port, String message)
            throws IOException, ParseException, ClassNotFoundException {
        Socket con = new Socket(host, port);

        ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
        JSONObject msg = (JSONObject) new JSONParser().parse(message);
        out.writeObject(msg.toString());

        ObjectInputStream in = new ObjectInputStream(con.getInputStream());

        String ret = (String) in.readObject();
        System.out.println(ret);
        while (ret == null) {
            System.out.println(ret);
            ret = (String) in.readObject();
        }
        con.close();
        return ret;
    }

    private class ClientHandler extends Client {

        public ClientHandler(Socket s) throws IOException {
            super(s);
        }

        @Override
        public void handleRequest(String message) {
            System.out.println(message);
            JSONParser parser = new JSONParser();
            JSONObject obj = null;
            try {
                obj = (JSONObject) parser.parse(message);
                JSONObject response = new JSONObject();
                switch ((String) obj.get("cmd")) {
                    case "sync":
                        response.put("hours", clock.getHours());
                        response.put("minutes", clock.getMinutes());
                        response.put("seconds", clock.getSeconds());
                        this.sendResponse(response.toJSONString());
                        break;
                }
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}