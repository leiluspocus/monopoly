package view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CasePanel extends JPanel { 
	
	private static final long serialVersionUID = 1L;
	private Image imgMaison;
	private Image imgPion1;
	private Image imgPion2;
	private Image imgPion3;
	private Image imgPion4;
	
	public CasePanel(){
		super();
		
		try{
			imgMaison = ImageIO.read(new File("../Monopoly/res/house.png"));
			imgPion1 = ImageIO.read(new File("chemin des images"));
			imgPion2 = ImageIO.read(new File("chemin des images"));
			imgPion3 = ImageIO.read(new File("chemin des images"));
			imgPion4 = ImageIO.read(new File("chemin des images"));
		}catch(IOException e){ //e.printStackTrace(); 
			
		}
	}
	
	public void paintComponent(Graphics g){
		
	}
	
	
	public void checkMaisons(int nbMaisons){ // Rajouter la gestion des pions
		
	}
}
