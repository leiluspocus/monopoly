package behaviour;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import util.Logger;
import agent.AgentBanque;

public class BankSharkBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private AgentBanque agentBanque;
	private boolean subscribeDecale = false;
	private ACLMessage messageReceived;

	public BankSharkBehaviour(AgentBanque agentBanque) {
		super(agentBanque);
		this.agentBanque = agentBanque; 
	}

	@Override
	public void action() {
		ACLMessage messageToSend;
		
		if(!subscribeDecale)
			messageReceived = agentBanque.blockingReceive();
		else
			subscribeDecale = false;
		
		if (messageReceived != null && messageReceived.getPerformative() == ACLMessage.SUBSCRIBE){

			String[] res = messageReceived.getContent().split("#");
			AID target = new AID (res[0], AID.ISLOCALNAME);
			if (res[1] != null) {
				Double value = Double.parseDouble(res[1]);
				
				if (value > 0) {
					messageToSend = new ACLMessage(ACLMessage.AGREE);
					messageToSend.setContent(value.intValue() + "");
					messageToSend.addReceiver(target);
					agentBanque.send(messageToSend);
				}
				
				if (value < 0) {
					messageToSend = new ACLMessage(ACLMessage.REQUEST);
					value = Math.abs(value);
					messageToSend.setContent(value.intValue() + "");
					messageToSend.addReceiver(target);
					agentBanque.send(messageToSend);
					
					messageReceived = agentBanque.blockingReceive();
					if(messageReceived.getPerformative() == ACLMessage.AGREE && messageReceived.getSender().getLocalName().equals(target.getLocalName()))
						Logger.info("Banque - Paiement de " + target.getLocalName() + " valide");
					else if(messageReceived.getPerformative() == ACLMessage.SUBSCRIBE && messageReceived.getSender().getLocalName().equals("MONOPOLY")){
						Logger.info("Banque - Message de " + messageReceived.getSender().getLocalName() + ":" + messageReceived.getPerformative());
						subscribeDecale = true;
					}
					else
						System.err.println("La banque a recu un message invalide de " + messageReceived.getSender().getLocalName() + ":" + messageReceived.getPerformative());
				} 
			}
			//System.out.println("Mouvement d'argent : " + target.getLocalName() + " -> " + value);
		}
	}
}
