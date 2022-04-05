package cryptoTrader.gui;

import java.util.List;
import java.util.ArrayList;

/**
 * Abstract representation of a subject with observers. 
 */
public abstract class Subject {
	
	private List<Observer> observers = new ArrayList<>();

	/**
	 * Attaches the new observer given.
	 * @param observer
	 */
	public void attach(Observer observer) {
		observers.add(observer);
	}

	/**
	 * Detaches the given observer.
	 * @param observer
	 */
	public void detach(Observer observer) {
		observers.remove(observer);
	}

	/**
	 * Notifies all the observers that the subject was changed.
	 */
	public void notifyObservers() {
		for (Observer observer : observers) observer.update(this);
	}

}
