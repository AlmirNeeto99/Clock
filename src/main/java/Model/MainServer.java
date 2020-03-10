package Model;

import java.io.IOException;
import java.net.ServerSocket;

import Util.EntryTable;

public class MainServer extends ServerSocket {

    private EntryTable table = new EntryTable();
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

                }
            }
        };
    }
}