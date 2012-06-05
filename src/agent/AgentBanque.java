package agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import util.Constantes;
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
		SearchConstraints ALL = new SearchConstraints();
		ALL.setMaxResults(new Long(-1));
		DFAgentDescription agentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription  = new ServiceDescription();
		serviceDescription.setType("joueur");
		agentDescription.addServices(serviceDescription);
		try {
			do{
				joueurs = DFService.search(this, agentDescription, ALL);
			}
			while(joueurs.length < Constantes.NB_JOUEURS);
			for (int i = joueurs.length; i > 0; i--)
				System.out.println(joueurs[i-1].getName().getLocalName() + " has been found !");
			System.out.println("");
		} 
		catch (FIPAException e) {System.out.println("Erreur lors de la recherche de resolveurs - Cause : " + e);} 
	}
}
