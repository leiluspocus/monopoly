package behaviour.player;

import agent.AgentJoueur;
import util.Logger;
import view.Case;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * Comportement tendant � �conomiser au maximum avant d'acheter un terrain
 */

public class PicsouBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	public PicsouBehaviour(Agent myAgent) {
		super(myAgent);
		((AgentJoueur)myAgent).setProbaDemandeLoyer(100);
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
				try {
					((AgentJoueur)myAgent).setCaseCourante((Case) msgReceived.getContentObject());
				} catch (UnreadableException e) {e.printStackTrace();}
				
				System.out.println(((AgentJoueur)myAgent).getCaseCourante());
				
				break;
	
				default: 
					Logger.err("Message non g�r� par le behaviour Picsou : " + msgReceived);
					break;
			}
		}
	}

}
