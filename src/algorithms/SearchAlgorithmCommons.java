package algorithms;

import java.util.concurrent.Semaphore;

import javax.swing.SwingUtilities;

import datastructures.PuzzleState;

import puzzle.PanelOutputWindow;
import puzzle.PanelPuzzleView;

public class SearchAlgorithmCommons
{
	private PanelOutputWindow logWindow = null;
	private PanelPuzzleView puzzleWindow = null;
	
	private boolean boIsStopped = false;
	private boolean boIsPaused = false;
	
	private Semaphore semPause = new Semaphore(0);
	private Semaphore semStepByStep = new Semaphore(0);
	
	public static int iTimerDelayInMs = 1000;
	
	public void setLogWindow(PanelOutputWindow logWindow)
	{
		this.logWindow = logWindow;
	}
	
	private PanelOutputWindow getLogWindow()
	{
		return logWindow;
	}
	
	protected void writeToLogWindow(final String s)
	{
		if(getLogWindow() != null)
		{
			getLogWindow().insertToLogWindow(s);
		}
		else
		{
			System.out.println(s);
		}
	}
	
	public void setPuzzleWindow(PanelPuzzleView puzzleWindow)
	{
		this.puzzleWindow = puzzleWindow;
	}
	
	private PanelPuzzleView getPuzzleWindow()
	{
		return puzzleWindow;
	}
	
	protected boolean showCurrentState(final PuzzleState puzzleNewState)
	{
		if(getPuzzleWindow() != null)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run() 
				{
					getPuzzleWindow().setPuzzleState(puzzleNewState);
				}
			});
		}
		
		if(!boIsStopped)
		{
			if(!boIsPaused)
			{
				try 
				{
					Thread.sleep(iTimerDelayInMs);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
				return true;
			}
			else
			{
				while(true)
				{
					if(semPause.tryAcquire())
					{
						boIsPaused = false;
						break;
					}
					else if(semStepByStep.tryAcquire())
						break;
					
					try 
					{
						Thread.sleep(100);
					} 
					catch (InterruptedException e) 
					{					
						e.printStackTrace();
					}
				}
				return true;
			}
		}
		else
		{
			writeToLogWindow("Stopped!");
			return false;
		}
	}
	
	public void stepAlgorithm()
	{
		semStepByStep.release();
	}

	public void stopAlgorithm()
	{
		boIsStopped = true;
	}
	
	public void pauseAlgorithm()
	{
		writeToLogWindow("Paused!");
		boIsPaused = true;
	}
	
	public void continueAlgorithm()
	{
		writeToLogWindow("Continuing...");
		boIsPaused = false;
		semPause.release();
	}
}
