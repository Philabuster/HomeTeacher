package com.Communcation;

public class GmailInfo {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	
	public GmailInfo(String firstName, String lastName, String email, String phone)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getPhone()
	{
		return phone;
	}

}