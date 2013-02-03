package com.Communcation;

public class DefaultMessages {
	
	public static String notReportedEmail = "Brother <Last Name>,\n\n" +
			"We are trying to collect home teaching numbers for the month of <Month>.\n" +
			"Can you please help us out so we dont have to make phone calls by clicking on the" +
			" attached link and filling out the form. It should populate with all the relevant information of " +
			"companion and families you home teach and only take a few seconds to fill out and submit.\n\n" +
			"<Reporting Link>\n\n" +
			"Here is your latest Home Teaching assingment as well.\n" +
			"<Assignment>" +
			"\nThanks,\n\nFounders Park 2nd Ward Elders Quorum";

	public static String notReportedText = "<Month> Home Teaching." +
			" Please fill out the link in the email, or respond to texts for each family with a yes or no." +
			" Thanks.";
	
	public static String notReportedFamText = "For <Month>, were you able to home teach the <Family>s?";
	
	public static String reportingLink = "https://docs.google.com/spreadsheet/viewform?fromEmail=true&formkey=dDIzRXVxMVhkNk96endpZ3M5T0V4WHc6MA";

	public static String[] famKeys = new String[] {"&entry_7=","&entry_10=","&entry_15=","&entry_21="};
	public static String monthKey = "&entry_2="; // month key for google form
	public static String yourName = "&entry_3="; // persons name key for google form
	public static String compName = "&entry_4="; // companions name key for google form
	public static String yourDL = "&entry_6="; // home teacher's district leader
	
	public static String userName = "founderspark2elders@gmail.com";
	public static String password = "founderspark2elders";
	
	public static String[] districtLeaders = new String[] {"1 - Spencer Criddle", "2 - Matt Newman", "3 - Phil McDonald", "4 - Michael Buster"};
}
