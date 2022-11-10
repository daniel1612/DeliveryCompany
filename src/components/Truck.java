package components;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import javax.sound.midi.Track;

import program.PostSystemPanel;

/**
 * Class Truck representing the vehicles for transporting packages.
 * * @version 09.05.2021  
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @see    Van  StandardTruck NonStandardTruck
 */
public  abstract class Truck extends Observable implements Node,Runnable,Cloneable{
	/** @param truckID - Serial number of vehicle, vehicle numbering starts from 2000.
	 * @param licensePlate - Vehicle ID number
	 * @param truckModel - Model of the Truck
	 * @param avaliable - Truck availability (available for transportation, 
	 * the field gets a false value when the vehicle goes to collect / deliver packages)
	 * @param timeLeft - Time left until the end of the transport
	 * @param packages - list of the packages in the truck
	 * @param truckCounter - count every creating object
	 * @param threadSuspend - boolean
	 */
	private static int countID=2000;
	private int truckID;
	private String licensePlate;
	private String truckModel;
	private boolean available=true;
	private int timeLeft=0;
	private ArrayList<Package> packages=new ArrayList<Package>();
	protected int initTime;
	protected boolean threadSuspend = false;
	
	
	/**
	 * A random default Costructor that produces an object with a vehicle license plate and model at random.
	 * A model consists of hundreds of M and a number between 0 and 4
	 */
	public Truck() {
		truckID=countID++;
		Random r= new Random();
		licensePlate=(r.nextInt(900)+100)+"-"+(r.nextInt(90)+10)+"-"+(r.nextInt(900)+100);
		truckModel="M"+r.nextInt(5);
		//addObserver(MainOffice.getInstance(initTime, truckID, null, countID));

	}
	/**
	 * A Constructor who receives as arguments a number plate and model of the vehicle and produces an object.
	 * @param licensePlate - String
	 * @param truckModel - String 
	 */
	public Truck(String licensePlate,String truckModel) {
		truckID=countID++;
		this.licensePlate=licensePlate;
		this.truckModel=truckModel;
	}

	/**
	 * copy constructor
	 * @param other
	 */
	public Truck(Truck other)
	{
		synchronized (this) 
		{
			this.available = other.available;
			this.initTime = other.initTime;
			this.licensePlate = new String(other.licensePlate);
			this.threadSuspend = other.threadSuspend;
			this.timeLeft = other.timeLeft;
			this.truckID = other.truckID;
			this.packages = new ArrayList<Package>(other.packages);
			this.truckModel = new String(other.truckModel);
			countID = other.getCountID();
		}

	}

	/**
	 * get package list method
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}

	/**
	 * get time left method
	 * @return
	 */
	public int getTimeLeft() {
		return timeLeft;
	}

	/**
	 * set time left method
	 */
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}


	/**
	 * to string method
	 */
	@Override
	public String toString() {
		return "truckID=" + truckID + ", licensePlate=" + licensePlate + ", truckModel=" + truckModel + ", available= " + available ;
	}

	/**
	 * get count id method
	 * @return
	 */
	public static int getCountID() {
		return countID;
	}
	/**
	 * set count id method
	 * @param countID
	 */
	public static void setCountID(int countID) {
		Truck.countID = countID;
	}

	/**
	 * collect package method
	 */
	@Override
	public synchronized void collectPackage(Package p) {
		setAvailable(false);
		int time=(p.getSenderAddress().street%10+1)*10;
		this.setTimeLeft(time);
		this.initTime = time;
		this.packages.add(p);
		p.setStatus(Status.COLLECTION);
		Tracking tracking= new Tracking(MainOffice.getClock(), this, p.getStatus());
		p.addTracking(tracking);
		//notifyObservers(tracking.toString());
		System.out.println(getName() + " is collecting package " + p.getPackageID() + ", time to arrive: "+ getTimeLeft());
	}

	/**
	 * deliver package method
	 */
	@Override
	public synchronized void deliverPackage(Package p) {}



	/**
	 * clone truck method
	 */
	public Truck clone() {
		Truck clone = null;
		try {
			clone = (Truck)super.clone();


		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}	

	/**
	 * prototype update method 
	 */
	public void prototypeUpdate()
	{
		setTruckID(countID++);
		Random r= new Random();
		licensePlate=(r.nextInt(900)+100)+"-"+(r.nextInt(90)+10)+"-"+(r.nextInt(900)+100);
		truckModel="M"+r.nextInt(5);
		setTimeLeft(0);
		setAvailable(true);
		packages.clear();
	}

	/**
	 * get is available method
	 * @return
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * set truck id method
	 * @param truckid
	 */
	public void setTruckID(int truckid)
	{
		truckID=truckid;
	}
	
	/**
	 * get truck id method
	 * @return
	 */
	public int getTruckID() {
		return truckID;
	}

	/**
	 * set available method
	 * @param available
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * get name method
	 */
	public String getName() {
		return this.getClass().getSimpleName()+" "+ truckID;
	}
	
	/**
	 * set suspend method
	 */
	public synchronized void setSuspend() {
		threadSuspend = true;
	}

	/**
	 * set resume method
	 */
	public synchronized void setResume() {
		threadSuspend = false;
		notify();
	}
	
	/**
	 * paintComponent method
	 * @param g
	 */
	public abstract void paintComponent(Graphics g);

}
