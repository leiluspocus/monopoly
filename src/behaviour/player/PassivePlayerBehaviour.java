package behaviour.player;

import util.Logger;
import agent.AgentJoueur;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class PassivePlayerBehaviour extends Behaviour{

	private static final long serialVersionUID = 1L;

	public PassivePlayerBehaviour(Agent a) 
	{
		super(a);
	}
	
	@Override
	public void action() 
	{
		ACLMessage msgReceived = myAgent.receive();
		
		switch(msgReceived.getPerformative())
		{
			/*
			 * Un joueur est sur une des propriétés de myAgent
			 */
			case ACLMessage.INFORM:
				((AgentJoueur)myAgent).demanderLoyer(((AgentJoueur)myAgent).getProbaDemandeLoyer(), msgReceived);
				break;
			/*
			 * Demande de paiement: à destination d'un joueur (loyer/terrain/case spéciale), ou de la banque (taxes)
			 * Envoi du paiement au destinataire
			 */
			case ACLMessage.REQUEST:
				((AgentJoueur)myAgent).payerMontantDu(msgReceived);
				break;
			/*
			 * Message receptionnant l'argent d'un joueur ou de la banque
			 */
			case ACLMessage.AGREE:
				int sommeRecue = Integer.parseInt(msgReceived.getContent().trim());
				((AgentJoueur)myAgent).setCapitalJoueur(((AgentJoueur)myAgent).getCapitalJoueur()+sommeRecue);
				break;
				
			default: Logger.err("Message inconnu : "+msgReceived); 
				break;
		}
	}

	@Override
	public boolean done() 
	{
		return false;
	}

}
