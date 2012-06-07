package behaviour.player;

import agent.AgentJoueur;
import util.Logger;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Comportement visant � acheter des terrains en fonction des prix que lui apportent les terrains
 */
public class IntelligentBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;
	

	public IntelligentBehaviour(Agent myAgent) {
		super(myAgent);
		((AgentJoueur)myAgent).setProbaDemandeLoyer(90);
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
