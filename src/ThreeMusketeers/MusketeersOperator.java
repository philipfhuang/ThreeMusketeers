package ThreeMusketeers;

import java.util.ArrayList;

public class MusketeersOperator {
	
	private ArrayList<ThreeMusketeersCommand> queue;
	
	public MusketeersOperator() {
		queue = new ArrayList<>();
	}	
	
	public void acceptCommand(ThreeMusketeersCommand command) {
		this.queue.add(command);
	}

	public void operateAll() {
		
		for (ThreeMusketeersCommand command: this.queue) {
			command.execute();
		}
		queue.clear();
	}

}
