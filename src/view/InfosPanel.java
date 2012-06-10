package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import util.Constantes;
import util.Logger;

public class InfosPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int numJoueur;
	
	private String nomJ = "Inconnu";
	private String pion = "Inconnu";
	private String caseJ = "Départ";
	private Vector<String> possessions = new Vector<String>();
	private Integer argent = 0;
	
	private Vector<Image> imgPions = new Vector<Image>();
	
	public InfosPanel(int joueur){
		this.numJoueur = joueur;
		
		try{
			for(int i = 0; i < 8; i++)
				imgPions.add(ImageIO.read(new File(Constantes.PATH_IMG+Constantes.lesPions[i].toString()+".png")));
				
		}catch(IOException e){ Logger.err("<Erreur casePanel ImageIO.read> " + e);}
	}
	
	public void paintComponent(Graphics g){
		
        g.drawString("Joueur : ", 5, 20);
        g.drawString(nomJ, 60, 20);
        
        g.drawString("Pion : ", 5, 40);
        if(pion == "Cheval")
        	g.drawImage(imgPions.get(0), 60, 23, this);
        else if(pion == "Canon")
        	g.drawImage(imgPions.get(1), 60, 23, this);
		else if(pion == "Voiture")
			g.drawImage(imgPions.get(2), 60, 30, this);
		else if(pion == "Bateau")
			g.drawImage(imgPions.get(3), 60, 30, this);
		else if(pion == "Chapeau")
			g.drawImage(imgPions.get(4), 60, 30, this);
		else if(pion == "Brouette")
			g.drawImage(imgPions.get(5), 60, 30, this);
		else if(pion == "Chaussure")
			g.drawImage(imgPions.get(6), 60, 30, this);
		else if(pion == "Fer")
			g.drawImage(imgPions.get(7), 60, 30, this);
        
        g.drawString("("+pion+")", 80, 40);
        
        g.drawString("Argent : ", 5, 60);
        g.drawString(argent.toString(), 60, 60);
        
        g.drawString("Case : ", 5, 80);
        g.drawString(caseJ, 60, 80);
        
        g.drawString("Possessions : ", 5, 100);
        for(int i=0; i < possessions.size(); i++){
        	g.drawString(possessions.get(i), 30, 120+i*20);
        }
        int j = 0;
        if(possessions.size() >= 4)
        	j = possessions.size() - 4;
        this.setPreferredSize(new Dimension(185, 200+j*20));
	}
	
	public void addInfo(String info, String value){
		if(info == "nomJoueur")
			this.nomJ = value;
		else if(info == "argent")
			this.argent = new Integer(value);
		else if(info == "possessions")
			this.possessions.add(value);
		else if(info == "pion")
			this.pion = value;
	}
}
