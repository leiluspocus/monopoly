package behaviour;

import jade.core.behaviours.Behaviour;

/**
 * Comportement visant à bloquer les autres agents en visant les cases 
 * de couleur ayant déjà été achetés par des agents
 */
public class EvilBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		
	}

	@Override
	public boolean done() {
		return false;
	}

}
