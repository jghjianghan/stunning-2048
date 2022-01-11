package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author Jiang Han
 */
public class Tile {
    int xPos, yPos, size, value;
    private Color textColor = new Color(5, 2, 48);
    Color color;
    double scale;
    private int radius = 10;
    private final int FONT_SIZE = 28;
    
    public Tile(int xPos, int yPos, Color color, int size, int value) {
        this(xPos, yPos, color, size, value, 1);
    }
    
    public Tile(int xPos, int yPos, Color color, int size, int value, double scale) {
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
        this.value = value;
        this.scale = scale;
    }
    
    public void draw(Graphics2D g2d){
        Color prevColor = g2d.getColor();
        g2d.setColor(color);
        
        RoundRectangle2D.Double tileShape = new RoundRectangle2D.Double(xPos, yPos, size, size, radius, radius);
        
        //scale tile
        AffineTransform scaleTile = new AffineTransform();
        int scalePivotX = (int)Math.round(tileShape.getCenterX());
        int scalePivotY = (int)Math.round(tileShape.getCenterY());
        scaleTile.translate(scalePivotX, scalePivotY);
        scaleTile.scale(scale, scale);
        scaleTile.translate(-scalePivotX, -scalePivotY);
        
        g2d.fill(scaleTile.createTransformedShape(tileShape));
        
        if (value != 0){            
            g2d.setColor(textColor);
            Font font = new Font("SansSerif", Font.BOLD, (int)Math.round(scale * FONT_SIZE));
            g2d.setFont(font);
            FontMetrics fontMetric = g2d.getFontMetrics();
            
            String strValue = Integer.toString(1 << value);
            
            g2d.drawString(
                    strValue,
                    (int)(xPos + size/2.0 - fontMetric.stringWidth(strValue)/2.0), 
                    (int)(yPos + size/2.0 + fontMetric.getAscent()/2.0));
        }
        
        g2d.setColor(prevColor);
        
    }
}
