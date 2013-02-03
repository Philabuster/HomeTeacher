package com.Communcation;

import com.google.gdata.data.*;
import com.google.gdata.data.contacts.*;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Contacts {
	
	private List<GmailInfo> contacts = new ArrayList<GmailInfo>();
	private ContactsService myService;
	private String userEmail;
	private boolean loud = false;
	
	public Contacts(String userEmail, String password)
	{
		this.userEmail = userEmail;
		myService = new ContactsService("<var>HomeTeacher</var>");
		try {
			myService.setUserCredentials(userEmail, password);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void retrieveAllContacts()
		    throws ServiceException, IOException {
		  // Request the feed
		  URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/" + userEmail + "/full");
		  Query myQuery = new Query(feedUrl);
		  myQuery.setMaxResults(5000);
		  
		  ContactFeed resultFeed = myService.getFeed(myQuery, ContactFeed.class);
		  // Print the results
		  if(loud) System.out.println(resultFeed.getTitle().getPlainText());
		  for (ContactEntry entry : resultFeed.getEntries()) {
			  String strFirstName = null;
			  String strLastName = null;
			  String strEmail = null;
			  String strPhone = null;
		    if (entry.hasName()) 
		    {
		      Name name = entry.getName();
		      if (name.hasFullName()) 
		      {
		        String fullNameToDisplay = name.getFullName().getValue();
		        if (name.getFullName().hasYomi()) 
		        {
		          fullNameToDisplay += " (" + name.getFullName().getYomi() + ")";
		        }
		        if(loud) System.out.println("\\\t\\\t" + fullNameToDisplay);
		      } 
		      else
		      {
		        if(loud) System.out.println("\\\t\\\t (no full name found)");
		      }
		      
		      if (name.hasNamePrefix()) 
		      {
		        if(loud) System.out.println("\\\t\\\t" + name.getNamePrefix().getValue());
		      } 
		      else 
		      {
		        if(loud) System.out.println("\\\t\\\t (no name prefix found)");
		      }
		      
		      if (name.hasGivenName()) 
		      {
		        String givenNameToDisplay = name.getGivenName().getValue();
		        if (name.getGivenName().hasYomi()) 
		        {
		          givenNameToDisplay += " (" + name.getGivenName().getYomi() + ")";
		        }
		        if(loud) System.out.println("\\\t\\\t" + givenNameToDisplay);
		        strFirstName = givenNameToDisplay;
		      } 
		      else 
		      {
		        if(loud) System.out.println("\\\t\\\t (no given name found)");
		      }
		      if (name.hasAdditionalName()) 
		      {
		        String additionalNameToDisplay = name.getAdditionalName().getValue();
		        if (name.getAdditionalName().hasYomi()) 
		        {
		          additionalNameToDisplay += " (" + name.getAdditionalName().getYomi() + ")";
		        }
		        if(loud) System.out.println("\\\t\\\t" + additionalNameToDisplay);
		      } 
		      else 
		      {
		        if(loud) System.out.println("\\\t\\\t (no additional name found)");
		      }
		      
		      if (name.hasFamilyName()) 
		      {
		        String familyNameToDisplay = name.getFamilyName().getValue();
		        if (name.getFamilyName().hasYomi()) 
		        {
		          familyNameToDisplay += " (" + name.getFamilyName().getYomi() + ")";
		        }
		        if(loud) System.out.println("\\\t\\\t" + familyNameToDisplay);
		        strLastName = familyNameToDisplay;
		      } 
		      else 
		      {
		        if(loud) System.out.println("\\\t\\\t (no family name found)");
		      }
		      
		      if (name.hasNameSuffix()) 
		      {
		        if(loud) System.out.println("\\\t\\\t" + name.getNameSuffix().getValue());
		      } 
		      else 
		      {
		        if(loud) System.out.println("\\\t\\\t (no name suffix found)");
		      } 
		    } 
		    else 
		    {
		      if(loud) System.out.println("\t (no name found)");
		    }
		    
		    if(loud) System.out.println("Phone Number:");
		    for (PhoneNumber phone : entry.getPhoneNumbers() )
		    {
		    	if(loud) System.out.print(" " + phone.getPhoneNumber() );
		    	if(phone.getLabel() != null )
		    	{
		    		if(loud) System.out.print( " " + phone.getLabel() );
		    	}
		    	
		    	if(loud) System.out.print("\n");
		    }
		    
		    if(loud) System.out.println("Email addresses:");
		    for (Email email : entry.getEmailAddresses()) 
		    {
		      if(loud) System.out.print(" " + email.getAddress());
		      
		      if (email.getRel() != null) 
		      {
		    	  if(loud) System.out.print(" rel:" + email.getRel());
		      }
		      if (email.getLabel() != null) 
		      {
		    	  if(loud) System.out.print(" label:" + email.getLabel());
		    	  if(email.getLabel().equalsIgnoreCase("Text"))
		    	  {
		    		  strPhone = email.getAddress();
		    	  }
		      }
		      if (email.getPrimary()) 
		      {
		    	  if(loud) System.out.print(" (primary) ");
		    	  strEmail = email.getAddress();
		    	  if(strPhone != null && strEmail != null && strPhone.equals(strEmail))
		    	  {
		    		  strEmail = null;
		    	  }
		      }
		      if(loud) System.out.print("\n");
		    }
		    if(loud) System.out.println("IM addresses:");
		    for (Im im : entry.getImAddresses()) {
		    	if(loud) System.out.print(" " + im.getAddress());
		      if (im.getLabel() != null) {
		    	if(loud) System.out.print(" label:" + im.getLabel());
		      }
		      if (im.getRel() != null) {
		    	if(loud) System.out.print(" rel:" + im.getRel());
		      }
		      if (im.getProtocol() != null) {
		    	if(loud) System.out.print(" protocol:" + im.getProtocol());
		      }
		      if (im.getPrimary()) {
		    	if(loud) System.out.print(" (primary) ");
		      }
		      if(loud) System.out.print("\n");
		    }
		    if(loud) System.out.println("Groups:");
		    for (GroupMembershipInfo group : entry.getGroupMembershipInfos()) {
		      String groupHref = group.getHref();
		      if(loud) System.out.println("  Id: " + groupHref);
		    }
		    if(loud) System.out.println("Extended Properties:");
		    for (ExtendedProperty property : entry.getExtendedProperties()) {
		      if (property.getValue() != null) {
		    	  if(loud) System.out.println("  " + property.getName() + "(value) = " +
		            property.getValue());
		      } else if (property.getXmlBlob() != null) {
		    	  if(loud) System.out.println("  " + property.getName() + "(xmlBlob)= " +
		            property.getXmlBlob().getBlob());
		      }
		    }
		    Link photoLink = entry.getContactPhotoLink();
		    String photoLinkHref = photoLink.getHref();
		    if(loud) System.out.println("Photo Link: " + photoLinkHref);
		    if (photoLink.getEtag() != null) {
		    	if(loud) System.out.println("Contact Photo's ETag: " + photoLink.getEtag());
		    }
		    if(loud) System.out.println("Contact's ETag: " + entry.getEtag());
		    
		    if(strFirstName != null && strLastName != null)
	    	{
		       contacts.add(new GmailInfo(strFirstName, strLastName, strEmail, strPhone));
	    	}
		  }
		}
	
	public List<GmailInfo> getContacts()
	{
		return contacts;
	}
}
