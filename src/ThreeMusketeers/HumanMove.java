package ThreeMusketeers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HumanMove implements MoveBehaviour{
	
	 Board board;
	 
	 public HumanMove( Board board) {
		 this.board = board;
	 }

	@Override
	public Move returnMove() { // TODO
    	Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        List<Move> list = board.getPossibleMoves();
        System.out.println("Currently available move:");
        for (int i = 0; i < list.size(); i ++) System.out.println((i + 1) + ": " + list.get(i));
        int input = -1;
		while (!(0 <= input && input < list.size())) {
			System.out.println("Choose the set you want to move from 1 to " + list.size());
			String str = scanner.nextLine();
			try{
				input = Integer.parseInt(str) - 1;
	        }
	        catch (NumberFormatException ex){
	        	System.out.println("Invalid input");
	            input = -1;
	        }
		}
        try{
            Move move = list.get(input);
            System.out.printf("Moving %s \n", move);
            return move;
        }
        catch (Exception e){
            System.out.println("Invalid enter.");
            return returnMove();
        }
    }

}
