package platform;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import util.Constantes;
import util.Constantes.Pion;

public class MainContainer {
	private static final int NB_JOUEURS = 6; //Peut-être changé

	public static void main(String[] args){
		Runtime rt = Runtime.instance();
		Profile p = null;
		try {
			p = new ProfileImpl();
			AgentContainer mc = rt.createMainContainer(p);
			
			AgentController bc = mc.createNewAgent("BANQUE", "agent.AgentBanque", null);
			bc.start();
			bc = mc.createNewAgent("MONOPOLY", "agent.AgentMonopoly", null);
			bc.start();
			
			
			bc = mc.createNewAgent("SEED", "agent.AgentSeed", null);
			bc.start();
			
			bc = mc.createNewAgent("PRISON", "agent.AgentPrison", null);
			bc.start();
			
			bc = mc.createNewAgent("BDC","agent.AgentBDC",new Object[]{50});
			bc.start();
			
			// On lance les joueurs une fois que le plateau est prêt 
			for(int i = 1; i <= NB_JOUEURS; ++i){
				Object[] params = new Object[2];
				params[0] = (Pion)Constantes.lesPions[i-1];
				params[1] = (Integer) i-1;
				bc = mc.createNewAgent("JOUEUR" + i, "agent.AgentJoueur", params);
				bc.start();
				Thread.sleep(60000);
			}
			
		}
		catch(Exception ex){ex.printStackTrace();}
	}
}
