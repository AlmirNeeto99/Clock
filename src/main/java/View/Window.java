package View;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import Model.Clock;

public class Window extends JFrame implements ActionListener {

    private final int SIZE = 600;

    private final int HEIGHT = 480;
    private final int WIDTH = SIZE;

    private Clock clock;

    private Container panel;

    private JLabel time = new JLabel("");
    Font labelFont = time.getFont();

    private JButton options = new JButton("Options");

    public Window(Clock c) {
        super();
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.clock = c;
        panel = this.getContentPane();
        createText();
        this.setVisible(true);
        this.setTitle("Clock");

        KeyListener key = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
                // System.out.println(ke);
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == 17) {
                    clock.setMinute(clock.getMinutes() + 10);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {

            }
        };
        addKeyListener(key);
    }

    private void createText() {
        time.setFont(new Font(labelFont.getName(), Font.PLAIN, 40));
        BorderLayout border = new BorderLayout();
        panel.setLayout(border);

        options.setHorizontalAlignment(JLabel.RIGHT);
        options.setPreferredSize(new Dimension(50, 25));

        time.setHorizontalAlignment(JLabel.CENTER);
        time.setText(clock.getTime());

        panel.add(options, border.PAGE_START);
        panel.add(time, border.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time.setText(clock.getTime());
        this.repaint();
    }
}