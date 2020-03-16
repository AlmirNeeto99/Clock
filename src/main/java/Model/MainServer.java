package Model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MainServer extends ServerSocket {

    private ArrayList<Set> table = new ArrayList();
    private int port;
    private Thread listen_thread;

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
        this.close();
        listen_thread.interrupt();
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
                    case "subscribe":
                        String host = (String) obj.get("host");
                        int port = Integer.parseInt(obj.get("port").toString());
                        if (!exists_entry(host, port)) {
                            int priority = get_random_id();
                            table.add(new Set(host, port, "client", priority));
                            response.put("status", "ok");
                        } else {
                            response.put("status", "duplicate");
                        }
                        this.sendResponse(response.toJSONString());
                        break;
                    case "get_server":
                        response.put("server", return_server());
                        this.sendResponse(response.toJSONString());
                        break;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private int get_random_id() {
        Random rand = new Random();
        int num = rand.nextInt(100);

        for (Set set : table) {
            if (set.getPrior() == num) {
                return get_random_id();
            }
        }
        return num;
    }

    private boolean exists_entry(String host, int port) {
        for (Set set : table) {
            if (set.getHost().equals(host) && set.getPort() == port)
                return true;
        }
        return false;
    }

    private String return_server() {
        for (Set s : table) {
            if (s.getModel().equals("server")) {
                return s.getHost() + ":" + s.getPort();
            }
        }
        return null;
    }

    private class Set {

        private String host;
        private int port;
        private String model;
        private int prior;

        public Set(String host, int port, String model, int prior) {
            this.host = host;
            this.port = port;
            this.model = model;
            this.prior = prior;
        }

        public Set(String model, int prior) {
            this.model = model;
            this.prior = prior;
        }

        public String getHost() {
            return this.host;
        }

        public int getPort() {
            return this.port;
        }

        public String getModel() {
            return this.model;
        }

        public int getPrior() {
            return this.prior;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public void setPrior(int prior) {
            this.prior = prior;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Set) {
                Set s = (Set) obj;
                if ((this.getHost() + ":" + this.port).equals((s.getHost() + ":" + s.getPort()))) {
                    return true;
                }
            }
            return false;
        }
    }
}