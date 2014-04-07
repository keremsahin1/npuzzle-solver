package extend;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PanelFileBrowser extends JPanel 
{
	private JLabel labelFile = null;
	private JTextField textFieldFilePath = null;
	private JButton buttonBrowse = null;
	
	private FileNameExtensionFilter fileExtensionFilter = null;
	
	public static final int FILE_DIALOG_OPEN = 0;
	public static final int FILE_DIALOG_SAVE = 1;
	
	private int iFileDialogType = 0;
	
	public PanelFileBrowser(int iFileDialogType)
	{
		super(new BorderLayout(2, 2));
		
		this.iFileDialogType = iFileDialogType;
		
		add(getLabelFile(), BorderLayout.WEST);
		add(getTextFieldFilePath(), BorderLayout.CENTER);
		add(getButtonBrowse(), BorderLayout.EAST);
	}
	
	private JLabel getLabelFile()
	{
		if(labelFile == null)
		{
			labelFile = new JLabel("File:");
		}
		
		return labelFile;
	}
	
	private JTextField getTextFieldFilePath()
	{
		if(textFieldFilePath == null)
		{
			textFieldFilePath = new JTextField();
			textFieldFilePath.setEditable(false);
		}
		
		return textFieldFilePath;
	}
	
	private JButton getButtonBrowse()
	{
		if(buttonBrowse == null)
		{
			buttonBrowse = new JButton("Browse");
			buttonBrowse.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(fileExtensionFilter);
					
					int iDialogResult = 0;
					if(iFileDialogType == FILE_DIALOG_SAVE)
						iDialogResult = fileChooser.showSaveDialog(null);
					else
						iDialogResult = fileChooser.showOpenDialog(null);
					
					if(iDialogResult == JFileChooser.APPROVE_OPTION)
					{
						getTextFieldFilePath().setText(fileChooser.getSelectedFile().getPath());
					}
				}
			});
		}
		
		return buttonBrowse;
	}
	
	public void setFileExtensionFilter(String sDescription, String...sExtensions)
	{
		fileExtensionFilter = new FileNameExtensionFilter(sDescription, sExtensions);
	}
	
	public String getFilePath()
	{
		return getTextFieldFilePath().getText();
	}
}
