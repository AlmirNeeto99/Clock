package View;

import Model.Clock;
import View.Window;

import java.io.IOException;

import javax.swing.Timer;

public class Main {

    private int delay = 1000;
    private Clock clock = new Clock();
    private Timer timer1 = new Timer(250, new Window(clock));
    private Timer timer2 = new Timer(delay, clock);

    public Main() {
        timer1.start();
        timer2.start();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new Main();
    }
}
