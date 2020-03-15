package Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Util.EntryTable;

public class MainServer extends ServerSocket {

    private EntryTable table = new EntryTable();
    private int port;
    private Thread listen_thread;

    private ArrayList<ClientHandler> clients = new ArrayList();

    public MainServer(int port) throws IOException {
        super(port);
        this.port = port;
    }

    public void start_listen() {
        listen_thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket s = accept();
                        ClientHandler h = new ClientHandler(s);
                        clients.add(h);
                        Thread t = new Thread(h);
                        t.start();
                    } catch (IOException e) {
                    }
                }
            }
        };
        listen_thread.start();
    }

    public void closeServer() throws IOException {
        for (ClientHandler clientHandler : clients) {
            clientHandler.close();
        }
        this.close();
        listen_thread.interrupt();
    }

    private class ClientHandler extends Client {

        public ClientHandler(Socket s) throws IOException {
            super(s);
        }

        @Override
        public void handleRequest(String message) {
            JSONParser parser = new JSONParser();
            JSONObject obj = null;
            try {
                obj = (JSONObject) parser.parse(message);
                JSONObject response = new JSONObject();
                switch ((String) obj.get("cmd")) {
                    case "subscribe":
                        response.put("status", "ok");
                        this.sendResponse(response.toJSONString());
                        break;
                    case "get_clients":
                        response.put("length", clients.size() + "");
                        this.sendResponse(response.toJSONString());
                        break;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}