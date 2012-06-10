package behaviour.player;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import util.Logger;
import view.Case;
import view.CaseAchetable;
import agent.AgentJoueur;

public abstract class ActivePlayerBehaviour extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;

	protected abstract void decideAchatTerrain(CaseAchetable caseCourante);
	
	public ActivePlayerBehaviour(Agent a) {
		super(a);
	}
	
	@Override
	public void action(){
		ACLMessage msgReceived = myAgent.blockingReceive();
		if (msgReceived != null){
			switch (msgReceived.getPerformative()){
				/*
				 * Indique au joueur sur quelle case il se trouve apr�s le d�placement effectu� (d� au jet� de d�s)
				 */
				case ACLMessage.INFORM_REF:
					try {
						((AgentJoueur)myAgent).setCaseCourante((Case) msgReceived.getContentObject());
						if (((AgentJoueur)myAgent).getCaseCourante() instanceof CaseAchetable){
							CaseAchetable caseCour = (CaseAchetable) ((AgentJoueur)myAgent).getCaseCourante(); 
							if (caseCour.getProprietaireCase() == null){
								decideAchatTerrain(caseCour);
							}
						}
					}
					catch (UnreadableException e){e.printStackTrace();}
					
					//System.out.println(((AgentJoueur)myAgent).getCaseCourante());
					
				break;
				default: 
					Logger.err("Message non g�r� par " + myAgent.getLocalName() + msgReceived.getSender().getLocalName() + ":" + msgReceived.getPerformative());
				break;
			}
		}	
	}
}
