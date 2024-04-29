package ThreeMusketeers;

import java.util.Iterator;

public class BoardIterator implements Iterator<Cell> {

    private final Cell[][] board;
    private final int size;
    private int col_index;
    private int row_index;

    public BoardIterator(Cell[][] board, int size) {
        this.board = board;
        this.size = size - 1;
        col_index = 0;
        row_index = 0;
    }

    @Override
    public boolean hasNext() {
        return row_index <= size ;
    }

    @Override
    public Cell next() {
        Cell cell = board[row_index][col_index];
        if (col_index < size) {
            col_index++;
        }
        else {
            col_index = 0;
            row_index++;
        }
        return cell;
    }
}

