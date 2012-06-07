package behaviour.player;

import agent.AgentJoueur;
import util.Logger;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Comportement visant � collectionner les terrains d'une m�me couleur
 */
public class CollectionneurBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;
	
	public CollectionneurBehaviour(Agent agentJoueur) {
		super(agentJoueur);
		((AgentJoueur)agentJoueur).setProbaDemandeLoyer(95);
	}

	@Override
	public void action() { 
		
		ACLMessage msgReceived = myAgent.blockingReceive();

		if (msgReceived != null)
		{
			switch (msgReceived.getPerformative()) 
			{
				/*
				 * Indique au joueur sur quelle case il se trouve apr�s le d�placement effectu� (d� au jet� de d�s)
				 */
				case ACLMessage.INFORM_REF:
					//TODO: acheter la propri�t� si celle-ci est disponible
					break;
	
				default: 
					Logger.info("Message non g�r� : "+msgReceived.getContent());
					break;
			}
		}
	}

}
