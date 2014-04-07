package puzzle;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class FramePuzzle extends JFrame
{
	private PanelPuzzleView panelPuzzleState = null;
	private PanelOutputWindow panelOutputWindow = null;
	private PanelSettings panelSettings = null;
	
	public FramePuzzle()
	{
		super("N-Puzzle Solver");
		setLayout(new BorderLayout(2, 2));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		getContentPane().add(getPanelPuzzleView(), BorderLayout.WEST);
		getContentPane().add(getPanelOutputWindow(), BorderLayout.EAST);
		getContentPane().add(getPanelSettings(), BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
	}
	
	private PanelPuzzleView getPanelPuzzleView()
	{
		if(panelPuzzleState == null)
		{
			panelPuzzleState = new PanelPuzzleView();
			panelPuzzleState.setLogWindow(getPanelOutputWindow());
		}
		
		return panelPuzzleState;
	}
	
	private PanelOutputWindow getPanelOutputWindow()
	{
		if(panelOutputWindow == null)
		{
			panelOutputWindow = new PanelOutputWindow();
		}
		
		return panelOutputWindow;
	}
	
	private PanelSettings getPanelSettings()
	{
		if(panelSettings == null)
		{
			panelSettings = new PanelSettings();
			panelSettings.setPuzzleView(getPanelPuzzleView());
			panelSettings.setLogWindow(getPanelOutputWindow());
		}
		
		return panelSettings;
	}
}
