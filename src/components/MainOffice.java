package components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JPanel;

/**
 *  * * @version 09.05.2021  
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 */

public class MainOffice implements Runnable ,PropertyChangeListener
{
	
	/**
	 * @param panel - JPanel
	 * @param threadSuspend - boolean
	 * @param maxPackages - int 
	 * @param main - static volatile MainOffice
	 * @param customer - Customer[]
	 * @param w - WriterTracking
	 * @param executor - ExecutorService
	 *  @param clock - Represents the amount of beats that have passed since the system was started.
	 * @param hub - An object of a sorting center, containing all the active branches in the game
	 * @param packages - A collection of all the packages that exist in the system 
	 * (including those that have already been provided to the customer).
	 */
	private ReadWriteLock RW = new ReentrantReadWriteLock();
	private static int clock=0;
	private static Hub hub;
	private static ArrayList<Package> packages=new ArrayList<Package>();
	private JPanel panel;
	private int maxPackages;
	private boolean threadSuspend = false;

	private static volatile MainOffice main;
	private Customer[] customer = new Customer[10];
	private static final int topCust=10;
	int counter=0;
	private static WriterTracking w = new WriterTracking();

	private ExecutorService executor = Executors.newFixedThreadPool(2);
	
	/**
	 * A builder who receives the number of branches that will be in the game and the number of vehicles per branch.
	 *  The builder creates a sorting center (Hub) and adds standard trucks in the quantity in the trucksForBranch parameter. 
	 *  In addition adds one non-standard truck to the sorting center. 
	 *  He then creates branches (Branch) in the quantity that appears in the branches parameter 
	 *  and to each such branch he adds Van-type trucks in a quantity that matches the trucksForBranch parameter.
	 * @param branches - int 
	 * @param trucksForBranch - int 
	 * @param numpck - int
	 */
	private MainOffice(int branches, int trucksForBranch, JPanel panel, int maxPack) {
		this.panel = panel;
		this.maxPackages = maxPack;
		addHub(trucksForBranch);
		addBranches(branches, trucksForBranch);
		for (int i = 0; i < topCust; i++)
			customer[i] = new Customer();
		System.out.println("\n\n========================== START ==========================");
	}

	/**
	 * get instance method
	 * @param branches
	 * @param trucks
	 * @param panel
	 * @param packages
	 * @return
	 */
	public static MainOffice getInstance(int branches, int trucks, JPanel panel, int packages) {
		if(main==null) {
			synchronized(MainOffice.class){   
				if (main == null) {
					main= new MainOffice(branches, trucks, panel, packages);
				}
			}
		}
		return main;

	}



	/**
	 * get hub method
	 * @return
	 */
	public static Hub getHub() {
		return hub;
	}

	/**
	 * get clock method
	 * @return
	 */
	public static int getClock() {
		return clock;
	}

	/**
	 * run method
	 */
	@Override
	public void run() {
		Thread hubThrad = new Thread(hub);
		hubThrad.start();
		for (Truck t : hub.listTrucks) {
			Thread trackThread = new Thread(t);
			trackThread.start();
		}
		for (Branch b : hub.getBranches()) {
			Thread branch = new Thread(b);
			for (Truck t : b.listTrucks) {
				Thread trackThread = new Thread(t);
				trackThread.start();
			}
			branch.start();
		}
		for (Customer c : customer) {
			executor.execute(c);
		}


		while (true) {
			synchronized (this) {
				while (threadSuspend)
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			

			if (isCustomersFinished()) {
				System.out.println("FINISHED!!");
				setSuspend();
				executor.shutdownNow();
				
			}

			tick();
		}

	}

	/**
	 * print report method
	 */
	public void printReport() {
		for (Package p: packages) {
			System.out.println("\nTRACKING " +p);
			for (Tracking t: p.getTracking())
				System.out.println(t);
		}
	}

	/**
	 * return the clock in clock format
	 * @return String
	 */
	public String clockString() {
		String s="";
		int minutes=clock/60;
		int seconds=clock%60;
		s+=(minutes<10) ? "0" + minutes : minutes;
		s+=":";
		s+=(seconds<10) ? "0" + seconds : seconds;
		return s;
	}
	
	/**
	 * this method operates all the branches and the trucks and performs one unit works
	 * and add package every five beats
	 */
	public void tick() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(clockString());
		clock++;
//		if (clock++%5==0 && maxPackages>0) {
//			addPackage();
//			maxPackages--;
//		}
		/*
		 * branchWork(hub); for (Branch b:hub.getBranches()) { branchWork(b); }
		 */

		panel.repaint();
	}


