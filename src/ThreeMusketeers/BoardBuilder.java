package ThreeMusketeers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class BoardBuilder {
	private String file;
	private boolean randomMode = false;
	private boolean fileMode = true;

	public BoardBuilder(String file) {
		this.file = file;
	}

	
	/**
     * Set the mode to the random mode
     */	
	public void setRandomMode() {
		this.fileMode = false;
		this.randomMode = true;
	}

	
	/**
     * Set the mode to the File mode
     */	
	public void setFileMode() {
		this.fileMode = true;
		this.randomMode = false;
	}

	/**
     * Get the Board instance
     * @return the board instance
     */	
	public Board getBoard() {
		Board b = new Board(this.file);
		b.setFileMode(fileMode);
		b.setRandomMode(randomMode);
		setUpBoard(b);
		return b;
	};

	/**
     * Loads a board file from a file path.
     * @param the board
     */

	private void setUpBoard(Board board) {
		if (this.fileMode) {
			File file = new File("Boards/" + this.file);
			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				System.err.printf("File at %s not found.", file);
				System.exit(1);
			}

			board.setTurn(Piece.Type.valueOf(scanner.nextLine().toUpperCase()));

			int row = 0, col = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] pieces = line.trim().split(" ");
				for (String piece : pieces) {
					Cell cell = new Cell(new Coordinate(row, col));
					board.add(cell);
	                board.notifyObservers(cell, CreateCell.create(piece));
					board.board[row][col] = cell;
					col += 1;
				}
				col = 0;
				row += 1;
			}
			scanner.close();
			System.out.printf("Loaded board from %s.\n", file);
		} else if (this.randomMode) {
			int turnInt = (int) (Math.random() * 2);
			if (turnInt < 1) {
				board.setTurn(Piece.Type.GUARD);
			} else {
				board.setTurn(Piece.Type.MUSKETEER);
			}
			
			final int maxM = 3;
			int countM = 0;
			for (int row = 0; row < board.board.length; row++) {
				for (int col = 0; col < board.board[row].length; col++) {
					int typeInt = (int) (Math.random() * 15);
					Cell cell = new Cell(new Coordinate(row, col));
					board.add(cell);
					if (typeInt < 5) {
						board.notifyObservers(cell, null);
					} else if (typeInt < 10) {
						board.notifyObservers(cell, CreateCell.create("O"));
					} else if (countM < maxM) {
						board.notifyObservers(cell, CreateCell.create("X"));
						countM++;
					}
					board.board[row][col] = cell;
				}
			}
			
			if (board.isGameOver()) {
				setUpBoard(board);
			} else {
				System.out.println("Generate a board successfully");
			}	
		}
	}

}
