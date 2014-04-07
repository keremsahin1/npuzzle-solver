package puzzle;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algorithms.AStarSearch;
import algorithms.BreadthFirstSearch;
import algorithms.IterativeDeepeningSearch;
import algorithms.MemoizingDepthFirstSearch;
import algorithms.SearchAlgorithmCommons;
import datastructures.PuzzleState;

public class PanelSettings extends JPanel 
{
	private JPanel panelAlgorithmSettings = null;
	
	private JPanel panelControlSettings = null;
	private JButton buttonStart = null;
	private JButton buttonPause = null;
	private JButton buttonNext = null;
	private JButton buttonStop = null;
	private JSlider sliderRunningSpeed = null;
	
	private JPanel panelMonteCarloSettings = null;
	private JTextField textFieldMonteCarloSimCnt = null;
	private JTextField textFieldTrueDistance = null;
	private JButton buttonStartMonteCarloSimulation = null;
	private JComboBox comboBoxMonteCarloPuzzleSize = null;
	
	private JComboBox comboBoxAlgorithmSelection = null;
	
	private PanelPuzzleView panelPuzzleView = null;
	private PanelOutputWindow panelLogWindow = null;
	
	private BreadthFirstSearch<PuzzleState> bfs = null;	
	private MemoizingDepthFirstSearch<PuzzleState> dfs = null;
	private IterativeDeepeningSearch<PuzzleState> ids = null;
	private AStarSearch<PuzzleState> ass = null;
	
	private AlgoRunThread threadAlgoRun = null;
	
	private int iTotalExpandedNode = 0;
	private int iTotalMemoryStorage = 0;
	
	public PanelSettings()
	{
		super(new BorderLayout(2, 2));
		setBorder(new TitledBorder("Settings"));
		
		add(getPanelAlgorithmSettings(), BorderLayout.WEST);
		add(getPanelControlSettings(), BorderLayout.CENTER);
		add(getPanelMonteCarloSettings(), BorderLayout.EAST);
	}
	
