package com.Communcation;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMessage {
	
	private String host = "smtp.gmail.com";
    private String from = null; 
    private String password = null;
    private String[] to = null;
    private String port = "587";
    private String subject = null;
    private String message = null;
    
    SendMessage(String from, String password)
    {
    	this.from = from;
    	this.password = password;
    	this.port = port;
    }
    
    public void sendTo( String[] to )
    {
    	this.to = to;
    }
    
    public void setSubject( String subject )
    {
    	this.subject = subject;
    }
    
    public void setMessage( String message )
    {
    	this.message = message;
    }
    
    public boolean sendMessage()
    {
    	boolean sent = true;
    	
    	Properties props = System.getProperties();
 	    props.put("mail.smtp.starttls.enable", "true"); // added this line
 	    props.put("mail.smtp.host", this.host);
 	    props.put("mail.smtp.user", this.from);
 	    props.put("mail.smtp.password", this.password);
 	    props.put("mail.smtp.port", this.port);
 	    props.put("mail.smtp.auth", "true");
 	    
 	   try
	    {
		    Session session = Session.getDefaultInstance(props, null);
		    MimeMessage message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(from));
	
		    InternetAddress[] toAddress = new InternetAddress[this.to.length];
	
		    // To get the array of addresses
		    for( int i=0; i < this.to.length; i++ ) { // changed from a while loop
		        toAddress[i] = new InternetAddress(this.to[i]);
		    }
	
		    for( int i=0; i < toAddress.length; i++) { // changed from a while loop
		        message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		    }
		    message.setSubject(this.subject);
		    message.setText(this.message);
		    Transport transport = session.getTransport("smtp");
		    transport.connect(this.host, this.from, this.password);
		    transport.sendMessage(message, message.getAllRecipients());
		    transport.close();
	    }
	    catch( Exception e)
	    {
	    	e.printStackTrace();
	    	sent = false;
	    }
    	
    	return sent;
    }
	
}
