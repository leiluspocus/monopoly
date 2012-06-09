package behaviour.player;

import jade.core.Agent;
import view.Case;
import agent.AgentJoueur;


/**
 * Comportement visant � acheter un terrain d�s qu'il est disponible
 */
public class AvideBehaviour extends ActivePlayerBehaviour {

	private static final long serialVersionUID = 1L;

	public AvideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
		((AgentJoueur)agentJoueur).setProbaDemandeLoyer(95);
	}

	@Override
	protected void decideAchatTerrain(Case caseCourante) 
	{
		
	}
}
