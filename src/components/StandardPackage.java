package components;
/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * * @version 09.05.21
 */
public class StandardPackage extends Package {
	/**
	 * Class represents packages with varying weights over one kilogram
	 * @param weight - Represents the weight of StandardPackage
	 */
	private double weight;
	
	/**
	 * Constructor that receives as priority arguments, sender and recipient addresses, package weight.
	 * @param priority - Priority
	 * @param senderAddress - Address
	 * @param destinationAdress - Address
	 * @param weight - double
	 */
	public StandardPackage(Priority priority, Address senderAddress, Address destinationAdress,double weight) {
		super( priority, senderAddress,destinationAdress);
		this.weight=weight;
		System.out.println("Creating " + this);
	}

	/**
	 * copy constructor
	 * @param other
	 */
	public StandardPackage(StandardPackage other)
	{
		super(other);
		this.weight = other.weight;
	}

	
	/**
	 * Get method that return the field weight
	 * @return weight - double
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Set method that update the field weight
	 * @param weight - double
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * return String that represents the name of the Branch.
	 * @return this.branchName - String
	 */
	@Override
	public String toString() {
		return "StandardPackage ["+ super.toString()+", weight=" + weight + "]";
	}
}
