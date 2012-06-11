package behaviour.player;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import util.Logger;
import view.Case;
import view.CaseAchetable;
import agent.AgentJoueur;

public abstract class ActivePlayerBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;
	private ACLMessage msgReceived = null;

	protected abstract void decideAchatTerrain(CaseAchetable caseCourante);
	protected abstract void decideAchatMaison();
	
	public ActivePlayerBehaviour(Agent a) {
		super(a);
	}
	
	@Override
	public void action(){
		msgReceived = myAgent.blockingReceive();
		if (msgReceived != null){
			switch (msgReceived.getPerformative()){
				/*
				 * Indique au joueur sur quelle case il se trouve après le déplacement effectué (dû au jeté de dés)
				 */
				case ACLMessage.INFORM_REF:
					
					try {
						CaseAchetable caseCour = null;
						((AgentJoueur)myAgent).setCaseCourante((Case) msgReceived.getContentObject());
						if (((AgentJoueur)myAgent).getCaseCourante() instanceof CaseAchetable){
							caseCour = (CaseAchetable) ((AgentJoueur)myAgent).getCaseCourante(); 
							if (caseCour.getProprietaireCase() == null){
								decideAchatTerrain(caseCour);
							}
						}
						decideAchatMaison();
						
					}catch (UnreadableException e){e.printStackTrace();}
					
				break;
				
				//Recevoir de l'argent
				case ACLMessage.AGREE:
					int sommeRecue = Integer.parseInt(msgReceived.getContent().trim());
					((AgentJoueur)myAgent).setCapitalJoueur(((AgentJoueur)myAgent).getCapitalJoueur()+sommeRecue);
					Logger.info("Mouvement d'argent : " + myAgent.getLocalName() + " -> +" + sommeRecue);
				break;
				
				//Payer une dette
				case ACLMessage.REQUEST:
					((AgentJoueur)myAgent).payerMontantDu(msgReceived);
				break;
				
				default: 
					System.err.println("Message non géré par l'ActiveBehaviour de " + myAgent.getLocalName() + " de " + msgReceived.getSender().getLocalName() + ":" + msgReceived.getPerformative());
				break;
			}
		}	
	}
	
	@Override
	public boolean done() {
		return msgReceived.getPerformative() == ACLMessage.INFORM_REF;
	}
}
