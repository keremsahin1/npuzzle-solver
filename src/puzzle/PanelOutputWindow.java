package puzzle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class PanelOutputWindow extends JPanel
{
	private JScrollPane scrollPane = null;
	private JTextArea textArea = null;
	private JPopupMenu popUpMenu = null;
	
	public PanelOutputWindow()
	{
		super(new BorderLayout(2, 2));
		setBorder(new TitledBorder("Logs"));
		
		add(getScrollPane(), BorderLayout.CENTER);
	}
	
	private JScrollPane getScrollPane()
	{
		if(scrollPane == null)
		{
			scrollPane = new JScrollPane();
			scrollPane.setPreferredSize(new Dimension(400, 400));
			scrollPane.setViewportView(getTextArea());
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		
		return scrollPane;
	}
	
	private JTextArea getTextArea()
	{
		if(textArea == null)
		{
			textArea = new JTextArea();
			textArea.setEditable(false);
			textArea.setComponentPopupMenu(getPopUpMenu());
			
			DefaultCaret caret = (DefaultCaret) textArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		}
		
		return textArea;
	}
	
	private JPopupMenu getPopUpMenu()
	{
		if(popUpMenu == null)
		{
			popUpMenu = new JPopupMenu("Menu");
			
			JMenuItem menuItemClear = new JMenuItem("Clear");
			menuItemClear.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					getTextArea().setText("");
				}
			});
			
			popUpMenu.add(menuItemClear);
		}
		
		return popUpMenu;
	}
	
	public void insertToLogWindow(final String s)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() 
			{
				getTextArea().append(s + "\n");
			}
		});
	}
}
