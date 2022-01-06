package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Jiang Han
 */
public class Sparkle {
    private static final double GRAVITY_ACC = 0.5;
    
    private double xCenter, yCenter, radius, yVector;
    private final double xVector, sizeVector;
    private final Color color;
    
    public Sparkle(int xCenter, int yCenter, double xVector, double yVector, Color color, double radius, double sizeVector) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.xVector = xVector;
        this.yVector = yVector;
        
        this.color = color;
        this.radius = radius;
        this.sizeVector = sizeVector;
    }
    
    public void draw(Graphics2D g2d){
        Color prevColor = g2d.getColor();
        g2d.setColor(color);
        
        Ellipse2D.Double circleShape = new Ellipse2D.Double(xCenter - radius, yCenter-radius, radius * 2, radius * 2);
        g2d.fill(circleShape);
        g2d.setColor(prevColor);
    }
    
    public boolean isInsideBound(int screenWidth, int screenHeight){
        return xCenter - radius >= 0 && xCenter + radius <= screenWidth && yCenter-radius >= 0 && yCenter + radius <= screenHeight;
    }
    
    public void advance(){
        xCenter += xVector;
        yCenter += yVector;
        radius = Math.max(0, radius + sizeVector);
        yVector += GRAVITY_ACC;
    }
}
