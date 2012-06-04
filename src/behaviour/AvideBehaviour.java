package behaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;

/**
 * Comportement visant à acheter un terrain dès qu'il est disponible
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
				 * Demande de paiement: à destination d'un joueur (loyer/terrain/case spéciale), ou de la banque (taxes)
				 */
				case ACLMessage.REQUEST:
					
					break;
				/*
				 * Un joueur est sur une des propriétés de myAgent
				 */
				case ACLMessage.INFORM:
					
					break;
				/*
				 * Indique au joueur sur quelle case il se trouve après le déplacement effectué (dû au jeté de dés)
				 */
				case ACLMessage.INFORM_REF:
					
					break;
				/*
				 * Message receptionnant l'argent du joueur creditaire
				 */
				case ACLMessage.AGREE:
					
					break;
					
				default: Logger.info("Message non géré : "+msgReceived.getContent());
					break;
			}
		}
		else
		{
			block();
		}
	}
 
}
