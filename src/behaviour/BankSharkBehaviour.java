package behaviour;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import agent.AgentBanque;

public class BankSharkBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentBanque agentBanque;

	public BankSharkBehaviour(AgentBanque agentBanque) {
		this.agentBanque = agentBanque;
		System.out.println("La banque a desormais atteint son comportement principal");
	}

	@Override
	public void action() {
		ACLMessage messageReceived = agentBanque.blockingReceive(); 
		if (messageReceived != null && messageReceived.getPerformative() == ACLMessage.SUBSCRIBE){

			String[] res = messageReceived.getContent().split("#");
			AID target = new AID (res[0], AID.ISLOCALNAME);
			int value = Integer.parseInt(res[1]);
			
			System.out.println("Mouvement d'argent : " + target.getLocalName() + " -> " + value);
		}
	}
}
