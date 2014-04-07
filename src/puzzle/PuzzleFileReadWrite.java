package puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class PuzzleFileReadWrite
{
	public static Integer[][] parsePuzzleFile(File puzzleFile)
	{
		Integer[][] iDoubArr = null;

		if(puzzleFile.exists())
		{
			try 
			{
				Scanner scanner = new Scanner(puzzleFile);

				Vector<String[]> vecPuzzle = new Vector<String[]>();
				while(scanner.hasNextLine())
				{
					String[] sArrSingleLine = scanner.nextLine().split("\\s+");
					vecPuzzle.add(sArrSingleLine);
				}
				scanner.close();

				iDoubArr = new Integer[vecPuzzle.size()][vecPuzzle.get(0).length];
				for (int iRowNo = 0; iRowNo < iDoubArr.length; iRowNo++) 
				{
					String[] sArrSingleLine = vecPuzzle.get(iRowNo);

					for (int iColNo = 0; iColNo < iDoubArr[0].length; iColNo++) 
					{
						iDoubArr[iRowNo][iColNo] = Integer.parseInt(sArrSingleLine[iColNo]);
					}
				}
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}

		return iDoubArr;
	}

	public static void writeToPuzzleFile(File puzzleFile, Integer[][] iDoubArrPuzzleState)
	{

		try 
		{
			FileWriter fileWriter = new FileWriter(puzzleFile);

			for (int iRowNo = 0; iRowNo < iDoubArrPuzzleState.length; iRowNo++) 
			{
				for (int iColNo = 0; iColNo < iDoubArrPuzzleState[0].length; iColNo++) 
				{
					fileWriter.write(String.valueOf(iDoubArrPuzzleState[iRowNo][iColNo]) + " ");
				}
				
				fileWriter.write('\n');
			}
			
			fileWriter.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
