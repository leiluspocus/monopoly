package behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import agent.AgentJoueur;

/**
 * Behaviour d�clench� apr�s tirage de d�s
 * N'acheter aucun terrain
 */
public class StupideBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

	public StupideBehaviour(AgentJoueur agentJoueur) {
		super(agentJoueur);
	}

	@Override
	public void action() {
		
		ACLMessage msgReceived = myAgent.receive();
		if (msgReceived != null)
		{
			
		}
		else
		{
			block();
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
