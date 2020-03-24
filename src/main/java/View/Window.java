package View;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

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

    private static JLabel model = new JLabel("NODE");

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
        model.setFont(new Font(labelFont.getName(), Font.PLAIN, 20));
        BorderLayout border = new BorderLayout();
        panel.setLayout(border);

        options.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GridBagLayout grid = new GridBagLayout();
                GridBagConstraints c = new GridBagConstraints();
                JDialog dialog = new JDialog();
                dialog.setLayout(grid);
                JLabel hours = new JLabel("Hours: ");
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 0;
                dialog.add(hours, c);
                JTextField h = new JTextField();
                h.setPreferredSize(new Dimension(50, 25));
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 1;
                dialog.add(h, c);
                JLabel minutes = new JLabel("Minutes: ");
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = 0;
                dialog.add(minutes, c);
                JTextField m = new JTextField();
                m.setPreferredSize(new Dimension(50, 25));
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = 1;
                dialog.add(m, c);
                JLabel seconds = new JLabel("Seconds: ");
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 2;
                c.gridy = 0;
                dialog.add(seconds, c);
                JTextField s = new JTextField();
                s.setPreferredSize(new Dimension(50, 25));
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 2;
                c.gridy = 1;
                dialog.add(s, c);

                JLabel delay = new JLabel("Delay: ");
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 3;
                c.gridy = 0;
                dialog.add(delay, c);
                JTextField d = new JTextField();
                s.setPreferredSize(new Dimension(50, 25));
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 3;
                c.gridy = 1;
                dialog.add(d, c);

                JButton b = new JButton("Ok");
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int hrs = Integer.parseInt(h.getText());
                        int mins = Integer.parseInt(m.getText());
                        int secs = Integer.parseInt(s.getText());
                        int dly = Integer.parseInt(d.getText());

                        Main.setTime(hrs, mins, secs,dly);
                    }
                });
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = 2;
                dialog.add(b, c);

                dialog.setPreferredSize(new Dimension(450, 450));
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        options.setHorizontalAlignment(JLabel.CENTER);
        options.setMaximumSize(new Dimension(50, 25));
        options.setPreferredSize(new Dimension(50, 25));

        model.setHorizontalAlignment(JLabel.CENTER);
        model.setText("Node");

        time.setHorizontalAlignment(JLabel.CENTER);
        time.setText(clock.getTime());

        panel.add(options, border.PAGE_START);
        panel.add(time, border.CENTER);
        panel.add(model, border.PAGE_END);

    }

    public static void setNodeModel(String modelo) {
        model.setText(modelo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time.setText(clock.getTime());
        this.repaint();
    }
}