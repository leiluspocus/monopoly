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

import util.Constantes;
import view.Infos;
import view.Monopoly;
import view.Plateau;
import behaviour.CreatePlateauBehaviour;

public class AgentMonopoly extends GuiAgent{
	private static final long serialVersionUID = 1L;
	private Vector<DFAgentDescription> lesJoueurs;
	private Plateau plateau;
	
	private Infos myInfos;
	
	private PropertyChangeSupport changes;

	protected void setup(){
		changes = new PropertyChangeSupport(this);
		Object[] params = this.getArguments();	
		myInfos = ((Infos)params[0]);
		register();
		lesJoueurs = fetchPlayers();
		
		addBehaviour(new CreatePlateauBehaviour(this));
	}
	
	public void addPossession(String joueur, String propriete){
		myInfos.addInfo("possessions", propriete, new Integer(joueur.substring(6)));
	}
	
	public void addFaillite(String joueur){
		myInfos.addInfo("faillite", "", new Integer(joueur.substring(6)));
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
        catch (FIPAException e) { System.err.println("Enregistrement de l'agent monopoly au service echoue - Cause : " + e); }
	}

	private Vector<DFAgentDescription> fetchPlayers() {
		Vector<DFAgentDescription> lesJoueurs = new Vector<DFAgentDescription>();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("joueur"); 
		template.addServices(sd);
		try {
			DFAgentDescription[] result;
			do{
				result = DFService.search(this, template);
			}while(result.length != Constantes.NB_JOUEURS);
			
			for (DFAgentDescription o : result ) {
				lesJoueurs.add(o);
			}
			return lesJoueurs;
		}
		catch(FIPAException fe) { System.err.println("Exception à la recuperation des joueurs "); fe.printStackTrace(); }
		return null;
	}
	
	public AID fetchJail() { 
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("jail"); 
		template.addServices(sd);
		try {
			DFAgentDescription[] result;
			do{
				result = DFService.search(this, template);
			}while( result.length != 1);
			
			return result[0].getName();
		}
		catch(FIPAException fe) { System.err.println("Exception à la recuperation des joueurs "); fe.printStackTrace(); }
		return null;
	}

	public Vector<DFAgentDescription> getLesJoueurs(){ return lesJoueurs; }

	public void addChangeListener(Monopoly m) {this.changes.addPropertyChangeListener(m);}

	public void sendEvent (String info) {
		//changes.firePropertyChange("line", null, info);
	}

	protected void onGuiEvent(GuiEvent ev) {
		//		if(ev.getType() == ENTER){
		//			crb.sendMsg((String)ev.getParameter(0), receiver);
		//		}
	}

	public Plateau getPlateau() { 
		return plateau;
	}

	public void setPlateau(Plateau pl) { plateau = pl; }
}
