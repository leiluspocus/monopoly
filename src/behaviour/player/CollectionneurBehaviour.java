package behaviour.player;

import agent.AgentJoueur;
import util.Logger;
import view.Case;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * Comportement visant à collectionner les terrains d'une même couleur
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
				 * Indique au joueur sur quelle case il se trouve après le déplacement effectué (dû au jeté de dés)
				 */
			case ACLMessage.INFORM_REF:
				try {
					((AgentJoueur)myAgent).setCaseCourante((Case) msgReceived.getContentObject());
				} catch (UnreadableException e) {e.printStackTrace();}
				
				System.out.println(((AgentJoueur)myAgent).getCaseCourante());
				
				break;
	
				default: 
					Logger.err("Message non géré par le behaviour Collectionneur : "+msgReceived);
					break;
			}
		}
	}

}
