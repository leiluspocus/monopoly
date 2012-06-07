package behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.Vector;

import util.Constantes;
import agent.AgentBanque;

public class GiveInitialCapital extends Behaviour {
	private static final long serialVersionUID = 1L;
	private AID monopolyAgent;
	private AgentBanque agentBanque;
	private ACLMessage messageReceived;

	public GiveInitialCapital(AID monopolyAgent, AgentBanque agentBanque) {
		this.monopolyAgent = monopolyAgent;
		this.agentBanque = agentBanque;
		System.out.println("L'agent BANQUE est pret a envoyer les premieres dotations");
	}

	@Override
	public void action() {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(monopolyAgent);
		request.setContent("Pourrais-je avoir la liste des joueurs ?");
		myAgent.send(request);
		
		messageReceived = myAgent.receive();
		if (messageReceived != null) {
			try {
				@SuppressWarnings("unchecked")
				Vector<DFAgentDescription> listeDesJoueurs = (Vector<DFAgentDescription>) messageReceived.getContentObject();
				System.out.println("Je suis l'agent " + myAgent.getLocalName() + " et j'ai recu la liste des joueurs de l'agent " + messageReceived.getSender().getLocalName());
				AgentBanque agent = (AgentBanque) myAgent;
				agent.setJoueurs(listeDesJoueurs);
				
				for (DFAgentDescription a : listeDesJoueurs ) {
					request = new ACLMessage(ACLMessage.INFORM);
					request.addReceiver(a.getName());
					request.setContent(Constantes.CAPITAL_DEPART + "");
					myAgent.send(request);
				}
			} catch (UnreadableException e) {e.printStackTrace();}
		}
		else{
			block();
		}
	}
	
	public int onEnd(){ //D�marre le comportement normal de l'agent banque
		System.out.println("Le behaviour RecupPlayersList a termine");
		reset();
		myAgent.addBehaviour(new BankSharkBehaviour(agentBanque));
	    return super.onEnd();
	}

	@Override
	public boolean done() {
		return messageReceived != null;
	}
}
