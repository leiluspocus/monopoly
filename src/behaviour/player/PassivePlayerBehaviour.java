package behaviour.player;

import util.Logger;
import agent.AgentJoueur;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class PassivePlayerBehaviour extends Behaviour{

	private static final long serialVersionUID = 1L;
	private boolean waitingForMoney;

	public PassivePlayerBehaviour(Agent a){
		super(a);
		waitingForMoney = false;
	}
	
	@Override
	public void action(){
		ACLMessage msgReceived = myAgent.receive();
		if (msgReceived != null)
		{
			switch(msgReceived.getPerformative()){
				/*
				 * Un joueur est sur une des propri�t�s de myAgent
				 */
				case ACLMessage.INFORM:
					waitingForMoney = ((AgentJoueur)myAgent).demanderLoyer(msgReceived);
				break;
				/*
				 * Demande de paiement: � destination d'un joueur (loyer/terrain/case sp�ciale), ou de la banque (taxes)
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
					
				default: Logger.err("Message inconnu from " + msgReceived.getSender().getName()); 
					break;
			}
		}
		else
		{
			block();
		}
	}

	@Override
	public boolean done() 
	{
		return !waitingForMoney;
	}

}
