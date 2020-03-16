package Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Model.Node;

public class Controller {

    private String mainServerHost;
    private int mainServerPort;

    private String clockServerHost;
    private int clockServerPort;

    private boolean isServer;
    private int priorityNumber;

    private Node node;

    private int testPort;

    public Controller(String host, int port) throws IOException {
        this.mainServerHost = host;
        this.mainServerPort = port;
    }

    public void start(int port) {
        testPort = port;
        try {
            node = new Node(testPort);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            subscribe();
            System.out.println("Subscribed");
            getServer();
            System.out.println("Pegou o server");
            if (!isServer) {
                sync();
            }
        } catch (ClassNotFoundException | IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void subscribe() throws ClassNotFoundException, IOException, ParseException {
        JSONObject obj = new JSONObject();

        String ip = get_ip();
        if (ip != null) {
            obj.put("cmd", "subscribe");
            obj.put("host", ip);
            obj.put("port", testPort);

            String response = node.makeRequest(mainServerHost, mainServerPort, obj.toString());

            JSONObject res = (JSONObject) new JSONParser().parse(response);
            if (res.get("status").equals("duplicate")) {
                System.out.println("There's already a clock running at this host and port");
                System.exit(0);
            }
        }
    }

    private void getServer() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "get_server");
            String response = null;
            response = node.makeRequest(mainServerHost, mainServerPort, obj.toString());
            JSONObject res = (JSONObject) new JSONParser().parse(response);
            if (res.get("status").equals("no_server")) {
                becomeServer();
                node.start_server();
                isServer = true;
            } else {
                clockServerHost = res.get("host").toString();
                System.out.println("Clock server host: " + clockServerHost);
                clockServerPort = Integer.parseInt(res.get("port").toString());
                System.out.println("Clock server port: " + clockServerPort);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void becomeServer() {
        System.out.println("virando server");
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "im_server");
            obj.put("host", get_ip());
            obj.put("port", testPort);
            String response = null;
            response = node.makeRequest(mainServerHost, mainServerPort, obj.toString());
            JSONObject res = (JSONObject) new JSONParser().parse(response);
            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sync() throws ClassNotFoundException, ParseException {
        long before = System.currentTimeMillis();
        JSONObject obj = new JSONObject();

        String ip = get_ip();
        System.out.println("meu ip");
        int hours = 0, minutes = 0, seconds = 0;
        if (ip != null) {
            obj.put("cmd", "sync");
            String response = null;
            try {
                response = node.makeRequest(clockServerHost, clockServerPort, obj.toString());
                JSONObject res = (JSONObject) new JSONParser().parse(response);
                hours = Integer.parseInt(res.get("hours").toString());
                minutes = Integer.parseInt(res.get("minutes").toString());
                seconds = Integer.parseInt(res.get("seconds").toString());

                long after = System.currentTimeMillis();

                long delay = ((after - before) / 2) / 1000; // seconds

                seconds += delay;

                if (seconds >= 60) {
                    minutes += 1;
                    seconds = seconds + 60;
                    seconds = 0;
                }
                if (minutes >= 60) {
                    hours += 1;
                    minutes = 0;
                }
                if (hours >= 24) {
                    hours = 0;
                }
                System.out.println("Hora: " + hours);
                System.out.println("Min: " + minutes);
                System.out.println("Secs: " + seconds);
            } catch (IOException ex) { // Server is down, try to ping, then call Bully.
                ex.printStackTrace();
                try {
                    pingServer();
                } catch (Exception e) {
                    ex.printStackTrace();
                    // bully();
                }
            }

        } else {
            return;
        }

    }

    private void pingServer() throws UnknownHostException, IOException {
        Socket ping = new Socket(clockServerHost, clockServerPort);
    }

    private String get_ip() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
        }
        return null;
    }
}