package com.Communcation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class MessageBox extends JDialog implements Observer
{
	private String imagePath = "images/loading.gif";
	private JTextArea status = null;
	private Container dialog = null;
	public boolean proceed = false;
	
	public MessageBox(JFrame frame, String message, JTextArea status)
	{
		super(frame, true);
		dialog = getContentPane();
		this.status = status;
		
		JLabel animatedGif = createImageIcon(imagePath);
		
		MigLayout mig = new MigLayout("","10[grow][grow]10","10[]5[grow]10");
		
		JPanel mainFrame = new JPanel(mig);
		
		JScrollPane scroller = new JScrollPane(status);
		
		mainFrame.add(new JLabel(message), "cell 0 1");
		mainFrame.add(animatedGif, "cell 0 2");
		mainFrame.add(scroller, "cell 1 0,span");
		
		dialog.add("CENTER",mainFrame);
		dialog.setSize(300, 500);
				
		dialog.setVisible(true);
		proceed = true;
	}
	
	public MessageBox(JFrame frame, String message)
	{
		super(frame, true);
		dialog = getContentPane();
		
		JLabel animatedGif = createImageIcon(imagePath);
		
		MigLayout mig = new MigLayout("","10[grow][grow]10","10[]10");
		
		JPanel mainFrame = new JPanel(mig);
		
		mainFrame.add(new JLabel(message), "cell 0 1");
		mainFrame.add(animatedGif, "cell 0 2");
		
		dialog.add(mainFrame, BorderLayout.CENTER);
		setBackground(Color.white);
		pack();
				
		setVisible(true);
	}
	
	public void done()
	{
		setVisible(false);
		dispose();
	}
	
	protected JLabel createImageIcon(String path) 
	{
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) 
		{
			Icon icon = new ImageIcon(imgURL);
			return new JLabel(icon);
		} 
		else 
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	@Override
	public void update(Observable o, Object text) 
	{
		if(text instanceof String)
		{
			final String t = status.getText() + text;
			
			try {
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run() 
					{
						status.setText(t);
					}	
				});
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
	}
}
