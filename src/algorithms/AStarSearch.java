package algorithms;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

import datastructures.PuzzleState;
import datastructures.State;

public class AStarSearch<T extends State<T>> extends SearchAlgorithmCommons
{
	private PriorityQueue<T> priQueue = null;
	private T startState = null;
	private Vector<T> visited = null;
	private int iHeuristicType = -1;

	private int iExpandedNodeCnt = 0;
	private int iMemoryStorage = 0;
	
	public static final int HEURISTIC_MISPLACED	= 0;
	public static final int HEURISTIC_MANHATTAN	= 1;

	public AStarSearch(T startState, int iHeuristicType)
	{
		this.startState = startState;
		this.iHeuristicType = iHeuristicType;

		priQueue = new PriorityQueue<T>(11, new ComparatorPriorityQueue());
		visited = new Vector<T>();
	}

	public T startAStar()
	{
		long lStartTime = System.currentTimeMillis();
		T goalState = null;
		T currentState = null;
		Vector<T> currentStateSuccessors= null;

		startState.setCost(0);
		startState.setPriority(getHeuristic(startState));
		priQueue.add(startState);
		visited.add(startState);

		while(true)
		{
			if(priQueue.isEmpty())
			{
				writeToLogWindow("  FAILURE: PriQueue is empty!");
				break;
			}
			
			currentState = priQueue.remove();
			if(showCurrentState((PuzzleState) currentState) == false) /*TODO Not generic*/
				return goalState;
			if(currentState.isAGoalState())
			{
				long lEstimatedTime = System.currentTimeMillis() - lStartTime;
				goalState = currentState;
				writeToLogWindow("  FINISHED: Goal state found!");
				writeToLogWindow("Estimated Time: " + lEstimatedTime + " ms");
				writeToLogWindow("Expanded Node Cnt: " + iExpandedNodeCnt);
				break;
			}
			
			currentStateSuccessors = currentState.getSuccessorStates();
			iExpandedNodeCnt++;
			iMemoryStorage += (currentStateSuccessors.size());
			for (int iSuccNo = 0; iSuccNo < currentStateSuccessors.size(); iSuccNo++) 
			{
				T currentSuccessor = currentStateSuccessors.get(iSuccNo);
				int iCost = currentState.getCost() + 1;
				int iPriority = iCost + getHeuristic(currentSuccessor);
				
				currentSuccessor.setCost(iCost);
				currentSuccessor.setPriority(iPriority);
				
				if(!currentSuccessor.isAlreadyContainedInVec(visited))
				{
					priQueue.add(currentSuccessor);
					visited.add(currentSuccessor);
				}
				else
				{
					T counterPartInVisited = findInVisited(currentSuccessor);
					if(counterPartInVisited != null)
					{
						if(counterPartInVisited.getPriority() > currentSuccessor.getPriority())
						{
							counterPartInVisited.setBackPointer(currentState);
							counterPartInVisited.setPriority(currentSuccessor.getPriority());
						}
					}
				}
			}
		}

		return goalState;
	}
	
	public int getExpandedNodeCnt()
	{
		return iExpandedNodeCnt;
	}
	
	public int getMemoryStorage()
	{
		return iMemoryStorage;
	}
	
	private T findInVisited(T state)
	{
		T foundState = null;
		
		for (int i = 0; i < visited.size(); i++) 
		{
			if(visited.get(i).compareTo(state) == 0)
			{
				foundState = visited.get(i);
				break;
			}
		}
		
		return foundState;
	} 

	private int getHeuristic(T state)
	{
		int iValue = -1;

		if(iHeuristicType == HEURISTIC_MANHATTAN)
			iValue =  state.calculateManhattanHeuristic();
		else if(iHeuristicType == HEURISTIC_MISPLACED)
			iValue = state.calculateMisplacedHeuristic();

		return iValue;
	}

	private class ComparatorPriorityQueue implements Comparator<T>
	{
		@Override
		public int compare(T state1, T state2)
		{
			return Double.compare((state1.getPriority()), (state2.getPriority()));
		}
	}
}
