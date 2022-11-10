package components;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 *
 */
/**@version 09.05.2021  
 *	this class describe a customer
 */
public class Customer implements Runnable
{
	/**
	 * @param count - static int
	 * @param addressCus - Address
	 * @param pac - ArrayList<Package>
	 * @param IsFinishes - boolean
	 * @param threadSuspend - boolean
	 * @param id - int
	 */
	private static int count=0;
	Address addressCus;
	ArrayList<Package> pac = new ArrayList<Package>();
	private boolean IsFinishes;
	private boolean threadSuspend = false;
	private int id;

	/**
	 * default constructor
	 */
	public Customer() {
		Random r = new Random();
		addressCus = new Address(r.nextInt(MainOffice.getHub().getBranches().size()), r.nextInt(999999)+100000);
		id = count;
		count++;
		
//		for(int i=0;i<5;i++) {
//			Random rand = new Random();
//			//קורע את המחשב 4000
//			//int time=rand.nextInt(400)+200;
//			int time= 300;
//			//			System.out.println(time+"---->daniel");
//			//addPackages();
//			try {
//				Thread.sleep(time);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

	}


	/**
	 * copy constructor
	 * @param other
	 */
	public Customer(Customer other)
	{
		this.addressCus = new Address(other.addressCus);
		this.IsFinishes = other.IsFinishes;
		this.pac = new ArrayList<Package>(other.pac);
		this.id = other.id;
		this.threadSuspend = other.threadSuspend;
	}
	

	/**
	 * add package method
	 */
	void addPackages() {
		Random r = new Random();
		Package p;
		Branch br;
		Priority priority=Priority.values()[r.nextInt(3)];

		Address sender=addressCus;
		Address dest = new Address(r.nextInt(MainOffice.getHub().getBranches().size()), r.nextInt(999999)+100000);

		switch (r.nextInt(3)){
		case 0:
			p = new SmallPackage(priority,  sender, dest, r.nextBoolean());
			br = MainOffice.getHub().getBranches().get(sender.zip);
			br.addPackage(p);
			p.setBranch(br); 
			break;
		case 1:
			p = new StandardPackage(priority,  sender, dest, r.nextFloat()+(r.nextInt(9)+1));
			br = MainOffice.getHub().getBranches().get(sender.zip); 
			br.addPackage(p);
			p.setBranch(br); 
			break;
		case 2:
			p=new NonStandardPackage(priority,  sender, dest,  r.nextInt(1000), r.nextInt(500), r.nextInt(400));
			MainOffice.getHub().addPackage(p);
			break;
		default:
			p=null;
			return;
		}
		
		p.setCustId(this.id);
		MainOffice.getPackages().add(p);
		pac.add(p);

	}


	/**
	 * run method
	 */
	@Override
	public void run() {
		synchronized (this) {
			while (threadSuspend) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		for (int i = 0; i < 5; i++) {
			synchronized (this) {
				while (threadSuspend) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			addPackages();
			try {
				Thread.sleep((new Random()).nextInt(5000 - 2000) + 2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			while (!isIsFinishes()) {
				Thread.sleep(10000);
				if (!this.IsFinishes) {
					if (ReturnedDeliver() == 5) {
						setIsFinishes(true);
						return;
					}
				}
				synchronized (this) {
					while (threadSuspend) {
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * set suspend method
	 */
	public synchronized void setSuspend()
	{
		
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
	 * IsFinishes method
	 * @return
	 */
	public boolean isIsFinishes() {
		return IsFinishes;
	}

	/**
	 * set IsFinishes method
	 * @param isFinishes
	 */
	public void setIsFinishes(boolean isFinishes) {
		IsFinishes = isFinishes;
	}

	/**
	 * read from the txt file and check if all the packages delivered
	 * @return
	 */
	public int ReturnedDeliver()
	{
		MainOffice.getInstance(0, 0, null, 0).getRW().readLock().lock();
		int deliveredNumber = 0;
		FileInputStream trackingIn = null;
		String line;

		try {
			trackingIn = new FileInputStream(WriterTracking.getFileName());       
			Scanner scan = new Scanner(trackingIn);
			while(scan.hasNextLine()) {  
				line = scan.nextLine();
				if (line.contains(" Customer num: " + this.id) && line.contains(Status.DELIVERED.toString())) {
					++deliveredNumber;
				}	
			}
			scan.close();
			trackingIn.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		MainOffice.getInstance(0, 0, null, 0).getRW().readLock().unlock();
		System.out.println("Count = " + deliveredNumber);
		return deliveredNumber;
	}

}

