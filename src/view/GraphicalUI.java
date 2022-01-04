package view;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 *
 * @author Jiang Han
 */
public class GraphicalUI extends JPanel {
    
    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 500;
    private static final int MARGIN = 10;
    private static final int TILE_LEN = (SCREEN_WIDTH - 5 * MARGIN) / 4;
    
    private static final Color[] COLOR_LIST = {
        new Color(205,193,180), //abu2
        new Color(121,183,172), //kolom 1 
        new Color(255,255,186), 
        new Color(214,181,208), 
        new Color(251,223,235), 
        new Color(188,221,212), //kolom 2
        new Color(238,224,221), 
        new Color(203,227,195), 
        new Color(247,183,210), 
        new Color(198,225,206), //kolom 3
        new Color(249,243,229), 
        new Color(214,228,143), 
        new Color(162,218,219), 
        new Color(224,224,226), //kolom 4
        new Color(235,148,157), 
        new Color(202,233,235), 
        new Color(243,202,218), 
    };
    
    private Vector<Tile> tiles = new Vector<>();
    
    int board[][] = {
        {1, 5, 14, 13},
        {7, 10, 8, 12},
        {0, 6, 2, 3},
        {15, 9, 11, 4},
    };
        
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        this.setBackground(new Color(187,173,160));
        
        Graphics2D g2d = (Graphics2D) g;
        
        tiles.clear();
        for(int i = 0; i<board.length; i++){
            for (int j = 0; j < board[i].length; j++) {
                tiles.add(new Tile(MARGIN * (j+1) + j * TILE_LEN, MARGIN * (i+1) + i * TILE_LEN, COLOR_LIST[board[i][j]], TILE_LEN, board[i][j]));
            }
        }
        
        for(Tile tile : tiles){
            tile.draw(g2d);
        }
    }
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        JPanel panel = new GraphicalUI();
        f.getContentPane().add("Center", panel);
        f.getContentPane().setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
