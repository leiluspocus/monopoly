package behaviour;

import jade.core.behaviours.Behaviour;
import agent.AgentJoueur;

/**
 * Comportement visant � acheter des terrains en fonction des prix que lui apportent les terrains
 */
public class IntelligentBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

	public IntelligentBehaviour(AgentJoueur agentJoueur) {
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
