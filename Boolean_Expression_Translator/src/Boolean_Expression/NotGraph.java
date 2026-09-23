/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boolean_Expression;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class NotGraph {
    
    
    public void paintNot(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;    
        g2d.setStroke(new BasicStroke(2));
        
        // Define the coordinates of the triangle
        int[] xPoints = {50, 50, 100}; // Adjusted x-coordinates to point to the right
        int[] yPoints = {100, 150, 125}; // Adjusted y-coordinates to center the triangle vertically
        int nPoints = 3;

        // Draw the triangle
        g2d.setColor(Color.BLACK);
        g2d.fillPolygon(xPoints, yPoints, nPoints);
        
        //draw lines
        g2d.drawLine(30,125,50,125);
        g2d.drawLine(100,125,120,125);

    }
    
}
