package behaviour;

import jade.core.behaviours.Behaviour;
import agent.AgentJoueur;

/**
 * Comportement visant à collectionner les terrains d'une même couleur
 */
public class CollectionneurBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

	public CollectionneurBehaviour(AgentJoueur agentJoueur) {
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
