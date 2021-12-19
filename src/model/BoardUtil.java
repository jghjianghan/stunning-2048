package model;

import javafx.util.Pair;

/**
 * 
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
    [15][14][13][12][11]...[2][1][0]
    */
    
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
    
    public static long generateInitialBoard() {
        //TODO
        return 0;
    }
    
    public static Move[] getAvailableMoves(long code){
        //TODO
        return null;
    }
    public static Pair<Integer, Long> applyMove(long code, Move move) {
        //TODO
        return null;
    }
    
    /**
     * Gets the value at some particular position on a board from its coded representation
     * @param code The coded representation of the board
     * @param pos The position in the board (0-15). 0 is top-left, 15 is bottom right
     * @return The value of the board at some position
     */
    public static int getValueAt(long code, int pos){
        return (int)((code & (0xfl << (pos * 4))) >>> (pos*4) );
    }
}
