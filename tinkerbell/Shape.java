package tinkerbell;

import java.awt.*;
import java.util.List;

// Shape class
class Shape {
    List<int[]> coords;  // List of coordinate pairs for a shape
    String name;
    Color color;

    public Shape(List<int[]> coords, String name, Color color) {
        this.coords = coords;
        this.name = name;
        this.color = color;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);

        // Ensure that x and y arrays are correctly populated
        int[] x = new int[coords.size()];
        int[] y = new int[coords.size()];

        // Loop through coords and populate the x and y arrays
        for (int i = 0; i < coords.size(); i++) {
            int[] coord = coords.get(i);
            x[i] = coord[0];  // x-coordinate
            y[i] = coord[1];  // y-coordinate
        }

        // Draw the polygon
        g2d.drawPolygon(x, y, coords.size());
    }
}