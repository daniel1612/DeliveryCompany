/**
 * 
 */
package components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @version 09.05.2021  
 */



public class NonStandardTruck extends Truck{
	/**
	 * A vehicle for transporting packages of non-standard size (exceptional cargo). All vehicles of this type are in the sorting center.
	 * @param width - The width of the Truck
	 * @param length - The length of the Truck
	 * @param height - The height of the Truck
	 */
	private int width, length, height;	


	/**
	 * A default Constructor that produces an object with a vehicle ID number and model at random.
	 */
	public NonStandardTruck() {
		super();
		Random r=new Random();
		width=(r.nextInt(3)+2)*100;
		length=(r.nextInt(6)+10)*100;
		height=(r.nextInt(2)+3)*100;
		System.out.println("Creating "+ this);
	}
	
	/**
	 * Constructor that accepts arguments: license plate number, vehicle model and maximum length / width / height of 
	 * cargo that the vehicle can carry.
	 * @param licensePlate - String
	 * @param truckModel - String 
	 * @param length - int 
	 * @param width - int 
	 * @param height - int 
	 */
	public NonStandardTruck(String licensePlate,String truckModel, int length, int width, int height) {
		super(licensePlate,truckModel);
		this.width=width;
		this.length=length;
		this.height=height;
	}
	
	/**
	 * copy constructor
	 * @param other
	 */
	public NonStandardTruck(NonStandardTruck other)
	{
		super(other);
		this.height = other.height;
		this.length = other.length;
		this.width = other.width;
	}

	/**
	 * Get method that return the field width
	 * @return width - int
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set method that update the field width
	 * @param width - int
	 */
	public void setWidth(int width) {
		this.width = width;
	}


	/**
	 * Get method that return the field length
	 * @return length - int
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Set method that update the field length
	 * @param length - int
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Get method that return the field height
	 * @return height - int
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set method that update the field height
	 * @param height - int
	 */
	public void setHeight(int height) {
		this.height = height;
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
			if (!this.isAvailable()) {
				Package p=this.getPackages().get(0);
				this.setTimeLeft(this.getTimeLeft()-1);
				if (this.getTimeLeft()==0) {
					if (p.getStatus()==Status.COLLECTION) {
						System.out.println("NonStandartTruck " + this.getTruckID() + "has collected package "+p.getPackageID());
						deliverPackage(p);
					}
						
					else {
						System.out.println("NonStandartTruck " + this.getTruckID() + "has delivered package "+p.getPackageID() + " to the destination");
						this.getPackages().remove(p);
						p.setStatus(Status.DELIVERED);
						p.addTracking(new Tracking(MainOffice.getClock(), null, p.getStatus()));
						setAvailable(true);
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
	@Override
	public void work() {

	}
	
	/**
	 * A method that handles the delivery of the package to the next person in the transfer chain
	 * @param p - Package
	 */
	@Override
	public synchronized void deliverPackage (Package p)  {
		int time=(Math.abs(p.getDestinationAddress().street-p.getDestinationAddress().street)%10+1)*10;
		this.setTimeLeft(time);
		this.initTime = time;
		p.setStatus(Status.DISTRIBUTION);
		p.addTracking(new Tracking(MainOffice.getClock(), this, p.getStatus()));
		System.out.println("NonStandartTruck "+ this.getTruckID() + " is delivering package " + p.getPackageID() + ", time left: "+ this.getTimeLeft()  );
	}
	
	/**
	 * toString method that convert the object to String
	 * @return String
	 */
	@Override
	public String toString() {
		return "NonStandardTruck ["+ super.toString() + ", length=" + length +  ", width=" + width + ", height="
				+ height + "]";
	}

	/**
	 * that method paint the NonStandardTruck to the screen
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (isAvailable()) return;
		Package p = this.getPackages().get(getPackages().size()-1);	
		Point start=null;
		Point end=null;
		Color col = null;
		if (p.getStatus()==Status.COLLECTION) {
			start = new Point(1140, 216);
			end = p.getSendPoint();
			col = new Color(255,180,180);
		}
		else if (p.getStatus()==Status.DISTRIBUTION) {
			start = p.getSendPoint();
			end = p.getDestPoint();
			col = Color.RED;
		}

		
		if (start!=null) {
			int x2 = start.getX();
			int y2 = start.getY();
			int x1 = end.getX();
			int y1 = end.getY();
				
			double ratio = (double) this.getTimeLeft()/this.initTime;
			double length = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			int dX = (int) (ratio*(x2-x1));
			int dY = (int) (ratio*(y2-y1));
				
			g.setColor(col);
			g.fillRect(dX+x1-8, dY+y1-8, 16, 16); 
			g.setColor(Color.BLACK);
			g.fillOval(dX+x1-12, dY+y1-12, 10, 10);
			g.fillOval(dX+x1, dY+y1, 10, 10);
			g.fillOval(dX+x1, dY+y1-12, 10, 10);
			g.fillOval(dX+x1-12, dY+y1, 10, 10);
		}
	}


	
}

