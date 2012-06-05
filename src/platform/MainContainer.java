package platform;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.util.Vector;

import util.Constantes;
import util.Constantes.Pion;

public class MainContainer {
	private static final int NB_JOUEURS = 6; //Peut-être changé
	private static Vector<String> lesNomsDeJoueur;
	
	private static void setup() {
		lesNomsDeJoueur = new Vector<String>();
		lesNomsDeJoueur.add("Annie");
		lesNomsDeJoueur.add("Bobby");
		lesNomsDeJoueur.add("Danny");
		lesNomsDeJoueur.add("Emmy");
		lesNomsDeJoueur.add("Fanny");
		lesNomsDeJoueur.add("Gary");
		lesNomsDeJoueur.add("Larry");
	}
	
	public static void main(String[] args){
		Runtime rt = Runtime.instance();
		Profile p = null;
		setup();
		try {
			p = new ProfileImpl();
			AgentContainer mc = rt.createMainContainer(p);

			AgentController bc = mc.createNewAgent("PRISON", "agent.AgentPrison", null);
			bc.start();
			
			bc = mc.createNewAgent("BDC","agent.AgentBDC",new Object[]{50});
			bc.start();
			
			bc = mc.createNewAgent("MONOPOLY", "agent.AgentMonopoly", null);
			bc.start();
			
			bc = mc.createNewAgent("SEED", "agent.AgentSeed", null);
			bc.start(); 
			
			bc = mc.createNewAgent("BANQUE", "agent.AgentBanque", null);
			bc.start();
			
			// On lance les joueurs une fois que le plateau est prêt 
			for(int i = 1; i <= NB_JOUEURS; ++i){
				Object[] params = new Object[3];
				params[0] = (Pion)Constantes.lesPions[i-1];
				params[1] = (Integer) i-1;
				params[2] = lesNomsDeJoueur.get(i);
				bc = mc.createNewAgent("JOUEUR" + i, "agent.AgentJoueur", params);
				bc.start();
			}
			
		}
		catch(Exception ex){ex.printStackTrace();}
	}
}
