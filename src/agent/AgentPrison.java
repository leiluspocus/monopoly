package agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import behaviour.JanitorJailBehaviour;

public class AgentPrison extends Agent{
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		register(); 
		addBehaviour(new JanitorJailBehaviour(this));
	}
	
	private void register() {
		DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType("jail");
        serviceDescription.setName(getLocalName());
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
        } 
        catch (FIPAException e) { System.err.println("Enregistrement de l'agent Prison au service echoue - Cause : " + e); }
	}
}
