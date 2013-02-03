package com.Communcation;

import javax.swing.JPanel;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;

public class SettingsView implements ActionListener
{
	private JPanel view = null;
	private GUI parent;
	private JTextField accountTextField;
	private JTextField passwordTextField;
	private JTextField spreadsheetTextField;
	private JTextField pdfNameTextField;
	private JTextField pdfComparisonTextField;
	private JButton btnSave;
	private JButton btnClear;
	private JLabel lblReportingLink;
	private JTextField reportingLinkTextField;
	
	public SettingsView(GUI parent)
	{
		this.parent = parent;
		initView();
	}
	
	private void initView()
	{
		view = new JPanel();
		view.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("15dlu"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("15dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("15dlu"),}));
		
		JLabel lblGmailEmailAccount = new JLabel("Gmail Email Account");
		view.add(lblGmailEmailAccount, "2, 4");
		
		accountTextField = new JTextField();
		view.add(accountTextField, "2, 6, fill, default");
		accountTextField.setColumns(10);
		
		JLabel lblGmailPassword = new JLabel("Gmail Password");
		view.add(lblGmailPassword, "2, 8");
		
		passwordTextField = new JTextField();
		view.add(passwordTextField, "2, 10, fill, default");
		passwordTextField.setColumns(10);
		
		JLabel lblSpreadsheetName = new JLabel("Spreadsheet Name");
		view.add(lblSpreadsheetName, "2, 12");
		
		spreadsheetTextField = new JTextField();
		view.add(spreadsheetTextField, "2, 14, fill, default");
		spreadsheetTextField.setColumns(10);
		
		JLabel lblHomeTeachersPdf = new JLabel("Home Teachers PDF Name");
		view.add(lblHomeTeachersPdf, "2, 16");
		
		pdfNameTextField = new JTextField();
		view.add(pdfNameTextField, "2, 18, fill, default");
		pdfNameTextField.setColumns(10);
		
		JLabel lblOldHomeTeachers = new JLabel("Old Home Teachers PDF Name for comparison");
		view.add(lblOldHomeTeachers, "2, 20");
		
		pdfComparisonTextField = new JTextField();
		view.add(pdfComparisonTextField, "2, 22, fill, default");
		pdfComparisonTextField.setColumns(10);
		
		lblReportingLink = new JLabel("Reporting Link ");
		view.add(lblReportingLink, "2, 24");
		
		reportingLinkTextField = new JTextField();
		view.add(reportingLinkTextField, "2, 26, fill, default");
		reportingLinkTextField.setColumns(10);
		
		JPanel panel = new JPanel();
		view.add(panel, "2, 28, fill, fill");
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(this);
		panel.add(btnSave);
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(this);
		panel.add(btnClear);
		
		AccountInfo ai = readInFile();
		if(ai != null)
		{
			accountTextField.setText(ai.emailAccount);
	        passwordTextField.setText(ai.password);
	        pdfNameTextField.setText(ai.pdfName);
	        pdfComparisonTextField.setText(ai.comparePdfName);
	        spreadsheetTextField.setText(ai.spreadsheetName);
	        reportingLinkTextField.setText(ai.reportingLink);
		}
	}
	
	public JPanel getView()
	{
		return view;
	}
	
	public static AccountInfo readInFile()
	{
		AccountInfo ai = null;
		File info = new File("accountInfo.dat");
		if(info.exists())
		{
			
			try
			{
			      //use buffering
				InputStream file = new FileInputStream( info );
				InputStream buffer = new BufferedInputStream( file );
				ObjectInput input = new ObjectInputStream ( buffer );
				try
				{
					//deserialize the info
			        ai = (AccountInfo)input.readObject();
				}
				finally
				{
					input.close();
				}
		    }
		    catch(ClassNotFoundException ex)
		    {
		    	ex.printStackTrace();
		    }
		    catch(IOException ex)
		    {
		    	ex.printStackTrace();
		    }
			
		}
		
		return ai;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == btnSave)
		{
			final JDialog d = new JDialog();  
	        MigLayout mig = new MigLayout("","10[grow][grow]10","10[grow]10");
			
			JPanel p1 = new JPanel(mig);
			p1.setBackground(Color.white);
	       
	        d.getContentPane().add(p1);  
	        d.setLocationRelativeTo(parent.mainFrame);  
	        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);  
	        d.setModal(true);  
			
			p1.add(new JLabel("Validating Information"), "cell 1 0");
			p1.add(new JLabel(parent.loading), "cell 2 0");
			d.pack();  
			d.setResizable(false);
			
			SwingWorker<?,?> worker = new SwingWorker<Void,Void>()
				{  
		          	protected Void doInBackground() throws InterruptedException
		          	{  
		          		boolean allInfo = true;
		    			AccountInfo ai = AccountInfo.getAccountInfo();
		    			ai.emailAccount = accountTextField.getText().trim();
		    			if(ai.emailAccount.equals(""))
		    			{
		    				allInfo = false;
		    			}
		    			ai.password = passwordTextField.getText().trim();
		    			if(ai.password.equals(""))
		    			{
		    				allInfo = false;
		    			}
		    			ai.spreadsheetName = spreadsheetTextField.getText().trim();
		    			if(ai.spreadsheetName.equals(""))
		    			{
		    				allInfo = false;
		    			}
		    			ai.pdfName = pdfNameTextField.getText().trim();
		    			if(ai.pdfName.equals(""))
		    			{
		    				allInfo = false;
		    			}
		    			ai.reportingLink = reportingLinkTextField.getText().trim();
		    			if(ai.reportingLink.equals(""))
		    			{
		    				allInfo = false;
		    			}
		    			ai.comparePdfName = pdfComparisonTextField.getText().trim();
		    			if(ai.comparePdfName.equals(""))
		    			{
		    				allInfo = false;
		    			}
		    			
		    			if(!allInfo)
		    			{
		    				JOptionPane.showMessageDialog(parent.mainFrame, "Missing Info. All fields must be filled in");
		    				return null;
		    			}
		    			
		    			try
		    			{
		    				 
		    				FileOutputStream fout = new FileOutputStream("accountInfo.dat");
		    				ObjectOutputStream oos = new ObjectOutputStream(fout);   
		    				oos.writeObject(ai);
		    				oos.close();
		    				System.out.println("Done");
		    		 
		    			}
		    			catch(Exception ex)
		    			{
		    			   ex.printStackTrace();
		    			}
		    			
//		    			if( ai.emailAccount.equals( accountTextField.getText().trim() ) &&
//		    					ai.password.equals( passwordTextField.getText().trim() ) &&
//		    					ai.spreadsheetName.equals( spreadsheetTextField.getText().trim() ) && 
//		    					ai.pdfName.equals( pdfNameTextField.getText().trim() ) &&
//		    					ai.comparePdfName.equals( pdfComparisonTextField.getText().trim() ) &&
//		    					ai.reportingLink.equals( reportingLinkTextField.getText().trim() ))
//		    			{
//		    				parent.switchFromSettingsView();
//		    				return;
//		    			}
		    			
		    			String userName = ai.emailAccount;
		    			String password = ai.password;
		    			Ward ward = null;
		    			
		    			DocumentList doc;
		    			String pdfInfo = null;
		    			try {
		    				doc = new DocumentList(userName, password);
		    				
		    				doc.downLoadFile(ai.pdfName, "pdf", ai.pdfName);
		    				pdfInfo = doc.readInFamiles(ai.pdfName);
		    			} catch (Exception e1) {
		    				// TODO Auto-generated catch block
		    				e1.printStackTrace();
		    				JOptionPane.showMessageDialog(parent.mainFrame, "Something went wrong. Make sure all info is correct.");
		    				return null;
		    			}
		    			
		    			if(pdfInfo != null)
		    			{
		    				PDFParser pdf = new PDFParser(pdfInfo);
		    				
		    				ward = pdf.createWard();
		    			}
		    			
		    			Contacts gmailInfo = new Contacts(userName, password);
		    			try {
		    				gmailInfo.retrieveAllContacts();
		    			} catch (ServiceException err) {
		    				err.printStackTrace();
		    			} catch (IOException err) {
		    				err.printStackTrace();
		    			}
		    			
		    			List<GmailInfo> gmailList = gmailInfo.getContacts();
		    			
		    			if(ward != null && gmailList != null)
		    			{
		    				for(Person p : ward.getAllHomeTeachers())
		    				{
		    					for(GmailInfo g : gmailList)
		    					{
		    						if(p.isThisPerson(g.getFirstName(), g.getLastName()))
		    						{
		    							p.setGmailInfo(g);
		    						}
		    					}
		    				}
		    			}
		    			
		    			if(ward != null)
		    			{
		    				parent.updateWard(ward);
		    				parent.switchFromSettingsView();
		    			}
		          		return null;  
		          	}  
		  
		  
		          	protected void done()
		          	{  
		          		d.dispose();  
		          	}  
		        }; 
		        
		        worker.execute();  
		        try
		        {
		        	d.setVisible(true);
		        }
		        catch(Exception ex)
		        {
		        	//do nothing
		        }
		}
		else if(e.getSource() == btnClear)
		{
			accountTextField.setText("");
			passwordTextField.setText("");
			spreadsheetTextField.setText("");
			pdfNameTextField.setText("");
			reportingLinkTextField.setText("");
		}
		
	}
}
