package behaviour;

import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.Vector;

import platform.MainContainer;
import util.Constantes;
import agent.AgentMonopoly;

public class GivePlayersToOthers extends Behaviour {
	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> listeDesJoueurs;
	private int nbRequest;
	private AgentMonopoly agentMonopoly;

	public GivePlayersToOthers(AgentMonopoly agentMonopoly, Vector<DFAgentDescription> lesJoueurs) {
		listeDesJoueurs = lesJoueurs;
		nbRequest = 0;
		this.agentMonopoly = agentMonopoly;
		
		try {
			AgentController bc = MainContainer.getMc().createNewAgent("BANQUE", "agent.AgentBanque", null);
			bc.start();
		} catch (StaleProxyException e) {e.printStackTrace();}
	}

	@Override
	public void action() {
		ACLMessage messageReceived = agentMonopoly.receive();
		if (messageReceived != null) {
			System.out.println("Je suis l'agent " + agentMonopoly.getLocalName() + " et j'ai reçu un message");
			if(messageReceived.getPerformative() == ACLMessage.REQUEST){
				System.out.println("Je suis l'agent " + agentMonopoly.getLocalName() + " et l'agent " + messageReceived.getSender().getLocalName() + " a demande la liste des Joueurs");
				ACLMessage messageToSend = messageReceived.createReply();
				
				try {
					messageToSend.setContentObject(listeDesJoueurs);
				} catch (IOException e) {e.printStackTrace();}
				agentMonopoly.send(messageToSend);
			}
		}
		else {
			block();
		}
	}

	@Override
	public boolean done() {
		System.out.println("Le Behaviour GivePlayersToOthers a termine");
		return nbRequest == Constantes.NB_AGENT_SERVICE;
	}
	
	public int onEnd(){
		System.out.println("Le behaviour RecupPlayersList a termine");
		reset();
		agentMonopoly.addBehaviour(new OrdonnanceurBehaviour(agentMonopoly, agentMonopoly.getLesJoueurs(), agentMonopoly.fetchJail()));
	    return super.onEnd();
	}
}
