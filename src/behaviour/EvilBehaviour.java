package behaviour;

import jade.core.behaviours.Behaviour;
import agent.AgentJoueur;

/**
 * Comportement visant � bloquer les autres agents en visant les cases 
 * de couleur ayant d�j� �t� achet�s par des agents
 */
public class EvilBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

	public EvilBehaviour(AgentJoueur agentJoueur) {
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
