
package controller;

import javafx.util.Pair;
import model.BoardUtil;
import model.Move;
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
        Move[] availableMoves = BoardUtil.getAvailableMoves(board);
        for(int i = 0; i<availableMoves.length; i++){
            if (availableMoves[i] == move){
                Pair<Integer, Long> result = BoardUtil.applyMove(board, move);
                board = result.getValue();
                score += result.getKey();
                ui.displayBoard(board);
                break;
            }
        }
    }
}
