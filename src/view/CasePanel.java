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

public class CasePanel extends JPanel { 
	
	private static final long serialVersionUID = 1L;
	private Image imgMaison;
	private Vector<Image> imgPions; 
	private Image imgCase;
	
	private int nbMaisons = 0;
	
	public CasePanel(int ind){
		super();
		this.setPreferredSize(new Dimension(60, 60));
		try{
			imgMaison = ImageIO.read(new File(Constantes.PATH_IMG+"house2.png"));
			imgCase = ImageIO.read(new File(Constantes.PATH_IMG+ind+".png"));
			for(int i = 0; i < 8; i++)
				imgPions.add(ImageIO.read(new File(Constantes.PATH_IMG+Constantes.lesPions[i])));
				
		}catch(IOException e){ //e.printStackTrace(); 
			
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(imgCase != null && imgMaison != null){
			g.drawImage(imgCase, 0, 0, 60, 60, this);
			
			for(int i=0; i < nbMaisons; i++)
				g.drawImage(imgMaison, 2+i*14, 1, this);
		}
	}
	
	
	public void checkMaisons(int nbMaisons){ // Rajouter la gestion des pions
		this.nbMaisons = nbMaisons;
	}
	
	public void showPions(int[] pions){
			
	}
}
