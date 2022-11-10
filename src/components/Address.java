package components;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 *
 */
/**@version 09.05.2021  
 * Address (of the sender or of the recipient). The address consists of two integers, separated by a line.
 */

public class Address {
	
	/**
	 * Address (of the sender or of the recipient). The address consists of two integers, separated by a line.
	 *  The first number (zip) determines the branch to which the address belongs, the second number (street) the street number.
	 *  @param zip - Branch number to which the address belongs.
	 *  @param street - Street number, able to get number with maximum 6 digits.
	 */
	
	public final int zip;
	public final int street;
	
	/**
	 * Construct and initializes a Address object with params zip and street.
	 * @param zip - zip represent the  branch number
	 * @param street - street represent the  branch street
	 */
	
	public Address(int zip, int street ) {
		this.zip=zip;
		this.street=street;
	}

	
	/**
	 * Copy Constractor.
	 * @param other - Adrres
	 */
	public Address(Address Add)
	{
		this.street = Add.street;
		this.zip = Add.zip;
	}
	
	/**
	 * Get Method that return the street param
	 * @return street
	 */

	public int getZip() {
		return zip;
	}

	/**
	 * Set Method that update the street param
	 * @param street - int 
	 */
	public int getStreet() {
		return street;
	}
	
	/**
	 * toString Method that convert the object to a String
	 */
	@Override
	public String toString() {
		return zip + "-" + street;
	}	

	
	/**
	 * equals Method that get another object and return true if they are equal and false otherwise. 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (street != other.street)
			return false;
		if (zip != other.zip)
			return false;
		return true;
	}
}
