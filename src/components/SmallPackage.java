package components;
/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
*
*/

/**
* Represents a Small Package and extends from Package
* * @version 09.05.21
*/
public class SmallPackage extends Package {
	/** @param acknowledge - This field receives a true value if the package requires delivery confirmation after delivery 
	 * to the recipient.
	 */
	private boolean acknowledge;
	/**
	 * A Constructor who accepts as arguments and priority addresses sends and receives the package, and if delivery confirmation is required.
	 * @param priority - Priority
	 * @param senderAddress - Address
	 * @param destinationAdress - Address
	 * @param acknowledge -boolean
	 */
	
	public SmallPackage(Priority priority, Address senderAddress,Address destinationAdress, boolean acknowledge){
		super(priority, senderAddress,destinationAdress);
		this.acknowledge=acknowledge;
		System.out.println("Creating " + this);

	}
	
	/**
	 * copy constructor
	 * @param other
	 */
	public SmallPackage(SmallPackage other)
	{
		super(other);
		this.acknowledge = other.acknowledge;
	}
	
	/**
	 * Get method that return the field acknowledge  
	 * @return boolean
	 */
	public boolean isAcknowledge() {
		return acknowledge;
	}
	
	/**
	 * Set method that update the field acknowledge 
	 * @param acknowledge - boolean
	 */
	public void setAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
	}
	
	/**
	 * return String that represents the name of the Branch.
	 * @return this.branchName - String
	 */
	@Override
	public String toString() {
		return "SmallPackage ["+ super.toString() +", acknowledge=" + acknowledge + "]";
	}
	
		
}
