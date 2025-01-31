package tinkerbell;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;


class SwerveDrive extends JPanel {
    private double vx = 0, vy = 0, omega = 0;
    private double targetVx = 0, targetVy = 0, targetOmega = 0; // Desired velocities
    private final double MAX_VELOCITY = 3.0;
    private final double MAX_ROTATION = 0.1;
    private final double DAMPING = 1;
    Robot controlRobot;
    MasterPainter painter;
    double deltaTime = 0.02;

    // Set to track pressed keys
    private final HashSet<Integer> pressedKeys = new HashSet<>();
    private boolean leftPressed = false; // Tracks if '<' is pressed
    private boolean rightPressed = false; // Tracks if '>' is pressed

    public SwerveDrive(Robot controlRobot, MasterPainter painter) {
        this.controlRobot = controlRobot;
        this.painter = painter;

        setFocusable(true); // Make sure key events are registered
        requestFocusInWindow(); // Ensure focus on startup

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                pressedKeys.add(keyCode);
                 updateControls();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                pressedKeys.remove(keyCode);
                updateControls();
            }
        });
        Timer timer = new Timer((int) deltaTime*1000, e -> updateState());
        timer.start();
    }

    // Update velocity and rotation based on the pressed keys
    private void updateControls() {
        targetVx = 0;
        targetVy = 0;
        targetOmega = 0;

        if (pressedKeys.contains(KeyEvent.VK_W)) targetVx = MAX_VELOCITY;
        if (pressedKeys.contains(KeyEvent.VK_S)) targetVx = -MAX_VELOCITY;
        if (pressedKeys.contains(KeyEvent.VK_A)) targetVy = -MAX_VELOCITY;
        if (pressedKeys.contains(KeyEvent.VK_D)) targetVy = MAX_VELOCITY;
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) targetOmega = -MAX_ROTATION;
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) targetOmega = MAX_ROTATION;

        /*
            THIS IS WHERE THE ADDITIONAL NON SWERVE FUNCTIONALITY BASED
            ON BUTTON PRESSES IS ADDED
        */
        // Call targetLeft when '<' is pressed
        if (pressedKeys.contains(KeyEvent.VK_COMMA) && !rightPressed) {
            leftPressed = true;
            targetLeft();
        } else { leftPressed = false; }
        // Call targetRight when '>' is pressed
        if (pressedKeys.contains(KeyEvent.VK_PERIOD) && !leftPressed) {
            rightPressed = true;
            targetRight();
        } else { rightPressed = false; }
    }

    // Gradually adjust velocities towards target values
    private void smoothControl(double deltaTime) {
        // Apply smoothing to the velocities and rotation
        vx += (targetVx - vx) * DAMPING * deltaTime;
        vy += (targetVy - vy) * DAMPING * deltaTime;
        omega += (targetOmega - omega) * DAMPING * deltaTime;
    }

    private void updateState() {
        // Smoothly adjust velocities towards the target values
        smoothControl(deltaTime);

        double newHeading = controlRobot.heading + omega;
        int newX = controlRobot.x + (int) (vx * Math.cos(newHeading) - vy * Math.sin(newHeading));
        int newY = controlRobot.y + (int) (vx * Math.sin(newHeading) + vy * Math.cos(newHeading));

        controlRobot.updateState(newX, newY, newHeading);
        painter.updateRobotPosition();
    }

    /*
        BELOW FIND THE FUNCITONS ADDEDD TO THE DRIVE CONTROL
    */
    // Function called when '<' is pressed
    private void targetLeft() {
        System.out.println("Targeting Left, STATE: "+controlRobot.getMegaTag2("cam2"));
        // Implement the behavior for targeting left
    }
    // Function called when '>' is pressed
    private void targetRight() {
        System.out.println("Targeting Right"+controlRobot.getMegaTag2("cam1"));
        // Implement the behavior for targeting right
    }
}