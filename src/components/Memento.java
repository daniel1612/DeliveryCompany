package components;

import java.util.ArrayList;

import javax.swing.JPanel;

import program.PostSystemPanel;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 *
 */
/**@version 09.05.2021  
 * Memento class
 */
public class Memento 
{
	/**
	 * @param clock - int
	 * @param Pack - ArrayList<Package>
	 * @param h - Hub
	 * @param Panel - PostSystemPanel
	 * @param counter - int
	 * @param maxPac - int
	 */
	private int clock;
	private ArrayList<Package> Pack;
	private Customer[] Cust = new Customer[2];
	private  Hub h;
	private PostSystemPanel Panel; 
	private int counter;
	private int maxPac;
	
	/**
	 * constructor that get an instance of a main office
	 * @param state
	 */
	public Memento(MainOffice state)
	{
		clock = state.getClock();
		h = new Hub(state.getHub());
		Pack = new ArrayList<Package>(state.getPackages());
		int i=0;
		for(Customer c : state.getCustomer())
		{
			Cust[i] = new Customer(c);
			i++;
		}
		counter = state.counter;
		//Panel = new PostSystemPanel(state.getPanel());
//		Panel.add(state.getPanel());
	}


	/**
	 * get clock method
	 * @return
	 */
	public int getClock() {
		return clock;
	}

	/**
	 * set clock method
	 * @param clock
	 */
	public void setClock(int clock) {
		this.clock = clock;
	}
	
	/**
	 * get package list method
	 * @return
	 */
	public ArrayList<Package> getPack() {
		return Pack;
	}
	
	/**
	 * set package list method
	 * @param pack
	 */
	public void setPack(ArrayList<Package> pack) {
		Pack = pack;
	}
	
	/**
	 * get customer list array method
	 * @return
	 */
	public Customer[] getCust() {
		return Cust;
	}
//
//	public void setCust(ArrayList<Customer> cust) {
//		Cust = cust;
//	}
	
	/**
	 * get hub method
	 * @return
	 */
	public Hub getH() {
		return h;
	}

	/**
	 * set hub method
	 * @param h
	 */
	public void setH(Hub h) {
		this.h = h;
	}

	/**
	 * get panel method
	 * @return
	 */
	public PostSystemPanel getPanel() {
		return Panel;
	}

	/**
	 * set panel method
	 * @param panel
	 */
	public void setPanel(PostSystemPanel panel) {
		Panel = panel;
	}

	/**
	 * get counter method
	 * @return
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * set counter
	 * @param counter
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}


	/**
	 * get MaxPac method
	 * @return
	 */
	public int getMaxPac() {
		return maxPac;
	}


	/**
	 * set MaxPac method
	 * @param maxPac
	 */
	public void setMaxPac(int maxPac) {
		this.maxPac = maxPac;
	}
	
	
	
}
