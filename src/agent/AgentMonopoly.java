package agent;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.beans.PropertyChangeSupport;
import java.util.Vector;

import util.Logger;
import view.Monopoly;
import behaviour.OrdonnanceurBehaviour;

public class AgentMonopoly extends GuiAgent{
	private static final long serialVersionUID = 1L; 
	
	PropertyChangeSupport changes;

	protected void setup(){
		System.out.println("SETUP MONOPOLY");
		changes = new PropertyChangeSupport(this);
		Monopoly m = new Monopoly(this);
		register();
		Vector<DFAgentDescription> lesJoueurs = fetchPlayers();
		addBehaviour(new OrdonnanceurBehaviour(this, lesJoueurs, fetchJail()));
		changes.addPropertyChangeListener(m); 

	}
	
	private void register() {
		DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription  = new ServiceDescription();
        serviceDescription.setType("monopoly");         
        serviceDescription.setName(getLocalName());
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription); 
        } 
        catch (FIPAException e) { Logger.err("Enregistrement de l'agent monopoly au service echoue - Cause : " + e); }
	}

	private Vector<DFAgentDescription> fetchPlayers() {
		Vector<DFAgentDescription> lesJoueurs = new Vector<DFAgentDescription>();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("joueur"); 
		template.addServices(sd);
		try {
			DFAgentDescription[] result =
					DFService.search(this, template);
			for (DFAgentDescription o : result ) {
				lesJoueurs.add(o);
			}
			return lesJoueurs;
		}
		catch(FIPAException fe) { System.out.println("Exception à la recuperation des joueurs "); fe.printStackTrace(); }
		return null;
	}
	
	private AID fetchJail() { 
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("joueur"); 
		template.addServices(sd);
		try {
			DFAgentDescription[] result =
					DFService.search(this, template);
			if ( result.length != 0)
				return result[0].getName();
		}
		catch(FIPAException fe) { System.out.println("Exception à la recuperation des joueurs "); fe.printStackTrace(); }
		return null;
	}



	public void sendEvent (String info) {
		//changes.firePropertyChange("line", null, info);
	}

	protected void onGuiEvent(GuiEvent ev) {
		//		if(ev.getType() == ENTER){
		//			crb.sendMsg((String)ev.getParameter(0), receiver);
		//		}
	}

}
