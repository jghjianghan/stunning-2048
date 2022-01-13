
package controller;

import java.util.List;
import model.Pair;
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
        ui.start(BoardUtil.decode(board), this);
    }
    
    public void moveBoard(Move move){
        if (BoardUtil.isMoveValid(board, move)){
            Pair<Integer, Long> result = BoardUtil.applyMove(board, move);
            List<TileTransition> transitions = BoardUtil.generateSlidingTransitions(board, move);
            
            board = result.getValue();
            score += result.getKey();
            
            if (BoardUtil.isGameOver(board)){
                ui.showGameOver();
            }
            ui.displayBoard(BoardUtil.decode(board), transitions, score);
        }
    }
}
