package ThreeMusketeers;

public class FileBasedBoardBuilder extends BoardBuilder{

	public FileBasedBoardBuilder(String file) {
		super(file);
		super.setFileMode();
	}
	
}
