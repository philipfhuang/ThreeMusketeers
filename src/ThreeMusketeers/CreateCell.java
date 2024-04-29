package ThreeMusketeers;

public class CreateCell {
	public static Piece create(String type) {
    	switch (type) {
        	case "O":
        		return new Guard();
        	case "X":
        		return new Musketeer();
        	default:
        		return null;
    	}
    }
}
