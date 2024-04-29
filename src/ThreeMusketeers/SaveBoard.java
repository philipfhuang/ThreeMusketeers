package ThreeMusketeers;

public class SaveBoard implements ThreeMusketeersCommand {
	private Board board;
	
	public SaveBoard(Board board) {
		this.board = board;
	}

	@Override
	public void execute() {
		this.board.saveBoard();
	}
	
}
