package view;

import controller.GameController;
import java.util.List;

/**
 * Interface for the user interface of the game
 * @author Jiang Han
 */
public interface UI {
    public void start(int[][] initialBoard, GameController controller);
    public void displayBoard(int[][] newBoardCode, List<TileTransition> transitionList, int score);    
    public void showGameOver();
    public void restart(int[][] initialBoard);
}