	private JSlider getSliderRunningSpeed()
	{
		if(sliderRunningSpeed == null)
		{
			sliderRunningSpeed = new JSlider(SwingConstants.HORIZONTAL);
			sliderRunningSpeed.setBorder(new TitledBorder("Running Speed"));
			sliderRunningSpeed.setValue(0);
			sliderRunningSpeed.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent arg0)
				{
					SearchAlgorithmCommons.iTimerDelayInMs = ((100 - sliderRunningSpeed.getValue()) * 10) + 1;
				}
			});
		}
		
		return sliderRunningSpeed;
	}
	
	private JPanel getPanelMonteCarloSettings()
	{
		if(panelMonteCarloSettings == null)
		{
			panelMonteCarloSettings = new JPanel(new GridBagLayout());
			panelMonteCarloSettings.setBorder(new TitledBorder("Monte Carlo Simulations"));
			
			
			GridBagConstraints gbc1 = new GridBagConstraints();
			gbc1.gridx = 0;
			gbc1.gridy = 0;
			gbc1.fill = GridBagConstraints.HORIZONTAL;
			gbc1.insets = new Insets(2,2,2,2);
			panelMonteCarloSettings.add(new JLabel("Simulation Cnt(N):"), gbc1);
			
			GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.gridx = 1;
			gbc2.gridy = 0;
			gbc2.fill = GridBagConstraints.HORIZONTAL;
			gbc2.insets = new Insets(2,2,2,2);
			panelMonteCarloSettings.add(getTfMonteCarloSimulationCnt(), gbc2);
			
			GridBagConstraints gbc3 = new GridBagConstraints();
			gbc3.gridx = 0;
			gbc3.gridy = 1;
			gbc3.fill = GridBagConstraints.HORIZONTAL;
			gbc3.insets = new Insets(2,2,2,2);
			panelMonteCarloSettings.add(new JLabel("True Distance(M):"), gbc3);
			
			GridBagConstraints gbc4 = new GridBagConstraints();
			gbc4.gridx = 1;
			gbc4.gridy = 1;
			gbc4.fill = GridBagConstraints.HORIZONTAL;
			gbc4.insets = new Insets(2,2,2,2);
			panelMonteCarloSettings.add(getTfMonteCarloTrueDistance(), gbc4);
			
			GridBagConstraints gbc5 = new GridBagConstraints();
			gbc5.gridx = 0;
			gbc5.gridy = 2;
			gbc5.fill = GridBagConstraints.HORIZONTAL;
			gbc5.insets = new Insets(2,2,2,2);
			panelMonteCarloSettings.add(new JLabel("Puzzle Size:"), gbc5);
			
			GridBagConstraints gbc6 = new GridBagConstraints();
			gbc6.gridx = 1;
			gbc6.gridy = 2;
			gbc6.fill = GridBagConstraints.HORIZONTAL;
			gbc6.insets = new Insets(2,2,2,2);
			panelMonteCarloSettings.add(getCbMonteCarloPuzzleSize(), gbc6);
			
			GridBagConstraints gbc7 = new GridBagConstraints();
			gbc7.gridx = 0;
			gbc7.gridy = 3;
			gbc7.gridwidth = 2;
			gbc7.fill = GridBagConstraints.HORIZONTAL;
			gbc7.insets = new Insets(2,2,2,2);
			panelMonteCarloSettings.add(getButtonStartMonteCarloSimulation(), gbc7);
		}
		
		return panelMonteCarloSettings;
	}
	
	private JButton getButtonStartMonteCarloSimulation()
	{
		if(buttonStartMonteCarloSimulation == null)
		{
			buttonStartMonteCarloSimulation = new JButton("Start");
			buttonStartMonteCarloSimulation.addActionListener(new ActionListener() 
			{	
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					int iSimCnt = Integer.parseInt(getTfMonteCarloSimulationCnt().getText());
					int iTrueDistance = Integer.parseInt(getTfMonteCarloTrueDistance().getText());
					int iPuzzleSize = (getCbMonteCarloPuzzleSize().getSelectedIndex() * 2) + 3;
					
					Vector<PuzzleState> vecVisited = new Vector<PuzzleState>();
					PuzzleState goalState = new PuzzleState(getGoalStateIntArray(iPuzzleSize));
					PuzzleState startState = null;
					Vector<PuzzleState> vecSuccStates = null;
					
					Random randNum = new Random();
					int iRandomIndex = 0;
					
					for (int i = 0; i < iSimCnt; i++)
					{
						startState = goalState;
						
						vecVisited.removeAllElements();
						vecVisited.add(startState);
						
						for (int j = 0; j < iTrueDistance; j++) 
						{
							vecSuccStates = startState.getSuccessorStates();
							
							do
							{
								iRandomIndex = randNum.nextInt(vecSuccStates.size());
							}while(vecSuccStates.get(iRandomIndex).isAlreadyContainedInVec(vecVisited));
							
							startState = vecSuccStates.get(iRandomIndex);
						}
						
						getPuzzleView().setPuzzleState(startState);
						threadAlgoRun = new AlgoRunThread();
						threadAlgoRun.start();
						while(threadAlgoRun.isAlive());
					}
					
					getLogWindow().insertToLogWindow("************");
					getLogWindow().insertToLogWindow("Avg Expanded Node Cnt: " + ((double)iTotalExpandedNode) / iSimCnt);
					getLogWindow().insertToLogWindow("Avg Memory Storage: " + ((double)iTotalMemoryStorage) / iSimCnt);
					getLogWindow().insertToLogWindow("************");
					
					iTotalExpandedNode = 0;
					iTotalMemoryStorage = 0;
				}
			});
		}
		
		return buttonStartMonteCarloSimulation;
	}
	
	private Integer[][] getGoalStateIntArray(int iSize)
	{
		Integer[][] iDoubArrGoalState = new Integer[iSize][iSize];
		int iValue = 1;
		
		for (int iRowNo = 0; iRowNo < iDoubArrGoalState.length; iRowNo++) 
		{
			for (int iColNo = 0; iColNo < iDoubArrGoalState[0].length; iColNo++) 
			{
				iDoubArrGoalState[iRowNo][iColNo] = iValue++;
			}
		}
		
		iDoubArrGoalState[iSize - 1][iSize - 1] = 0;
		
		return iDoubArrGoalState;
	}
	
	private JComboBox getCbMonteCarloPuzzleSize()
	{
		if(comboBoxMonteCarloPuzzleSize == null)
		{
			String[] sArr = new String[]{"3x3", "5x5", "7x7"};			
			comboBoxMonteCarloPuzzleSize = new JComboBox(sArr);
		}
		
		return comboBoxMonteCarloPuzzleSize;
	}
	
	private JTextField getTfMonteCarloSimulationCnt()
	{
		if(textFieldMonteCarloSimCnt == null)
		{
			textFieldMonteCarloSimCnt = new JTextField();
			textFieldMonteCarloSimCnt.setText("5");
		}
		
		return textFieldMonteCarloSimCnt;
	}
	
	private JTextField getTfMonteCarloTrueDistance()
	{
		if(textFieldTrueDistance == null)
		{
			textFieldTrueDistance = new JTextField();
			textFieldTrueDistance.setText("5");
		}
		
		return textFieldTrueDistance;
	}
	
	private JPanel getPanelAlgorithmSettings()
	{
		if(panelAlgorithmSettings == null)
		{
			panelAlgorithmSettings = new JPanel(new GridBagLayout());
			panelAlgorithmSettings.setBorder(new TitledBorder("Algorithm Settings"));
			
			GridBagConstraints gbc1 = new GridBagConstraints();
			gbc1.gridx = 0;
			gbc1.gridy = 0;
			gbc1.fill = GridBagConstraints.HORIZONTAL;
			gbc1.insets = new Insets(2,2,2,2);
			panelAlgorithmSettings.add(new JLabel("Algorithm:"), gbc1);
			
			GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.gridx = 1;
			gbc2.gridy = 0;
			gbc2.fill = GridBagConstraints.HORIZONTAL;
			gbc2.insets = new Insets(2,2,2,2);
			panelAlgorithmSettings.add(getCbAlgorithmSelection(), gbc2);
		}
		
		return panelAlgorithmSettings;
	}
	
	private JComboBox getCbAlgorithmSelection()
	{
		if(comboBoxAlgorithmSelection == null)
		{
			String[] sArr = new String[]{"BFS", "Memoizing DFS", "IDS", "A* with Misplaced Heuristic", "A* with Manhattan Heuristic"};
			
			comboBoxAlgorithmSelection = new JComboBox(sArr);
		}
		
		return comboBoxAlgorithmSelection;
	}
	
	private JPanel getPanelControlSettings()
	{
		if(panelControlSettings == null)
		{
			panelControlSettings = new JPanel(new GridBagLayout());
			panelControlSettings.setBorder(new TitledBorder("Control Settings"));
			
			GridBagConstraints gbc0 = new GridBagConstraints();
			gbc0.gridx = 0;
			gbc0.gridy = 0;
			gbc0.fill = GridBagConstraints.HORIZONTAL;
			gbc0.gridwidth = 4;
			gbc0.insets = new Insets(2,2,2,2);
			panelControlSettings.add(getSliderRunningSpeed(), gbc0);
			
			GridBagConstraints gbc1 = new GridBagConstraints();
			gbc1.gridx = 0;
			gbc1.gridy = 1;
			gbc1.fill = GridBagConstraints.HORIZONTAL;
			gbc1.insets = new Insets(2,2,2,2);
			panelControlSettings.add(getButtonStart(), gbc1);
			
			GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.gridx = 1;
			gbc2.gridy = 1;
			gbc2.fill = GridBagConstraints.HORIZONTAL;
			gbc2.insets = new Insets(2,2,2,2);
			panelControlSettings.add(getButtonPause(), gbc2);
			
			GridBagConstraints gbc3 = new GridBagConstraints();
			gbc3.gridx = 2;
			gbc3.gridy = 1;
			gbc3.fill = GridBagConstraints.HORIZONTAL;
			gbc3.insets = new Insets(2,2,2,2);
			panelControlSettings.add(getButtonNext(), gbc3);
			
			GridBagConstraints gbc4 = new GridBagConstraints();
			gbc4.gridx = 3;
			gbc4.gridy = 1;
			gbc4.fill = GridBagConstraints.HORIZONTAL;
			gbc4.insets = new Insets(2,2,2,2);
			panelControlSettings.add(getButtonStop(), gbc4);
		}
		
		return panelControlSettings;
	}
	
	private JButton getButtonStart()
	{
		if(buttonStart == null)
		{
			buttonStart = new JButton("Start");
			buttonStart.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					threadAlgoRun = new AlgoRunThread();
					threadAlgoRun.start();
				}
			});
		}
		
		return buttonStart;
	}
	
	private JButton getButtonPause()
	{
		if(buttonPause == null)
		{
			buttonPause = new JButton("Pause");
			buttonPause.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					int iAlgorithmSelection = getCbAlgorithmSelection().getSelectedIndex();
					
					if(threadAlgoRun != null)
					{
						if(threadAlgoRun.isAlive())
						{
							if(iAlgorithmSelection == 0)
							{
								if(bfs != null)
								{
									if(getButtonPause().getText().compareTo("Continue") == 0)
									{
										getButtonPause().setText("Pause");
										bfs.continueAlgorithm();
									}
									else
									{
										getButtonPause().setText("Continue");
										bfs.pauseAlgorithm();
									}
								}
							}
							
							else if(iAlgorithmSelection == 1)
							{
								if(dfs != null)
								{
									if(getButtonPause().getText().compareTo("Continue") == 0)
									{
										getButtonPause().setText("Pause");
										dfs.continueAlgorithm();
									}
									else
									{
										getButtonPause().setText("Continue");
										dfs.pauseAlgorithm();
									}
								}
							}
							
							else if(iAlgorithmSelection == 2)
							{
								if(ids != null)
								{
									if(getButtonPause().getText().compareTo("Continue") == 0)
									{
										getButtonPause().setText("Pause");
										ids.continueAlgorithm();
									}
									else
									{
										getButtonPause().setText("Continue");
										ids.pauseAlgorithm();
									}
								}
							}
							
							else if((iAlgorithmSelection == 3) || (iAlgorithmSelection == 4))
							{
								if(ass != null)
								{
									if(getButtonPause().getText().compareTo("Continue") == 0)
									{
										getButtonPause().setText("Pause");
										ass.continueAlgorithm();
									}
									else
									{
										getButtonPause().setText("Continue");
										ass.pauseAlgorithm();
									}
								}
							}
						}
					}
				}
			});
		}
		
		return buttonPause;
	}
	
	private JButton getButtonStop()
	{
		if(buttonStop == null)
		{
			buttonStop = new JButton("Stop");
			buttonStop.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					int iAlgorithmSelection = getCbAlgorithmSelection().getSelectedIndex();
					
					if(threadAlgoRun != null)
					{
						if(threadAlgoRun.isAlive())
						{
							getButtonPause().setText("Pause");
							
							if(iAlgorithmSelection == 0)
							{
								if(bfs != null)
									bfs.stopAlgorithm();
							}
							else if(iAlgorithmSelection == 1)
							{
								if(dfs != null)
									dfs.stopAlgorithm();
							}
							else if(iAlgorithmSelection == 2)
							{
								if(ids != null)
									ids.stopAlgorithm();
							}
							else if((iAlgorithmSelection == 3) || (iAlgorithmSelection == 4))
							{
								if(ass != null)
									ass.stopAlgorithm();
							}
						}
					}
				}
			});
		}
		
		return buttonStop;
	}
	
	private JButton getButtonNext()
	{
		if(buttonNext == null)
		{
			buttonNext = new JButton("Next");
			buttonNext.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					int iAlgorithmSelection = getCbAlgorithmSelection().getSelectedIndex();
					
					if(threadAlgoRun != null)
					{
						if(threadAlgoRun.isAlive())
						{	
							if(iAlgorithmSelection == 0)
							{
								if(bfs != null)
									bfs.stepAlgorithm();
							}
							else if(iAlgorithmSelection == 1)
							{
								if(dfs != null)
									dfs.stepAlgorithm();
							}
							else if(iAlgorithmSelection == 2)
							{
								if(ids != null)
									ids.stepAlgorithm();
							}
							else if((iAlgorithmSelection == 3) || (iAlgorithmSelection == 4))
							{
								if(ass != null)
									ass.stepAlgorithm();
							}
						}
					}
				}
			});
		}
		
		return buttonNext;
	}
	
	private class AlgoRunThread extends Thread
	{	
        public void run()
        {
        	int iAlgorithmSelection = getCbAlgorithmSelection().getSelectedIndex();
        	PuzzleState goalState = null;
        	
        	if(iAlgorithmSelection == 0)
			{
				bfs = new BreadthFirstSearch<PuzzleState>(getPuzzleView().getPuzzleState());
				bfs.setPuzzleWindow(getPuzzleView());
				bfs.setLogWindow(getLogWindow());
				
				goalState = bfs.startBFS();
				iTotalExpandedNode += bfs.getExpandedNodeCnt();
				iTotalMemoryStorage += bfs.getMemoryStorage();
				if(goalState != null)
					generateSolution(goalState);
			}
			else if(iAlgorithmSelection == 1)
			{
				dfs = new MemoizingDepthFirstSearch<PuzzleState>(getPuzzleView().getPuzzleState());
				dfs.setPuzzleWindow(getPuzzleView());
				dfs.setLogWindow(getLogWindow());
				
				goalState = dfs.startDFS();
				iTotalExpandedNode += dfs.getExpandedNodeCnt();
				iTotalMemoryStorage += dfs.getMemoryStorage();
				if(goalState != null)
					generateSolution(goalState);
			}
			else if(iAlgorithmSelection == 2)
			{
				ids = new IterativeDeepeningSearch<PuzzleState>(getPuzzleView().getPuzzleState());
				ids.setPuzzleWindow(getPuzzleView());
				ids.setLogWindow(getLogWindow());
				
				goalState = ids.startIDS();
				iTotalExpandedNode += ids.getExpandedNodeCnt();
				iTotalMemoryStorage += ids.getMemoryStorage();
				if(goalState != null)
					generateSolution(goalState);
			}
			else if(iAlgorithmSelection == 3)
			{
				ass = new AStarSearch<PuzzleState>(getPuzzleView().getPuzzleState(), AStarSearch.HEURISTIC_MISPLACED);
				ass.setPuzzleWindow(getPuzzleView());
				ass.setLogWindow(getLogWindow());
				
				goalState = ass.startAStar();
				iTotalExpandedNode += ass.getExpandedNodeCnt();
				iTotalMemoryStorage += ass.getMemoryStorage();
				if(goalState != null)
					generateSolution(goalState);
			}
        	
			else if(iAlgorithmSelection == 4)
			{
				ass = new AStarSearch<PuzzleState>(getPuzzleView().getPuzzleState(), AStarSearch.HEURISTIC_MANHATTAN);
				ass.setPuzzleWindow(getPuzzleView());
				ass.setLogWindow(getLogWindow());
				
				goalState = ass.startAStar();
				iTotalExpandedNode += ass.getExpandedNodeCnt();
				iTotalMemoryStorage += ass.getMemoryStorage();
				if(goalState != null)
					generateSolution(goalState);
			}
        }
    }
	
	private void generateSolution(PuzzleState goalState)
	{
		int iPathLength = -1;
		PuzzleState state = goalState;
		String sSolution = "Solution Path: ";
		Vector<String> vecString = new Vector<String>();
		
		while(state != null)
		{
			vecString.add("[" + state.getMissingTileX() + "," + state.getMissingTileY() + "]");
			state = state.getBackPointer();
			iPathLength++;
		}
		
		while(vecString.size() != 0)
			sSolution += (vecString.remove(vecString.size() - 1) + " ");
		
		getLogWindow().insertToLogWindow("Path Length: " + iPathLength);
		getLogWindow().insertToLogWindow(sSolution);
	}
	
	public void setPuzzleView(PanelPuzzleView panelPuzzleView)
	{
		this.panelPuzzleView = panelPuzzleView;
	}
	
	private PanelPuzzleView getPuzzleView()
	{
		return panelPuzzleView;
	}
	
	public void setLogWindow(PanelOutputWindow panelLogWindow)
	{
		this.panelLogWindow = panelLogWindow;
	}
	
	public PanelOutputWindow getLogWindow()
	{
		return panelLogWindow;
	}
}
