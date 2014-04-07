package puzzle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.TitledBorder;

import datastructures.PuzzleState;

import utils.RandomLists;
import extend.PanelFileBrowser;

public class PanelPuzzleView extends JPanel 
{
	private JPanel panelFileBrowseSave = null;
	private JPanel panelVisualState = null;
	private JPanel panelFileBrowseLoad = null;
	
	private JButton[][] buttonArray = null;
	
	private JPopupMenu popUpMenu = null;
	
	private PanelOutputWindow panelOutputWindow = null;
	
	private PuzzleState puzzleState;
	
	public PanelPuzzleView()
	{
		super(new BorderLayout(2, 2));
		setBorder(new TitledBorder("Puzzle State"));
		setPreferredSize(new Dimension(500, 550));
		
		GridBagConstraints gbcFileBrowseLoad = new GridBagConstraints();
		gbcFileBrowseLoad.gridx = 0;
		gbcFileBrowseLoad.gridy = 0;
		gbcFileBrowseLoad.insets = new Insets(2, 2, 2, 2);
		gbcFileBrowseLoad.fill = GridBagConstraints.BOTH;
		add(getPanelFileBrowseLoad(), BorderLayout.NORTH);
		
		GridBagConstraints gbcFileBrowseSave = new GridBagConstraints();
		gbcFileBrowseSave.gridx = 0;
		gbcFileBrowseSave.gridy = 1;
		gbcFileBrowseSave.insets = new Insets(2, 2, 2, 2);
		gbcFileBrowseSave.fill = GridBagConstraints.BOTH;
		add(getPanelFileBrowseSave(), BorderLayout.CENTER);
		
		GridBagConstraints gbcVisualState = new GridBagConstraints();
		gbcVisualState.gridx = 0;
		gbcVisualState.gridy = 2;
		gbcVisualState.insets = new Insets(2, 2, 2, 2);
		gbcVisualState.fill = GridBagConstraints.BOTH;
		add(getPanelVisualPuzzleState(), BorderLayout.SOUTH);
	}
	
	private void randomlyUpdatePuzzleVisualState(int iSize)
	{
		setPuzzleState(new PuzzleState(RandomLists.generateShuffledIntDoubleArray(iSize, iSize)));
	}
	
