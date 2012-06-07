package util;

import java.awt.Dimension;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Logger extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JTextArea jta = new JTextArea();
	
	public Logger(){
		super();
		createGUI();
	}
	
	private void createGUI(){
		this.setTitle("Monopoly logs");
		this.setSize(new Dimension(300, 660));
		this.setLocation(5, 5);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(jta);
		
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
		info(line);
		System.err.println(line);
	}
}