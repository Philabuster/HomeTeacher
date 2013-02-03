package com.Communcation;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class InboxReader {
	
	private Message messages[] = null;

	public InboxReader(String username, String password)
	{
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
			try {
				Session session = Session.getDefaultInstance(props, null);
				Store store = session.getStore("imaps");
				store.connect("imap.gmail.com", username, password);
				System.out.println(store);

				Folder inbox = store.getFolder("Inbox");
				inbox.open(Folder.READ_ONLY);
//				messages = inbox.getMessages();
				FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
				messages = inbox.search(ft);
				
				for(Message message:messages) {
				System.out.println(message);
			}
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	public Message[] getMessages()
	{
		return messages;
	}
}
