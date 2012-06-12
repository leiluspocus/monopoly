package util;

import jade.core.AID;

import java.util.Vector;

public class Helper {

	public static Vector<String> computePlayersName() {
		Vector<String> lesNomsDeJoueur = new Vector<String>();
		lesNomsDeJoueur.add("Annie");
		lesNomsDeJoueur.add("Bobby");
		lesNomsDeJoueur.add("Danny");
		lesNomsDeJoueur.add("Emmy");
		lesNomsDeJoueur.add("Fanny");
		lesNomsDeJoueur.add("Gary");
		lesNomsDeJoueur.add("Larry");
		return lesNomsDeJoueur;
	}

	public static String getStrategy(int indice) {
		switch ( indice ) {
		case 0:
			return "Avide"; 
		case 1:
			return "Collectionneur"; 
		case 2:
			return "Evil";
		case 3:
			return "Intelligent";
		case 4:
			return "Picsou";
		case 5:
			return "Stupide"; 
		}
		return "Avide";
	}
	
	public static boolean compareAID(AID a1, AID a2) {
		String name1, name2;
		if ( a1 == null ) {
			name1 = "";
		}
		else {
			name1 = a1.getLocalName();
		}
		if ( a2 == null ) {
			name2 = "";
		}
		else {
			 name2 = a2.getLocalName();	
		} 
		return name1.equals(name2);
	}
}
