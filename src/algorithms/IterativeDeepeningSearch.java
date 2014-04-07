package algorithms;

import java.util.Vector;

import datastructures.PuzzleState;
import datastructures.MyStack;
import datastructures.State;

public class IterativeDeepeningSearch<T extends State<T>> extends SearchAlgorithmCommons
{
	private T startState = null;
	private MyStack<T> stack = null;
	private int iExpandedNodeCnt = 0;
	private int iMemoryStorage = 0;

	public IterativeDeepeningSearch(T startState)
	{
		this.startState = startState;

		stack = new MyStack<T>();
	}

	public T startIDS()
	{
		long lStartTime = System.currentTimeMillis();
		int iIterationNo = 0;
		int iMaxDepth = 1;		
		T goalState = null;
		T currentState = null;
		Vector<T> vecCurrSuccStates = null;		
		Vector<T> explored = null;

		while(true)
		{
			stack = new MyStack<T>();
			explored = new Vector<T>();			

			stack.push(startState);			
			explored.add(startState);

			iIterationNo = 0;			
			while(true)
			{
				if(stack.isEmpty())
				{
					writeToLogWindow("  FAILURE: P is empty!");
					break;
				}
				
				currentState = stack.pop();
				if(showCurrentState((PuzzleState) currentState) == false) /*TODO Not generic*/
					break;
				
				if(currentState.isAGoalState())
				{
					long lEstimatedTime = System.currentTimeMillis() - lStartTime;
					goalState = currentState;
					writeToLogWindow("  FINISHED: Goal state found!");
					writeToLogWindow("Estimated Time: " + lEstimatedTime + " ms");
					writeToLogWindow("Expanded Node Cnt: " + iExpandedNodeCnt);
					return goalState;
				}

				if(currentState.getDepth() < iMaxDepth)
				{
					vecCurrSuccStates = currentState.getSuccessorStates();
					iExpandedNodeCnt++;
					iMemoryStorage += (vecCurrSuccStates.size());
					for (int iSuccNo = 0; iSuccNo < vecCurrSuccStates.size(); iSuccNo++)
					{
						if(!vecCurrSuccStates.get(iSuccNo).isAlreadyContainedInVec(explored))
						{
							explored.add(vecCurrSuccStates.get(iSuccNo));
							stack.push(vecCurrSuccStates.get(iSuccNo));
						}
					}					
				}
				
				writeToLogWindow("DFS Level:" + iMaxDepth + "_Iteration:" + iIterationNo);
				iIterationNo++;
			}

			iMaxDepth++;
		}
	}
	
	public int getExpandedNodeCnt()
	{
		return iExpandedNodeCnt;
	}
	
	public int getMemoryStorage()
	{
		return iMemoryStorage;
	}
}
