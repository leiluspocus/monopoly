package behaviour;

import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Vector;

import util.Constantes;
import agent.AgentMonopoly;

public class GivePlayersToOthers extends Behaviour {
	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> listeDesJoueurs;
	private int nbRequest;

	public GivePlayersToOthers(AgentMonopoly agentMonopoly, Vector<DFAgentDescription> lesJoueurs) {
		listeDesJoueurs = lesJoueurs;
		nbRequest = 0;
	}

	@Override
	public void action() {
		ACLMessage messageReceived = myAgent.receive();
		if (messageReceived != null) {
			String line = messageReceived.getContent();

			if(messageReceived.getPerformative() == ACLMessage.REQUEST){
				System.out.println("Je suis l'agent " + myAgent.getLocalName() + " et j'ai re�u : " + line);
				ACLMessage messageToSend = messageReceived.createReply();
				
				try {
					messageToSend.setContentObject(listeDesJoueurs);
				} catch (IOException e) {e.printStackTrace();}
				
				myAgent.send(messageToSend);
			}
		}
		else {
			block();
		}
	}

	@Override
	public boolean done() {
		return nbRequest == Constantes.NB_AGENT_SERVICE;
	}

}
