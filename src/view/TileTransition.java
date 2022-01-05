package view;

/**
 *
 * @author Jiang Han
 */
public class TileTransition {
    int prevRow, prevCol, nextRow, nextCol, prevValue;
    double prevScale, nextScale;

    public TileTransition(int prevRow, int prevCol, int nextRow, int nextCol, int value) {
        this(prevRow, prevCol, nextRow, nextCol, value, 1, 1);
    }

    public TileTransition(int prevRow, int prevCol, int nextRow, int nextCol, int value, double prevScale, double nextScale) {
        this.prevRow = prevRow;
        this.prevCol = prevCol;
        this.nextRow = nextRow;
        this.nextCol = nextCol;
        this.prevValue = value;
        this.prevScale = prevScale;
        this.nextScale = nextScale;
    }
}
