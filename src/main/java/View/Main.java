package View;

import Model.Clock;
import javax.swing.Timer;

public class Main {

    private int delay = 1000;
    private Clock clock = new Clock();
    private Timer timer = new Timer(delay, clock);

    public Main() {
        timer.start();
    }

    private void printClock() {
        System.out.println(clock.getTime());
    }

    public static void main(String[] args) throws InterruptedException {

        Main m = new Main();

        while (true) {
            m.printClock();
            Thread.sleep(1000);
        }
    }
}
