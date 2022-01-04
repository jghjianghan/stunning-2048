package view;

import controller.GameController;

/**
 *
 * @author Jiang Han
 */
public interface UI {
    public void start(long initialBoard, GameController controller);
    public void displayBoard(long boardCode);
    
}
