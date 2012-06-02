package behaviour;

import util.Logger;
import jade.core.behaviours.Behaviour;

/**
 * Comportement visant à acheter un terrain dès qu'il est disponible
 */
public class AvideBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {

		Logger.info("Action d'avide");
	}

	@Override
	public boolean done() {
		return false;
	}

}
