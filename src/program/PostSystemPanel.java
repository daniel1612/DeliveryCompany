package program;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import components.*;
import components.Package;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CyclicBarrier;


/**
 * This class is manage the all gui program
 * @author falfo
 *
 */
public class PostSystemPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Main frame;
	private JPanel p1;
	private JButton[] b_num;
	private String[] names = {"Create system","Start","Stop","Resume","All packages info","Branch info","CloneBranch","Restore","Report"};
	private JScrollPane scrollPane;
	private boolean isTableVisible = false;
	private boolean isTable2Visible = false;
	private int colorInd = 0;
	private boolean started = false;
	private MainOffice game = null;
	private int packagesNumber;
	private int branchesNumber;
	private int trucksNumber;
	private CareTaker Ct;


	/**
	 * Constructor
	 */
	public PostSystemPanel(Main f) {
		Ct = new CareTaker();
		frame = f;
		isTableVisible = false;
		setBackground(new Color(255,255,255));
		p1=new JPanel();
		p1.setLayout(new GridLayout(1,7,0,0));
		//p1.setBackground(new Color(0,150,255));
		b_num=new JButton[names.length];

		for(int i=0;i<names.length;i++) {
			b_num[i]=new JButton(names[i]);
			b_num[i].addActionListener(this);
			b_num[i].setBackground(Color.lightGray);
			p1.add(b_num[i]);		
		}

		setLayout(new BorderLayout());
		add("South", p1);
	}	

	/**
	 * This method is creating a new main office and initialize the system
	 * @param branches
	 * @param trucks
	 * @param packages
	 */
	public void createNewPostSystem(int branches, int trucks, int packages) {
		if (started) return;
		//game = new MainOffice(branches, trucks, this, packages);
		game= MainOffice.getInstance(branches, trucks, this, packages);


		packagesNumber = packages;
		trucksNumber = trucks;
		branchesNumber = branches;

		repaint();
	}

	/**
	 * This method is paint the simulation to the screen
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	

		if (game==null) return;

		Hub hub = game.getHub();
		ArrayList<Branch> branches = hub.getBranches();

		int offset = 403/(branchesNumber+1);
		int y=100;
		int y2=246;
		int offset2 = 140/(branchesNumber+1);

		g.setColor(new Color(0,102,0));
		g.fillRect(1120, 216, 40, 200);


		for (Branch br: branches) {
			br.paintComponent(g,y,y2);
			y+=offset;
			y2+=offset2;
		}


		int x = 150;
		int offset3 = (1154-300)/(packagesNumber-1);

		for (Package p: game.getPackages()) {
			p.paintComponent(g,x,offset);
			x+=offset3;
		}


		for (Branch br: branches) {
			for(Truck tr: br.getTrucks()) {
				tr.paintComponent(g);
			}
		}

		for(Truck tr: hub.getTrucks()) {
			tr.paintComponent(g);
		}

	}


	/**
	 * set color index method
	 * @param ind
	 */
	public void setColorIndex(int ind) {
		this.colorInd = ind;
		repaint();
	}

	/**
	 * set backround method
	 * @param num
	 */
	public void setBackgr(int num) {
		switch(num) {
		case 0:
			setBackground(new Color(255,255,255));
			break;
		case 1:
			setBackground(new Color(0,150,255));
			break;

		}
		repaint();
	}


	/**
	 * add dialog method
	 */
	public void add(){
		CreatePostSystemlDialog dial = new CreatePostSystemlDialog(frame,this,"Create post system");
		dial.setVisible(true);
	}

	
	/**
	 * This method start the game
	 */
	public void start() {
		if (game==null || started) return;
		Thread t = new Thread(game);
		started = true;
		t.start();
	}
	
	/**
	 * resume method, resume the game if the user press "resume"
	 */
	public void resume() {
		if (game == null) return;
		game.setResume();
	}

	/**
	 * stop method, stop the game if the user press "stop"
	 */
	public void stop() {
		if (game == null) return;
		game.setSuspend();
	}


	/**
	 * open the packages info if the user press "package info"
	 */
	public void info() {
		if (game == null || !started) return;
		if(isTable2Visible == true) {
			scrollPane.setVisible(false);
			isTable2Visible = false;
		}
		if(isTableVisible == false) {
			int i=0;
			String[] columnNames = {"Package ID", "Sender", "Destination", "Priority", "Staus"};
			ArrayList<Package> packages = game.getPackages();
			String [][] data = new String[packages.size()][columnNames.length];
			for(Package p : packages) {
				data[i][0] = ""+p.getPackageID();
				data[i][1] = ""+p.getSenderAddress();
				data[i][2] = ""+p.getDestinationAddress();
				data[i][3] = ""+p.getPriority();
				data[i][4] = ""+p.getStatus();
				i++;
			}
			JTable table = new JTable(data, columnNames);
			scrollPane = new JScrollPane(table);
			scrollPane.setSize(450,table.getRowHeight()*(packages.size())+24);
			add(scrollPane, BorderLayout.CENTER );
			isTableVisible = true;
		}
		else
			isTableVisible = false;

		scrollPane.setVisible(isTableVisible);
		repaint();
	}


	/**
	 * open the branch Info  if the user press "branch Info"
	 */
	public void branchInfo() {
		if (game == null || !started) return;

		if(scrollPane!=null) scrollPane.setVisible(false);
		isTableVisible = false;
		isTable2Visible = false;
		String[] branchesStrs = new String[game.getHub().getBranches().size()+1];
		branchesStrs[0] = "Sorting center";
		for(int i=1; i<branchesStrs.length; i++)
			branchesStrs[i] = "Branch "+i;
		JComboBox cb = new JComboBox(branchesStrs);
		String[] options = { "OK", "Cancel" };
		String title = "Choose branch";
		int selection = JOptionPane.showOptionDialog(null, cb, title,
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
				options[0]);

		if (selection==1||selection==-1) return;
		//System.out.println(cb.getSelectedIndex());
		if(isTable2Visible == false) {
			int i=0;
			String[] columnNames = {"Package ID", "Sender", "Destination", "Priority", "Staus"};
			Branch branch;
			List<Package> packages = null;
			int size=0;
			if(cb.getSelectedIndex()==0) {
				packages = game.getHub().getPackages();
				size = packages.size();
			}
			else {
				packages = game.getHub().getBranches().get(cb.getSelectedIndex()-1).getPackages();
				size = packages.size();
				int diff = 0;
				for(Package p : packages) {
					if (p.getStatus()==Status.BRANCH_STORAGE) {
						diff++;
					}
				}
				size = size - diff/2;
			}
			String [][] data = new String[size][columnNames.length];
			for(Package p : packages) {
				boolean flag = false;
				for(int j=0; j<i; j++)
					if (data[j][0].equals(""+p.getPackageID())) {
						flag = true;
						break;
					}
				if (flag) continue;
				data[i][0] = ""+p.getPackageID();
				data[i][1] = ""+p.getSenderAddress();
				data[i][2] = ""+p.getDestinationAddress();
				data[i][3] = ""+p.getPriority();
				data[i][4] = ""+p.getStatus();
				i++;
			}
			JTable table = new JTable(data, columnNames);
			scrollPane = new JScrollPane(table);
			scrollPane.setSize(450,table.getRowHeight()*(size)+24);
			add(scrollPane, BorderLayout.CENTER);
			isTable2Visible = true;
		}
		else
			isTable2Visible = false;

		scrollPane.setVisible(isTable2Visible);
		repaint();
	}

	/**
	 * exit the program
	 */
	public void destroy(){  	        
		System.exit(0);
	}

	/**
	 * if the user press "Clone branch", This method create a new branch
	 */
	public void CloneBranch() {
		if (game == null) return;
		Ct.addMemento(game.createMemento());
		
		if(scrollPane!=null) scrollPane.setVisible(false);
		isTableVisible = false;
		isTable2Visible = false;
		String[] branchesStrs = new String[game.getHub().getBranches().size()];
		int count=0;
		//	   branchesStrs[0] = "Branch "+1;
		for(int i=0; i<branchesStrs.length; i++) {
			count++;
			branchesStrs[i] = "Branch "+count;
		}
		JComboBox cb = new JComboBox(branchesStrs);
		String[] options = { "OK", "Cancel" };
		String title = "Choose branch to clone";
		int selection = JOptionPane.showOptionDialog(null, cb, title,
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
				options[0]);
		if (selection==1||selection==-1) return;
		System.out.println(cb.getSelectedIndex());

		Branch b=game.getHub().getBranches().get(cb.getSelectedIndex()).clone();
		b.changeBranch();

		game.getHub().getBranches().add(b);

		repaint();


	}

	/**
	 * start the memento, restore the last situation
	 */
	public void restore()
	{
		if(game==null || !started)
		{
			Ct.addMemento(game.createMemento());
			game.setMemento(Ct.Undo());
			
			repaint();
		}

	}
	
	/**
	 * open the txt file
	 */
	public void report()
	{
		try  
		{  
			//constructor of file class having file as argument  
			File file = new File(WriterTracking.getFileName());   
			if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
			{  
				System.out.println("not supported");  
				return;  
			}  
			Desktop desktop = Desktop.getDesktop();  
			if(file.exists())         //checks file exists or not  
				desktop.open(file);              //opens the specified file  
		}  
		catch(Exception e)  
		{  
			e.printStackTrace();  
		}  
	}


	/**
	 * Action performed method
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == b_num[0]) 
		{
			add();
			b_num[0].setEnabled(false);
		}
			
		else if(e.getSource() == b_num[1]) 
		{
			b_num[7].setEnabled(false);
			start();
			b_num[1].setEnabled(false);
		}
		else if(e.getSource() == b_num[2])  
			stop();
		else if(e.getSource() == b_num[3])  
			resume(); 
		else if(e.getSource() == b_num[4])  
			info();
		else if(e.getSource() == b_num[5])  
			branchInfo();

		else if(e.getSource() == b_num[6])  
			CloneBranch();
		else if(e.getSource() == b_num[7])
		{
			restore();
		}
		else if(e.getSource() == b_num[8])
		{
			report();
		}
	}
}