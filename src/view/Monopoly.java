package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import agent.AgentMonopoly;



public class Monopoly extends JFrame implements ActionListener,PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private Plateau p;
	private AgentMonopoly myAgent;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu gameMenu = new JMenu("Jeu");
	private JMenuItem newMItem = new JMenuItem("Nouveau");
	private JMenuItem exitMItem = new JMenuItem("Quitter");
	
	private GridLayout northGLayout = new GridLayout(0, 11);
	private GridLayout eastGLayout = new GridLayout(9, 0);
	private GridLayout southGLayout = new GridLayout(0, 11);
	private GridLayout westGLayout = new GridLayout(9, 0);
	
	private Vector<CasePanel> cases = new Vector<CasePanel>();
	
	public Monopoly(AgentMonopoly agent){
		super();
		p = new Plateau();
		myAgent = agent;
		createGUI();
	}
	
	private void createGUI(){
		// Set up the frame
		this.setTitle("Monopoly");
		this.setSize(new Dimension(660, 660));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set up the menu bar
		newMItem.addActionListener(this);
		gameMenu.add(newMItem);
		exitMItem.addActionListener(this);
		gameMenu.add(exitMItem);
		menuBar.add(gameMenu);
		this.setJMenuBar(menuBar);
		
		// Main layout & panels
		JPanel panelNorth = new JPanel();
		JPanel panelEast = new JPanel();
		JPanel panelSouth = new JPanel();
		JPanel panelWest = new JPanel();
		JPanel panelCenter = new JPanel();
		panelNorth.setLayout(northGLayout);
		panelNorth.setSize(new Dimension(60, 660));
		panelEast.setLayout(eastGLayout);
		panelEast.setSize(new Dimension(660, 60));
		panelSouth.setLayout(southGLayout);
		panelSouth.setSize(new Dimension(60, 660));
		panelWest.setLayout(westGLayout);
		panelWest.setSize(new Dimension(660, 60));
		panelCenter.setSize(new Dimension(540, 540));
		panelCenter.add(new JLabel(new ImageIcon("../Monopoly/res/center2.png")));
		this.getContentPane().add(panelNorth, BorderLayout.NORTH);
		this.getContentPane().add(panelEast, BorderLayout.EAST);
		this.getContentPane().add(panelSouth, BorderLayout.SOUTH);
		this.getContentPane().add(panelWest, BorderLayout.WEST);
		this.getContentPane().add(panelCenter, BorderLayout.CENTER);
		
		// Cases panels
		for(int i = 0; i < p.getPlateau().size(); i++){
			cases.add(new CasePanel());
			cases.get(i).add(new JLabel(new ImageIcon("../Monopoly/res/"+i+".png")));
		}
		
		// Tests
		panelWest.add(new JLabel(new ImageIcon("../Monopoly/res/start2.png")));
		panelNorth.add(new JLabel(new ImageIcon("../Monopoly/res/start2.png")));
		panelEast.add(new JLabel(new ImageIcon("../Monopoly/res/start2.png")));
		panelSouth.add(new JLabel(new ImageIcon("../Monopoly/res/jail.png")));
		panelSouth.add(new JLabel(new ImageIcon("../Monopoly/res/start2.png")));
		
		this.setVisible(true);
	}
	
	// Reset the frame with houses and tokens
	public void redraw(){
		for(int i = 0; i < cases.size(); i++){
			if(p.getPlateau().get(i).getClass().equals(CaseTerrain.class)){
				int nbMaisons = ((CaseTerrain)p.getPlateau().get(i)).getNbMaisons();
				// Changer l'affichage de la case
				cases.get(i).checkMaisons(nbMaisons);
				cases.get(i).repaint();
			}
		}
	}

	public void propertyChange(PropertyChangeEvent arg0) {
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == newMItem){
			
		}
		else if(arg0.getSource() == exitMItem){
			System.exit(0);
		}
	}

}
