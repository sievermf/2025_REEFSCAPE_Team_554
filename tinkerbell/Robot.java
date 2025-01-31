package tinkerbell;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Robot {
    int x, y, width, height;
    double heading;
    boolean hasCamera;
    List<Camera> cameras;
    Color color;
    String label;

    public Robot(int x, int y, int width, int height, double heading, boolean hasCamera, Color color, String label) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.heading = Math.toRadians(heading);
        this.hasCamera = hasCamera;
        this.cameras = new ArrayList<>();
        this.color = color;
        this.label = label;
    }

    public void addCamera(Camera camera) {
        if (hasCamera) {
            this.cameras.add(camera);
            camera.initMegaTag2Robot(this.x, this.y, this.width, this.height,
                                     this.heading);
        }
    }

    public void updateState(int x, int y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;

        // Ensure the heading stays within 0 to 2pi range
        this.heading = (this.heading % (2 * Math.PI) + (2 * Math.PI)) % (2 * Math.PI);

        // Make sure to check for new tags in the new state
        if (hasCamera) {
            for (Camera cam : this.cameras) {
                cam.updateMegaTag2Robot(this.x, this.y, this.heading);
                cam.checkTagsInView();
            }
        }
    }
    public void updateDimensions(int width, int height) {
        // update the dimensions of the robot paired with this camera
        this.width = width;
        this.height = height;
    }

    public void checkCameraTagsInView() {
        if (hasCamera) {
            for (Camera cam : this.cameras) {
                cam.checkTagsInView();
            }
        }
    }

    public List<Number> getCamStateEstimate() {
        double totX = 0.;
        double totY = 0.;
        double totHeading = 0.;
        int numViableCams = 0;
        if (hasCamera) {
            for (Camera cam : this.cameras) {
                if (!Double.isNaN(cam.closestDist)) {
                    numViableCams ++;
                    List<Number> megaTag2Data = getMegaTag2(cam.label);
                    totX += megaTag2Data.get(0).doubleValue();
                    totY += megaTag2Data.get(1).doubleValue();
                    totHeading += megaTag2Data.get(2).doubleValue();
                }
            }
            double avgX = totX / numViableCams;
            double avgY = totY / numViableCams;
            double avgHeading = totHeading / numViableCams;
            return List.of(avgX, avgY, avgHeading);
        }
        throw new IllegalArgumentException("Robot " + this.label + " has no cameras.");
    }

    public String getTargetingAprilTagID() {
        double closestTagDist = Double.MAX_VALUE;
        String targetingAprilTagID= "Na";
        if (hasCamera) {
            for (Camera cam : this.cameras) {
                if (!Double.isNaN(cam.closestDist)) {
                    if (closestTagDist > cam.closestDist) {
                        closestTagDist = cam.closestDist;
                        targetingAprilTagID = cam.closestID;
                    }
                }
            }
            return targetingAprilTagID;
        }
        throw new IllegalArgumentException("Robot " + this.label + " has no cameras.");
    }

    // Function to find the camera with the given camID and return its robot states
    public List<Number> getMegaTag2(String camName) {
        if (hasCamera) {
            for (Camera cam : cameras) {
                if (cam.label.equals(camName)) {
                    return cam.megaTag2Robot.getStates();
                }
            }
            throw new IllegalArgumentException("Camera with name " + camName + " not found.");
        }
        throw new IllegalArgumentException("Robot " + this.label + " has no cameras.");
    }

    // Function to return the robot's state (x, y, heading)
    public List<Number> getStates() {
        return List.of(this.x, this.y, this.heading, 0, 0, 0);
    }

    public void draw(Graphics2D g2d, boolean megaTag2Robot) {
        g2d.setColor(this.color);
        int halfWidth = this.width / 2;
        int halfHeight = this.height / 2;

        if (megaTag2Robot) {
            g2d.setStroke(new BasicStroke(1,
                                          BasicStroke.CAP_BUTT,
                                          BasicStroke.JOIN_MITER,
                                          10.0f,
                                          new float[]{6f, 3f},
                                          0f));
        } else { g2d.setStroke(new BasicStroke(2)); }

        int[] xPoints = {
            (int) (this.x + halfWidth * Math.cos(this.heading) - halfHeight * Math.sin(this.heading)),
            (int) (this.x - halfWidth * Math.cos(this.heading) - halfHeight * Math.sin(this.heading)),
            (int) (this.x - halfWidth * Math.cos(this.heading) + halfHeight * Math.sin(this.heading)),
            (int) (this.x + halfWidth * Math.cos(this.heading) + halfHeight * Math.sin(this.heading))
        };

        int[] yPoints = {
            (int) (this.y + halfWidth * Math.sin(this.heading) + halfHeight * Math.cos(this.heading)),
            (int) (this.y - halfWidth * Math.sin(this.heading) + halfHeight * Math.cos(this.heading)),
            (int) (this.y - halfWidth * Math.sin(this.heading) - halfHeight * Math.cos(this.heading)),
            (int) (this.y + halfWidth * Math.sin(this.heading) - halfHeight * Math.cos(this.heading))
        };

        g2d.drawPolygon(xPoints, yPoints, 4);

        // Draw direction line
        int x2 = (int) (this.x + (this.width / 2) * Math.cos(this.heading));
        int y2 = (int) (this.y + (this.width / 2) * Math.sin(this.heading));
        g2d.drawLine(x, y, x2, y2);

        if (megaTag2Robot) { g2d.setStroke(new BasicStroke(2)); }

        if (hasCamera) {
            // Ensure that the robot name gets displayed if it has cam
            g2d.drawString(this.label + ":", 100, 12);
            int camNum = 1;
            for (Camera cam : this.cameras) {
                // ensure the camera tag stuff is up to date
                cam.checkTagsInView();
                g2d.setColor(cam.color);
                drawCamera(g2d, cam, camNum);
                camNum ++;
            }
        }
    }

    private void drawCamera(Graphics2D g2d, Camera cam, int camNum) {
        double radians = this.heading + cam.orientation;
        int camX = (int) (this.x + cam.offsetX * Math.cos(this.heading) - cam.offsetY * Math.sin(this.heading));
        int camY = (int) (this.y + cam.offsetX * Math.sin(this.heading) + cam.offsetY * Math.cos(this.heading));
        g2d.fillOval(camX - 2, camY - 2, 4, 4);

        // Draw FOV triangle
        int end1X = (int) (camX + cam.depth * Math.cos(radians - cam.fieldOfView / 2));
        int end1Y = (int) (camY + cam.depth * Math.sin(radians - cam.fieldOfView / 2));
        int end2X = (int) (camX + cam.depth * Math.cos(radians + cam.fieldOfView / 2));
        int end2Y = (int) (camY + cam.depth * Math.sin(radians + cam.fieldOfView / 2));

        g2d.drawLine(camX, camY, end1X, end1Y);
        g2d.drawLine(camX, camY, end2X, end2Y);
        g2d.drawLine(end1X, end1Y, end2X, end2Y);

        // Draw the line to its closest april tag
        if (!Double.isNaN(cam.closestX) && !Double.isNaN(cam.closestY)) {
            g2d.setStroke(new BasicStroke(1,
                                          BasicStroke.CAP_BUTT,
                                          BasicStroke.JOIN_MITER,
                                          10.0f,
                                          new float[]{10f, 5f},
                                          0f));
            g2d.drawLine(camX, camY, (int) cam.closestX, (int) cam.closestY);
            g2d.setStroke(new BasicStroke(2));
        }

        // Draw the cameras sight info
        g2d.drawString(cam.label + " - ID:" + cam.closestID + "  DIST: "
                       + String.format("%.1f",cam.closestDist),
                       100,
                       12*(camNum+1));

        // Draw its megtag represnetation if available
        if (cam.drawMegaTag2Robot) {
            cam.megaTag2Robot.draw(g2d, true);
        }
    }
}