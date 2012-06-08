package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import util.Constantes;
import util.Constantes.Pion;
import util.Logger;

public class CasePanel extends JPanel { 
	
	private static final long serialVersionUID = 1L;
	private Image imgMaison;
	private Vector<Image> imgPions = new Vector<Image>(); 
	private Image imgCase;
	private Vector<Pion> pions = new Vector<Pion>();
	
	private int nbMaisons = 0;
	
	public CasePanel(int ind){
		super();
		this.setPreferredSize(new Dimension(60, 60));
		try{
			imgMaison = ImageIO.read(new File(Constantes.PATH_IMG+"house2.png"));
			imgCase = ImageIO.read(new File(Constantes.PATH_IMG+ind+".png")); // Fond de la case correspondant au panel
			for(int i = 0; i < 8; i++)
				imgPions.add(ImageIO.read(new File(Constantes.PATH_IMG+Constantes.lesPions[i].toString()+".png")));
				
		}catch(IOException e){ Logger.err("<Erreur casePanel ImageIO.read> " + e);}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(imgCase != null && imgMaison != null){
			g.drawImage(imgCase, 0, 0, 60, 60, this);
			
			for(int i=0; i < nbMaisons; i++)
				g.drawImage(imgMaison, 2+i*14, 1, this);
		}
		
		for(int i = 0; i < pions.size(); i++){
			switch(pions.get(i)){
			case Cheval: g.drawImage(imgPions.get(0), 0, 20, this); break;
			case Canon: g.drawImage(imgPions.get(1), 15, 20, this); break;
			case Voiture: g.drawImage(imgPions.get(2), 30, 20, this); break;
			case Bateau: g.drawImage(imgPions.get(3), 45, 20, this); break;
			case Chapeau: g.drawImage(imgPions.get(4), 0, 40, this); break;
			case Brouette: g.drawImage(imgPions.get(5), 15, 40, this); break;
			case Chaussure: g.drawImage(imgPions.get(6), 30, 40, this); break;
			case Fer: g.drawImage(imgPions.get(7), 45, 40, this); break;
			default: break;
			}
		}
		pions.clear();
	}
	
	
	public void checkMaisons(int nbMaisons){
		if(nbMaisons > 4)
			nbMaisons = 4;
		this.nbMaisons = nbMaisons;
	}
	
	public void addPion(Pion p){
		pions.add(p);
	}
}
