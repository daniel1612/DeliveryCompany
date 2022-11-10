package components;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * * @version 09.05.21
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

public class StandardTruck extends Truck{
	/**
	 * Truck for transporting packages from the sorting center to the branches and back. All vehicles of this type are in the sorting center
	 * @param maxWeight - int that represents the max weight that the StandardTruck able to load.
	 * @param  destination - Target Branch : local Branch/Hub
	 * @param source - Branch
	 */
	private int maxWeight;
	private Branch destination=null;
	private Branch source = null;
	
	/**
	 * A default constructor that produces an object with a vehicle license plate number and model at random.
	 */
	public StandardTruck() {
		super();
		maxWeight=((new Random()).nextInt(2)+2)*100;
		System.out.println("Creating "+ this);

	}
	
	/**
	 * copy constructor
	 * @param other
	 */
	public StandardTruck(StandardTruck other)
	{
		super(other);
		this.destination = new Branch(other.destination);
		this.source = new Branch(other.source);
		this.maxWeight = other.maxWeight;
	}
	
	/**
	 * Consructor that accepts as arguments: license plate number, vehicle model and maximum weight.
	 * @param licensePlate - String
	 * @param truckModel - String
	 * @param maxWeight - int 
	 */
	public StandardTruck(String licensePlate,String truckModel,int maxWeight) {
		super(licensePlate,truckModel);
		this.maxWeight=maxWeight;
	}
	
	/**
	 * get destination method
	 * @return
	 */
	public Branch getDestination() {
		return destination;
	}
	
	/**
	 * set destination method
	 * @param destination
	 */
	public void setDestination(Branch destination) {
		this.destination = destination;
	}
	/**
	 * Get method that return the field maxWeight
	 * @return maxWeight - int
	 */
	public int getMaxWeight() {
		return maxWeight;
	}

	/**
	 * set max weight
	 * @param maxWeight
	 */
	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}

	/**
	 * to string method
	 */
	@Override
	public String toString() {
		return "StandartTruck ["+ super.toString() +",maxWeight=" + maxWeight + "]";
	}
	
	/**
	 * unload packages at destination method
	 * @param dest
	 */
	public void unload (Branch dest) {
		Status status;
		synchronized(dest) {
			if (dest==MainOffice.getHub())
				status=Status.HUB_STORAGE;
			else 
				status=Status.DELIVERY;
			
			for (Package p: getPackages()) {
				p.setStatus(status);
				dest.addPackage(p);
				p.addTracking(dest, status);	
			}
			getPackages().removeAll(getPackages());
			System.out.println("StandardTruck " + getTruckID() + " unloaded packages at " + destination.getName());
		}
	}
	
	
	/**
	 * load package at destination method
	 * @param sender
	 * @param dest
	 * @param status
	 */
	public void load (Branch sender, Branch dest, Status status) {
		double totalWeight=0;
		synchronized(sender) {
			for (int i=0; i< sender.listPackages.size();i++) {
				Package p=sender.listPackages.get(i);
				if (p.getStatus()==Status.BRANCH_STORAGE || (p.getStatus()==Status.HUB_STORAGE && MainOffice.getHub().getBranches().get(p.getDestinationAddress().zip)==dest)) {
					if (p instanceof SmallPackage && totalWeight+1<=maxWeight || totalWeight+((StandardPackage)p).getWeight()<=maxWeight) {
						getPackages().add(p);
						sender.listPackages.remove(p);
						i--;
						p.setStatus(status);
						p.addTracking(this, status);
					}
				}
			}
			System.out.println(this.getName() + " loaded packages at " + sender.getName());
		}
	}
	
	/**
	 * run method
	 */
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    synchronized(this) {
                while (threadSuspend)
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    }
			if (!isAvailable()) {
				setTimeLeft(getTimeLeft()-1);
				if (getTimeLeft()==0) {
					System.out.println("StandardTruck "+ getTruckID()+ " arrived to " + destination.getName());
					unload(destination);
					if (destination==MainOffice.getHub()) {
						setAvailable(true);
					}
						
					else {
						load(destination, MainOffice.getHub(), Status.HUB_TRANSPORT);
						setTimeLeft(((new Random()).nextInt(6)+1)*10);
						this.initTime = this.getTimeLeft();
						source = destination;
						destination=MainOffice.getHub();
						System.out.println(this.getName() + " is on it's way to the HUB, time to arrive: "+ getTimeLeft());
					}			
				}
			}
			else
				synchronized(this) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
	}
	
	/**
	 * work method
	 */
	public void work() {

	}

	/**
	 * this method paint the StandardTruck to the screen
	 */
	@Override
	public void paintComponent(Graphics g) {
		Point start=null;
		Point end=null;
		Color col = new Color(102,255,102);
		if (this.getPackages()==null || destination==null) return;

		if (this.getPackages().size()==0) {
			if (destination!=MainOffice.getHub()) {
				end = this.destination.getBranchPoint();
				start = this.destination.getHubPoint();
			}
			else {
				start = this.source.getBranchPoint();
				end = this.source.getHubPoint();
			}
		}
		else {			
			Package p = this.getPackages().get(getPackages().size()-1);
			col = new Color(0,102,0);
			if (p.getStatus()==Status.HUB_TRANSPORT) {
				start = this.source.getBranchPoint();
				end = this.source.getHubPoint();
			}
			else if (p.getStatus()==Status.BRANCH_TRANSPORT){
				end = this.destination.getBranchPoint();
				start = this.destination.getHubPoint();
			}
		}
		
		
		if (start!=null) {
			int x2 = start.getX();
			int y2 = start.getY();
			int x1 = end.getX();
			int y1 = end.getY();
				
			double ratio = (double) this.getTimeLeft()/this.initTime;
			//System.out.println(x2+" "+x1+" "+ratio);
			double length = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			int dX = (int) (ratio*(x2-x1));
			int dY = (int) (ratio*(y2-y1));
				
			g.setColor(col);
			g.fillRect(dX+x1-8, dY+y1-8, 16, 16); 
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier", Font.BOLD,13));
			if (this.getPackages().size()>0)
				g.drawString(""+this.getPackages().size(), dX+x1-3, dY+y1-8-5);
			g.fillOval(dX+x1-12, dY+y1-12, 10, 10);
			g.fillOval(dX+x1, dY+y1, 10, 10);
			g.fillOval(dX+x1, dY+y1-12, 10, 10);
			g.fillOval(dX+x1-12, dY+y1, 10, 10);
		}
		
	}



	
}
