package datastructures;

import java.util.Vector;

public class MyStack<T extends State<T>>
{
	private Vector<T> vecData = null;
	private int iMaxStackSize = Integer.MAX_VALUE;
	
	public MyStack()
	{
		vecData = new Vector<T>();
	}
	
	public void setMaxSize(int iMaxSize)
	{
		this.iMaxStackSize = iMaxSize;
	}
	
	public boolean push(T newData)
	{
		if(size() < iMaxStackSize)
		{
			vecData.add(newData);
			return true;
		}
		
		return false;
	}
	
	public T pop()
	{
		return vecData.remove(vecData.size() - 1);
	}
	
	public T top()
	{
		return vecData.get(vecData.size() - 1);
	}
	
	public boolean isEmpty()
	{
		if(vecData.size() == 0)
			return true;
		
		return false;
	}
	
	public int size()
	{
		return vecData.size();
	}
}