	private JPopupMenu getPopUpMenu()
	{
		if(popUpMenu == null)
		{
			popUpMenu = new JPopupMenu("Menu");
			
			JMenuItem menuItemRandomize3 = new JMenuItem("Randomize 3x3");
			menuItemRandomize3.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					randomlyUpdatePuzzleVisualState(3);
				}
			});
			
			JMenuItem menuItemRandomize5 = new JMenuItem("Randomize 5x5");
			menuItemRandomize5.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					randomlyUpdatePuzzleVisualState(5);
				}
			});
			
			JMenuItem menuItemRandomize7 = new JMenuItem("Randomize 7x7");
			menuItemRandomize7.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					randomlyUpdatePuzzleVisualState(7);
				}
			});
			
			popUpMenu.add(menuItemRandomize3);
			popUpMenu.add(menuItemRandomize5);
			popUpMenu.add(menuItemRandomize7);
		}
		
		return popUpMenu;
	}
	
	private JPanel getPanelFileBrowseSave()
	{
		if(panelFileBrowseSave == null)
		{
			panelFileBrowseSave = new JPanel(new GridBagLayout());
			panelFileBrowseSave.setBorder(new TitledBorder("Save to File"));
			
			final PanelFileBrowser panelFileBrowse = new PanelFileBrowser(PanelFileBrowser.FILE_DIALOG_SAVE);
			panelFileBrowse.setFileExtensionFilter("Puzzle Files (*.puzzle)", "puzzle");
			
			JButton buttonSave = new JButton("Save");
			buttonSave.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					File file = new File(panelFileBrowse.getFilePath());
					
					try 
					{
						file.createNewFile();
						PuzzleFileReadWrite.writeToPuzzleFile(file, getPuzzleState().getData());
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			});
			
			GridBagConstraints gbcFileBrowse = new GridBagConstraints();
			gbcFileBrowse.gridx = 0;
			gbcFileBrowse.gridy = 0;
			gbcFileBrowse.insets = new Insets(2, 2, 2, 2);
			gbcFileBrowse.fill = GridBagConstraints.BOTH;
			gbcFileBrowse.weightx = 1.0;
			panelFileBrowseSave.add(panelFileBrowse, gbcFileBrowse);
			
			GridBagConstraints gbcSave = new GridBagConstraints();
			gbcSave.gridx = 1;
			gbcSave.gridy = 0;
			gbcSave.insets = new Insets(2, 2, 2, 2);
			gbcSave.fill = GridBagConstraints.BOTH;
			panelFileBrowseSave.add(buttonSave, gbcSave);
		}
		
		return panelFileBrowseSave;
	}
	
	private JPanel getPanelFileBrowseLoad()
	{
		if(panelFileBrowseLoad == null)
		{
			panelFileBrowseLoad = new JPanel(new GridBagLayout());
			panelFileBrowseLoad.setBorder(new TitledBorder("Load from File"));
			
			final PanelFileBrowser panelFileBrowse = new PanelFileBrowser(PanelFileBrowser.FILE_DIALOG_OPEN);
			panelFileBrowse.setFileExtensionFilter("Puzzle Files (*.puzzle)", "puzzle");
			
			JButton buttonLoad = new JButton("Load");
			buttonLoad.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					File file = new File(panelFileBrowse.getFilePath());
					
					if(!file.exists())
					{
						JOptionPane.showMessageDialog(null, "File not exists!", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						setPuzzleState(new PuzzleState(PuzzleFileReadWrite.parsePuzzleFile(file)));
					}
				}
			});
			
			GridBagConstraints gbcFileBrowse = new GridBagConstraints();
			gbcFileBrowse.gridx = 0;
			gbcFileBrowse.gridy = 0;
			gbcFileBrowse.insets = new Insets(2, 2, 2, 2);
			gbcFileBrowse.fill = GridBagConstraints.BOTH;
			gbcFileBrowse.weightx = 1.0;
			panelFileBrowseLoad.add(panelFileBrowse, gbcFileBrowse);
			
			GridBagConstraints gbcLoad = new GridBagConstraints();
			gbcLoad.gridx = 1;
			gbcLoad.gridy = 0;
			gbcLoad.insets = new Insets(2, 2, 2, 2);
			gbcLoad.fill = GridBagConstraints.BOTH;
			panelFileBrowseLoad.add(buttonLoad, gbcLoad);
		}
		
		return panelFileBrowseLoad;
	}
	
	private JPanel getPanelVisualPuzzleState()
	{
		if(panelVisualState == null)
		{
			panelVisualState = new JPanel();
			panelVisualState.setLayout(new GridLayout(getPuzzleState().getData().length, getPuzzleState().getData()[0].length));
			panelVisualState.setPreferredSize(new Dimension(400, 400));
			panelVisualState.setBorder(new TitledBorder(""));
			
			buttonArray = new JButton[getPuzzleState().getData().length][getPuzzleState().getData()[0].length];
			for (int iRowNo = 0; iRowNo < getPuzzleState().getData().length; iRowNo++) 
			{
				for (int iColNo = 0; iColNo < getPuzzleState().getData()[0].length; iColNo++)
				{
					buttonArray[iRowNo][iColNo] = new JButton();
					buttonArray[iRowNo][iColNo].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
					buttonArray[iRowNo][iColNo].setEnabled(false);
					buttonArray[iRowNo][iColNo].setInheritsPopupMenu(true);
					
					if(getPuzzleState().getData()[iRowNo][iColNo] != 0)
						buttonArray[iRowNo][iColNo].setText(String.valueOf(getPuzzleState().getData()[iRowNo][iColNo]));
					else
						buttonArray[iRowNo][iColNo].setText(String.valueOf(""));

					panelVisualState.add(buttonArray[iRowNo][iColNo]);
				}
			}
			
			panelVisualState.setComponentPopupMenu(getPopUpMenu());
		}
		
		return panelVisualState;
	}
	
	public void setPuzzleState(PuzzleState puzzleNewState)
	{
		puzzleState = puzzleNewState;
		
		remove(getPanelVisualPuzzleState());
		panelVisualState = null;
		add(getPanelVisualPuzzleState(), BorderLayout.SOUTH);
		
		validate();
	}
	
	public PuzzleState getPuzzleState()
	{
		if(puzzleState == null)
		{
			Integer[][] iDoubArrPuzzleState = new Integer[3][3];
			
			for (int iRowNo = 0; iRowNo < iDoubArrPuzzleState.length; iRowNo++) 
			{
				for (int iColNo = 0; iColNo < iDoubArrPuzzleState[0].length; iColNo++) 
				{
					iDoubArrPuzzleState[iRowNo][iColNo] = 0;
				}
			}
			
			puzzleState = new PuzzleState(iDoubArrPuzzleState);
		}
		return puzzleState;
	}
	
	public void setLogWindow(PanelOutputWindow panelOutputWindow)
	{
		this.panelOutputWindow = panelOutputWindow;
	}
}
