package view;

import controller.GameController;
import java.util.List;

/**
 *
 * @author Jiang Han
 */
public interface UI {
    public void start(long initialBoard, GameController controller);
    public void displayBoard(long newBoardCode, List<TileTransition> transitionList, int score);    
}
