package view;

import controller.GameController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import model.BoardUtil;
import model.Move;

/**
 *
 * @author Jiang Han
 */
public final class GraphicalUI extends JPanel implements UI, ActionListener, KeyListener {
    
    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 500;
    private static final int MARGIN = 10;
    private static final int TILE_LEN = (SCREEN_WIDTH -5 * MARGIN) / 4;
    
    int lastBoard;
    
    private final ConcurrentLinkedQueue<List<Tile>> tileListQueue = new ConcurrentLinkedQueue<>();
    private GameController controller;
    
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
    
    public GraphicalUI(){
        addKeyListener(this);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        this.setBackground(new Color(187,173,160));
        
        Graphics2D g2d = (Graphics2D) g;
        
        if (!tileListQueue.isEmpty()){
            List<Tile> tiles = tileListQueue.peek();
            for(Tile tile : tiles){
                tile.draw(g2d);
            }
            
            if (tileListQueue.size() > 1)
                tileListQueue.poll();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         repaint();
    }

    @Override
    public void displayBoard(long boardCode) {
        int[][] board = BoardUtil.decode(boardCode);
        List<Tile> tiles = new ArrayList();
        for(int i = 0; i<board.length; i++){
            for (int j = 0; j < board[i].length; j++) {
                tiles.add(new Tile(MARGIN * (j+1) + j * TILE_LEN, MARGIN * (i+1) + i * TILE_LEN, COLOR_LIST[board[i][j]], TILE_LEN, board[i][j]));
            }
        }
        tileListQueue.add(tiles);
    }

    @Override
    public void start(long initialBoard, GameController controller) {
        this.controller = controller;
        displayBoard(initialBoard);
        Timer timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent arg0) {}

    @Override
    public void keyPressed(KeyEvent arg0) {}

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println(e.getKeyCode());
        switch (e.getKeyCode()) {
            case 38: //up
                controller.moveBoard(Move.UP);
                break;
            case 40: //down
                controller.moveBoard(Move.DOWN);
                break;
            case 37: //left
                controller.moveBoard(Move.LEFT);
                break;
            case 39: //right
                controller.moveBoard(Move.RIGHT);
                break;
        }
    }
}
