package ThreeMusketeers;

public class CellUndoMove implements ThreeMusketeersCommand {
	
	private Board board;
	private Move move;
	
	public CellUndoMove(Board board, Move move) {
		this.board = board;
		this.move = move;
	}
	
	@Override
	public void execute() {
		this.board.undoMove(move);
	}
}
