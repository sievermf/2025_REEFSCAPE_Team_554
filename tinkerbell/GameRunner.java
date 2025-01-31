package tinkerbell;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class GameRunner {

    public static void main(String[] args) {
        // Create a MasterPainter object
        MasterPainter painter = new MasterPainter(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // Create some shapes and add them to the painter
        Shape fieldBorder = new Shape(List.of(new int[] {66, 0},
                                              new int[] {625, 0},
                                              new int[] {691, 50},
                                              new int[] {691, 267},
                                              new int[] {625, 317},
                                              new int[] {66, 317},
                                              new int[] {0, 267},
                                              new int[] {0, 50}),
                                              "Field Border",
                                              Color.WHITE);
        Shape blueCoralReef = new Shape(List.of(new int[] {144, 140},
                                                new int[] {177, 121},
                                                new int[] {210, 140},
                                                new int[] {210, 178},
                                                new int[] {177, 197},
                                                new int[] {144, 178}),
                                                "Blue Coral Reef",
                                                Color.BLUE);
        Shape redCoralReef = new Shape(List.of(new int[] {481, 140},
                                               new int[] {514, 121},
                                               new int[] {547, 140},
                                               new int[] {547, 178},
                                               new int[] {514, 197},
                                               new int[] {481, 178}),
                                               "Red Coral Reef",
                                               Color.RED);
        Shape blueBarge = new Shape(List.of(new int[] {66, 0},
                                               new int[] {625, 0},
                                               new int[] {691, 50},
                                               new int[] {691, 267},
                                               new int[] {625, 317},
                                               new int[] {66, 317}),
                                               "Blue Barge",
                                               Color.BLUE);
        Shape redBarge = new Shape(List.of(new int[] {66, 0},
                                               new int[] {625, 0},
                                               new int[] {691, 50},
                                               new int[] {691, 267},
                                               new int[] {625, 317},
                                               new int[] {66, 317}),
                                               "Red Barge",
                                               Color.RED);

        List<Shape> shapeList = new ArrayList<>();
        shapeList = Arrays.asList(
            fieldBorder, blueCoralReef, redCoralReef
        );
        painter.setShapes(shapeList);

        // Create some AprilTags and add them to the painter
        AprilTag tag1 = new AprilTag(657, 291, 144, "1");
        AprilTag tag2 = new AprilTag(657, 26, 36, "2");
        AprilTag tag3 = new AprilTag(455, 0, 0, "3");
        AprilTag tag4 = new AprilTag(365, 75, 270, "4");
        AprilTag tag5 = new AprilTag(365, 242, 270, "5");
        AprilTag tag6 = new AprilTag(530, 187, -30, "6");
        AprilTag tag7 = new AprilTag(547, 158, 270, "7");
        AprilTag tag8 = new AprilTag(530, 130, 210, "8");
        AprilTag tag9 = new AprilTag(498, 130, 150, "9");
        AprilTag tag10 = new AprilTag(481, 158, 90, "10");
        AprilTag tag11 = new AprilTag(498, 187, 30, "11");
        AprilTag tag12 = new AprilTag(34, 291, 216, "12");
        AprilTag tag13 = new AprilTag(34, 26, -36, "13");
        AprilTag tag14 = new AprilTag(326, 75, 90, "14");
        AprilTag tag15 = new AprilTag(326, 242, 90, "15");
        AprilTag tag16 = new AprilTag(236, 317, 180, "16");
        AprilTag tag17 = new AprilTag(160, 187, 30, "17");
        AprilTag tag18 = new AprilTag(144, 160, 90, "18");
        AprilTag tag19 = new AprilTag(160, 130, 150, "19");
        AprilTag tag20 = new AprilTag(193, 130, 210, "20");
        AprilTag tag21 = new AprilTag(209, 158, 270, "21");
        AprilTag tag22 = new AprilTag(193, 187, -30, "22");

        List<AprilTag> aprilTagList = new ArrayList<>();
        aprilTagList = Arrays.asList(
            tag1, tag2, tag3, tag4, tag5, tag6, tag7, tag8, tag9, tag10, tag11,
            tag12, tag13, tag14, tag15, tag16, tag17, tag18, tag19, tag20, tag21,
            tag22
        );
        painter.setAprilTags(aprilTagList);

        // Create some robots and add them to the painter
        Robot robot1 = new Robot(25,160,36,36,0,
                                 true, Color.BLUE, "Team 554");
        Camera cam1 = new Camera(5, -12, 20, 62.5, 150,
                                 "limelight-left", aprilTagList, Color.CYAN,
                                 1, 1);
        robot1.addCamera(cam1);
        Camera cam2 = new Camera(5, 12, -20, 62.5,
                                 150, "limelight-right", aprilTagList,
                                 Color.ORANGE, 1, 1);
        robot1.addCamera(cam2);
        painter.addRobot(robot1);

        SwerveDrive swerveControler = new SwerveDrive(robot1, painter);

        // Robot robot2 = new Robot(534,206,36,36,240,false,Color.GREEN,"test");
        // painter.addRobot(robot2);

        // Set up the frame and add the painter
        JFrame frame = new JFrame("Robot Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(swerveControler);
        frame.add(painter);
        frame.setSize(750, 400);  // Set frame size
        frame.setVisible(true);  // Display the window

    }
}


