package behaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Comportement visant � bloquer les autres agents en visant les cases 
 * de couleur ayant d�j� �t� achet�s par des agents
 */
public class EvilBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	public EvilBehaviour(Agent agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() {
		
	} 
}
