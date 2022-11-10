package components;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @version 09.05.21
 */
public class Tracking {
	/**
	 * The Class Tracking represents a record in the package transfer history. Each package contains a collection of records of this type, 
	 * each time the status (and location) of a package is changed a new record is added to the collection. 
	 * Each record includes the time the record was created, the point where the package is located and the status of the package.
	 * @param time - Represents the time of the system in the moment the object creating
	 * @param node - Customer / branch / sorting center / transport vehicle. When the package is with the customer 
	 * (sender or recipient), the value of this field is null.
	 * @param status - Package status as soon as a record is created
	 */
	public final int time;
	public final Node node;
	public final Status status;
	private static int counter=1;
	// invoise data stream
	
	/**
	 * Constructs and initializes an object with record history
	 * @param time - int 
	 * @param node - Node
	 * @param status - Status
	 */
	public Tracking(int time, Node node, Status status) {
		super();
		this.time = time;
		this.node = node;
		this.status = status;
		
		counter++;
	}

	/**
	 * toString method that convert the object to String
	 * @return String
	 */
	@Override
	public String toString() {
		String name = (node==null)? "Customer" : node.getName();
		return time + ": " + name + ", status=" + status;
	}

	
	
}
