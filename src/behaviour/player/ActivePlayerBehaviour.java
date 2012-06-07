package behaviour.player;

import jade.core.behaviours.SequentialBehaviour;
import agent.AgentJoueur;
import behaviour.DropDiceBehaviour;

public class ActivePlayerBehaviour extends SequentialBehaviour {

	private static final long serialVersionUID = 1L;
	private Integer strategy;
	
	public String getNom() {
		String nom = ((AgentJoueur) myAgent).getNom();
		return nom;
	}
	 
	public ActivePlayerBehaviour(AgentJoueur agentJoueur, Object[] params) {
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
				System.out.println("Joueur " + getNom() + " adopte la strategie Avide !");
				addSubBehaviour(new AvideBehaviour(myAgent));
				break;
			}
			case 1:
			{
				System.out.println("Joueur " + getNom() + " adopte la strategie Collectionneur !");
				addSubBehaviour(new CollectionneurBehaviour(myAgent));
				break;
			}
			case 2:
			{
				System.out.println("Joueur " + getNom() + " adopte la strategie Evil !");
				addSubBehaviour(new EvilBehaviour(myAgent));
				break;
			}
			case 3:
			{
				System.out.println("Joueur " + getNom() + " adopte la strategie Intelligent !");
				addSubBehaviour(new IntelligentBehaviour(myAgent));
				break;
			}
			case 4:
			{
				System.out.println("Joueur " + getNom() + " adopte la strategie Picsou !");
				addSubBehaviour(new PicsouBehaviour(myAgent));
				break;
			}
			case 5:
			{
				System.out.println("Joueur " + getNom() + " adopte la strategie Stupide !");
				addSubBehaviour(new StupideBehaviour(myAgent));
				break;
			}
			default:
				System.out.println("Joueur " + getNom() + " adopte la strategie Avide !");
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
			//addSubBehaviour(new DropDiceBehaviour(myAgent));
			pickStrategy(); 
		    myAgent.addBehaviour(this);
	    }
	    return super.onEnd();
	}

}
