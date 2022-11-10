package components;
	
/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @version 09.05.21
 * Point class
 */
public class Point {
	/**
	 * @param x - int 
	 * @param y - int
	 */
	private int x;
	private int y;
	
	/**
	 * constructor
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	
	/**
	 * copy constructor
	 * @param other
	 */
	public Point(Point other) {
		this.x = other.x;
		this.y = other.y;
	}

	/**
	 * get x method
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * get y method
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * set x method
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * set y method
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
}
