package tinkerbell;

import java.awt.*;


// AprilTag class
class AprilTag {
    double x, y;
    double theta;
    String id;
    int aprilTagWidth = 15;

    public AprilTag(double x, double y, double theta, String id) {
        this.x = x;
        this.y = y;
        this.theta = Math.toRadians(theta);
        this.id = id;
    }

    public void draw(Graphics2D g2d) {
        // Calculate the end point of the line based on angle
        int x1 = (int) (x + (aprilTagWidth/2) * Math.cos(theta));
        int y1 = (int) (y + (aprilTagWidth/2) * Math.sin(theta));
        int x2 = (int) (x - (aprilTagWidth/2) * Math.cos(theta));
        int y2 = (int) (y - (aprilTagWidth/2) * Math.sin(theta));

        // Draw the line centered at (x, y)
        g2d.setColor(Color.MAGENTA);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.drawString("ID: " + id, (int) x + 15, (int) y + 15);
    }
}