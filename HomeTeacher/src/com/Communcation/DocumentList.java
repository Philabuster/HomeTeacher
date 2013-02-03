package com.Communcation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.google.gdata.client.Query;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ServiceException;

public class DocumentList {
	
	private DocsService docsService;
	private DocumentListFeed feed;
	private URL feedUrl; 
	
	public DocumentList( String userName, String password ) throws Exception
	{
//		try
//		{
			docsService = new DocsService("HomeTeacher");  
			docsService.setUserCredentials(userName, password);  
			feedUrl = new URL("https://docs.google.com/feeds/default/private/full");
//		}
//		catch ( Exception e)
//		{
//			e.printStackTrace();
//		}
	} 
	
	/**
	 * Downloads a file of the name and type from the google doc account to the specified location
	 * @param fileName - name of the file on Google Doc
	 * @param fileType - extension of file i.e. pdf
	 * @param downloadLocation - file path and name of downloaded document
	 * @return
	 * @throws ServiceException 
	 * @throws IOException 
	 */
	public boolean downLoadFile(String fileName, String fileType, String downloadLocation) throws IOException, ServiceException
	{
		boolean success = false;
		Query myQuery = new Query(feedUrl);
		
//		try {
			feed = docsService.getFeed(myQuery,DocumentListFeed.class);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		} catch (ServiceException e1) {
//			e1.printStackTrace();
//		}

		for (DocumentListEntry entry : feed.getEntries()) 
		{				
			System.out.println(entry.getType());
			System.out.println(entry.getTitle().getPlainText());
			if(entry.getType().equals(fileType) && 
					entry.getTitle().getPlainText().equalsIgnoreCase(fileName))
			{
				MediaContent content = (MediaContent) entry.getContent();
				MediaSource ms = null;
//				try {
					ms = docsService.getMedia(content);
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (ServiceException e) {
//					e.printStackTrace();
//				}
				
				InputStream inStream = null;
			    FileOutputStream outStream = null;

//			    try
//			    {
				    try 
				    {
				    	inStream = ms.getInputStream();
				    	outStream = new FileOutputStream(downloadLocation);
	
				    	int c;
				    	while ((c = inStream.read()) != -1)
				    	{
				    		outStream.write(c);
				    	}
				    } 
				    finally 
				    {
				    	if (inStream != null) 
				    	{
				    		inStream.close();
				    	}
				    	if (outStream != null)
				    	{
				    		outStream.flush();
				    		outStream.close();
				    	}
				    }
				    success = true;
					break;
			    }
//			    catch (IOException io)
//			    {
//			    	io.printStackTrace();
//			    }
			    
			    
//			}
		}
		
		
		return success;
	}
	
	/**
	 * This is designed to find a named pdf on the current computer, that contains all the home teaching companionships
	 * and their families and return it in a list containing every home teaching assignment.
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public String readInFamiles(String pdfName) throws IOException
	{
		PDDocument doc;
		
//		try {
			doc = PDDocument.load( pdfName );
			PDFTextStripper stripper = new PDFTextStripper();
			
			return stripper.getText(doc);
			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}


	
	
}
