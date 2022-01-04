package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author Jiang Han
 */
public class Tile {
    int xPos, yPos, size, value;
    private Font font = new Font("SansSerif", Font.BOLD, 28);
    private Color textColor = new Color(5, 2, 48);
    Color color;
    private int radius = 10;
    
    public Tile(int xPos, int yPos, Color color, int size, int value) {
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
        this.value = value;
    }
    
    public void draw(Graphics2D g2d){
        Color prevColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fill(new RoundRectangle2D.Double(xPos, yPos, size, size, radius, radius));
        
        if (value != 0){        
            g2d.setColor(textColor);
            g2d.setFont(font);
            FontMetrics fontMetric = g2d.getFontMetrics();
            
            String strValue = Integer.toString(1 << value);
            
            g2d.drawString(strValue, (int)(xPos + size/2.0 - fontMetric.stringWidth(strValue)/2.0), (int)(yPos + size/2.0 + fontMetric.getAscent()/2.0));
        }
        
        g2d.setColor(prevColor);
        
    }
}
