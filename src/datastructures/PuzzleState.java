package datastructures;

import java.util.Vector;

import utils.MultiDimArrayCopy;

public class PuzzleState implements State<PuzzleState>
{
	private static final int SUCCESSOR_DIRECTION_LEFT 	= 0;
	private static final int SUCCESSOR_DIRECTION_RIGHT 	= 1;
	private static final int SUCCESSOR_DIRECTION_UP 	= 2;
	private static final int SUCCESSOR_DIRECTION_DOWN 	= 3;

	private Integer[][] iDoubArrPuzzleState = null;
	private Integer[][] iDoubArrGoalState = null;
	
	private PuzzleState backPointer = null;
	private int iPriority = -1;
	private int iCost = -1;
	private int iDepth = 0;

	private int iMissingTileIndexX = -1;
	private int iMissingTileIndexY = -1;

	public PuzzleState(Integer[][] iDoubArrPuzzleState)
	{
		this.iDoubArrPuzzleState = iDoubArrPuzzleState;

		initMissingTileIndexes();
		initGoalState();
	}

	public Integer[][] getData()
	{
		return iDoubArrPuzzleState;
	}

	public int getRowCnt()
	{
		return iDoubArrPuzzleState.length;
	}

	public int getColCnt()
	{
		return iDoubArrPuzzleState[0].length;
	}

	public void setBackPointer(PuzzleState backPointer)
	{
		this.backPointer = backPointer;
	}

	public PuzzleState getBackPointer()
	{
		return backPointer;
	}
	
	private void initGoalState()
	{
		int iValue = 1;
		
		iDoubArrGoalState = new Integer[getRowCnt()][getColCnt()];
		
		for (int i = 0; i < getRowCnt(); i++) 
		{
			for (int j = 0; j < getColCnt(); j++) 
			{
				iDoubArrGoalState[i][j] = iValue;
				iValue++;
			}
		}
		
		iDoubArrGoalState[getRowCnt() - 1][getColCnt() - 1] = 0;
	}

	private void initMissingTileIndexes()
	{	
		if((iMissingTileIndexX < 0) || (iMissingTileIndexY < 0))
		{	
			for (int iRowNo = 0; iRowNo < iDoubArrPuzzleState.length; iRowNo++)
			{
				for (int iColNo = 0; iColNo < iDoubArrPuzzleState[0].length; iColNo++) 
				{
					if(iDoubArrPuzzleState[iRowNo][iColNo] == 0)
					{
						iMissingTileIndexX = iRowNo;
						iMissingTileIndexY = iColNo;

						return;
					}
				}
			}
		}
	}

	public int getMissingTileX()
	{
		return iMissingTileIndexX;
	}

	public int getMissingTileY()
	{
		return iMissingTileIndexY;
	}

	public boolean isAGoalState()
	{
		for (int iRowNo = 0; iRowNo < iDoubArrPuzzleState.length; iRowNo++)
		{
			for (int iColNo = 0; iColNo < iDoubArrPuzzleState[0].length; iColNo++) 
			{
				if(iDoubArrPuzzleState[iRowNo][iColNo] != iDoubArrGoalState[iRowNo][iColNo])
					return false;
			}
		}

		return true;
	}

	public boolean isSame(PuzzleState puzzleState)
	{
		if((getColCnt() != puzzleState.getColCnt()) || (getRowCnt() != puzzleState.getRowCnt()))
			return false;

		for (int iRowNo = 0; iRowNo < iDoubArrPuzzleState.length; iRowNo++) 
		{
			for (int iColNo = 0; iColNo < iDoubArrPuzzleState[0].length; iColNo++) 
			{
				if(iDoubArrPuzzleState[iRowNo][iColNo] != puzzleState.getData()[iRowNo][iColNo])
					return false;
			}
		}

		return true;
	}

	public boolean isAlreadyContainedInVecVec(Vector<Vector<PuzzleState>> vecVecAllStates)
	{
		for (int iLevelNo = 0; iLevelNo < vecVecAllStates.size(); iLevelNo++) 
		{
			for (int iStateNoInOneLevel = 0; iStateNoInOneLevel < vecVecAllStates.get(iLevelNo).size(); iStateNoInOneLevel++) 
			{
				if(isSame(vecVecAllStates.get(iLevelNo).get(iStateNoInOneLevel)))
					return true;
			}
		}

		return false;
	}

	public boolean isAlreadyContainedInVec(Vector<PuzzleState> vecAllStates)
	{
		for (int iStateNo = 0; iStateNo < vecAllStates.size(); iStateNo++) 
		{
			if(isSame(vecAllStates.get(iStateNo)))
				return true;
		}

		return false;
	}

