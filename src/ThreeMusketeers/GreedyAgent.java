package ThreeMusketeers;

import java.util.List;

public class GreedyAgent extends Agent {
	   

    public GreedyAgent(Board board) {
        super();
        this.moveBehaviour = new GreedyMove(board);
    }

    public GreedyAgent(Board board, BoardEvaluatorImpl boardEvaluator, int depth) {
        super();
        this.moveBehaviour = new GreedyMove(board, boardEvaluator, depth);
    }

    /**
     * Gets a valid move that the GreedyAgent can do.
     * Uses MiniMax strategy with Alpha Beta pruning
     * @return a valid Move that the GreedyAgent can perform on the Board
     */
    
}
