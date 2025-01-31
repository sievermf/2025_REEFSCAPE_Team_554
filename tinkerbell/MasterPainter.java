package tinkerbell;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.List;


// MasterPainter class
class MasterPainter extends JPanel {
    private List<Shape> shapes;
    private List<AprilTag> aprilTags;
    private List<Robot> robots;
    BufferedImage backgroundImage;

    public MasterPainter(List<Shape> shapes, List<AprilTag> aprilTags, List<Robot> robots) {
        this.shapes = shapes;
        this.aprilTags = aprilTags;
        this.robots = robots;
        this.backgroundImage = new BufferedImage(750, 400, BufferedImage.TYPE_INT_ARGB);

        drawBackground(); //draw the static background
    }

    private void drawBackground() {
        // This method is responsible for drawing the background, tags, etc.
        // Graphics2D g2d = (Graphics2D) this.backgroundImage.getGraphics();
        Graphics2D g2d = backgroundImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 750, 400);

        g2d.setStroke(new BasicStroke(2));

        // Draw all shapes
        for (Shape shape : shapes) {
            shape.draw(g2d);
        }

        // Draw all AprilTags
        for (AprilTag tag : aprilTags) {
            tag.draw(g2d);
        }
        // Dispose the graphics context after drawing to avoid potential memory leaks
        g2d.dispose();
        // repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the static background once
        g.drawImage(backgroundImage, 0, 0, null);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // g2d.drawImage(this.backgroundImage, 0, 0, null);
        g2d.setStroke(new BasicStroke(2));

        // Draw the robot on top
        for (Robot robot : robots) {
            robot.draw(g2d, false);
        }
    }

    public void updateRobotPosition() {
        repaint();  // Redraw only the updated position of the robot
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
        drawBackground();
    }

    public void addAprilTag(AprilTag aprilTag) {
        aprilTags.add(aprilTag);
        drawBackground();
    }

    public void addRobot(Robot robot) {
        robots.add(robot);
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
        drawBackground();
    }

    public void setAprilTags(List<AprilTag> aprilTags) {
        this.aprilTags = aprilTags;
        drawBackground();
    }

    public void setRobost(List<Robot> robots) {
        this.robots = robots;
        updateRobotPosition();
    }
}