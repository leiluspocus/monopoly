package agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import util.Logger;

// Ceci est un commentaire inutile
public class AgentBDC extends Agent{
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		register();
	}
	
	private void register() {
		DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType("knowledge");
        serviceDescription.setName(getLocalName());
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
        } 
        catch (FIPAException e) { Logger.err("Enregistrement de l'agent BDC au service echoue - Cause : " + e); }
	}
}