	/**
	 * launch the branch work 
	 * @param b
	 */
	public void branchWork(Branch b) {
		for (Truck t : b.listTrucks) {
			t.work();
		}
		b.work();
	}

	/**
	 * add hub to the main office
	 * @param trucksForBranch
	 */
	public void addHub(int trucksForBranch) {
		hub=new Hub();
		for (int i=0; i<trucksForBranch; i++) {
			Truck t = new StandardTruck();
			hub.addTruck(t);
		}
		Truck t=new NonStandardTruck();
		hub.addTruck(t);
	}
	

	/**
	 * add branch to the the main office
	 * @param branches
	 * @param trucks
	 */
	public void addBranches(int branches, int trucks) {
		for (int i=0; i<branches; i++) {
			Branch branch=new Branch();
			for (int j=0; j<trucks; j++) {
				branch.addTruck(new Van());
			}
			hub.add_branch(branch);		
		}
	}


	/**
	 * set suspend method
	 */
	public synchronized void setSuspend() {
		threadSuspend = true;
		for (Customer c : customer)
			c.setSuspend();

		for (Truck t : hub.listTrucks) {
			t.setSuspend();
		}
		for (Branch b: hub.getBranches()) {
			for (Truck t : b.listTrucks) {
				t.setSuspend();
			}
			b.setSuspend();
		}
		hub.setSuspend();
	}
	
	/**
	 * the property change method
	 */
	public void propertyChange(PropertyChangeEvent evt) 
	{	
		w.Write((String)evt.getNewValue());
	}

	/**
	 * set resume method
	 */
	public synchronized void setResume() {
		threadSuspend = false;
		for (Customer c : customer)
			c.setResume();
		notify();
		hub.setResume();
		for (Truck t : hub.listTrucks) {
			t.setResume();
		}
		for (Branch b: hub.getBranches()) {
			b.setResume();
			for (Truck t : b.listTrucks) {
				t.setResume();
			}
		}
	}
	
	/**
	 * set packages list method
	 * @param packages
	 */
	public static void setPackages(ArrayList<Package> packages) {
		MainOffice.packages = packages;
	}
	
	/**
	 * get packages list method
	 * @return
	 */
	public static ArrayList<Package> getPackages(){
		return MainOffice.packages;
	}
	
	/**
	 * get customer method
	 * @return
	 */
	public Customer[] getCustomer() {
		return customer;
	}
	
	/**
	 * set customer method
	 * @param customer
	 */
	public void setCustomer(Customer[] customer) {
		this.customer = customer;
	}
	
	/**
	 * get w method
	 * @return
	 */
	public static WriterTracking getW() {
		return w;
	}
	
	/**
	 * set w method
	 * @param w
	 */
	public static void setW(WriterTracking w) {
		MainOffice.w = w;
	}

	/**
	 * this method check if all the customers end the simulation
	 * @return
	 */
	private boolean isCustomersFinished() {
		int i = 0;
		for (Customer c : customer) {
			if (c.isIsFinishes()) {
				i++;
			}
		}
		return i == customer.length;
	}
	
	/**
	 * get RW method
	 * @return
	 */
	public ReadWriteLock getRW() {
		return RW;
	}

	/**
	 * set RW method
	 * @param rW
	 */
	public void setRW(ReadWriteLock rW) {
		RW = rW;
	}
	
	/**
	 * create memento method
	 * @return
	 */
	public Memento createMemento() { 
		return new Memento(this); 
	} 
	
	
//	public void InitializeCustomers()
//	{
//		e = Executors.newFixedThreadPool(2); 
//		
//		
//		for(int i=0; i < 10 ;i++)
//		{
//			this.customer.add(i, (new Customer()) );
//			e.execute( this.customer.get(i) );
//		}
//		
//	}
	
	/**
	 * set memnto method
	 * @param memento
	 */
	public void setMemento(Memento memento) { 
		clock = memento.getClock();
		this.packages = new ArrayList<Package>(memento.getPack());
		int i=0;
		for(Customer c : memento.getCust())
		{
			this.customer[i] = new Customer(c);
		}
		this.hub = new Hub(memento.getH());
		this.counter = memento.getCounter();
		this.maxPackages = memento.getMaxPac();
		//panel = new PostSystemPanel(memento.getPanel());
//		panel.add(memento.getPanel());
	}
	
}
