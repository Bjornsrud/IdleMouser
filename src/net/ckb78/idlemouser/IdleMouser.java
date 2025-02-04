package net.ckb78.idlemouser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * IdleMouser 1.0
 * <p>
 * Moves the cursor 10 pixels in a random direction every set number of seconds.
 * <p>
 * This is useful when working on computers where someone has set the time before screensaver
 * or lockscreen activation way too low, and you cannot access the settings to change it.
 */
public class IdleMouser {

    private static final int IDLE_TIME_DEFAULT = 3 * 60 * 1000;
    private static final int MOVE_DISTANCE = 10;

    private static Timer timer;
    private static boolean isOn = false;

    public static void main(String[] args) {
        createAndShowGUI();
        startIdleTimer();
    }

    private static void startIdleTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isOn) {
                    moveCursor();
                }
            }
        }, IDLE_TIME_DEFAULT, IDLE_TIME_DEFAULT);
    }

    private static void moveCursor() {
        try {
            Robot robot = new Robot();
            Point currentPos = MouseInfo.getPointerInfo().getLocation();
            int deltaX = getRandomValueInRange();
            int deltaY = getRandomValueInRange();
            robot.mouseMove((int) currentPos.getX() + deltaX, (int) currentPos.getY() + deltaY);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static int getRandomValueInRange() {
        Random random = new Random();
        return random.nextInt(IdleMouser.MOVE_DISTANCE - -10 + 1) + -10;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("IdleMouser 1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(320, 150);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel titleLabel = new JLabel("<html><b>Christian Bjørnsrud 2024</b><hr></html>");
        JTextField idleTimeField = new JTextField(Integer.toString(IDLE_TIME_DEFAULT / 1000), 4);
        JLabel switchLabel = new JLabel("IdleMouser OFF/ON:");
        ToggleSwitch toggleSwitch = new ToggleSwitch();

        JButton applyButton = getApplyButton(idleTimeField);

        JPanel idleTimePanel = new JPanel();
        idleTimePanel.add(idleTimeField);
        idleTimePanel.setPreferredSize(new Dimension(30, 35));

        JPanel togglePanel = new JPanel();
        togglePanel.add(toggleSwitch);
        togglePanel.setPreferredSize(new Dimension(50, 35));

        toggleSwitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isOn = !isOn;
                toggleSwitch.setSelected(isOn);
            }
        });

        panel.add(titleLabel);
        panel.add(new JLabel());
        panel.add(switchLabel);
        panel.add(togglePanel);
        panel.add(new JLabel("Wait timer (Seconds):"));
        panel.add(idleTimePanel);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.SOUTH, applyButton);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    private static JButton getApplyButton(JTextField idleTimeField) {
        JButton applyButton = new JButton("SET NEW WAIT TIMER VALUE");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idleTime = Integer.parseInt(idleTimeField.getText()) * 1000;
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (isOn) {
                            moveCursor();
                        }
                    }
                }, idleTime, idleTime);
            }
        });
        return applyButton;
    }

    static class ToggleSwitch extends JComponent {
        private boolean selected = false;

        public ToggleSwitch() {
            setPreferredSize(new Dimension(50, 20));
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            g2d.setColor(Color.GRAY);
            g2d.fillRoundRect(0, 0, width, height, height, height);

            if (selected) {
                g2d.setColor(Color.GREEN);
                g2d.fillOval(width - height, 0, height, height);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillOval(0, 0, height, height);
            }
        }
    }
}