package util;

<<<<<<< Updated upstream
public class Convert {

=======
import util.Constantes.ActionSpeciale;

public class Convert {

	public static ActionSpeciale stringToAction(String string) {
		if ( ActionSpeciale.ALLERENPRISON.toString().equals(string) ) {
			return ActionSpeciale.ALLERENPRISON;
		}
		if ( ActionSpeciale.CAISSECOMMUNAUTE.toString().equals(string) ) {
			return ActionSpeciale.CAISSECOMMUNAUTE;
		}
		if ( ActionSpeciale.CHANCE.toString().equals(string) ) {
			return ActionSpeciale.CHANCE;
		}
		if ( ActionSpeciale.DEPART.toString().equals(string) ) {
			return ActionSpeciale.DEPART;
		}
		if ( ActionSpeciale.IMPOTS.toString().equals(string) ) {
			return ActionSpeciale.IMPOTS;
		}
		if ( ActionSpeciale.PARCGRATUIT.toString().equals(string) ) {
			return ActionSpeciale.PARCGRATUIT;
		}
		return ActionSpeciale.VISITERPRISON;
	}
>>>>>>> Stashed changes
}
