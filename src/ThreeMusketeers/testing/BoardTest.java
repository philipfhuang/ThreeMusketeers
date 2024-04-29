package ThreeMusketeers.testing;

import org.junit.*;

import ThreeMusketeers.Board;
import ThreeMusketeers.Cell;
import ThreeMusketeers.Coordinate;
import ThreeMusketeers.FileBasedBoardBuilder;

public class BoardTest {

    private Board board;

    @Before
    public void setup() {
        this.board = (new FileBasedBoardBuilder("Boards/Starter.txt")).getBoard();
    }

    @Test
    public void testGetCell() {
        Cell cell = board.getCell(new Coordinate(1, 4));
        Assert.assertNotNull(cell.getPiece());
    }

}
