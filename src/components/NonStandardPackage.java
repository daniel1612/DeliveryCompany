package components;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @version 09.05.2021  
 */

/**
 * Represents Packages with NonStandard size
 */

public class NonStandardPackage extends Package {
	/** @param width -  The Package width
	 * @param length - The Package length
	 * @param height - The Package height
	 */
	private int width, length, height;	
	
	/**
	 * Constructor that accepts as arguments arguments, sender and recipient addresses, and package dimensions.
	 * @param priority - Priority
	 * @param senderAddress-Address
	 * @param destinationAdress -Address
	 * @param width - int 
	 * @param length - int 
	 * @param height - int 
	 */
	public NonStandardPackage(Priority priority, Address senderAddress,Address destinationAdress,int width, int length, int height) {
			super( priority, senderAddress,destinationAdress);
			this.width=width;
			this.length=length;
			this.height=height;	
			System.out.println("Creating " + this);
	}
	
	/**
	 * copy constructor
	 * @param other
	 */
	public NonStandardPackage(NonStandardPackage other)
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
	 * Boolean method that get another object and return true if they are equal, and false if not.
	 * @param obj - Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NonStandardPackage other = (NonStandardPackage) obj;
		if (height != other.height)
			return false;
		if (length != other.length)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	/**
	 * return String that represents the name of the NonStandardPackage.
	 * @return this.branchName - String
	 */
	@Override
	public String toString() {
		return "NonStandardPackage ["+super.toString() + ", width=" + width + ", length=" + length + ", height=" + height + "]";
	}
	
}
