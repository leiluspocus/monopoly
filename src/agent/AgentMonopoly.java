package agent;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.beans.PropertyChangeSupport;

import view.Monopoly;

public class AgentMonopoly extends GuiAgent{
	private static final long serialVersionUID = 1L;
	
	PropertyChangeSupport changes;

	protected void setup(){
		changes = new PropertyChangeSupport(this);
		Monopoly m = new Monopoly(this);
		changes.addPropertyChangeListener(m);
		
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
