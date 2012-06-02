package agent;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.beans.PropertyChangeSupport;
import java.util.Vector;

import view.Monopoly;

public class AgentMonopoly extends GuiAgent{
	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> lesJoueurs;
	
	PropertyChangeSupport changes;

	protected void setup(){
		changes = new PropertyChangeSupport(this);
		Monopoly m = new Monopoly(this);
		register();
		fetchPlayers();
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
        catch (FIPAException e) { System.out.println("Enregistrement de l'agent au service echoue - Cause : " + e); }
	}

	private void fetchPlayers() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("joueurs"); 
		template.addServices(sd);
		try {
			DFAgentDescription[] result =
					DFService.search(this, template);
			for (DFAgentDescription o : result ) {
				lesJoueurs.add(o);
			}
		}
		catch(FIPAException fe) { System.out.println("Exception à la recuperation des joueurs "); fe.printStackTrace(); }

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
