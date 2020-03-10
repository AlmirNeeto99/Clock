package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import Util.MessageHandler;

public class PeerServer extends ServerSocket {

    private Thread accept_thread;

    private final ArrayList<PeerHandler> connected_peers = new ArrayList<PeerHandler>();

    public PeerServer(int port) throws IOException {
        super(port);
    }

    public void start() {
        accept_thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Socket s = null;
                    try {
                        s = accept();
                        PeerHandler p = new PeerHandler(s);
                        p.start();
                        connected_peers.add(p);
                    } catch (IOException ex) {

                    }
                }
            }
        };
        accept_thread.start();
    }

    public void stop() {
        accept_thread.interrupt();
    }

    public ArrayList<Socket> get_connected_peers() {
        ArrayList<Socket> arr = new ArrayList();
        for (PeerHandler peerHandler : connected_peers) {
            arr.add(peerHandler.get_socket());
        }
        return arr;
    }

    private class PeerHandler {

        private Socket peer;
        private Thread t;
        private final ObjectInputStream in;

        public PeerHandler(Socket peer) throws IOException {
            this.peer = peer;
            this.in = new ObjectInputStream(peer.getInputStream());
        }

        public Socket get_socket() {
            return this.peer;
        }

        public void start() {
            t = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        String message;
                        try {
                            if ((message = (String) in.readObject()) != null) {
                                MessageHandler.handle(peer, message);
                            }
                        } catch (ClassNotFoundException | IOException e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                        }
                    }
                }
            };
            t.start();
        }
    }
}