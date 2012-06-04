package behaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;

/**
 * Comportement visant � acheter un terrain d�s qu'il est disponible
 */
public class AvideBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	public AvideBehaviour(Agent agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() 
	{
		Logger.info("Action d'avide");
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
