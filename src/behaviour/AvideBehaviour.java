package behaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import util.Logger;

/**
 * Comportement visant à acheter un terrain dès qu'il est disponible
 */
public class AvideBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	public AvideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() {
		Logger.info("Action d'avide");
	}
 
}
