package behaviour;

import jade.core.behaviours.CyclicBehaviour;
import agent.AgentBanque;

public class BankSharkBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentBanque agentBanque;

	public BankSharkBehaviour(AgentBanque agentBanque) {
		this.agentBanque = agentBanque;
		System.out.println("La banque est desormais dans son comportement normal");
	}

	@Override
	public void action() {

	}
}
