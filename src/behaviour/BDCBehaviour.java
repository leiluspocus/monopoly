package behaviour;

import jade.core.behaviours.Behaviour;
import agent.AgentBDC;

public class BDCBehaviour extends Behaviour {
	private static final long serialVersionUID = 1L;
	
	public BDCBehaviour(AgentBDC agentBDC) {
	}

	@Override
	public void action() {
		/*ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		for(int i = 0; i < Constantes.NB_JOUEURS; i++)
			request.addReceiver(slaves[i].getName());
		
		myAgent.send(request);*/
	}

	@Override
	public boolean done() {
		return false;
	}

}
