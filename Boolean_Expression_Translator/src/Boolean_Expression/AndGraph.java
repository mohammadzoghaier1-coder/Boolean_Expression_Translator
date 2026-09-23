package Boolean_Expression;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author unknown
 */
public class AndGraph{
    
    public void printAnd(Graphics g)
    {
    Graphics2D g2d = (Graphics2D) g;

        // Set color and stroke
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(2));

        // Draw the main body of the AND gate
        g2d.fillRect(100, 100, 50, 50);
        g2d.fillOval(125, 100, 50, 50);
        // Draw input lines
        g2d.drawLine(80, 110, 100, 110);
        g2d.drawLine(80, 140, 100, 140);
        g2d.drawLine(170, 125, 190, 125 );
    
    }
    
            
    
    
    
    
}
