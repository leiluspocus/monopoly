package behaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Behaviour d�clench� apr�s tirage de d�s
 * N'acheter aucun terrain
 */
public class StupideBehaviour extends OneShotBehaviour {
	
	private static final long serialVersionUID = 1L;

	public StupideBehaviour(Agent agentJoueur) {
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


}
