package util;

public class Logger extends JFrame {
	private JTextArea jta = new JTextArea();
	
	public LogFrame(){
		super();
		createGUI();
	}
	
	private void createGUI(){
		this.setTitle("Monopoly logs");
		this.setSize(new Dimension(240, 660));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(jta);
		
		this.setVisible(true);
	}
	
	public void info(String line){
		Pattern p = Pattern.compile("^.*\\\n+$");
		Matcher m = p.matcher(line);
		if(m.find())
			jta.append(line);
		else
			jta.append(line+"\n");
			
		System.out.println(line);
	}
	
	public void err(String line){
		jta.append("<error>");
		info(line);
		System.err.println(line);
	}
}