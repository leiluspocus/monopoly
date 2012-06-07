package behaviour.player;

import agent.AgentJoueur;
import util.Logger;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Comportement visant à bloquer les autres agents en visant les cases 
 * de couleur ayant déjà été achetés par des agents
 */
public class EvilBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	public EvilBehaviour(Agent agentJoueur) {
		super(agentJoueur);
		((AgentJoueur)agentJoueur).setProbaDemandeLoyer(100);
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
