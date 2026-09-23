package Boolean_Expression;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class OrGraph{
    
    public void paintOr(Graphics g)
    {
     Graphics2D g2d = (Graphics2D) g;

        // Set color and stroke
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));

        // Draw the main body   of the OR gate
        g2d.drawArc(50, 100, 100, 50, -90, 180);
        g2d.drawArc(90, 100, 25, 50, -90, 180);
        // Draw output line
        g2d.drawLine(90, 120, 115, 120);
        g2d.drawLine(90, 130, 115, 130);
        // Draw output label
        g2d.drawLine(150, 125, 175, 125);
        
    }
    
        
}
