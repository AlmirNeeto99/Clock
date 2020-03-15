package MainServer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private MainServer mainServer;

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
            out.writeObject(obj.toJSONString());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            String msg = (String) in.readObject();
            JSONObject res = (JSONObject) new JSONParser().parse(msg);
            assertEquals("ok", (String) res.get("status"));
        }
        Socket s = new Socket("127.0.0.1", 3000);
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

        JSONObject obj = new JSONObject();
        obj.put("cmd", "get_clients");
        out.writeObject(obj.toJSONString());
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        String msg = (String) in.readObject();
        JSONObject res = (JSONObject) new JSONParser().parse(msg);
        assertEquals(11, Integer.parseInt((String) res.get("length")));
        s.close();
    }
}