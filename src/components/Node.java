package components;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @version 09.05.2021  
 */
public interface Node {
	
	/**
	 * Represents the location of a package, can refer to branches and trucks 
	 * (all points where the package can be in the various stages of its transfer). 
	 */

	//=========================================================================================

	/**
	 * A method that handles the collection / receipt of a package by the implementing department.
	 * @param p - Package
	 */
	public void collectPackage(Package p);
	
	/**
	 * A method that handles the delivery of the package to the next person in the transfer chain
	 * @param p - Package
	 */
	public void deliverPackage(Package p);
	/**
	 * A method that performs a work unit.
	 */
	public void work();
	/**
	 * This method return the name of the object that implements this node
	 * @return String
	 */
	public String getName();
}
