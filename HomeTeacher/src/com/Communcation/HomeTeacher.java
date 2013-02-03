package com.Communcation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class HomeTeacher {
	
	List<District> districts = new ArrayList<District>();

	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		Ward ward = null;
		AccountInfo ai = SettingsView.readInFile();
		
		if(ai == null)
		{
			new GUI(ward);
		}
		else
		{
			try
			{
				String userName = ai.emailAccount;
				String password = ai.password;
				
				DocumentList doc = new DocumentList(userName, password);
				
				doc.downLoadFile(ai.pdfName, "pdf", ai.pdfName);
				String pdfInfo = doc.readInFamiles(ai.pdfName);
				
				if(pdfInfo != null)
				{
					PDFParser pdf = new PDFParser(pdfInfo);
					
					ward = pdf.createWard();
				}
				
				Contacts gmailInfo = new Contacts(userName, password);
				
				gmailInfo.retrieveAllContacts();
				
				
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
					
					new GUI(ward);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
//				JOptionPane.showMessageDialog(null, "Missing or Bad Info. Please update credentials");
				new GUI(null);
			}
		}

	}

}
