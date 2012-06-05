package behaviour;

import jade.core.behaviours.OneShotBehaviour;
import agent.AgentBDC;

public class BDCBehaviour extends OneShotBehaviour {
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

}
