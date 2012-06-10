package behaviour.player;

import jade.core.Agent;
import view.CaseAchetable;
import agent.AgentJoueur;

/**
 * Comportement tendant à économiser au maximum avant d'acheter un terrain
 */

public class PicsouBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;

	public PicsouBehaviour(Agent myAgent) {
		super(myAgent);
		((AgentJoueur)myAgent).setProbaDemandeLoyer(100);
	}

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		// TODO Auto-generated method stub
		
	}
}
