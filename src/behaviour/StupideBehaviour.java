package behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

/**
 * Behaviour déclenché après tirage de dés
 * N'acheter aucun terrain
 */
public class StupideBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

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
