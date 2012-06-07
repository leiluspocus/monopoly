package behaviour.player;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;
import agent.AgentJoueur;


/**
 * Comportement visant à acheter un terrain dès qu'il est disponible
 */
public class AvideBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	public AvideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
		((AgentJoueur)agentJoueur).setProbaDemandeLoyer(95);
	}

	@Override
	public void action() 
	{
		Logger.info("Action d'avide");
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
