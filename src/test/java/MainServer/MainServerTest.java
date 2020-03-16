package MainServer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Model.MainServer;

public class MainServerTest {
    private static MainServer mainServer;

    @Before
    public void setUp() throws IOException {
        mainServer = new MainServer(3000);
        mainServer.start_listen();
    }

    @After
    public void aff() throws IOException {
        mainServer.closeServer();
    }

    @Test
    public void testConnectionOK() throws IOException, SocketTimeoutException {
        Socket s = new Socket("127.0.0.1", 3000);
        s.close();
    }

    @Test(expected = IOException.class)
    public void testConnectionError() throws IOException, SocketTimeoutException {
        Socket s = new Socket("127.0.0.1", 4507);
        s.close();
    }

    @Test
    public void testMessageSent() throws UnknownHostException, IOException {
        Socket s = new Socket("127.0.0.1", 3000);
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        out.writeObject("teste");
        // out.flush();
        s.close();
    }

    @Test
    public void testSubscribe() throws UnknownHostException, IOException, ClassNotFoundException, ParseException {
        Socket s = new Socket("127.0.0.1", 3000);
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

        JSONObject obj = new JSONObject();
        obj.put("cmd", "subscribe");
        out.writeObject(obj.toJSONString());
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        String msg = (String) in.readObject();
        JSONObject res = (JSONObject) new JSONParser().parse(msg);
        assertEquals("ok", (String) res.get("status"));
        s.close();
    }

    // Mudar esse teste... Está retornando o tamanho do array, mas é para retornar
    // os elementos
    @Test
    public void testGetClients() throws UnknownHostException, IOException, ClassNotFoundException, ParseException {
        ArrayList<Socket> clients = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Socket s = new Socket("127.0.0.1", 3000);
            clients.add(s);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

            JSONObject obj = new JSONObject();
            obj.put("cmd", "subscribe");
            obj.put("host", "127.0.0.1");
            obj.put("port", 3000 + i);
            obj.put("priority", i);
            out.writeObject(obj.toJSONString());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            String msg = (String) in.readObject();
            JSONObject res = (JSONObject) new JSONParser().parse(msg);
            assertEquals("ok", (String) res.get("status"));
        }
        Socket s = new Socket("127.0.0.1", 3000);
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

        JSONObject obj = new JSONObject();
        obj.put("cmd", "get_server");
        out.writeObject(obj.toJSONString());
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        String msg = (String) in.readObject();
        JSONObject res = (JSONObject) new JSONParser().parse(msg);

        JSONArray arr = (JSONArray) new JSONParser().parse(res.get("clients").toString());
        s.close();
    }

    @Test
    public void testDuplicateSubscribe()
            throws UnknownHostException, IOException, ClassNotFoundException, ParseException {
        Socket s1 = new Socket("127.0.0.1", 3000);
        ObjectOutputStream out1 = new ObjectOutputStream(s1.getOutputStream());

        JSONObject obj1 = new JSONObject();
        obj1.put("cmd", "subscribe");
        obj1.put("host", "first");
        obj1.put("port", 3000);
        obj1.put("priority", 4571);
        out1.writeObject(obj1.toJSONString());

        ObjectInputStream in1 = new ObjectInputStream(s1.getInputStream());
        String msg = (String) in1.readObject();
        JSONObject res = (JSONObject) new JSONParser().parse(msg);
        assertEquals("ok", (String) res.get("status"));

        Socket s2 = new Socket("127.0.0.1", 3000);
        ObjectOutputStream out2 = new ObjectOutputStream(s2.getOutputStream());

        JSONObject obj2 = new JSONObject();
        obj2.put("cmd", "subscribe");
        obj2.put("host", "first");
        obj2.put("port", 3000);
        obj2.put("priority", 4571);
        out2.writeObject(obj2.toJSONString());

        ObjectInputStream in2 = new ObjectInputStream(s2.getInputStream());
        String msg2 = (String) in2.readObject();
        JSONObject res2 = (JSONObject) new JSONParser().parse(msg2);
        assertEquals("duplicate", (String) res2.get("status"));

        s1.close();
        s2.close();
    }

    @Test
    public void testNotDuplicate() throws UnknownHostException, IOException, ClassNotFoundException, ParseException {
        Socket s1 = new Socket("127.0.0.1", 3000);
        ObjectOutputStream out1 = new ObjectOutputStream(s1.getOutputStream());

        JSONObject obj1 = new JSONObject();
        obj1.put("cmd", "subscribe");
        obj1.put("host", "first");
        obj1.put("port", 3000);
        obj1.put("priority", 4571);
        out1.writeObject(obj1.toJSONString());

        ObjectInputStream in1 = new ObjectInputStream(s1.getInputStream());
        String msg = (String) in1.readObject();
        JSONObject res = (JSONObject) new JSONParser().parse(msg);
        assertEquals("ok", (String) res.get("status"));

        Socket s2 = new Socket("127.0.0.1", 3000);
        ObjectOutputStream out2 = new ObjectOutputStream(s2.getOutputStream());

        JSONObject obj2 = new JSONObject();
        obj2.put("cmd", "subscribe");
        obj2.put("host", "second");
        obj2.put("port", 3000);
        obj2.put("priority", 4571);
        out2.writeObject(obj2.toJSONString());

        ObjectInputStream in2 = new ObjectInputStream(s2.getInputStream());
        String msg2 = (String) in2.readObject();
        JSONObject res2 = (JSONObject) new JSONParser().parse(msg2);
        assertEquals("ok", (String) res2.get("status"));

        s1.close();
        s2.close();
    }
}