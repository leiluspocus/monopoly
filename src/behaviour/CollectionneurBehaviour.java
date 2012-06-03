package behaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Comportement visant � collectionner les terrains d'une m�me couleur
 */
public class CollectionneurBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	public CollectionneurBehaviour(Agent agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() { 
	} 

}
