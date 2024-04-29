package ThreeMusketeers;

import java.util.List;
import java.lang.Math;

public class BoardEvaluatorImpl implements BoardEvaluator {

    /**
     * Calculates a score for the given Board
     * A higher score means the Musketeer is winning
     * A lower score means the Guard is winning
     * Add heuristics to generate a score given a Board
     * @param board Board to calculate the score of
     * @return int Score of the given Board
     */
    @Override
    public int evaluateBoard(Board board) {
    	int score = board.getGuardCells().size() * 3;
        List<Cell> musketeers = board.getMusketeerCells();

        for (Cell musketeer : musketeers) {
            score -= board.getPossibleDestinations(musketeer).size() * 2;
        }

        for (int i = 0; i < musketeers.size() - 1; i++) {
            Cell prev = musketeers.get(i);
            for (int j = 1; j < musketeers.size(); j++) {
                Cell next = musketeers.get(j);
                if (prev.getCoordinate().row == next.getCoordinate().row || prev.getCoordinate().col == next.getCoordinate().col) {
                    score -= 5;
                }
            }
        }

        return score;
    }
}
