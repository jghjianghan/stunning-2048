
package controller;

import java.util.List;
import javafx.util.Pair;
import model.BoardUtil;
import model.Move;
import view.TileTransition;
import view.UI;

/**
 *
 * @author Jiang Han
 */
public class GameController {
    private UI ui;
    private long board;
    private int score = 0;
    
    public GameController(UI ui){
        this.ui = ui;
        board = BoardUtil.generateInitialBoard();
        ui.start(board, this);
    }
    
    public void moveBoard(Move move){
        if (BoardUtil.isMoveValid(board, move)){
            Pair<Integer, Long> result = BoardUtil.applyMove(board, move);
            List<TileTransition> transitions = BoardUtil.generateSlidingTransitions(board, move);
            
            board = result.getValue();
            score += result.getKey();
            
            ui.displayBoard(board, transitions);
        }
        if (BoardUtil.isGameOver(board)){
            //show game over
        }
    }
}
