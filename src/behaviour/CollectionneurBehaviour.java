package behaviour;

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
	}

	@Override
	public void action() { 
		
ACLMessage msgReceived = myAgent.receive();
		
		if (msgReceived != null)
		{
			switch (msgReceived.getPerformative()) 
			{
				/*
				 * Demande de paiement: � destination d'un joueur (loyer/terrain/case sp�ciale), ou de la banque (taxes)
				 */
				case ACLMessage.REQUEST:
					
					break;
				/*
				 * Un joueur est sur une des propri�t�s de myAgent
				 */
				case ACLMessage.INFORM:
					
					break;
				/*
				 * Indique au joueur sur quelle case il se trouve apr�s le d�placement effectu� (d� au jet� de d�s)
				 */
				case ACLMessage.INFORM_REF:
					
					break;
				/*
				 * Message receptionnant l'argent du joueur creditaire
				 */
				case ACLMessage.AGREE:
					
					break;
					
				default: Logger.info("Message non g�r� : "+msgReceived.getContent());
					break;
			}
		}
		else
		{
			block();
		}
	} 

}
