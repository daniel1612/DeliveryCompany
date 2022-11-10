package components;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;


import program.PostSystemPanel;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @version 09.05.21
 * @see SmallPackage -- StandardPackage -- NonStandardPackage
 */
public abstract class Package
{
	
	/**
	 * A general type represents packages
	 * @param packageID - Package ID number. Package numbering starts from 1000.
	 * @param priority - priority.
	 * @param status - currently status
	 * @param senderAddress - Address of the sender
	 * @param destinationAddress - Address of the destination
	 * @param tracking - Collection of records with transfer history (objects of the Tracking Department). 
	 * The creation of the package is initialized as an empty collection and the first object 
	 * relating to the creation of the package is immediately added to it. In each of its transfer operations a package 
	 * is added to this collection and another object.
	 *  At the end of the program run for each package its transfer history is printed based on this collection
	 *  @param PackageCounter - static int that count every Package object that creating.
	 *  @param branch - Branch
	 *  @param sendPoint - Point
	 *  @param destPoint - Point
	 *  @param bInPoint - Point
	 *  @param bOutPoint - Point
	 *  @param support - PropertyChangeSupport
	 *  @param CustId - int
	 */
	private static int countID=1000;
	final private int packageID;
	private Priority priority;
	private Status status;
	private Address senderAddress;
	private Address destinationAddress;
	private ArrayList<Tracking> tracking = new ArrayList<Tracking>();
	private Branch branch = null;
	private Point sendPoint;
	private Point destPoint;
	private Point bInPoint;
	private Point bOutPoint;
	private PropertyChangeSupport support;
	private int CustId;

	/**
	 * A Constructor who accepts as arguments arguments, addresses the sender and receives a package.
	 * @param priority- Priorty of the package
	 * @param senderAddress - the Address of the sender Address
	 * @param destinationAdress - the Address of the destination Address
	 */
	public Package(Priority priority, Address senderAddress,Address destinationAdress) {
		support = new PropertyChangeSupport(this);
		packageID = countID++;
		this.priority=priority;
		this.status=Status.CREATION;
		this.senderAddress=senderAddress;
		this.destinationAddress=destinationAdress;
		addPropertyChangeListener(MainOffice.getInstance(countID, countID, null, packageID));
		addTracking(new Tracking(MainOffice.getClock(), getBranch(), status));

	}	
	
	/**
	 * copy constructor
	 * @param P
	 */
	public Package(Package P)
	{
		this.bInPoint = new Point(P.bInPoint);
		this.bOutPoint = new Point(P.bOutPoint);
		this.sendPoint = new Point(P.sendPoint);
		this.destPoint = new Point(P.destPoint);
		this.priority = P.priority;
		this.packageID = P.packageID;
		this.branch = new Branch(P.branch);
		this.status = P.status;
		this.destinationAddress = new Address(P.destinationAddress);
		this.senderAddress =  new Address(P.senderAddress);
		this.tracking = new ArrayList<Tracking>(P.tracking);
		
	}

	/**
	 * set branch method
	 * @param branch
	 */
	public void setBranch(Branch branch) { 
		this.branch = branch;
	}

	/**
	 * get branch method 
	 * @return
	 */
	public Branch getBranch() {
		return this.branch;
	}

	/**
	 * get priority method
	 * @return
	 */
	public Priority getPriority() {
		return priority;
	}


	/**
	 * set priority method
	 * @param priority
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * get status method
	 * @return
	 */
	public Status getStatus() {
		return status;
	}


	/**
	 * set status method
	 * @param status
	 */
	public void setStatus(Status status) 
	{
		this.status = status;	
	}

	/**
	 * get package id method
	 * @return
	 */
	public int getPackageID() { 
		return packageID;
	}


	/**
	 * get sender address method
	 * @return
	 */
	public Address getSenderAddress() {
		return senderAddress;
	}

	/**
	 * set sender address method
	 * @param senderAddress
	 */
	public void setSenderAddress(Address senderAddress) {
		this.senderAddress = senderAddress;
	}

	/**
	 * get destination address method
	 * @return
	 */
	public Address getDestinationAddress() {
		return destinationAddress;
	}

