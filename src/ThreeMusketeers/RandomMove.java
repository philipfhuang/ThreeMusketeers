package ThreeMusketeers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMove implements MoveBehaviour{
	
	Board board;
	
	public RandomMove(Board board) {
		this.board = board;
	}
	
	@Override
	public Move returnMove() { // TODO
		Random random = new Random();
        List<Move> moves = board.getPossibleMoves();
        return moves.get(random.nextInt(moves.size()));
    }
}
