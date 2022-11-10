package components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;



/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * * @version 09.05.2021  
 * @see  Hub

 */

public class Branch extends Observable implements Node, Runnable,Cloneable{
	
	

	/**
	 * Describes a local branch. Maintains a list of packages stored at the branch or intended for collection from the sender's
	 *  address to this branch,and a list of vehicles that collect the packages from
	 *   the sending customers and deliver the packages to the receiving customers.
	 *   @param branchId - Represents the number of the Branch
	 *   @param branchName - Represents the name of the Branch
	 *   @param listTrucks - A collection of Truck belonging to this branch
	 *   @param listPackages - A collection of Package that are in the branch and packages that must be collected are shipped by this Branch.
	 *   @param branchCounter - Static integer that count every new object of Branch 
	 *   @param hubPoint - Point that point to the hub location
	 *   @param branchPoint - Point that point to the branch location
	 *   @param threadSuspend - boolean , true if the system suspend. false otherwise
	 *   
	 */
	
	private static int counter=0;
	private int branchId;
	private String branchName;
	protected ArrayList <Package> unsafeListPackages = new ArrayList<Package>();
	protected List<Package> listPackages = unsafeListPackages; //Collections.synchronizedList(unsafeListPackages);
	protected ArrayList <Truck> listTrucks = new ArrayList<Truck>();
	private Point hubPoint;
	private Point branchPoint;
	protected boolean threadSuspend = false;

	
	
	/**
	 * Default Constructor, calculates the serial number of the branch and creates the name of the branch, 
	 * the two remaining fields are initialized to blank collections.
	 */
	public Branch() {
		this("Branch "+counter);
	}

	/**
	 * A Constructor who gets a branch name, calculates the serial number of the branch, 
	 * and the two remaining fields are initialized to empty collections.
	 * @param branchName - String
	 */
	public Branch(String branchName) {
		this.branchId=counter++;
		this.branchName=branchName;
		System.out.println("\nCreating "+ this);
	}

	
	/**
	 * A Constructor who gets a branch name, calculates the serial number of the branch, 
	 * and the two remaining fields are initialized to empty collections.
	 * @param branchName - String
	 * @param plist - Package[]
	 * @param tlist - Truck[]
	 */
	public Branch(String branchName, Package[] plist, Truck[] tlist) {
		this.branchId=counter++;
		this.branchName=branchName;
		addPackages(plist);
		addTrucks(tlist);
	}

	
	/**
	 * A Constructor who gets a branch calculates the serial number of the branch, 
	 * and the two remaining fields are initialized to empty collections.
	 * @param other - Branch
	 */
	public Branch(Branch other)
	{
		this.branchId = other.branchId;
		this.branchName = new String(other.branchName);
		if(other.branchPoint!=null) this.branchPoint = new Point(other.branchPoint);
		if(other.hubPoint!=null) this.hubPoint = new Point(other.hubPoint);
		this.listPackages = new ArrayList<Package>(other.listPackages);
		this.listTrucks = new ArrayList<Truck>(other.listTrucks);
		this.threadSuspend = other.threadSuspend;
		//this.counter = other.counter;
		
	}

	/**
	 * synchronized method that return the listPackage
	 * @return List <Package>
	 */
	public synchronized List <Package> getPackages(){
		return this.listPackages;
	}
	
	
	/**
	 * this method print the branch details
	 */
	public void printBranch() {
		System.out.println("\nBranch name: "+branchName);
		System.out.println("Packages list:");
		for (Package pack: listPackages)
			System.out.println(pack);
		System.out.println("Trucks list:");
		for (Truck trk: listTrucks)
			System.out.println(trk);
	}


	/**
	 * addpackage method
	 * @param pack
	 */
	public synchronized void addPackage(Package pack) {
		listPackages.add(pack);
	}

	/**
	 * get Truck list method
	 * @return
	 */
	public ArrayList <Truck> getTrucks(){
		return this.listTrucks;
	}

	/**
	 * add Truck method
	 * @param trk
	 */
	public void addTruck(Truck trk) {
		listTrucks.add(trk);
	}

	/**
	 * get hub point method
	 * @return
	 */
	public Point getHubPoint() {
		return hubPoint;
	}
	
	/**
	 * set branch point method 
	 * @param branchPoint
	 */
	public void setBranchPoint(Point branchPoint) {
		this.branchPoint = branchPoint;
	}
	
	/**
	 * get branch point method
	 * @return
	 */
	public Point getBranchPoint() {
		return branchPoint;
	}

	
	/**
	 * add package list method
	 * @param plist
	 */
	public synchronized void addPackages(Package[] plist) {
		for (Package pack: plist)
			listPackages.add(pack);
	}