	public Vector<PuzzleState> getSuccessorStates()
	{
		Vector<PuzzleState> vecSuccessors = new Vector<PuzzleState>();

		PuzzleState shiftLeftSucc = getOneSuccessorState(SUCCESSOR_DIRECTION_LEFT);
		PuzzleState shiftRightSucc = getOneSuccessorState(SUCCESSOR_DIRECTION_RIGHT);
		PuzzleState shiftUpSucc = getOneSuccessorState(SUCCESSOR_DIRECTION_UP);
		PuzzleState shiftDownSucc = getOneSuccessorState(SUCCESSOR_DIRECTION_DOWN);

		if(shiftLeftSucc != null)
			vecSuccessors.add(shiftLeftSucc);

		if(shiftRightSucc != null)
			vecSuccessors.add(shiftRightSucc);

		if(shiftUpSucc != null)
			vecSuccessors.add(shiftUpSucc);

		if(shiftDownSucc != null)
			vecSuccessors.add(shiftDownSucc);

		return vecSuccessors;
	}

	private PuzzleState getOneSuccessorState(int iSuccessorDirection)
	{
		PuzzleState newPuzzleState = null;
		Integer[][] iDoubArrNew = null;

		int iMissingTileX = getMissingTileX();
		int iMissingTileY = getMissingTileY();

		if((iSuccessorDirection == SUCCESSOR_DIRECTION_LEFT) && (iMissingTileY > 0))
		{
			iDoubArrNew = MultiDimArrayCopy.copy2dIntArray(getData());
			swapHorizontally(iDoubArrNew, iMissingTileY - 1, iMissingTileY, iMissingTileX);
		}
		else if((iSuccessorDirection == SUCCESSOR_DIRECTION_RIGHT) && (iMissingTileY < (getColCnt() - 1)))
		{
			iDoubArrNew = MultiDimArrayCopy.copy2dIntArray(getData());
			swapHorizontally(iDoubArrNew, iMissingTileY + 1, iMissingTileY, iMissingTileX);
		}
		else if((iSuccessorDirection == SUCCESSOR_DIRECTION_UP) && (iMissingTileX > 0))
		{
			iDoubArrNew = MultiDimArrayCopy.copy2dIntArray(getData());
			swapVertically(iDoubArrNew, iMissingTileX - 1, iMissingTileX, iMissingTileY);
		}
		else if((iSuccessorDirection == SUCCESSOR_DIRECTION_DOWN) && (iMissingTileX < (getRowCnt() - 1)))
		{
			iDoubArrNew = MultiDimArrayCopy.copy2dIntArray(getData());
			swapVertically(iDoubArrNew, iMissingTileX + 1, iMissingTileX, iMissingTileY);
		}

		if(iDoubArrNew != null)
		{
			newPuzzleState = new PuzzleState(iDoubArrNew);
			newPuzzleState.setBackPointer(this);
			newPuzzleState.setDepth(getDepth() + 1);
		}

		return newPuzzleState;
	}

	private static void swapHorizontally(Integer[][] iDoubArr, int iElemYIndex, int iMissingTileY, int iX)
	{
		iDoubArr[iX][iMissingTileY] = iDoubArr[iX][iElemYIndex];
		iDoubArr[iX][iElemYIndex] = 0;
	}

	private static void swapVertically(Integer[][] iDoubArr, int iElemXIndex, int iMissingTileX, int iY)
	{
		iDoubArr[iMissingTileX][iY] = iDoubArr[iElemXIndex][iY];
		iDoubArr[iElemXIndex][iY] = 0;
	}

	public void printData()
	{
		System.out.println("* * *");
		for (int i = 0; i < getRowCnt(); i++) 
		{
			for (int j = 0; j < getColCnt(); j++) 
			{
				System.out.print(getData()[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	public int calculateMisplacedHeuristic()
	{
		int iResult = 0;
		
		for (int iRowNo = 0; iRowNo < getRowCnt(); iRowNo++) 
		{
			for (int iColNo = 0; iColNo < getColCnt(); iColNo++)
			{
				if(getData()[iRowNo][iColNo] != iDoubArrGoalState[iRowNo][iColNo])
					iResult++;
			}
		}
		
		return iResult;
	}
	
	public int calculateManhattanHeuristic()
	{
		int iResult = 0;
		int iExpectedRowNo = 0;
		int iExpectedColNo = 0;
		
		for (int iRowNo = 0; iRowNo < getRowCnt(); iRowNo++)
		{
			for (int iColNo = 0; iColNo < getColCnt(); iColNo++)
			{
				iExpectedColNo = (getData()[iRowNo][iColNo] - 1 + getColCnt()) % getColCnt();
				iExpectedRowNo = ((getData()[iRowNo][iColNo] - 1 + getColCnt() * getRowCnt()) / getColCnt()) % getColCnt();
				
				iResult += (Math.abs(iRowNo - iExpectedRowNo) + Math.abs(iColNo - iExpectedColNo)); 
			}
		}
		
		return iResult;
	}
	
	public void setPriority(int iPri)
	{
		this.iPriority = iPri;
	}
	
	public int getPriority()
	{
		return iPriority;
	}

	public void setCost(int iCost) 
	{
		this.iCost = iCost;
	}

	public int getCost() 
	{
		return iCost;
	}

	@Override
	public int compareTo(PuzzleState o)
	{
		if(isSame(o))
			return 0;
		else
			return 1;
	}

	public int getDepth()
	{
		return iDepth;
	}
	
	private void setDepth(int iDepth)
	{
		this.iDepth = iDepth;
	}
}
