package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import util.Constantes;
import util.Constantes.Pion;
import util.Logger;
import view.Case;
import behaviour.PlayerBehaviour;

public class AgentJoueur extends Agent{
	private static final long serialVersionUID = 1L;
	
	private String 				nomJoueur;
	private DFAgentDescription 	seed;
	private AID	 		    	monopoly;
	private Pion 				pion;
	private Case 				caseCourante;
	private int					capitalJoueur;
	
	private void fetchSeedAgent() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("seed"); 
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			if (result.length > 0) {
				seed = result[0];
			}
		}
		catch(FIPAException fe) { Logger.err("Exception à la recuperation du seedagent par le joueur "); fe.printStackTrace(); }
	} 
	
	private void registerPlayer() 
	{
		DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType("joueur");
        serviceDescription.setName(getLocalName());
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
        } 
        catch (FIPAException e) { Logger.err("Enregistrement de l'agent joueur au service echoue - Cause : " + e); }  
	}
	
	protected void setup() {
		System.out.println("SETUP JOUEUR");
		// Sequential Behaviour => Je lance le des, et j'applique ma tactique de jeu
		fetchSeedAgent(); 
		Object[] params = this.getArguments();		
		setPion((Pion)params[0]);
		setNom((String)params[2]);
		setCapitalJoueur(Constantes.CAPITAL_DEPART);
		registerPlayer(); 
		// SequentialBehaviour cyclique
		addBehaviour(new PlayerBehaviour(this, params));
	}
	
	public AID getSeed() { return seed.getName();	}
	public AID getMonopoly() { return monopoly; }
	public Pion getPion() { return pion; }
	public String getNom() { return nomJoueur; }
	public Case getCaseCourante() { return caseCourante; }
	public int getCapitalJoueur() { return capitalJoueur; }

	public void setPion(Pion pion) { this.pion = pion; }
	public void setNom(String nom) { nomJoueur = nom; }
	public void setCaseCourante(Case caseCourante) { this.caseCourante = caseCourante; }
	public void setCapitalJoueur(int capitalJoueur) { this.capitalJoueur = capitalJoueur; } 
	public void setMonopoly(AID m) { monopoly = m; }
	
}
