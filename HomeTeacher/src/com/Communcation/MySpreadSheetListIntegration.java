package com.Communcation;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

public class MySpreadSheetListIntegration 
{
	private SpreadsheetService service = null;
	private SpreadsheetEntry spreadSheet = null;
	private WorksheetEntry workSheet = null;
	
	public MySpreadSheetListIntegration (String userName, String password) 
			throws AuthenticationException, MalformedURLException, IOException, ServiceException
	{
		AccountInfo ai = AccountInfo.getAccountInfo();
		if(ai.spreadsheetName == null)
		{
			JOptionPane.showMessageDialog(null, "missing name of spreadsheet to read from Google docs.\nGo to settings and enter in a name");
			return;
		}
	    service = new SpreadsheetService("MySpreadsheetIntegration-v1");
	    service.setUserCredentials(userName, password);
	        
        // Define the URL to request.  This should never change.
        URL SPREADSHEET_FEED_URL = new URL(
            "https://spreadsheets.google.com/feeds/spreadsheets/private/full");

        // Make a request to the API and get all spreadsheets.
        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        // Iterate through all of the spreadsheets returned
        for (SpreadsheetEntry spreadsheet : spreadsheets) {
          if(spreadsheet.getTitle().getPlainText().equals(ai.spreadsheetName))
          {
        	  this.spreadSheet = spreadsheet;
          }
        }
        
        if( this.spreadSheet != null )
        {
        	List<WorksheetEntry> worksheets = spreadSheet.getWorksheets();
        	for(WorksheetEntry ws : worksheets)
        	{
        		this.workSheet = ws;
        	}
        }
        else
        {
			JOptionPane.showMessageDialog(null, "could not find the spreadsheet " + ai.spreadsheetName + "\nPlease change the name in the settings.");
			return;
        }
        
        // Fetch the list feed of the worksheet.
        URL listFeedUrl = workSheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        // Iterate through each row, printing its cell values.
//        for (ListEntry row : listFeed.getEntries()) {
//          // Print the first column's cell value
//          System.out.print(row.getTitle().getPlainText() + "\t");
//          // Iterate over the remaining columns, and print each cell value
//          for (String tag : row.getCustomElements().getTags()) {
//            System.out.print(row.getCustomElements().getValue(tag) + "\t");
//          }
//          System.out.println();
//        }
	}
	
	public WorksheetEntry getWorksheet()
	{
		return workSheet;
	}
	
	public List<String> reportedThisMonth(String month)
	{
		List<String> reported = new ArrayList<String>();
		List<ListEntry> allRows = new ArrayList<ListEntry>();
		
		if(workSheet == null)
		{
			return null;
		}
		
		// Fetch the list feed of the worksheet.
        URL listFeedUrl = workSheet.getListFeedUrl();
        ListFeed listFeed = null;
        
		try {
			listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
          // Iterate over the remaining columns, and print each cell value
        	allRows.add(row);
          for (String tag : row.getCustomElements().getTags()) {
            System.out.print(row.getCustomElements().getValue(tag) + "\t");
          }
          System.out.println();
        }
        
        boolean found = false;
        row:for(int i = allRows.size()-1; i > -1; i--)
        {
        	for(String tag : allRows.get(i).getCustomElements().getTags())
        	{
        		if(!tag.equals("reportingforthemonthof") &&
        				!tag.equals("yourname") && !tag.equals("companions") )
        		{
        			continue;
        		}
        		else if(tag.equals("reportingforthemonthof"))
        		{
        			if(allRows.get(i).getCustomElements().getValue(tag).equalsIgnoreCase(month))
        			{
        				continue;
        			}
        			else
        			{
        				continue row;
        			}
        		}
    			if(tag.equals("yourname"))
    			{
    				reported.add(allRows.get(i).getCustomElements().getValue(tag)); //add the person who reported
    				found = true;
    			}
    			else if(tag.equals("companions"))
    			{
    				reported.add(allRows.get(i).getCustomElements().getValue(tag)); //add their companion
    				continue row;
    			}
        	}
        	
        	if(found == true)
        	{
        		break;
        	}
        }
        
        return reported;
	}
	
	
}
