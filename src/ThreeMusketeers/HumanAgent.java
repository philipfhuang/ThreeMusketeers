package ThreeMusketeers;

import java.util.Scanner;

public class HumanAgent extends Agent { 

    public HumanAgent(Board board) {
        super();
        this.moveBehaviour = new HumanMove(board);
    }

    /**
     * Asks the human for a move with from and to coordinates and makes sure its valid.
     * Create a Move object with the chosen fromCell and toCell
     * @return the valid human inputted Move
     */
    
    
}
