package behaviour.player;

import agent.AgentJoueur;
import util.Logger;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Comportement visant � bloquer les autres agents en visant les cases 
 * de couleur ayant d�j� �t� achet�s par des agents
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
