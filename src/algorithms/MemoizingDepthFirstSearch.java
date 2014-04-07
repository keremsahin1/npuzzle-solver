package algorithms;

import java.util.Vector;

import datastructures.PuzzleState;
import datastructures.MyStack;
import datastructures.State;

public class MemoizingDepthFirstSearch<T extends State<T>> extends SearchAlgorithmCommons
{
	private T startState = null;
	private MyStack<T> stack = null;
	private int iExpandedNodeCnt = 0;
	private int iMemoryStorage = 0;

	public MemoizingDepthFirstSearch(T startState)
	{
		this.startState = startState;

		stack = new MyStack<T>();
	}

	public T startDFS()
	{
		long lStartTime = System.currentTimeMillis();
		int iIterationNo = 0;
		T goalState = null;
		T currentState = null;
		Vector<T> vecCurrSuccStates = null;
		Vector<T> explored = new Vector<T>();

		stack.push(startState);
		explored.add(startState);

		while(true)
		{
			if(stack.isEmpty())
			{
				writeToLogWindow("  FAILURE: Stack is empty!");
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
				break;
			}
			
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

			writeToLogWindow("Iteration" + iIterationNo);
			iIterationNo++;
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
}
