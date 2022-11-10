package components;

import java.util.ArrayList;


/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * * @version 09.05.2021  
 * @see  CareTaker

 */
public class CareTaker 
{
	
	/**
	 * CareTaker class, to the memento
	 * @param statesList - ArrayList<Memento>
	 * @param curState - int
	 */
	private ArrayList<Memento> statesList;
	private int curState = -1;
	
	/**
	 * default constructor
	 */
	public CareTaker()
	{
		this.statesList = new ArrayList<>();
	}
	
	/**
	 * add memento to the list
	 * @param m
	 */
	public void addMemento(Memento m) { 
		statesList.add(m); 
		curState = this.statesList.size() - 1;
	} 
	/**
	 * get memento method
	 * @param index
	 * @return stateslist
	 */
	public Memento getMemento(int index) { 
		return statesList.get(index); 
	} 
	
	/**
	 * undo method
	 * @return memento
	 */
	public Memento Undo()
	{
		System.out.println("Undoing state");
		if(this.curState <= 0)
		{
			curState = 0;
			return getMemento(curState);
		}
		
		curState--;
		return getMemento(curState);
	}

}
