package agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import util.Logger;

public class AgentBanque extends Agent{
	private static final long serialVersionUID = 1L;
	private DFAgentDescription[] joueurs; //La liste des agents jouant au Monopoly
	
	protected void setup() {
		register();
		addTargets();
		
	}
	
	private void register() {
		DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType("banque");
        serviceDescription.setName(getLocalName());
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
        } 
        catch (FIPAException e) { Logger.err("Enregistrement de l'agent Banque au service echoue - Cause : " + e); }
	}
	
	/*  
	 * Recherche de tous les agents joueurs
	 */
	private void addTargets() {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
	}
}