	/**
	 * set destination method
	 * @param destinationAdress
	 */
	public void setDestinationAddress(Address destinationAdress) {
		this.destinationAddress = destinationAdress;
	}

	/**
	 * add tracking to list method
	 * @param node
	 * @param status
	 */
	public void addTracking(Node node, Status status) {
		tracking.add(new Tracking(MainOffice.getClock(), node, status));
		String line = "PackageID: "+packageID+" ,"+status + " Customer num: " + this.CustId;
		support.firePropertyChange(line, this, line);

	}

	/**
	 * add tracking to list method
	 * @param t
	 */
	public void addTracking(Tracking t) {
		tracking.add(t);
		String line = "PackageID: "+packageID+" ,"+t.toString() + " Customer num: " + this.CustId;
		support.firePropertyChange(line, this, line);
	}
	
	
	/**
	 * get tracking list method
	 * @return
	 */
	public ArrayList<Tracking> getTracking() {
		return tracking;
	}

	/**
	 * print tracking method
	 */
	public void printTracking() {
		for (Tracking t: tracking)
			System.out.println(t);
	}

	/**
	 * to string method
	 */
	@Override
	public String toString() {
		return "packageID=" + packageID + ", priority=" + priority + ", status=" + status + ", startTime="
				+ ", senderAddress=" + senderAddress + ", destinationAddress=" + destinationAddress;
	}
	
	/**
	 * get send point method 
	 * @return
	 */
	public Point getSendPoint() {
		return sendPoint;
	}
	
	/**
	 * get dest point method
	 * @return
	 */
	public Point getDestPoint() {
		return destPoint;
	}
	
	/**
	 * get binpoint method
	 * @return
	 */
	public Point getBInPoint() {
		return bInPoint;
	}
	
	/**
	 * get boutpoint method
	 * @return
	 */
	public Point getBOutPoint() {
		return bOutPoint;
	}
	
	
	/**
	 * paint the package to the screen
	 * @param g
	 * @param x
	 * @param offset
	 */
	public void paintComponent(Graphics g, int x, int offset) {
		if (status==Status.CREATION || (branch==null && status == Status.COLLECTION))
			g.setColor(new Color(204,0,0));
		else
			g.setColor(new Color(255,180,180));
		g.fillOval(x, 20, 30, 30);

		if (status==Status.DELIVERED)
			g.setColor(new Color(204,0,0));
		else
			g.setColor(new Color(255,180,180));
		g.fillOval(x, 583, 30, 30);


		if (branch!=null) {
			g.setColor(Color.BLUE);
			g.drawLine(x+15,50,40,100+offset*this.senderAddress.getZip());
			sendPoint = new Point(x+15,50);
			bInPoint = new Point(40, 100+offset*this.senderAddress.getZip());
			g.drawLine(x+15,583,40,130+offset*this.destinationAddress.getZip());
			destPoint = new Point(x+15,583);
			bOutPoint = new Point(40,130+offset*this.destinationAddress.getZip());

		}
		else {
			g.setColor(Color.RED);
			g.drawLine(x+15,50,x+15,583);
			g.drawLine(x+15,50,1140, 216);
			sendPoint = new Point(x+15,50);
			destPoint = new Point(x+15,583);

		}
	}


	public String GetTrackingToString()
	{
		return MainOffice.getPackages().indexOf(this) + 1 + ") " +  "Package: " + packageID + " Status: " + status;
	}
	
	
	/**
	 * add property change listener method 
	 * @param pcl
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl)
	{
		support.addPropertyChangeListener(pcl); 
	} 
	
	/**
	 * remove property change listener method 
	 * @param pcl
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl)
	{
		support.removePropertyChangeListener(pcl); 
	}

	/**
	 * get support method
	 * @return
	 */
	public PropertyChangeSupport getSupport() {
		return support;
	}
	
	/**
	 * set support method
	 * @param support
	 */
	public void setSupport(PropertyChangeSupport support) {
		this.support = support;
	}
	
	/**
	 * get customer id method
	 * @return
	 */
	public int getCustId() {
		return CustId;
	}

	/**
	 * set customer id method
	 * @param custId
	 */
	public void setCustId(int custId) {
		CustId = custId;
	} 



}
