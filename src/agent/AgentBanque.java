package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Vector;

import behaviour.GiveInitialCapital;

public class AgentBanque extends Agent{
	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> joueurs; //La liste des agents jouant au Monopoly
	private AID monopolyAgent;
	
	protected void setup() {
		register();
		monopolyAgent = findMonopolyAgent();
		addBehaviour(new GiveInitialCapital(monopolyAgent, this));
	}
	
	private AID findMonopolyAgent() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("monopoly"); 
		template.addServices(sd);
		
		DFAgentDescription[] result = null;
		SearchConstraints ALL = new SearchConstraints();
        ALL.setMaxResults(1l);

		try {
			do{
				result = DFService.search(this, template, ALL);
			}while(result.length == 0);
		} catch (FIPAException e) {e.printStackTrace();}
		
		//System.out.println("L'agent Banque a trouvé l'agent Monopoly : " + result[0].getName().getLocalName());
		return result[0].getName();
	}
	
	private void register() {
		DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("banque");
        serviceDescription.setName(getLocalName());
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
        } 
        catch (FIPAException e) { System.err.println("Enregistrement de l'agent Banque au service echoue - Cause : " + e); }
	}
	
	public Vector<DFAgentDescription> getJoueurs() {return joueurs;}
	public void setJoueurs(Vector<DFAgentDescription> joueurs) {this.joueurs = joueurs;}
}
