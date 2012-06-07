package behaviour.player;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;

/**
 * Behaviour déclenché après tirage de dés
 * N'acheter aucun terrain
 */
public class StupideBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	
	
	public StupideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() {
		
		ACLMessage msgReceived = myAgent.blockingReceive();

		if (msgReceived != null)
		{
			switch (msgReceived.getPerformative()) 
			{
				/*
				 * Indique au joueur sur quelle case il se trouve après le déplacement effectué (dû au jeté de dés)
				 */
				case ACLMessage.INFORM_REF:
					//TODO: acheter la propriété si celle-ci est disponible
					break;
	
				default: 
					Logger.info("Message non géré : "+msgReceived.getContent());
					break;
			}
		}
	}


}
