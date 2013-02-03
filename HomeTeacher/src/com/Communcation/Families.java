package com.Communcation;

import java.util.ArrayList;
import java.util.List;

public class Families {
	
	private Person headOfHouse;
	private Person wife;
	private String address;
	private List<Person> children = new ArrayList<Person>();
	
	public Families(Person headOfHouse)
	{
		this.headOfHouse = headOfHouse;
		
		if(address == null && headOfHouse.getFullAddress() != null)
		{
			address = headOfHouse.getFullAddress();
		}
	}
	
	public void setWife(Person wife)
	{
		this.wife = wife;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void addChild(Person child)
	{
		children.add(child);
	}
	
	public Person getHeadOfHouse()
	{
		return headOfHouse;
	}
	
	public Person getWife()
	{
		return wife;
	}
	
	public List<Person> getChildren()
	{
		return children;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String toString()
	{
		String fam = null;
		
		if(headOfHouse != null)
		{
			fam = headOfHouse.getLastName() + "\n" + headOfHouse.getFullAddress() 
					+ "\n   " 	+ (headOfHouse.getPhone()!=null?headOfHouse.getPhone():"missing phone info") 
					+ "\n   " 	+ (headOfHouse.getEmail()!=null?headOfHouse.getEmail():"missing email info") 
					+ "\n" + headOfHouse.getFirstAndMiddle() + " | " + headOfHouse.getGender() + ", " + headOfHouse.getAllBirthInfo();
		}
		
		if(wife != null)
		{
			if(fam != null)
			{
				fam += "\n" + wife.getFirstAndMiddle() + " | " + wife.getGender() + ", " + wife.getAllBirthInfo();
			}
			else
			{
				fam = wife.getLastName() + "\n" + wife.getFullAddress()
						+ "\n" + wife.getFirstAndMiddle() + " | " + wife.getGender() + ", " + wife.getAllBirthInfo();
			}
		}
		
		if(children != null)
		{
			if(fam == null)
			{
				fam = "Missing Parents info";
			}
			for(Person child : children)
			{
				fam += "\n" + child.getFirstAndMiddle() + " | " + child.getGender() + ", " + child.getAllBirthInfo();
			}
		}
		
		return fam;
	}
	
}
