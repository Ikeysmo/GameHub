package ticTacToe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Draws a pretty firework on the display.
 * 
 * @author Barbara Lerner
 */
public class Firework 
{
    // The radius we want the firework to have
    private int fireworkRadius;
    
    // The color the firework should have.
    private Color fireworkColor;
    
    // Where the center of the firework should be.
    private double horizontalOffset;
    private double verticalOffset;
    
    /**
     * Creates and displays a firework
     * @param point the center of the firework
     * @param color the color the firework should be
     * @param radius the size of the firework
     * @param canvas the canvas to draw on
     */
    public Firework(Point2D center, Color color, int radius)
    {
        fireworkRadius = radius;
        fireworkColor = color;
        horizontalOffset = center.getX();
        verticalOffset = center.getY();
    }
    
    /**
     * Draw a firework
     * @param g the graphics object to draw on
     */
    public void paint(Graphics g) {
    	// As of Java 1.2 (the current verion is 1.6), the object passed in for this parameter
    	// has the type Graphics2D.  To take advantage of the additional drawing capabilities,
    	// we need to cast the object into a Graphics2D object.
    	Graphics2D g2D = (Graphics2D) g;
    	
    	// Tells the graphics object which screen coordinate to treat as the origin.
    	g2D.translate(horizontalOffset, verticalOffset);
    	
    	
    	g2D.setColor(fireworkColor);
    	
        // angle is the angle of the line from the center.  The first line
        // is horizontal
        double angle = 0;

        // Repeat until we have swept through a circle, which is 2 * PI radians.
        while (angle < 2 * Math.PI) {
        	// This tells the graphics object how far to rotate the object.
        	g2D.rotate(angle);
        	
            // Draw a line.  Note that we always draw a horizontal line and we always use
        	// (0, 0) as the origing.  The translate and rotate calls reposition the
        	// origin and do the rotation.
        	g2D.drawLine(-fireworkRadius, 0, fireworkRadius, 0);
                      
            // Increase the angle so the next line will be angled differently
            // and also so the loop will eventually exit.
            angle = angle + Math.PI / 8;
        }
    }
}