	/**
	 * add truck list method
	 * @param tlist
	 */
	public void addTrucks(Truck[] tlist) {
		for (Truck trk: tlist)
			listTrucks.add(trk);
	}


	/**
	 * get branch id method
	 * @return
	 */
	public int getBranchId() {
		return branchId;
	}


	/**
	 * get branch name method
	 */
	public String getName() {
		return branchName;
	}

	/**
	 * branch to string method
	 * @return String
	 */
	@Override
	public String toString() {
		return "Branch " + branchId + ", branch name:" + branchName + ", packages: " + listPackages.size()
		+ ", trucks: " + listTrucks.size();
	}

	/**
	 * collect package method
	 * @param Package
	 */
	@Override
	public synchronized void  collectPackage(Package p) {
		for (Truck v : listTrucks) {
			if (v.isAvailable()) {
				synchronized(v) {
					v.notify();
				}
				v.collectPackage(p);
				
				return;
			}
		}
	}

	/**
	 * deliver package method
	 * @param Package
	 */
	@Override
	public synchronized void deliverPackage(Package p) {
		for (Truck v : listTrucks) {
			if (v.isAvailable()) {
				synchronized(v) {
					v.notify();
				}
				v.deliverPackage(p);
				return;
			}
		}	
	}

	@Override
	public void work() {	
		/*for (Package p: listPackages) {
			if (p.getStatus()==Status.CREATION) {
				collectPackage(p);
			}
			if (p.getStatus()==Status.DELIVERY) {
				deliverPackage(p);
			}
		}*/	
	}


	/**
	 * check if package in branch 
	 * @return boolean
	 */
	private boolean arePackagesInBranch() {
		for(Package p: listPackages) {
			if (p.getStatus() == Status.BRANCH_STORAGE)
				return true;
		}
		return false;
	}

	/**
	 * paint the branch to the screen
	 * @param g
	 * @param y
	 * @param y2
	 */
	public void paintComponent(Graphics g, int y, int y2) {
		if (arePackagesInBranch())
			g.setColor(new Color(0,0,153));
		else
			g.setColor(new Color(51,204,255));
		g.fillRect(20, y, 40, 30);

		g.setColor(new Color(0,102,0));
		g.drawLine(60, y+15, 1120, y2);
		branchPoint = new Point(60,y+15);
		hubPoint = new Point(1120,y2);
	}

//	@Override
//	public void run() {
//		while(true) {
//			synchronized(this) {
//				while (threadSuspend)
//					try {
//						wait();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			}
//			synchronized(this) {
//				for (Package p: listPackages) {
//					if (p.getStatus()==Status.CREATION) {
//						collectPackage(p);
//					}
//					if (p.getStatus()==Status.DELIVERY) {
//						deliverPackage(p);
//					}
//				}
//			}
//		}
//	}
	
	/**
	 * run method
	 */
	@Override
	public void run() {
		while(true) {
			synchronized(this) {
				while (threadSuspend)
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			synchronized(this) {
				for(int i=0;i<listPackages.size();i++) {
					if (listPackages.get(i).getStatus()==Status.CREATION) {
						collectPackage(listPackages.get(i));
					}
					if (listPackages.get(i).getStatus()==Status.DELIVERY) {
						deliverPackage(listPackages.get(i));
					}
				}
			}
		}
	}


//
//	public Object clone() {
//		Object clone = null;
//		try {
//			clone = (Branch)super.clone();
//			changeBranch(clone);
//			
//			
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		return clone;
//	}	
	
	
	

	public Branch clone() {
		Branch clone = null;
		try {
			clone = (Branch)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}	
//	
//
//	public Branch clone() {
//		Branch b = new Branch();
//		for(int i=0;i<getTrucks().size();i++) {
//			b.getTrucks().add(new Van());
//		}
//		return b;
//	}	
	
	public void changeBranch() {
		int temp=getBranchId();
		setBranchId(counter++);
		
		Truck t = listTrucks.get(0);
		int size = listTrucks.size();
		listTrucks = new ArrayList<Truck>();
		
		for(int i=0;i<size;i++) {
			Truck t2 = t.clone();
			t2.prototypeUpdate();
			listTrucks.add(t2);
			new Thread(t2).start();			
		}
		
		
	}
	
	
	public synchronized void setSuspend() {
		threadSuspend = true;
	}

	public synchronized void setResume() {
		threadSuspend = false;
		notify();
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
	
	
}
