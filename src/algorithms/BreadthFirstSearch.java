package algorithms;

import java.util.Vector;

import datastructures.PuzzleState;
import datastructures.State;

public class BreadthFirstSearch<T extends State<T>> extends SearchAlgorithmCommons
{
	private T startState;
	private int iExpandedNodeCnt = 0;
	private int iMemoryStorage = 0;

	public BreadthFirstSearch(T startState)
	{
		this.startState = startState;
	}

	public T startBFS()
	{
		long lStartTime = System.currentTimeMillis();
		int iK = 0;

		Vector<Vector<T>> vecVecAllStates = new Vector<Vector<T>>();
		Vector<T> vecStatesInNextLevel = null;
		Vector<T> vecSuccStates = null;
		T goalState = null;

		vecVecAllStates.add(new Vector<T>());
		vecVecAllStates.get(0).add(startState);

		while(true)
		{
			goalState = checkForGoalStateInVx(vecVecAllStates.get(iK));
			if(goalState != null)
			{
				long lEstimatedTime = System.currentTimeMillis() - lStartTime;
				
				if(showCurrentState((PuzzleState) goalState) == false) /*TODO Not generic*/
					return goalState;
				
				writeToLogWindow("  FINISHED: Goal state found!");
				writeToLogWindow("Estimated Time: " + lEstimatedTime + " ms");
				writeToLogWindow("Expanded Node Cnt: " + iExpandedNodeCnt);
				writeToLogWindow("Memory Storage: " + iMemoryStorage);
				return goalState;
			}
			else if(vecVecAllStates.get(iK).size() == 0)
			{
				writeToLogWindow("  FAILURE: V" + iK + " is empty!");
				return goalState;
			}

			vecStatesInNextLevel = new Vector<T>();
			for (int iStateNo = 0; iStateNo < vecVecAllStates.get(iK).size(); iStateNo++)
			{
				vecSuccStates = vecVecAllStates.get(iK).get(iStateNo).getSuccessorStates();
				iExpandedNodeCnt++;
				iMemoryStorage += (vecSuccStates.size());
				for (int iSuccStateNo = 0; iSuccStateNo < vecSuccStates.size(); iSuccStateNo++) 
				{
					if(!vecSuccStates.get(iSuccStateNo).isAlreadyContainedInVecVec(vecVecAllStates))
					{
						vecStatesInNextLevel.add(vecSuccStates.get(iSuccStateNo));

						if(showCurrentState((PuzzleState) vecSuccStates.get(iSuccStateNo)) == false) /*TODO Not generic*/
							return goalState;
					}
				}
			}
			vecVecAllStates.add(vecStatesInNextLevel);
			
			writeToLogWindow("Iteration: " + iK);
			iK++;
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

	private T checkForGoalStateInVx(Vector<T> vecStates)
	{
		T goalState = null;

		for (int iInd = 0; iInd < vecStates.size(); iInd++) 
		{
			if(vecStates.get(iInd).isAGoalState())
			{
				goalState = vecStates.get(iInd);
				break;
			}
		}

		return goalState;
	}
}
