package tinkerbell;

import java.awt.*;
import java.util.List;
import java.util.Random;



class Camera {
    int offsetX, offsetY;
    double orientation, fieldOfView, depth;
    String label;
    double closestX = Double.NaN; // To store the closest tag's X position
    double closestY = Double.NaN; // To store the closest tag's Y position
    double closestDist = Double.NaN;
    String closestID = "Na";
    Color color;
    int noisePos;
    double noiseHeading;
    Robot megaTag2Robot;
    boolean drawMegaTag2Robot = false;

    // List of AprilTags the camera can detect
    List<AprilTag> tags;

    public Camera(int offsetX,
                  int offsetY,
                  double orientation,
                  double fieldOfView,
                  double depth,
                  String label,
                  List<AprilTag> tags,
                  Color color,
                  int noisePos,
                  double noiseHeading) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.orientation = Math.toRadians(orientation);
        this.fieldOfView = Math.toRadians(fieldOfView);
        this.depth = depth;
        this.label = label;
        this.tags = tags;
        this.color = color;
        this.noisePos = noisePos;
        this.noiseHeading = Math.toRadians(noiseHeading);

        this.megaTag2Robot = new Robot(0, 0, 0, 0, 0,
                                       false, this.color, (this.label+"-MegaTag2Robot"));
    }

    public void checkTagsInView() {
        double closestDistance = Double.MAX_VALUE;
        boolean tagFound = false;

        // Compute camera's global position using robot position and heading
        double cameraHeading = this.megaTag2Robot.heading + this.orientation;
        // Ensure the heading stays within 0 to 2pi range
        cameraHeading = (cameraHeading % (2 * Math.PI) + (2 * Math.PI)) % (2 * Math.PI);
        double cameraGlobalX = this.megaTag2Robot.x + offsetX * Math.cos(this.megaTag2Robot.heading) - offsetY * Math.sin(this.megaTag2Robot.heading);
        double cameraGlobalY = this.megaTag2Robot.y + offsetX * Math.sin(this.megaTag2Robot.heading) + offsetY * Math.cos(this.megaTag2Robot.heading);

        for (AprilTag tag : tags) {
            // Compute vector from camera to tag
            double dx = tag.x - cameraGlobalX;
            double dy = tag.y - cameraGlobalY;
            double distanceToTag = Math.sqrt(dx * dx + dy * dy);

            // Compute angle from camera to tag (global)
            double angleToTag = Math.atan2(dy, dx);

            // Convert to camera-relative angle
            double relativeAngle = wrapToPi(angleToTag - cameraHeading);
            // Ensure the heading stays within 0 to 2pi range
            // relativeAngle = (relativeAngle % (2 * Math.PI) + (2 * Math.PI)) % (2 * Math.PI);

            // Check if tag is within field of view and depth
            if (Math.abs(relativeAngle) <= fieldOfView / 2 && distanceToTag <= depth) {
                if (distanceToTag < closestDistance) {
                    closestDistance = distanceToTag;
                    closestX = tag.x;
                    closestY = tag.y;
                    closestID = tag.id;
                    tagFound = true;
                }
            }
        }

        // If no tag is found, reset closestX and closestY
        if (!tagFound) {
            closestX = Double.NaN;
            closestY = Double.NaN;
            closestDist = Double.NaN;
            closestID = "Na";
            // System.out.println("No tags in view.");
        } else {
            closestDist = closestDistance; // Store the final closest distance
            // System.out.println("Closest tag ID: " + closestID + " at (" + closestX + ", " + closestY + ") with distance " + closestDistance);
        }
    }

    // Wrap angle to (-π, π]
    private double wrapToPi(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle <= -Math.PI) angle += 2 * Math.PI;
        return angle;
    }

    public void initMegaTag2Robot(int x, int y, int width, int height,
                                  double heading) {
        // called by robot so should draw megaTag2Robot
        this.drawMegaTag2Robot = true;

        // update robot info
        this.megaTag2Robot.updateState(x, y, heading);
        this.megaTag2Robot.updateDimensions(width, height);
    }
    public void updateMegaTag2Robot(int x, int y, double heading) {
        // add noise
        Random random = new Random();

        // Generate a random integer between -noise and noise
        int noiseX = random.nextInt((2 * noisePos) + 1) - noisePos;
        int noiseY = random.nextInt((2 * noisePos) + 1) - noisePos;
        double noiseH = (Math.random() * 2 - 1) * noiseHeading;

        //add those values to the input states
        int newX = x + noiseX;
        int newY = y + noiseY;
        double newHeading = heading + noiseH;

        this.megaTag2Robot.updateState(newX, newY, newHeading);
    }
}