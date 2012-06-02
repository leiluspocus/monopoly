package behaviour;

import jade.core.behaviours.CyclicBehaviour;
import util.Logger;
import agent.AgentJoueur;

/**
 * Comportement visant à acheter un terrain dès qu'il est disponible
 */
public class AvideBehaviour extends CyclicBehaviour {
	
	private static final long serialVersionUID = 1L;

	public AvideBehaviour(AgentJoueur agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() {
		Logger.info("Action d'avide");
	}
 
}
