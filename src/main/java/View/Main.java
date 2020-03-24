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

    private Timer timer;
    private static Controller controller = new Controller("127.0.0.1", 4000);
    private static Window win;

    public Main() throws IOException {

        System.out.println("Welcome to tic tac:");
        System.out.println("1 - MainServer");
        System.out.println("2 - Node");
        int choice = new Scanner(System.in).nextInt();

        System.out.println("Type the port to listen to:");
        int port = new Scanner(System.in).nextInt();

        if (choice == 1) {
            new MainServer(port).start_listen();

        } else {
            clock = new Clock();
            win = new Window(clock);
            timer = new Timer(250, win);
            timer.start();
            controller.start(port, clock);
        }
    }

    public static void changeModel(String model){
        win.setNodeModel(model);
    }

    public static void setTime(int hours, int minutes, int seconds, int delay){
        controller.setClockTime(hours, minutes, seconds, delay);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new Main();
    }
}
