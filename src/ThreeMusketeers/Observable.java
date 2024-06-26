package ThreeMusketeers;

import java.util.ArrayList;
import java.util.List;


public class Observable {
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	public void add(Observer o) {
		observers.add(o);
	}
	
	public void notifyObservers(Cell cell, Piece piece) {
		for (Observer o : observers) {
			if (((Cell) o).getCoordinate().equals(cell.getCoordinate())) ((Cell) o).update(piece);
		}
	}

}
