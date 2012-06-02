package behaviour;

import jade.core.behaviours.Behaviour;
import agent.AgentJoueur;

/**
 * Comportement tendant à économiser au maximum avant d'acheter un terrain
 */

public class PicsouBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

	public PicsouBehaviour(AgentJoueur agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() {
		
	}

	@Override
	public boolean done() {
		return false;
	}

}
