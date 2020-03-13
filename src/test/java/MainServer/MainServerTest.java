package MainServer;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

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

    @Test
    public void testConnectionOK() throws IOException, SocketTimeoutException {
        Socket s = new Socket("127.0.0.1", 3000);
        s.close();
        mainServer.closeServer();
    }

    @Test(expected = IOException.class)
    public void testConnectionError() throws IOException, SocketTimeoutException {
        Socket s = new Socket("127.0.0.1", 4507);
        s.close();
        mainServer.closeServer();
    }
}