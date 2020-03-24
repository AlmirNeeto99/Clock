package Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Model.Clock;
import Model.Node;
import View.Main;

public class Controller {

    private String mainServerHost;
    private int mainServerPort;

    private String clockServerHost;
    private int clockServerPort;

    private boolean isServer;
    private int priorityNumber;

    private static Node node;

    private int testPort;

    private Thread sync_thread;

    private static int delay = 500;

    private Clock clock;

    public Controller(String host, int port) {
        this.mainServerHost = host;
        this.mainServerPort = port;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    private void run_clock() {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    clock.att();
                    try {
                        sleep(delay);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        t.start();
    }

    public void start(int port, Clock clock) {
        this.clock = clock;
        run_clock();
        Random r = new Random();
        delay = r.nextInt(500) + 500;
        System.out.println("Sorted delay:" + delay);
        testPort = port;
        try {
            node = new Node(testPort, clock);
            System.out.println("Connected to main server");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            subscribe();
            getServer();
            if (!isServer) {
                sync_forever();
            }
        } catch (ClassNotFoundException | IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sync_forever() {
        sync_thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sync();
                    } catch (IOException | ClassNotFoundException | ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        sync_thread.start();
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
                Main.changeModel("Server");
                becomeServer();
                node.start_server();
                isServer = true;
            } else {
                Main.changeModel("Client");
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
        System.out.println("Becoming server");
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "im_server");
            obj.put("host", get_ip());
            obj.put("port", testPort);
            String response = null;
            response = node.makeRequest(mainServerHost, mainServerPort, obj.toString());
            JSONObject res = (JSONObject) new JSONParser().parse(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sync() throws ClassNotFoundException, ParseException, IOException {
        long before = System.currentTimeMillis();
        JSONObject obj = new JSONObject();

        int hours = 0, minutes = 0, seconds = 0;

        obj.put("cmd", "sync");
        String response = null;

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
            seconds = 0;
        }
        if (minutes >= 60) {
            hours += 1;
            minutes = 0;
        }
        if (hours >= 24) {
            hours = 0;
        }

        this.node.att_clock(hours, minutes, seconds);
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

    public static void setClockTime(int hours, int minutes, int seconds, int dly) {
        if (seconds >= 60) {
            minutes += 1;
            seconds = 0;
        }
        if (minutes >= 60) {
            hours += 1;
            minutes = 0;
        }
        if (hours >= 24) {
            hours = 0;
        }
        delay = dly;
        node.att_clock(hours, minutes, seconds);
    }
}