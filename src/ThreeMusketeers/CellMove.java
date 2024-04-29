package ThreeMusketeers;

public class CellMove implements ThreeMusketeersCommand {

	private Board board;
	private Move move;
	
	public CellMove(Board board, Move move) {
		this.board = board;
		this.move = move;
	}
	
	@Override
	public void execute() {
		this.board.move(this.move);
	}

}
