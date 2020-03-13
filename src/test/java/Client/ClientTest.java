package Client;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Model.Client;
import Model.MainServer;

public class ClientTest {
    private MainServer mainServer;

    @Before
    public void setUp() throws IOException {
        mainServer = new MainServer(3000);
        mainServer.start_listen();
    }

    @Test
    public void testConnection() throws UnknownHostException, IOException {
        Client c = new Client(new Socket("127.0.0.1", 3000));
        c.makeRequest("batata doce");
        c.close();
    }

    @Test(expected = IOException.class)
    public void testConnectionWithError() throws UnknownHostException, IOException {
        Client c = new Client(new Socket("127.0.0.1", 5000));
    }
}