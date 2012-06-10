package behaviour.player;

import jade.core.Agent;
import view.CaseAchetable;
import agent.AgentJoueur;

/**
 * Comportement visant � bloquer les autres agents en visant les cases 
 * de couleur ayant d�j� �t� achet�s par des agents
 */
public class EvilBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;

	public EvilBehaviour(Agent agentJoueur) {
		super(agentJoueur);
		((AgentJoueur)agentJoueur).setProbaDemandeLoyer(100);
	}

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		// TODO Auto-generated method stub
		
	}
}
