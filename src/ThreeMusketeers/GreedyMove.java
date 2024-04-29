package ThreeMusketeers;

import java.util.List;

public class GreedyMove implements MoveBehaviour{
	
	Board board, boardCopy;
    BoardEvaluatorImpl boardEvaluator;
    int depth = 10;
    
    public GreedyMove(Board board) {
        this.board = board;
        this.boardEvaluator = new BoardEvaluatorImpl();
    }

    public GreedyMove(Board board, BoardEvaluatorImpl boardEvaluator, int depth) {
        this.board = board;
        this.boardEvaluator = boardEvaluator;
        this.depth = depth;}
    
	@Override
	public Move returnMove() {
		boardCopy = new Board(board);
        int bestScore = boardCopy.getTurn().equals(Piece.Type.MUSKETEER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Move chosenMove = null;

        List<Move> possibleMoves = boardCopy.getPossibleMoves();
        System.out.println("Moves:" + possibleMoves);
        for (Move move: possibleMoves) {
            Move moveCopy = new Move(move);
            Piece.Type turn = boardCopy.getTurn();
            boardCopy.move(move);

            int score = this.minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            System.out.printf("Move: %s Score: %d\n", move, score);
            if (turn.equals(Piece.Type.MUSKETEER) && score > bestScore) {
                bestScore = score;
                chosenMove = new Move(moveCopy);
            }
            else if (turn.equals(Piece.Type.GUARD) && score < bestScore) {
                bestScore = score;
                chosenMove = new Move(moveCopy);
            }

            boardCopy.undoMove(moveCopy);
        }

        assert chosenMove != null;
        System.out.printf("[%s (Greedy Agent)] Moving piece %s to %s. Best Score: %d\n",
                board.getTurn().getType(), chosenMove.fromCell.getCoordinate(), chosenMove.toCell.getCoordinate(),
                bestScore);
        return new Move(
                board.getCell(chosenMove.fromCell.getCoordinate()),
                board.getCell(chosenMove.toCell.getCoordinate()));
    }
	
	
	private int minimax(int depth, int alpha, int beta) {
		if (depth == 0 || boardCopy.isGameOver()) {
            return boardEvaluator.evaluateBoard(boardCopy);
        }

        int bestScore = boardCopy.getTurn().equals(Piece.Type.MUSKETEER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> possibleMoves = boardCopy.getPossibleMoves();
        for (Move move: possibleMoves) {
            Move moveCopy = new Move(move);
            Piece.Type turn = boardCopy.getTurn();
            boardCopy.move(move);

            int score = this.minimax(depth - 1, alpha, beta);

            if (turn.equals(Piece.Type.MUSKETEER)) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            }
            else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }

            boardCopy.undoMove(moveCopy);

            if (beta <= alpha) {
                break;
            }
        }

        return boardEvaluator.evaluateBoard(boardCopy);


	}
}
