
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import model.BoardUtil;
import model.Move;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Jiang Han
 */
public class BoardUtilTest {

    @Test
    public void testEncodeDecode() {
        int board[][] = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        int boards[][][] = new int[5][][];
        boards[0] = board;
        board = new int[][]{
            {1, 5, 14, 13},
            {7, 10, 8, 12},
            {0, 6, 2, 3},
            {15, 9, 11, 4},};
        boards[1] = board;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 2; i < 5; i++) {
            boards[i] = new int[4][4];
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    boards[i][j][k] = random.nextInt(16);
                }
            }
        }

        for (int i = 0; i < boards.length; i++) {
            int newBoard[][] = BoardUtil.decode(BoardUtil.encode(boards[i]));
            Assert.assertArrayEquals(boards[i], newBoard);
        }
    }

    @Test
    public void testGetValueAt() {
        int board[][] = new int[4][4];
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = random.nextInt(16);
            }
        }

        long code = BoardUtil.encode(board);
        for (int i = 0; i < 16; i++) {
            Assert.assertEquals(BoardUtil.getValueAt(code, i), board[i / 4][i % 4]);
        }
    }
    
    @Test
    public void testSetValueAt() {
        int board[][] = new int[4][4];
        int board2[][] = new int[4][4];
        int resultBoard[][];        

        int values[] = {2, 0, 1, 9, 12, 15};
        
        for (int k = 0; k < values.length; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    long code = BoardUtil.encode(board);
                    long resultCode = BoardUtil.setValueAt(code, i*4 + j, values[k]);
                    board2[i][j] = values[k];
                    resultBoard = BoardUtil.decode(resultCode);
                    Assert.assertArrayEquals(board2, resultBoard);
                    board2[i][j] = 0;
                }
            }
        }
    }

    @Test
    public void testAvailableMoves1(){
        int board[][];
        Move expectedResult[];
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), Move.values());
        
        board = new int[][]{
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), Move.values());
        
        board = new int[][]{
            {1, 2, 1, 2},
            {2, 1, 2, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[0]);
    }
    
    @Test
    public void testAvailableMoves2(){
        int board[][];
        Move expectedResult[];
        board = new int[][]{
            {1, 2, 1, 2},
            {2, 3, 3, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.RIGHT});
        
        board = new int[][]{
            {1, 2, 1, 2},
            {3, 3, 2, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.RIGHT});
        
        board = new int[][]{
            {1, 2, 1, 2},
            {2, 1, 3, 3},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.RIGHT});
        
        board = new int[][]{
            {1, 2, 2, 2},
            {2, 1, 2, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), Move.values());
        
        board = new int[][]{
            {1, 2, 3, 2},
            {2, 1, 3, 1},
            {1, 2, 1, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.UP, Move.DOWN});
        
        board = new int[][]{
            {1, 2, 1, 2},
            {2, 1, 3, 1},
            {1, 2, 3, 2},
            {2, 1, 2, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.UP, Move.DOWN});
     
        board = new int[][]{
            {1, 2, 1, 2},
            {2, 1, 2, 1},
            {1, 2, 1, 3},
            {2, 1, 2, 3},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.UP, Move.DOWN});
    }
    
    @Test
    public void testAvailableMoves3() {
        int board[][];
        Move expectedResult[];
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 1, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.RIGHT, Move.UP});
        
        board = new int[][]{
            {1, 1, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.RIGHT, Move.DOWN});
        
        board = new int[][]{
            {0, 0, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.RIGHT, Move.DOWN});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 1, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.RIGHT, Move.UP});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 0, 0, 0},
            {1, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.RIGHT, Move.UP, Move.DOWN});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 1},
            {0, 0, 0, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.UP, Move.DOWN});
        
        board = new int[][]{
            {0, 0, 0, 1},
            {0, 0, 0, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.UP, Move.DOWN});
        
        board = new int[][]{
            {1, 0, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.RIGHT, Move.UP, Move.DOWN});
    }
    
    @Test
    public void testAvailableMoves4() {
        int board[][];
        Move expectedResult[];
        board = new int[][]{
            {1, 0, 0, 0},
            {2, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.RIGHT, Move.DOWN});
        
        board = new int[][]{
            {1, 2, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.RIGHT, Move.DOWN});
        
        board = new int[][]{
            {0, 0, 1, 2},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.DOWN});
        
        board = new int[][]{
            {0, 0, 0, 2},
            {0, 0, 0, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.DOWN});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 2},
            {0, 0, 0, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.UP});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 2, 1},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.UP});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {2, 1, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.RIGHT, Move.UP});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 0, 0, 0},
            {2, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.RIGHT, Move.UP});
    }
    
    @Test
    public void testAvailableMoves5() {
        int board[][];
        Move expectedResult[];
        
        board = new int[][]{
            {1, 0, 0, 0},
            {2, 0, 0, 0},
            {1, 0, 0, 0},
            {2, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.RIGHT});
        
        board = new int[][]{
            {0, 1, 0, 0},
            {0, 2, 0, 0},
            {0, 1, 0, 0},
            {0, 2, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT, Move.RIGHT});
        
        board = new int[][]{
            {0, 0, 0, 1},
            {0, 0, 0, 2},
            {0, 0, 0, 1},
            {0, 0, 0, 2},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.LEFT});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 2, 1, 2},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.UP});
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 2, 1, 2},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.UP, Move.DOWN});
        
        board = new int[][]{
            {1, 2, 1, 2},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        Assert.assertArrayEquals(BoardUtil.getAvailableMoves(BoardUtil.encode(board)), new Move[]{Move.DOWN});
    }
    
    @Test
    public void testPrint() {
        int board[][] = new int[][]{
            {1, 5, 14, 13},
            {7, 10, 8, 12},
            {0, 6, 2, 3},
            {15, 9, 11, 4}
        };
        String expectedResult = "1\t5\t14\t13\n7\t10\t8\t12\n0\t6\t2\t3\n15\t9\t11\t4\n";
        Assert.assertEquals(expectedResult, BoardUtil.print(BoardUtil.encode(board)));
    }
}
