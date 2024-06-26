package ThreeMusketeers;

public class Cell implements Observer {
    private Coordinate coordinate;
    private Piece piece;

    /**
     * Creates a cell with the given coordinate.
     * Piece is null if there is no piece on the cell.
     * @param coordinate Coordinate of the cell on the board.
     */
    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Create a copy of a Cell
     * @param cell a Cell to make a copy of
     */
    public Cell(Cell cell) {
        this.coordinate = cell.coordinate;
        this.piece = cell.piece;
        
    }
    
    @Override
	public void update(Object o) {
		this.piece = (Piece) o;
	}

    protected Coordinate getCoordinate() {
        return coordinate;
    }

    protected boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public String toString() {
        return hasPiece() ? piece.getSymbol() : " ";
    }
}
