package ThreeMusketeers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ThreeMusketeers {

    private Board board;
    private Agent musketeerAgent, guardAgent;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Move> moves = new ArrayList<>();
    private int steps = 0;
    private List<Long> musketeerTimerList = new ArrayList<>();
    private List<Long> guardTimerList = new ArrayList<>();

    // Record all file paths
    private final List<String> files = new ArrayList<>();
    
    private final MusketeersOperator commands = new MusketeersOperator();

    // All possible game modes
    public enum GameMode {
        Human("Human vs Human"),
        HumanRandom("Human vs Computer (Random)"),
        HumanGreedy("Human vs Computer (Greedy)");

        private final String gameMode;
        GameMode(final String gameMode) {
            this.gameMode = gameMode;
        }
    }

    /**
     * Play game with human input mode selector
     */
    public void play(){
        System.out.println("Welcome! \n");
        selectBoard();
        final GameMode mode = getModeInput();
        System.out.println("Playing " + mode.gameMode);
        play(mode);
    }

    /**
     * Play game without human input mode selector
     * @param mode the GameMode to run
     */
    public void play(GameMode mode){
        selectMode(mode);
        runGame();
    }

    /**
     * Mode selector sets the correct agents based on the given GameMode
     * @param mode the selected GameMode
     */
    private void selectMode(GameMode mode) {
        switch (mode) {
            case Human -> {
                musketeerAgent = new HumanAgent(board);
                guardAgent = new HumanAgent(board);
            }
            case HumanRandom -> {
                String side = getSideInput();
                
                // The following statement may look weird, but it's what is known as a ternary statement.
                // Essentially, it sets musketeerAgent equal to a new HumanAgent if the value M is entered,
                // Otherwise, it sets musketeerAgent equal to a new RandomAgent
                musketeerAgent = side.equals("M") ? new HumanAgent(board) : new RandomAgent(board);
                
                guardAgent = side.equals("G") ? new HumanAgent(board) : new RandomAgent(board);
            }
            case HumanGreedy -> {
                String side = getSideInput();
                musketeerAgent = side.equals("M") ? new HumanAgent(board) : new GreedyAgent(board);
                guardAgent = side.equals("G") ? new HumanAgent(board) : new GreedyAgent(board);
            }
        }
    }

    /**
     * Runs the game, handling human input for move actions
     * Handles moves for different agents based on current turn 
     */
    private void runGame() {
        while(!board.isGameOver()) {
            System.out.println("\n" + board);

            final Agent currentAgent;
            final List<Long> timerList;
            if (board.getTurn() == Piece.Type.MUSKETEER) {
                timerList = musketeerTimerList;
                currentAgent = musketeerAgent;
            }
            else {
                timerList = guardTimerList;
                currentAgent = guardAgent;
            }

            if (currentAgent instanceof HumanAgent) // Human move
                switch (getInputOption()) {
                    case "M":
                        long startTime = System.currentTimeMillis();
                        move(currentAgent);

                        if (musketeerAgent instanceof GreedyAgent && guardAgent instanceof GreedyAgent) {
                            steps += 1;
                        }
                        long endTime = System.currentTimeMillis();
                        long usedTime = endTime - startTime;
                        timerList.add(usedTime);
                        long sum = 0;
                        for (long time : timerList) {
                            sum += time;
                        }
                        long average = sum / timerList.size();
                        System.out.printf("You took %f second for this move. Your average move is %f second %n", (double)usedTime/1000, (double)average/1000);
                        break;
                    case "U":
                        if (moves.size() == 0) {
                            System.out.println("No moves to undo.");
                            continue;
                        }
                        else if (moves.size() == 1 || isHumansPlaying()) {
                            undoMove();
                        }
                        else {
                            undoMove();
                            undoMove();
                        }
                        break;
                    case "S":
                    	commands.acceptCommand(new SaveBoard(this.board));
                        commands.operateAll();
                        break;
                }
            else { // Computer move
                System.out.printf("[%s] Calculating move...\n", currentAgent.getClass().getSimpleName());
                move(currentAgent);
            }
        }

        System.out.println(board);
        System.out.printf("\n%s won!%n", board.getWinner().getType());
        if (musketeerAgent instanceof GreedyAgent || guardAgent instanceof GreedyAgent) {
            getRank();
        }
    }

    private void getRank() {
        System.out.println("Do you want to record your steps for this game? y for yes, n for no");
        String decision = scanner.next().toUpperCase();
        if (decision.equals("N")) {
            return;
        }
        System.out.println("Pleas enter your name!");
        String name = scanner.next();
        List<Tuple> ranks = new ArrayList<>();
        File storedRank = new File("rank/.rank");
        try {
            Scanner reader = new Scanner(storedRank);
            while (reader.hasNextLine()) {
                String[] info = reader.nextLine().split(",");
                ranks.add(new Tuple(info[0], Integer.parseInt(info[2])));
            }
        } catch (FileNotFoundException ignored) {
        }
        ranks.add(new Tuple(name, steps));
        Collections.sort(ranks);
        try {
            FileWriter writeRank = new FileWriter("rank/.rank");
            for (Tuple rank : ranks) {
                writeRank.write(rank.name + "," + rank.steps);
            }
            writeRank.close();
        }catch (Exception ignored) {

        }
        for (int i = 0; i < ranks.size(); i++) {
            System.out.printf("%d %s %d%n", i + 1, ranks.get(i).name, ranks.get(i).steps);
        }
    }

    /**
     * Gets a move from the given agent, adds a copy of the move using the copy constructor to the moves stack, and
     * does the move on the board.
     * @param agent Agent to get the move from.
     */
    protected void move(final Agent agent) { // TODO
        Move move = agent.getMove();
        this.moves.add(new Move(move));
        commands.acceptCommand(new CellMove(this.board, move));
        commands.operateAll();
    }

    /**
     * Removes a move from the top of the moves stack and undoes the move on the board.
     */
    private void undoMove() { // TODO
    	Move undo = this.moves.remove(moves.size() - 1);
        commands.acceptCommand(new CellUndoMove(this.board, undo));
        commands.operateAll();
    }

    /**
     * Get human input for move action
     * @return the selected move action, 'M': move, 'U': undo, and 'S': save
     */
    private String getInputOption() {
        System.out.printf("[%s] Enter 'M' to move, 'U' to undo, and 'S' to save: ", board.getTurn().getType());
        if (scanner.hasNext("chat")) {
        	board.chaton();
        	System.out.println("chat on !!\nEnter 'M' to move, 'U' to undo, and 'S' to save:");
        }else {
        while (!scanner.hasNext("[MUSmus]")) {
            System.out.print("Invalid option. Enter 'M', 'U', or 'S': ");
            scanner.next();
        }}
        return scanner.next().toUpperCase();
    }

    /**
     * Returns whether both sides are human players
     * @return True if both sides are Human, False if one of the sides is a computer
     */
    private boolean isHumansPlaying() {
        return musketeerAgent instanceof HumanAgent && guardAgent instanceof HumanAgent;
    }

    /**
     * Get human input for side selection
     * @return the selected Human side for Human vs Computer games,  'M': Musketeer, G': Guard
     */
    private String getSideInput() {
        System.out.print("\nEnter 'M' to be a Musketeer or 'G' to be a Guard: ");
        while (!scanner.hasNext("[MGmg]")) {
            System.out.println("Invalid option. Enter 'M' or 'G': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
    }

    /**
     * Get human input for selecting the game mode
     * @return the chosen GameMode
     */
    private GameMode getModeInput() {
        System.out.println("""
        		
                    0: Human vs Human
                    1: Human vs Computer (Random)
                    2: Human vs Computer (Greedy)""");
        System.out.print("Choose a game mode to play i.e. enter a number: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid option. Enter 0, 1, or 2: ");
            scanner.next();
        }
        final int mode = scanner.nextInt();
        if (mode < 0 || mode > 2) {
            System.out.println("Invalid option.");
            return getModeInput();
        }
        return GameMode.values()[mode];
    }
    
    /**
     * Gets all the text files from the boards directory and puts them into the given listView
     * @param listView ListView to update
     * @return the index in the listView of Starter.txt
     */
    private int getFiles() { // TODO
    	Path dirName = Paths.get("boards");
		DirectoryStream<Path> dir = null;
		
    	try {
			dir = Files.newDirectoryStream(dirName);
			for (Path file : dir) {
				File f = file.toFile();
				files.add(f.getName());
		      }
		} catch (IOException e) {
			System.out.println("No such directory");
		}
    	
    	return files.indexOf("Starter.txt");
    }

    private int getBoardInput() { 
    	System.out.println("""
    			
                0: Starter Board
                1: Choose existing board
                2: Random Generated Board""");
    	System.out.print("Choose a board to play i.e. enter a number: ");
    	while (!scanner.hasNextInt()) {
    		System.out.print("Invalid option. Enter 0, 1, or 2: ");
    		scanner.next();
    	}
    	final int index = scanner.nextInt();
    	if (index < 0 || index > 2) {
    		System.out.println("Invalid option.");
    		return getBoardInput();
    	}
    	return index;
    }
    
    private void selectBoard() {
    	int boardType = getBoardInput();
    	int StartBoard = getFiles();
    	switch(boardType) {
    		case 0:
    			this.board = (new FileBasedBoardBuilder(this.files.get(StartBoard))).getBoard();
    			break;
    		case 1:
    			int index = 0;
    			
    			System.out.println();
    			while (files.size() != index) {
    				System.out.println(String.format("%d: %s", index, files.get(index)));
    				index++;
    			}
    			System.out.print("Choose a board to play i.e. enter a number: ");
    	    	while (!scanner.hasNextInt()) {
    	    		System.out.print("Invalid option. Enter 0, 1, or 2...: ");
    	    		scanner.next();
    	    	}
    	    	this.board = (new FileBasedBoardBuilder(files.get(scanner.nextInt()))).getBoard();
    			break;
    		case 2:
    			this.board = (new RandomBoardBuilder()).getBoard();
    			break;
    	}
    }

    public static void main(String[] args) {
        ThreeMusketeers game = new ThreeMusketeers();
        game.play();
    }
}