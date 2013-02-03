package com.Communcation;

import java.io.Serializable;

public class AccountInfo implements Serializable
{	
	private static AccountInfo instance;
	public String emailAccount;
	public String password;
	public String pdfName;
	public String comparePdfName;
	public String spreadsheetName;
	public String reportingLink;

	private AccountInfo(){};
	
	public static AccountInfo getAccountInfo()
	{
		if(instance == null)
		{
			instance = SettingsView.readInFile();
			if(instance == null)
			{
				instance = new AccountInfo();
			}
		}
		
		return instance;
	}
}
