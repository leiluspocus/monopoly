package behaviour.player;

import jade.core.Agent;
import view.Case;
import agent.AgentJoueur;

/**
 * Comportement visant à collectionner les terrains d'une même couleur
 */
public class CollectionneurBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	public CollectionneurBehaviour(Agent agentJoueur) {
		super(agentJoueur);
		((AgentJoueur)agentJoueur).setProbaDemandeLoyer(95);
	}

	@Override
	protected void decideAchatTerrain(Case caseCourante) 
	{
			
	}

}
