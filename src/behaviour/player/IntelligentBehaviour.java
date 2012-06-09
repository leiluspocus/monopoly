package behaviour.player;

import jade.core.Agent;
import view.Case;
import agent.AgentJoueur;

/**
 * Comportement visant � acheter des terrains en fonction des prix que lui apportent les terrains
 */
public class IntelligentBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;
	

	public IntelligentBehaviour(Agent myAgent) {
		super(myAgent);
		((AgentJoueur)myAgent).setProbaDemandeLoyer(90);
	}


	@Override
	protected void decideAchatTerrain(Case caseCourante) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
