package behaviour.player;

import jade.core.Agent;
import view.CaseAchetable;
import agent.AgentJoueur;

/**
 * Behaviour d�clench� apr�s tirage de d�s
 * N'acheter aucun terrain
 */
public class StupideBehaviour extends ActivePlayerBehaviour {
	
	private static final long serialVersionUID = 1L;

	public StupideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
		((AgentJoueur)myAgent).setProbaDemandeLoyer(45);
	}

	@Override
	protected void decideAchatTerrain(CaseAchetable caseCourante) {
		System.out.println(((AgentJoueur)myAgent).getLocalName() + " est stupide : il n'ach�te aucun terrain ni maison");
	}

	@Override
	protected void decideAchatMaison() {
		
	}
}
