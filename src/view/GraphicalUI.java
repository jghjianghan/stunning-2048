package view;

import controller.GameController;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import model.BoardUtil;
import model.Move;

/**
 *
 * @author Jiang Han
 */
public final class GraphicalUI extends JPanel implements UI, ActionListener, KeyListener {
    
    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 600;
    public static final int BOARD_ANCHOR_X = 0;
    public static final int BOARD_ANCHOR_Y = 100;
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;
    private static final int MARGIN = 10;
    private static final int TILE_LEN = (BOARD_WIDTH -5 * MARGIN) / 4;
    
    private final boolean[] hasSeen;
    private Timer timer;
    int lastBoard;
    
    private final ConcurrentLinkedQueue<List<Tile>> tileListQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Sparkle> sparkleList = new ConcurrentLinkedQueue<>();
    
    private GameController controller;
    private int score = 0;
    private int scoreIncrement = 0;
    private double incrementPos = 0;
    private int incrementAlpha = 0;
    
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
    private boolean isGameOver;
    
    public GraphicalUI(){
        hasSeen = new boolean[20];
        for(int i = 0; i<=3; i++){
            hasSeen[i] = true;
        }
        addKeyListener(this);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(new Color(187,173,160));
        Graphics2D g2d = (Graphics2D) g;
        Map<?, ?> desktopHints = (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
            if (desktopHints != null) {
                g2d.setRenderingHints(desktopHints);
            }
        
        //Title
        Font font = new Font("SansSerif", Font.BOLD, 48);
        g2d.setFont(font);
        FontMetrics fontMetric = g2d.getFontMetrics();
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("2048", MARGIN, (int)Math.round(50 + fontMetric.getAscent() / 2.0));
        
        //Score Label
        String scoreLabel = "Score: ";
        font = new Font("SansSerif", Font.PLAIN, 24);
        g2d.setFont(font);
        fontMetric = g2d.getFontMetrics();
        int labelLen = fontMetric.stringWidth(scoreLabel);
        g2d.setColor(Color.WHITE);
        g2d.drawString(scoreLabel, 250, (int)Math.round(50 + fontMetric.getAscent() / 2.0));
        
        //Score
        font = new Font("SansSerif", Font.BOLD, 36);
        g2d.setFont(font);
        fontMetric = g2d.getFontMetrics();
        String scoreValue = Integer.toString(score);
        g2d.drawString(scoreValue, 250 + labelLen, (int)Math.round(50 + fontMetric.getAscent() / 2.0));
        int labelScoreLen = fontMetric.stringWidth(scoreValue);
        
        //Score Increment
        if (incrementAlpha > 0){
            font = new Font("SansSerif", Font.BOLD, 24);
            g2d.setFont(font);
            fontMetric = g2d.getFontMetrics();
            String incrementLabel = "+" + scoreIncrement;
            
            g2d.setColor(new Color(255, 223, 61, incrementAlpha)); //orange emas
            g2d.drawString(
                    incrementLabel, 
                    250 + labelLen + labelScoreLen, 
                    (int)Math.round(50 + fontMetric.getAscent() / 2.0 + incrementPos));
            
            incrementAlpha -= 6;
            incrementPos -= 0.8;
        }
        
        if (!tileListQueue.isEmpty()){
            List<Tile> tiles = tileListQueue.peek();
            for(Tile tile : tiles){
                tile.draw(g2d);
            }
            
            if (tileListQueue.size() > 1)
                tileListQueue.poll();
        }
        
        //Fireworks
        int sparkleCount = sparkleList.size();
        for (int i = 0; i < sparkleCount; i++) {
            Sparkle currentSparkle = sparkleList.poll();
            currentSparkle.draw(g2d);
            currentSparkle.advance();
            if (currentSparkle.isInsideBound(SCREEN_WIDTH, SCREEN_HEIGHT)){
                sparkleList.add(currentSparkle);
            }
        }
        if (isGameOver){
            font = new Font("SansSerif", Font.BOLD, 28);
            g2d.setFont(font);
            fontMetric = g2d.getFontMetrics();
            String gameoverLabel = "Game Over";
            g2d.setColor(Color.RED);
            g2d.drawString(
                    gameoverLabel, 
                    (int)Math.round(SCREEN_WIDTH/2.0 - fontMetric.stringWidth(gameoverLabel)/2.0),
                    (int)Math.round(95));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         repaint();
    }

    @Override
    public void displayBoard(long boardCode, List<TileTransition> transitionList, int newScore) {
        if (newScore != score){
            scoreIncrement = newScore - score;
            incrementAlpha = 255;
            incrementPos = 0;
            this.score = newScore;
        }
        int[][] board = BoardUtil.decode(boardCode);
        if (BoardUtil.isGameOver(boardCode)){
            isGameOver = true;
        }
        
        //board transition
        int steps = 10;
        
        for(int i=0; i<steps; i++){
            List<Tile> tiles = new ArrayList();
            //prefill with empty tile
            for(int r = 0; r<board.length; r++){
                for (int c = 0; c < board[r].length; c++) {
                    tiles.add(new Tile(
                            BOARD_ANCHOR_X + MARGIN * (c+1) + c * TILE_LEN,
                            BOARD_ANCHOR_Y + MARGIN * (r+1) + r * TILE_LEN,
                            COLOR_LIST[0], 
                            TILE_LEN, 0));
                }
            }
            
            //fill with transition tile
            for(TileTransition transition : transitionList){
                 int prevX = BOARD_ANCHOR_X + MARGIN * (transition.prevCol+1) + transition.prevCol * TILE_LEN;
                 int prevY = BOARD_ANCHOR_Y + MARGIN * (transition.prevRow+1) + transition.prevRow * TILE_LEN;
                 
                 int nextX = BOARD_ANCHOR_X + MARGIN * (transition.nextCol+1) + transition.nextCol * TILE_LEN;
                 int nextY = BOARD_ANCHOR_Y + MARGIN * (transition.nextRow+1) + transition.nextRow * TILE_LEN;
                 
                 int diffX = nextX - prevX;
                 int diffY = nextY - prevY;
                 double diffSize = transition.nextScale - transition.prevScale;
                 int currentBoardValue = transition.prevValue;
                 
                 double progress = (double) i / steps;
                 tiles.add(new Tile(
                         (int)Math.round(prevX + progress * diffX), 
                         (int)Math.round(prevY + progress * diffY),
                         COLOR_LIST[currentBoardValue], 
                         TILE_LEN,
                         currentBoardValue,
                         transition.prevScale + progress * diffSize)
                 );
            }
            tileListQueue.add(tiles);
        }
        
        //final board state
        List<Tile> tiles = new ArrayList();
        for(int i = 0; i<board.length; i++){
            for (int j = 0; j < board[i].length; j++) {
                if (!hasSeen[board[i][j]]){            
                    scheduleFireworks(i, j);
                }
                tiles.add(new Tile(BOARD_ANCHOR_X + MARGIN * (j+1) + j * TILE_LEN, BOARD_ANCHOR_Y + MARGIN * (i+1) + i * TILE_LEN, COLOR_LIST[board[i][j]], TILE_LEN, board[i][j]));
            }
        }
        for(int i = 0; i<board.length; i++){
            for (int j = 0; j < board[i].length; j++) {
                hasSeen[board[i][j]] = true;
            }
        }
        
        tileListQueue.add(tiles);
    }

    private void scheduleFireworks(int row, int col){
        int xCenter = BOARD_ANCHOR_X + MARGIN * (col+1) + col * TILE_LEN + (int)Math.round(TILE_LEN/2);
        int yCenter = BOARD_ANCHOR_Y + MARGIN * (row+1) + row * TILE_LEN + (int)Math.round(TILE_LEN/2);
        
        int sparkleCount = 20;
        
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        
        for(int i = 0; i<sparkleCount; i++){        
            sparkleList.add(new Sparkle(
                    xCenter, 
                    yCenter, 
                    rand.nextDouble(-15, 15), 
                    rand.nextDouble(-20, 1), 
                    new Color(rand.nextInt(146, 256), rand.nextInt(0, 150), rand.nextInt(0, 50)), 
                    rand.nextInt(8, 15),
                    rand.nextDouble(-0.5, 0.1)));
        }
    }
    
    @Override
    public void start(long initialBoard, GameController controller) {
        this.controller = controller;
        
        List<TileTransition> transitions =  new ArrayList<>();
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                int value = BoardUtil.getValueAt(initialBoard, (i<<2) + j);
                if (value != 0){
                    transitions.add(new TileTransition(i, j, i, j, value, 0, 1));
                }
            }
        }
        
        displayBoard(initialBoard, transitions, 0);
        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent arg0) {}

    @Override
    public void keyPressed(KeyEvent arg0) {}

    @Override
    public void keyReleased(KeyEvent e) {
        while(tileListQueue.size() > 1){
            tileListQueue.poll();
        }
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
