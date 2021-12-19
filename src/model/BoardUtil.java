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
    
    public static String print(long code){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<16; i++){
            sb.append((code & 0xf));
            sb.append('\t');
            code >>>= 4;
            
            if (i%4==3)
                sb.append('\n');
        }
        return sb.toString();
    }
    
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
    public static int getValueAt(long code, int pos){
        return (int)((code & (0xfl << (pos * 4))) >>> (pos*4) );
    }
    
}
