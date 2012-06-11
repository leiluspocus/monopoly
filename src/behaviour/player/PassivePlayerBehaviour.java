package behaviour.player;

import util.Logger;
import agent.AgentJoueur;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class PassivePlayerBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;

	public PassivePlayerBehaviour(Agent a){
		super(a);
	}
	
	@Override
	public void action(){
		ACLMessage msgReceived = myAgent.blockingReceive();
		switch(msgReceived.getPerformative()){
			/*
			 * Un joueur est sur une des propriétés de myAgent
			 */
			case ACLMessage.INFORM:
				((AgentJoueur)myAgent).demanderLoyer(msgReceived);
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
				Logger.info("Mouvement d'argent : " + myAgent.getLocalName() + " -> +" + sommeRecue);
			break;
			
			/*
			 * Message reçu de l'ordonnanceur, "reveil toi, c'est à toi de jouer"
			 */
			case ACLMessage.PROPAGATE:
				((AgentJoueur)myAgent).setPropagateMessage(msgReceived);
			break;

			//Fin de partie, liberation des regles sur l'achat des maisons.
			case ACLMessage.UNKNOWN:
				System.out.println(myAgent.getLocalName() + " : abolition des regles bien compris");
				((AgentJoueur)myAgent).setRegles(false);
			break;
			
			case ACLMessage.CFP: // Le monopoly demande au joueur son nouveau capital
				int valCapital = ((AgentJoueur) myAgent).getCapitalJoueur();
				ACLMessage reply = msgReceived.createReply();
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				reply.setContent(valCapital + "");
				myAgent.send(reply);
				break;
				
			default: System.err.println("PassivePlayerBehaviour de " + myAgent.getLocalName() + ": message inconnu de " + msgReceived.getSender().getLocalName() + ":" + msgReceived.getPerformative()); 
				break;
		}
	}

	@Override
	public boolean done() {
		return ((AgentJoueur)myAgent).aRecuPropagate();
	}
}
