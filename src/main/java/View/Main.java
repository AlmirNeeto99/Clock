package View;

import Model.Clock;
import Model.MainServer;
import View.Window;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.Timer;

import Controller.Controller;

public class Main {

    private int delay = 1000;
    private Clock clock;
    private Timer timer1;
    private Timer timer2;
    private Controller controller = new Controller("127.0.0.1", 4000);

    public Main() throws IOException {

        System.out.println("Welcome to tic tac:");
        System.out.println("1 - MainServer");
        System.out.println("2 - Node");
        System.out.println("3 - Node");
        int choice = new Scanner(System.in).nextInt();

        clock = new Clock();
        timer1 = new Timer(250, new Window(clock));
        timer2 = new Timer(delay, clock);
        timer1.start();
        timer2.start();

        if (choice == 1) {
            new MainServer(4000).start_listen();

        } else if (choice == 2) {
            controller.start(5000);
        } else {
            controller.start(6000);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new Main();
    }
}
