package behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.Vector;

import platform.MainContainer;
import agent.AgentMonopoly;

public class GivePlayersToOthers extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> listeDesJoueurs;
	private AgentMonopoly agentMonopoly;

	public GivePlayersToOthers(AgentMonopoly agentMonopoly, Vector<DFAgentDescription> lesJoueurs) {
		listeDesJoueurs = lesJoueurs;
		this.agentMonopoly = agentMonopoly;
		
		try {
			AgentController bc = MainContainer.getMc().createNewAgent("BANQUE", "agent.AgentBanque", null);
			bc.start();
		} catch (StaleProxyException e) {e.printStackTrace();}
	}

	@Override
	public void action() {
		ACLMessage messageReceived = agentMonopoly.blockingReceive(); 
		if (messageReceived != null) {
			if(messageReceived.getPerformative() == ACLMessage.REQUEST){
				//System.out.println("Je suis l'agent " + agentMonopoly.getLocalName() + " et " + messageReceived.getSender().getLocalName() + " a demande la liste des Joueurs");
				ACLMessage messageToSend = messageReceived.createReply();
				
				try {
					messageToSend.setContentObject(listeDesJoueurs);
				} catch (IOException e) {e.printStackTrace();}
				agentMonopoly.send(messageToSend);
			}
		}
	}
	
	@Override
	public int onEnd(){
	//	System.out.println("Le Behaviour GivePlayersToOthers a termine");
		reset();
		agentMonopoly.addBehaviour(new OrdonnanceurBehaviour(agentMonopoly, agentMonopoly.getPlateau(), agentMonopoly.getLesJoueurs(), agentMonopoly.fetchJail()));
	    return super.onEnd();
	}
}
