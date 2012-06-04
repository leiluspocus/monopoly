package behaviour;

import jade.core.behaviours.SequentialBehaviour;
import util.Logger;
import agent.AgentJoueur;

public class PlayerBehaviour extends SequentialBehaviour {

	private static final long serialVersionUID = 1L;
	private Integer strategy;
	
	public String getNom() {
		String nom = ((AgentJoueur) myAgent).getNom();
		return nom;
	}
	 
	public PlayerBehaviour(AgentJoueur agentJoueur, Object[] params) {
		super(agentJoueur);
		 
		addSubBehaviour(new DropDiceBehaviour(myAgent));
		strategy = (Integer) params[1];
		pickStrategy(); 
	}

	private void pickStrategy() {
		switch(strategy)
		{
			case 0:
			{
				Logger.info("Joueur " + getNom() + " adopte la strat�gie Avide !");
				addSubBehaviour(new AvideBehaviour(myAgent));
				break;
			}
			case 1:
			{
				Logger.info("Joueur " + getNom() + " adopte la strat�gie Collectionneur !");
				addSubBehaviour(new CollectionneurBehaviour(myAgent));
				break;
			}
			case 2:
			{
				Logger.info("Joueur " + getNom() + " adopte la strat�gie Evil !");
				addSubBehaviour(new EvilBehaviour(myAgent));
				break;
			}
			case 3:
			{
				Logger.info("Joueur " + getNom() + " adopte la strat�gie Intelligent !");
				addSubBehaviour(new IntelligentBehaviour(myAgent));
				break;
			}
			case 4:
			{
				Logger.info("Joueur " + getNom() + " adopte la strat�gie Picsou !");
				addSubBehaviour(new PicsouBehaviour(myAgent));
				break;
			}
			case 5:
			{
				Logger.info("Joueur " + getNom() + " adopte la strat�gie Stupide !");
				addSubBehaviour(new StupideBehaviour(myAgent));
				break;
			}
			default:
				Logger.info("Joueur " + getNom() + " adopte la strat�gie Avide !");
				addSubBehaviour(new AvideBehaviour(myAgent));
				break;
		}
	}
	
	@Override
	public int onEnd() { 
	    reset();
	    // Ne pas rescheduler les behaviour si le joueur a perdu la partie
	    if (!((AgentJoueur)myAgent).isEnFaillite())
	    {
			addSubBehaviour(new DropDiceBehaviour(myAgent));
			pickStrategy(); 
		    myAgent.addBehaviour(this);
	    }
	    return super.onEnd();
	}

}
