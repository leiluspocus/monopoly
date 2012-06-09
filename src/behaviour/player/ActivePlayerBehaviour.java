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

	protected abstract void decideAchatTerrain(Case caseCourante);
	
	public ActivePlayerBehaviour(Agent a) 
	{
		super(a);
	}
	
	@Override
	public void action() 
	{
		ACLMessage msgReceived = myAgent.blockingReceive();

		if (msgReceived != null){
			switch (msgReceived.getPerformative()){
				/*
				 * Indique au joueur sur quelle case il se trouve après le déplacement effectué (dû au jeté de dés)
				 */
				case ACLMessage.INFORM_REF:
				{
					try 
					{
						((AgentJoueur)myAgent).setCaseCourante((Case) msgReceived.getContentObject());
						// La case n'appartient à personne
						if ( ((AgentJoueur)myAgent).getCaseCourante().getProprietaireCase() == null )
						{
							// .. et la case est achetable
							if ( ((AgentJoueur)myAgent).getCaseCourante() instanceof CaseAchetable )
							{
								decideAchatTerrain(((AgentJoueur)myAgent).getCaseCourante());
							}
						}
					}
					catch (UnreadableException e)
					{
						e.printStackTrace();
					}
					
					System.out.println(((AgentJoueur)myAgent).getCaseCourante());
					
					break;
				}
				default: 
					Logger.err("Message non géré par le behaviour Avide from "+ msgReceived.getSender().getName() );
					break;
			}
		}	
	}

}
