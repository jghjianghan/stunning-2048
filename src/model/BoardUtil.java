package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import view.TileTransition;

/**
 * An utility class that handles the representation of the board.
 * Also simulates the mechanism of the game and scoring system.
 * @author Jiang Han
 */
public class BoardUtil {
    /*
    Dalam matrix:
    [ 0][ 1][ 2][ 3]
    [ 4][ 5][ 6][ 7]
    [ 8][ 9][10][11]
    [12][13][14][15]
   
    Dalam kode:
    [15][14][13][12][11]...[2][1][0] (masing-masing 4 bit)
    */
    
    private static int lastNewPos;
    private static int lastNewValue;
    
    /**
     * Converts board into a printable string
     * @param code The board in coded format
     * @return The formatted string of the board
     */
    public static String print(long code){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<16; i++){
            sb.append((code & 0xf));
            if (i%4==3)
                sb.append('\n');
            else
                sb.append('\t');
            code >>>= 4;
        }
        return sb.toString();
    }
    
    /**
     * Given a board as 4x4 integer array, codes the board into a long variable.
     * @param board 4x4 integer array of the board (elements should be in range [0,15])
     * @return The coded result of the board
     */
    public static long encode(int[][] board){
        long code = 0;
        for(int i = 3; i>=0; i--){
            for (int j = 3; j >= 0; j--) {
                code <<= 4;
                code |= board[i][j];
            }
        }
        return code;
    }
    
    /**
     * Given a board data in coded format, returns it's 4x4 integer array format
     * @param code The coded format of the board
     * @return A 4x4 integer array representation of the board
     */
    public static int[][] decode(long code) {
        int board[][] = new int[4][4];
        for(int i = 0; i<16; i++){
            board[i/4][i%4] = (int)(code & 0xf);
            code >>>= 4;
        }
        return board;
    }
    
    /**
     * Gets the exponent value at some particular position on a board from its coded representation
     * @param code The coded representation of the board
     * @param pos The position in the board (0-15). 0 is top-left, 15 is bottom right
     * @return The exponent of the position. Zero exponent means the position is empty
     */
    public static int getValueAt(long code, int pos){
        return (int)((code & (0xfl << (pos << 2))) >>> (pos << 2) );
    }
    
    /**
     * Sets the exponent value at some particular position on a board from its coded representation
     * @param code The coded representation of the board
     * @param pos The position in the board (0-15). 0 is top-left, 15 is bottom right
     * @param value The new value to store
     * @return The new code
     */
    public static long setValueAt(long code, int pos, int value){
        return (code & (~(0xfl << (pos << 2)))) | ((long)value << (pos<<2));
    }

    /**
     * Generates the starting board (initial state) for the game.
     * Game starts with 2 tiles (with probability of 90% valued 2 and 10% valued 4) on random positions.
     * @return The initial board
     */
    public static long generateInitialBoard() {
        Random rand = new Random();
        int pos1 = rand.nextInt(16);
        int pos2 = pos1;
        while(pos2 == pos1){
            pos2 = rand.nextInt(16);
        }
        int value1 = (Math.random() >= 0.9) ? 2 : 1;
        int value2 = (Math.random() >= 0.9) ? 2 : 1;
        
        return setValueAt(setValueAt(0, pos1, value1), pos2, value2);
    }
    
    /**
     * Checks whether a particular move is valid in a game state.
     * A move is consider valid if and only if it changes the game state.
     * In other words, some times must change position or change value (due to merging)
     * @param code Current game state
     * @param move The move to be checked
     * @return Whether the move valid in current state
     */
    public static boolean isMoveValid(long code, Move move){
        switch(move){
            case LEFT:
                for(int i = 0; i<4; i++){
                    for (int j = 1; j < 4; j++) {
                        int currExp = getValueAt(code, (i<<2) + j);
                        if (currExp != 0){
                            int leftExp = getValueAt(code, (i<<2) + (j-1));
                            if (leftExp == 0 || leftExp == currExp)
                                return true;
                        }
                    }
                }
                break;
            case RIGHT:
                for(int i = 0; i<4; i++){
                    for (int j = 0; j < 3; j++) {
                        int currExp = getValueAt(code, (i<<2) + j);
                        if (currExp != 0){
                            int rightExp = getValueAt(code, (i<<2) + (j+1));
                            if (rightExp == 0 || rightExp == currExp)
                                return true;
                        }
                    }
                }
                break;
            case UP:
                for(int i = 1; i<4; i++){
                    for (int j = 0; j < 4; j++) {
                        int currExp = getValueAt(code, (i<<2) + j);
                        if (currExp != 0){
                            int upExp = getValueAt(code, ((i-1)<<2) + j);
                            if (upExp == 0 || upExp == currExp)
                                return true;
                        }
                    }
                }
                break;
            case DOWN:
                for(int i = 0; i<3; i++){
                    for (int j = 0; j < 4; j++) {
                        int currExp = getValueAt(code, (i<<2) + j);
                        if (currExp != 0){
                            int downExp = getValueAt(code, ((i+1)<<2) + j);
                            if (downExp == 0 || downExp == currExp)
                                return true;
                        }
                    }
                }
                break;
        }
        
        return false;
    }

    /**
     * Gets an array of moves that are valid in a particular game state
     * @param code The current game state
     * @return An array of available moves in current state
     */
    public static Move[] getAvailableMoves(long code){
        List<Move> availableMoves = new ArrayList<>(4);
        
        for (Move move : Move.values()){
            if (isMoveValid(code, move)){
                availableMoves.add(move);
            }
        }       
        
        return availableMoves.toArray(new Move[0]);
    }    
    
    /**
     * Checks whether a game is over or not.
     * In other words, checks whether a game state is a terminal state.
     * Game is considered over when 
     * @param code The game state to check
     * @return Whether the game is over (state is terminal state)
     */
    public static boolean isGameOver(long code){
        return getAvailableMoves(code).length == 0;
    }
    
    /**
     * Simulate a sliding effect on the board (without the addition of new tile).
     * @param code The game state
     * @param move The move to apply on the state
     * @return A pair of score result from the action and the next game state code
     */
    public static Pair<Integer, Long> slideTiles(long code, Move move) {
        int point = 0;
        switch(move){
            case LEFT:
                for (int row = 0; row < 4; row++)
                {
                    int col = 1;
                    while (col < 4 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                        col++;
                    
                    int pointerPos = row<<2;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; col < 4; col++)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                point += 1 << (pointerValue+1);
                                code = setValueAt(code, pointerPos++, pointerValue + 1);
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                code = setValueAt(code, ++pointerPos, currentValue);
                                pointerValue = currentValue;
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                for (int row = 0; row < 4; row++)
                {
                    int col = 2;
                    while (col >=0 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                        col--;
                    
                    int pointerPos = (row<<2) + 3;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; col >= 0; col--)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                point += 1 << (pointerValue+1);
                                code = setValueAt(code, pointerPos--, pointerValue + 1);
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                code = setValueAt(code, --pointerPos, currentValue);
                                pointerValue = currentValue;
                            }
                        }
                    }
                }
                break;
            case UP:
                for (int col = 0; col < 4; col++)
                {
                    int row = 1;
                    while (row < 4 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                        row++;
                    
                    int pointerPos = col;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; row < 4; row++)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                point += 1 << (pointerValue+1);
                                code = setValueAt(code, pointerPos, pointerValue + 1);
                                pointerPos += 4;
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                pointerPos += 4;
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                            }
                        }
                    }
                }
                break;
            case DOWN:
                for (int col = 0; col < 4; col++)
                {
                    int row = 2;
                    while (row >=0 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                        row--;
                    
                    int pointerPos = 12 + col;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; row >= 0; row--)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                point += 1 << (pointerValue+1);
                                code = setValueAt(code, pointerPos, pointerValue + 1);
                                pointerPos -= 4;
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                pointerPos -= 4;
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                            }
                        }
                    }
                }
                break;
        }
        return new Pair<>(point, code);
    }
    
    /**
     * Generate the list of tile transitions that occur
     * when an action is applied on a game state (without the transition of a new tile).
     * @param code The current game state
     * @param move The move to be applied
     * @return List of tile transitions caused by a move applied to a game state
     */
    public static List<TileTransition> generateSlidingTransitions(long code, Move move){
        List<TileTransition> transitions = new ArrayList<>();
        int newRow = lastNewPos / 4;
        int newCol = lastNewPos % 4;
        transitions.add(new TileTransition(newRow, newCol, newRow, newCol, lastNewValue, 0, 1)); //create new tile
        
        switch(move){
            case LEFT:
                for (int row = 0; row < 4; row++)
                {
                    transitions.add(new TileTransition(row, 0, row, 0, getValueAt(code, (row << 2))));
                    int col = 1;
                    while (col < 4 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                    {
                        //transisi diam di tempat
                        transitions.add(new TileTransition(row, col, row, col, getValueAt(code, (row << 2) + col)));
                        col++;
                    }
                    
                    int pointerPos = row<<2;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; col < 4; col++)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                transitions.add(new TileTransition(row, col, row, pointerPos % 4, currentValue));
                                
                                code = setValueAt(code, pointerPos++, pointerValue + 1);
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                transitions.add(new TileTransition(row, col, row, pointerPos % 4, currentValue));
                                
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                code = setValueAt(code, ++pointerPos, currentValue);
                                pointerValue = currentValue;
                                
                                transitions.add(new TileTransition(row, col, row, pointerPos % 4, currentValue));
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                for (int row = 0; row < 4; row++)
                {
                    transitions.add(new TileTransition(row, 3, row, 3, getValueAt(code, (row << 2) + 3)));
                    int col = 2;
                    while (col >=0 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                    {
                        transitions.add(new TileTransition(row, col, row, col, getValueAt(code, (row << 2) + col)));
                        col--;
                    }
                    
                    int pointerPos = (row<<2) + 3;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; col >= 0; col--)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                transitions.add(new TileTransition(row, col, row, pointerPos % 4, currentValue));
                                
                                code = setValueAt(code, pointerPos--, pointerValue + 1);
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                transitions.add(new TileTransition(row, col, row, pointerPos % 4, currentValue));
                                
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                code = setValueAt(code, --pointerPos, currentValue);
                                pointerValue = currentValue;
                                
                                transitions.add(new TileTransition(row, col, row, pointerPos % 4, currentValue));
                            }
                        }
                    }
                }
                break;
            case UP:
                for (int col = 0; col < 4; col++)
                {
                    transitions.add(new TileTransition(0, col, 0, col, getValueAt(code, col)));
                    int row = 1;
                    while (row < 4 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                    {
                        transitions.add(new TileTransition(row, col, row, col, getValueAt(code, (row << 2) + col)));
                        row++;
                    }
                    
                    int pointerPos = col;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; row < 4; row++)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                transitions.add(new TileTransition(row, col, pointerPos / 4, col, currentValue));
                                    
                                code = setValueAt(code, pointerPos, pointerValue + 1);
                                pointerPos += 4;
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                transitions.add(new TileTransition(row, col, pointerPos / 4, col, currentValue));
                                
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                pointerPos += 4;
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                
                                transitions.add(new TileTransition(row, col, pointerPos / 4, col, currentValue));
                            }
                        }
                    }
                }
                break;
            case DOWN:
                for (int col = 0; col < 4; col++)
                {
                    transitions.add(new TileTransition(3, col, 3, col, getValueAt(code, 12 + col)));
                    int row = 2;
                    while (row >=0 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                    {
                        transitions.add(new TileTransition(row, col, row, col, getValueAt(code, (row << 2) + col)));
                        row--;
                    }
                    
                    int pointerPos = 12 + col;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; row >= 0; row--)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                transitions.add(new TileTransition(row, col, pointerPos / 4, col, currentValue));

                                code = setValueAt(code, pointerPos, pointerValue + 1);
                                pointerPos -= 4;
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                transitions.add(new TileTransition(row, col, pointerPos / 4, col, currentValue));
                                
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                pointerPos -= 4;
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                
                                transitions.add(new TileTransition(row, col, pointerPos / 4, col, currentValue));
                            }
                        }
                    }
                }
                break;
        }
        
        return transitions;
    }
    
    /**
     * Actually applies a move to a game state (with addition of a new tile)
     * @param code The current game state
     * @param move The move to be applied
     * @return Pair of score caused by the action and the next game state
     */
    public static Pair<Integer, Long> applyMove(long code, Move move){
        Pair<Integer, Long> result = slideTiles(code, move);
        
        code = result.getValue();
        
        ArrayList<Integer> emptyCells = new ArrayList<>();
        for(int i = 0; i<16; i++){
            if (getValueAt(code, i) == 0){
                emptyCells.add(i);
            }
        }
        if (emptyCells.isEmpty()){
            return result;
        } else {    
            int value = (Math.random() >= 0.9) ? 2 : 1;
            Random rand = new Random();
            int pos = emptyCells.get(rand.nextInt(emptyCells.size()));
            lastNewPos = pos;
            lastNewValue = value;
            return new Pair<>(result.getKey(), setValueAt(code, pos, value));
        }
    }
}
