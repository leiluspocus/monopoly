package util;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Logger extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JTextArea jta = new JTextArea();
	private static HashMap<Integer, String> infosJoueurs = new HashMap<Integer, String>();
	private static Vector<String> nomsJoueurs = new Vector<String>();
	
	public Logger(){
		super();
		initInfosJoueurs();
		createGUI();
	}
	
	public void initInfosJoueurs() {
		int i=1;
		nomsJoueurs = Helper.computePlayersName();
		for (@SuppressWarnings("unused") String j : nomsJoueurs) {
			infosJoueurs.put(i, null);
			++i;
		}
	}
	
	private void createGUI(){
		this.setTitle("Monopoly logs");
		this.setSize(new Dimension(500, 660));
		this.setLocation(5, 5);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(new JScrollPane(jta));
		
		this.setVisible(true);
	}
	
	public static void info(String line){
		Pattern p = Pattern.compile("^.*\\\n+$");
		Matcher m = p.matcher(line);
		if(m.find())
			jta.append(line);
		else
			jta.append(line+"\n");
			
		System.out.println(line);
	}
	
	public static void err(String line){
		jta.append("<error>");
		
		Pattern p = Pattern.compile("^.*\\\n+$");
		Matcher m = p.matcher(line);
		if(m.find())
			jta.append(line);
		else
			jta.append(line+"\n");
		
		System.err.println(line);
	}
	
	public static void majInfosForPlayer(String joueur, String newInfos) {
		//On recupere le LocalName de l'agent Jade
		int indiceJoueur = Integer.parseInt(joueur.replace("JOUEUR", "").trim());
		if ( infosJoueurs.containsKey(indiceJoueur)) {
			infosJoueurs.put(indiceJoueur, newInfos);
			System.out.println("Joueur " + nomsJoueurs.get(indiceJoueur) + " strategie "  + Helper.getStrategy(indiceJoueur-1) +" infos >" + newInfos);
		}
		else {
			System.err.println("Joueur inconnu " + joueur + " essaie d'afficher qqch dans le logger");
		}
	}
	
}