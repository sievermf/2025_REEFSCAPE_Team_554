package tinkerbell;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;


class SwerveDrive extends JPanel {
    private double vx = 0, vy = 0, rot = 0;
    private double targetVx = 0, targetVy = 0, targetRot = 0; // Desired velocities
    private final double MAX_VELOCITY = 3.5;
    private final double MAX_ROTATION = .1;
    private final double DAMPING = .5;
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
        targetRot = 0;

        if (pressedKeys.contains(KeyEvent.VK_W)) targetVx = MAX_VELOCITY;
        if (pressedKeys.contains(KeyEvent.VK_S)) targetVx = -MAX_VELOCITY;
        if (!pressedKeys.contains(KeyEvent.VK_W)
            && !pressedKeys.contains(KeyEvent.VK_S)
            && !rightPressed
            && !leftPressed) vx = 0;
        if (pressedKeys.contains(KeyEvent.VK_A)) targetVy = -MAX_VELOCITY;
        if (pressedKeys.contains(KeyEvent.VK_D)) targetVy = MAX_VELOCITY;
        if (!pressedKeys.contains(KeyEvent.VK_A)
            && !pressedKeys.contains(KeyEvent.VK_D)
            && !rightPressed
            && !leftPressed) vy = 0;
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) targetRot = -MAX_ROTATION;
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) targetRot = MAX_ROTATION;
        if (!pressedKeys.contains(KeyEvent.VK_LEFT)
            && !pressedKeys.contains(KeyEvent.VK_RIGHT)
            && !rightPressed
            && !leftPressed) rot = 0;

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
        vx += Math.max(-MAX_VELOCITY, Math.min(MAX_VELOCITY, (targetVx - vx) * DAMPING * deltaTime));
        vy += Math.max(-MAX_VELOCITY, Math.min(MAX_VELOCITY, (targetVy - vy) * DAMPING * deltaTime));
        rot += Math.max(-MAX_ROTATION, Math.min(MAX_ROTATION, (targetRot - rot) * DAMPING * deltaTime));
    }

    private void updateState() {
        // Smoothly adjust velocities towards the target values
        smoothControl(deltaTime);

        double newHeading = controlRobot.heading + rot;
        int newX = controlRobot.x + (int) (vx * Math.cos(newHeading) - vy * Math.sin(newHeading));
        int newY = controlRobot.y + (int) (vx * Math.sin(newHeading) + vy * Math.cos(newHeading));

        controlRobot.updateState(newX, newY, newHeading);
        painter.updateRobotPosition();
    }

    /*
        BELOW FIND THE FUNCITONS ADDEDD TO THE DRIVE CONTROL
    */
    double kP = 0.1; // POSITION GAIN
    double kRot = 0.035; // ROTATIOON GAIN
    // Function called when '<' is pressed
    private void targetLeft() {
        // System.out.println("Targeting Left");
        List<Double> stateError = getStateError("L");

        if (!stateError.stream().allMatch(value -> value == 0.0)) {
            targetVx = kP * stateError.get(0);
            targetVy = kP * stateError.get(1);
            targetRot = kRot * stateError.get(2);
        }

    }
    // Function called when '>' is pressed
    private void targetRight() {
        // System.out.println("Targeting Right");
        List<Double> stateError = getStateError("R");

        if (!stateError.stream().allMatch(value -> value == 0.0)) {
            targetVx = kP * stateError.get(0);
            targetVy = kP * stateError.get(1);
            targetRot = kRot * stateError.get(2);
        }
    }
    // get the cameralabel for megaTag2 and apriltag ID targeting
    private List<Double> getStateError(String side) {
        double errX = 0.;
        double errY = 0.;
        double errHeading = 0.;
        String targetAprilTagID = controlRobot.getTargetingAprilTagID();

        // If no apriltag in sight return list of 0
        if (targetAprilTagID.equals("Na")
            || !CONSTANTS.coralScoreLocations.containsKey(targetAprilTagID + side)) {
            return List.of(errX, errY, errHeading);
        } else {
            List<Number> targetState = CONSTANTS.coralScoreLocations.get(targetAprilTagID + side);
            List<Number> curState = controlRobot.getCamStateEstimate();
            System.out.println(targetState);
            System.out.println(curState);
            System.out.println();

            errX = targetState.get(0).doubleValue() - curState.get(0).doubleValue();
            errY = targetState.get(1).doubleValue() - curState.get(1).doubleValue();
            errHeading = targetState.get(2).doubleValue() - Math.toRadians(curState.get(2).doubleValue());

            // returns [errX, errY, errHeading]
            return List.of(errX, errY, errHeading);
        }
    }
}