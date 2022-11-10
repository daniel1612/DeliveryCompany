package components;

import java.util.ArrayList;
import java.util.Random;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @version 09.05.21
 */
public class Hub extends Branch{
	
	/** 
	 * Describe the main Branch. (this class extends from Class Branch)
	 */
	
	/** @param branches - Collection of objects of all local branches.
	 *  @param currentIndex - int
	 */
	private ArrayList<Branch> branches=new ArrayList<Branch>();
	private int currentIndex=0;
	
	/**
	 * default constructor
	 */
	public Hub() {
		super("HUB");
	}
	
	/**
	 * copy constructor
	 * @param other
	 */
	public Hub(Hub other) {
		super(other);
		this.branches = new ArrayList<Branch>(other.branches);
	}

	/**
	 * get branches method
	 * @return
	 */
	public ArrayList<Branch> getBranches() {
		return branches;
	}

	/**
	 * add branch to list method
	 * @param branch
	 */
	public void add_branch(Branch branch) {
		branches.add(branch);
	}
	
	/**
	 * send StandardTruck to the destination
	 * @param t
	 */
	public synchronized void sendTruck(StandardTruck t) {
		synchronized(t) {
			t.notify();
		}
		t.setAvailable(false);
		t.setDestination(branches.get(currentIndex));
		t.load(this, t.getDestination(), Status.BRANCH_TRANSPORT);
		t.setTimeLeft(((new Random()).nextInt(10)+1)*10);
		t.initTime = t.getTimeLeft();
		System.out.println(t.getName() + " is on it's way to " + t.getDestination().getName() + ", time to arrive: "+t.getTimeLeft());	
		currentIndex=(currentIndex+1)%branches.size();
	}
	
	/**
	 * ship NonStandardTruck to the destination
	 * @param t
	 */
	public synchronized void shipNonStandard(NonStandardTruck t) {
		for (Package p: listPackages) {
			if (p instanceof NonStandardPackage) {
				/*if (((NonStandardPackage) p).getHeight() <= t.getHeight() 
					&& ((NonStandardPackage) p).getLength()<=t.getLength()
					&& ((NonStandardPackage) p).getWidth()<=t.getWidth()){*/
						synchronized(t) {
							t.notify();
						}
						t.collectPackage(p);
						listPackages.remove(p);
						return;
					//}
			}
		}	
	}
	
	/**
	 * work method
	 */
	@Override
	public void work() {

	}
	
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
			for (Truck t : listTrucks) {
				if (t.isAvailable()){
					if(t instanceof NonStandardTruck) {
						shipNonStandard((NonStandardTruck)t);
					}
					else {
						sendTruck((StandardTruck)t);
					}
				}	
			}
		}
	}



}
