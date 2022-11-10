package components;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 *
 */

/**
 * enum which includes a list of statuses corresponding to the shipping stages
 */
public enum Status {
	/**
	 * CREATION - when the package create 
	 */
	CREATION,
	/**
	 *COLLECTION - This status a package receives when a transport vehicle is sent to pick it up from the sender's address.
	 */
	COLLECTION,
	/**
	 *BRANCH_STORAGE - The package collected from a customer has arrived at the sender's local branch.
	 */
	BRANCH_STORAGE,
	/**
	 *HUB_TRANSPORT - the package on the way from the local branch to the sorting center.
	 */
	HUB_TRANSPORT,
	/**
	 *HUB_STORAGE - The package has arrived at the sorting center and is waiting to be transferred to the destination branch.
	 */
	HUB_STORAGE,
	/**
	 *BRANCH_TRANSPORT - the package on the way from the sorting center to the destination branch.
	 */
	BRANCH_TRANSPORT,
	/**
	 *DELIVERY - The package has arrived at the destination branch and is ready for delivery to the end customer.
	 */
	DELIVERY,
	/**
	 * DISTRIBUTION - The package on the way from the destination branch to the end customer.
	 */
	DISTRIBUTION,
	/**
	 *DELIVERED - The package was delivered to the end customer.
	 */
	DELIVERED;

}
