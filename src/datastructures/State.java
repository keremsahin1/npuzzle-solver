package datastructures;

import java.util.Vector;

public interface State<T extends State<T>> extends Comparable<T>
{
	boolean isAGoalState();
	boolean isSame(T state);
	boolean isAlreadyContainedInVecVec(Vector<Vector<T>> vecVecAllStates);
	boolean isAlreadyContainedInVec(Vector<T> vecAllStates);
	Vector<T> getSuccessorStates();
	T getBackPointer();
	void setBackPointer(T backPointer);
	public int calculateManhattanHeuristic();
	public int calculateMisplacedHeuristic();
	public void setPriority(int iPri);
	public int getPriority();
	public void setCost(int iCost);
	public int getCost();
	public int getDepth();
}
