package platform;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainContainer {
	private static final int NB_JOUEURS = 6;

	public static void main(String[] args){
		Runtime rt = Runtime.instance();
		Profile p = null;
		try {
			p = new ProfileImpl("proprietes.txt");
			AgentContainer mc = rt.createMainContainer(p);
			
			AgentController bc = mc.createNewAgent("BANQUE", "agent.AgentBanque", null);
			bc.start();
			bc = mc.createNewAgent("MONOPOLY", "agent.AgentMonopoly", null);
			bc.start();
			
			for(int i = 1; i <= NB_JOUEURS; ++i){
				bc = mc.createNewAgent("JOUEUR" + i, "agent.AgentJoueur", null);
				bc.start();
			}
			
			bc = mc.createNewAgent("SEED", "agent.AgentSeed", null);
			bc.start();
			
			bc = mc.createNewAgent("PRISON", "agent.AgentPrison", null);
			bc.start();
			
			bc = mc.createNewAgent("BDC","agent.AgentBDC",new Object[]{50});
			bc.start();
		}
		catch(Exception ex){ex.printStackTrace();}
	}
}
