package utils;

public class MultiDimArrayCopy 
{
	public static Integer[][] copy2dIntArray(Integer[][] iDoubArr)
	{
		Integer[][] iDoubArrRet = new Integer[iDoubArr.length][iDoubArr[0].length];
		
		for (int iRowNo = 0; iRowNo < iDoubArrRet.length; iRowNo++) 
		{
			for (int iColNo = 0; iColNo < iDoubArrRet[0].length; iColNo++) 
			{
				iDoubArrRet[iRowNo][iColNo] = iDoubArr[iRowNo][iColNo];
			}
		}
		
		return iDoubArrRet;
	}
}
