package ThreeMusketeers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Board extends Observable implements Iterable<Cell>{
    public int size = 5;

    // 2D Array of Cells for representation of the game board
    public final Cell[][] board = new Cell[size][size];
    private Piece.Type turn;
    private Piece.Type winner;
    private int count = 0;
    private boolean cheat = false;
    private boolean chat = false;
    private String filePath;
	private boolean randomMode, fileMode;


    /**
     * Create a Board with the current player turn set and a specified board.
     * @param boardFilePath The path to the board file to import (e.g. "Boards/Starter.txt")
     */
    public Board(String filePath) {
        this.filePath = filePath;
		this.fileMode = true;
		this.randomMode = false;
    }

    /**
     * Creates a Board copy of the given board.
     * @param board Board to copy
     */
    public Board(Board board) {
        this.size = board.size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.board[row][col] = new Cell(board.board[row][col]);
            }
        }
        this.turn = board.turn;
        this.winner = board.winner;
        this.fileMode = board.fileMode;
		this.randomMode = board.randomMode;
    }
    
    /**
     * Set the board whether the file mode
     * @param whether the board is file mode
     */
    public void setFileMode(boolean fileMode) {
		this.fileMode = fileMode;
	}
	
    /**
     * Set the board whether the random mode
     * @param whether the board is random mode
     */
	public void setRandomMode(boolean randomMode) {
		this.randomMode = randomMode;
	}
	
	/**
     * Set the current turn of the board
     * @param The current turn
     */
	public void setTurn(Piece.Type type) {
		this.turn = type;
	}

    /**
     * @return the Piece.Type (Muskeeteer or Guard) of the current turn
     */
    public Piece.Type getTurn() {
        return turn;
    }

    /**
     * Get the cell located on the board at the given coordinate.
     * @param coordinate Coordinate to find the cell
     * @return Cell that is located at the given coordinate
     */
    public Cell getCell(Coordinate coordinate) {
        return this.board[coordinate.row][coordinate.col];
    }

    /**
     * @return the game winner Piece.Type (Muskeeteer or Guard) if there is one otherwise null
     */
    public Piece.Type getWinner() {
        return winner;
    }

    /**
     * Gets all the Musketeer cells on the board.
     * @return List of cells
     */
    public List<Cell> getMusketeerCells() { // TODO
    	List<Cell> musketeerCells = new ArrayList<>();
    	for (Cell cell : this) {
            if (cell.hasPiece() && cell.getPiece().getType() == Piece.Type.MUSKETEER) {
                musketeerCells.add(cell);
            }
    	}
        return musketeerCells;
    }

    /**
     * Gets all the Guard cells on the board.
     * @return List of cells
     */
    public List<Cell> getGuardCells() { // TODO
    	List<Cell> guardCells = new ArrayList<>();
    	for (Cell cell : this) {
            if (cell.hasPiece() && cell.getPiece().getType() == Piece.Type.GUARD) {
                guardCells.add(cell);
            }
    	}
        return guardCells;
    }

    /**
     * Gets all the Empty cells on the board.
     * @return List of cells
     */
    private List<Cell> getEmptyCells() {
        List<Cell> emptyCells = new ArrayList<>();
        for (Cell cell : this) {
            if (!cell.hasPiece()) {
                emptyCells.add(cell);
            }
        }
        return emptyCells;
    }

    /**
     * Executes the given move on the board and changes turns at the end of the method.
     * @param move a valid move
     */
    public void changecheat() {
    	this.cheat = cheat == true ? false : true;
    }
    
    public void move(Move move) { // TODO
    	super.notifyObservers(move.toCell, move.fromCell.getPiece());
    	super.notifyObservers(move.fromCell, null);
    	changeTurn();
    }

    public void chaton() {
    	this.chat = chat == true ? false : true;
    }
    
    public String chat() {
    	List<String> s = new ArrayList<>();
    	s.add("Great walk!!!");
    	s.add("keep going!!!");
    	s.add("almost win!!!");
    	s.add("you can do that!");
    	s.add("not bad!");
    	int random = (int )(Math.random() * s.size());
		return s.get(random);
    	
    }
    /**
     * Undo the move given.
     * @param move Copy of a move that was done and needs to be undone. The move copy has the correct piece info in the
     *             from and to cell fields. Changes turns at the end of the method.
     */
    public void undoMove(Move move) { // TODO
    	super.notifyObservers(move.fromCell, move.fromCell.getPiece());
    	super.notifyObservers(move.toCell, move.toCell.getPiece());
      turn = turn == Piece.Type.MUSKETEER ? Piece.Type.GUARD : Piece.Type.MUSKETEER;
    }
        
    


    /**
     * Checks if the given move is valid. Things to check:
     * (1) the toCell is next to the fromCell
     * (2) the fromCell piece can move onto the toCell piece.
     * @param move a move
     * @return     True, if the move is valid, false otherwise
     */
    public Boolean isValidMove(Move move) {
        Cell fromCell = move.fromCell;
        if (fromCell.getPiece().getType() == Piece.Type.MUSKETEER && move.toCell.getPiece() == null) {
            return false;
        }
        Coordinate fromCoordinate = fromCell.getCoordinate();
        Coordinate toCoordinate = move.toCell.getCoordinate();
        if (fromCell.getPiece().getType() != this.turn) return false;
        if (!isNextTo(fromCoordinate, toCoordinate)) return false;
        if (!onBoard(toCoordinate)) return false;

        return fromCell.getPiece().canMoveOnto(move.toCell);
    }

    /**
     * Get all the possible cells that have pieces that can be moved this turn.
     * @return      Cells that can be moved from the given cells
     */
    public List<Cell> getPossibleCells() {
        List<Cell> allCellsThisTurn = getTurn() == Piece.Type.MUSKETEER ? getMusketeerCells() : getGuardCells();
        List<Cell> possibleCells = new ArrayList<>();
        for (Cell cell : allCellsThisTurn) {
            if (!getPossibleDestinations(cell).isEmpty())
                possibleCells.add(cell);
        }
        return possibleCells;
    }

    /**
     * Get all the possible cell destinations that is possible to move to from the fromCell.
     * @param fromCell The cell that has the piece that is going to be moved
     * @return List of cells that are possible to get to
     */
    public List<Cell> getPossibleDestinations(Cell fromCell) {
        List<Cell> destinations = new ArrayList<>();
        int[][] possibleMoves = {{-1,0}, {0,1}, {1,0}, {0,-1}};

        for (int[] move: possibleMoves) {
            Coordinate oldCoordinate = fromCell.getCoordinate();
            int row = move[0] + oldCoordinate.row;
            int col = move[1] + oldCoordinate.col;
            Coordinate newCoordinate = new Coordinate(row, col);
            if (!onBoard(newCoordinate)) continue;

            Cell toCell = getCell(newCoordinate);
            if (isValidMove(new Move(fromCell, toCell)))
                destinations.add(toCell);
        }
        return destinations;
    }

    /**
     * Get all the possible moves that can be made this turn.
     * @return List of moves that can be made this turn
     */
    public List<Move> getPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        List<Cell> possibleCells = this.getPossibleCells();
        for (Cell fromCell: possibleCells) {
            List<Cell> possibleDestinations = this.getPossibleDestinations(fromCell);
            for (Cell toCell : possibleDestinations) {
                moves.add(new Move(fromCell, toCell));
            }
        }
        return moves;
    }

    /**
     * Checks if the game is over and sets the winner if there is one.
     * @return True, if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        if (inSameRowOrSameCol(getMusketeerCells())) {
            winner = Piece.Type.GUARD;
            return true;
        }
        if (getPossibleCells().isEmpty()) {
            winner = Piece.Type.MUSKETEER;
            return true;
        }
        return false;
    }

    /**
     * Saves the current board state to the boards directory
     */
    public void saveBoard() {
        String filePath = String.format("boards/%s.txt",
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        File file = new File(filePath);

        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(turn.getType() + "\n");
            for (Cell[] row: board) {
                StringBuilder line = new StringBuilder();
                for (Cell cell: row) {
                    if (cell.getPiece() != null) {
                        line.append(cell.getPiece().getSymbol());
                    } else {
                        line.append("_");
                    }
                    line.append(" ");
                }
                writer.write(line.toString().strip() + "\n");
            }
            writer.close();
            System.out.printf("Saved board to %s.\n", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to save board to %s.\n", filePath);
        }
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder("  | A B C D E\n");
        boardStr.append("--+----------\n");
        for (int i = 0; i < size; i++) {
            boardStr.append(i + 1).append(" | ");
            for (int j = 0; j < size; j++) {
                Cell cell = board[i][j];
                boardStr.append(cell).append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    private Boolean onBoard(Coordinate coordinate) {
        return 0 <= coordinate.col && coordinate.col < this.size &&
                0 <= coordinate.row && coordinate.row < this.size;
    }

    private Boolean isNextTo(Coordinate fromCoordinate, Coordinate toCoordinate) {
        int xDiff = Math.abs(fromCoordinate.col - toCoordinate.col);
        int yDiff = Math.abs(fromCoordinate.row - toCoordinate.row);
        return (xDiff == 0 && yDiff == 1) || (xDiff == 1 && yDiff == 0) ;
    }

    private Boolean inSameRowOrSameCol(List<Cell> cells) {
        long numRows = cells.stream().map(cell -> cell.getCoordinate().row).distinct().count();
        long numCols = cells.stream().map(cell -> cell.getCoordinate().col).distinct().count();
        return numRows == 1 || numCols == 1;
    }

    private void changeTurn() {
        setTurn(getTurn() == Piece.Type.MUSKETEER ? Piece.Type.GUARD : Piece.Type.MUSKETEER);
    }
    @Override
    public Iterator<Cell> iterator() {
        return new BoardIterator(board, size);
    }
}
