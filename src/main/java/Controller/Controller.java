package Controller;

import java.io.IOException;
import java.util.Random;
import Model.Multicast;

public class Controller {

    private boolean isServer;
    private int priorityNumber;

    private Multicast multi;

    public Controller() throws IOException {
        multi = new Multicast(5544);
        multi.joinGroup("224.0.0.255");
    }

    public void startUp() {
        Random ran = new Random();
        this.priorityNumber = ran.nextInt(50);
        // multi.discover();
    }
}