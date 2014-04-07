package utils;

import java.util.Arrays;
import java.util.Collections;

public class RandomLists 
{
	public static Integer[] generateShuffledIntArray(int iLength)
	{
		Integer[] iArr = new Integer[iLength];
		
		for (int i = 0; i < iArr.length; i++) 
		{
			iArr[i] = i;
		}
		
		Collections.shuffle(Arrays.asList(iArr));
		
		return iArr;
	}
	
	public static Integer[][] generateShuffledIntDoubleArray(int iRowCnt, int iColCnt)
	{
		Integer[][] iDoubArr = new Integer[iRowCnt][iColCnt];
		Integer[] iArrShuffled = generateShuffledIntArray(iRowCnt * iColCnt);
		
		for (int iRowNo = 0; iRowNo < iRowCnt; iRowNo++)
		{
			for (int iColNo = 0; iColNo < iColCnt; iColNo++)
			{
				iDoubArr[iRowNo][iColNo] = iArrShuffled[(iRowNo * iDoubArr[0].length) + iColNo];
			}
		}
		
		return iDoubArr;
	}
}
