package behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Vector;

import util.Logger;
import view.Carte;
import view.Case;
import agent.AgentBDC;

public class BDCBehaviour extends Behaviour {
	private static final long serialVersionUID = 1L;
	private int end;
	private AgentBDC agentBDC;
	
	public BDCBehaviour(AgentBDC agentBDC) {
		this.agentBDC = agentBDC;
		end = 2;
	}

	/**
	 * QUERY_IF from 
	 */
	@Override
	public void action() {
		ACLMessage messageReceived = agentBDC.receive();
		if (messageReceived != null) {
			if(messageReceived.getPerformative() == ACLMessage.QUERY_IF){
				Logger.info("Agent " + agentBDC.getLocalName() + " - Agent " + messageReceived.getSender().getLocalName() + " a demande la liste des Cartes");
				ACLMessage messageToSend = messageReceived.createReply();
				Vector<Carte> v = new Vector<Carte>();
				messageToSend.setPerformative(ACLMessage.INFORM_IF);
				
				v.addAll(agentBDC.buildCartesChances());
				v.addAll(agentBDC.buildCartesCommunaute());
				
				try {
					messageToSend.setContentObject(v);
				} catch (IOException e) {e.printStackTrace();}
				agentBDC.send(messageToSend);
				end--;
			}
			else
				if(messageReceived.getPerformative() == ACLMessage.QUERY_REF){
					System.out.println("Agent " + agentBDC.getLocalName() + " - Agent " + messageReceived.getSender().getLocalName() + " a demande la liste des Cases");
					ACLMessage messageToSend = messageReceived.createReply();
					Vector<Case> v = new Vector<Case>();
					messageToSend.setPerformative(ACLMessage.INFORM_REF);
					
					v.addAll(agentBDC.buildCasesSpeciales());
					v.addAll(agentBDC.buildCasesAchetables());
					v.addAll(agentBDC.buildCasesTerrains());
					
					try {
						messageToSend.setContentObject(v);
					} catch (IOException e) {e.printStackTrace();}
					agentBDC.send(messageToSend);
					end--;
				}
				else
					System.err.println("Agent " + myAgent.getLocalName() + " - Agent " + messageReceived.getSender().getLocalName() + " a envoye un performatif incorrect");
		}
		else {
			block();
		}
	}

	@Override
	public boolean done() {
		//System.out.println("Etape Behaviour de la Base de Connaissance : " + end);
		return end == 0;
	}
}
