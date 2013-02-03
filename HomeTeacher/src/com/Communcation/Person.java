package com.Communcation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Person {
	
	private String lastName;
	private String middleName;
	private String firstName;
	private String birthDate;
	private String email;
	private String phone;
	private String strtAddress;
	private String cityStZp;
	private GmailInfo gmail;
	private String age;
	private String birthYr;
	private String gender;
	
	public Person(String firstName, String lastName)
	{
		String checkForMiddle[] = firstName.split(" ");
		
		if(checkForMiddle.length == 2 && !firstName.contains(","))
		{
			this.firstName = checkForMiddle[0].trim();
			this.middleName = checkForMiddle[1].trim();
		}
		else
		{
			this.firstName = firstName;
		}
		
		this.lastName = lastName;
	}
	
	public void setBirthDate(String birthDate)
	{
		Pattern p = Pattern.compile( "(\\d{1,2}\\s+[A-Z]{1}[a-z]{2})\\s+(\\d{4})\\s+(\\d{1,3})|(\\d{1,2}\\s+[A-Z]{1}[a-z]{2})\\s*" );
		
		Matcher m = p.matcher(birthDate);
		
		if( m.find() )
		{			
			if(m.group(1) != null)
			{
				this.birthDate = m.group(1).trim();
			}
			
			if(m.group(2) != null)
			{
				this.birthYr = m.group(2).trim();
			}
			
			if(m.group(3) != null)
			{
				this.age = m.group(3).trim();
			}
			
			if(m.group(4) != null)
			{
				this.birthDate = m.group(4).trim();
			}

		}
		else
		{
			this.birthDate = birthDate;
		}
	}
	
	public void setAge(String age)
	{
		this.age = age;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	public void setStreetAddress( String street )
	{
		this.strtAddress = street;
	}
	
	public void setCity( String city )
	{
		this.cityStZp = city;
	}
	
	public void setGender( String sex )
	{
		this.gender = sex;
	}
	
	public String getFullName()
	{
		String name = null;
		
		if( firstName != null && middleName != null && lastName != null )
		{
			name = firstName + " " + middleName + " " + lastName;
		}
		else if( firstName != null && lastName != null )
		{
			name = firstName + " " + lastName;
		}
		else if( lastName != null )
		{
			name = lastName;
		}
		else if( firstName != null )
		{
			name = firstName;
		}
		
		return name;
	}
	
	public String getFirstAndMiddle()
	{
		if(firstName != null && middleName != null)
		{
			return firstName + ", " + middleName; 
		}
		else if(firstName != null)
		{
			return firstName;
		}
		else
		{
			return "missing name info";
		}
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getBirthDate()
	{
		return birthDate;
	}
	
	public String getAge()
	{
		return age;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getGender()
	{
		return gender;
	}
	
	public String getAllBirthInfo()
	{
		if(birthDate != null && birthYr != null && age != null)
		{
			return birthDate + ", " + birthYr + ", age " + age;
		}
		else if( birthDate != null)
		{
			return birthDate;
		}
		else
		{
			return "missing birth info";
		}
	}
	
	public String getFullAddress()
	{
		if( this.strtAddress != null && this.cityStZp != null )
		{
			return indent + strtAddress + "\n" + indent +  cityStZp;
		}
		else if( this.strtAddress != null )
		{
			return indent + strtAddress;
		}
		else if( this.cityStZp != null)
		{
			return indent + this.cityStZp;
		}
		
		return null;
	}
	
	public String getMiddleName()
	{
		return middleName;
	}
	
	public void setGmailInfo(GmailInfo gmail)
	{
		this.gmail = gmail;
	}
	
	public GmailInfo getGmailInfo()
	{
		return this.gmail;
	}
	
	public boolean isThisPerson( Person person )
	{
		if(person == null)
		{
			return false;
		}
		
		if(person.getLastName() != null && person.getLastName().equalsIgnoreCase(this.lastName) == false)
		{
			return false;
		}
		
		if( person.getFirstName() != null && this.firstName != null )
		{
			String first = this.firstName.toLowerCase();
			String compare = person.getFirstName().toLowerCase();
			
			if( compare.contains(" ") )
			{
				String cSplit[] = compare.split(" ");

				for( String c : cSplit )
				{
					if( checkThreeChars( first, c) )
					{
						return true;
					}
				}
			}
			else if( checkThreeChars ( first, compare ) )
			{
				return true;
			}
		}
		
		if( person.getMiddleName() != null && this.middleName != null )
		{
			String middle = this.middleName.toLowerCase();
			String compare = person.getMiddleName().toLowerCase();
			
			if( compare.contains(" ") )
			{
				String cSplit[] = compare.split(" ");

				for( String c : cSplit )
				{
					if( checkThreeChars( middle, c) )
					{
						return true;
					}
				}
			}
			else if( checkThreeChars ( middle, compare ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isThisPerson( String firstName, String lastName )
	{		
		if(lastName.equalsIgnoreCase(this.lastName) == false)
		{
			return false;
		}
		
		if( firstName != null && this.firstName != null )
		{
			String first = this.firstName.toLowerCase();
			String compare = firstName.toLowerCase();
			
			if( compare.contains(" ") )
			{
				String cSplit[] = compare.split(" ");

				for( String c : cSplit )
				{
					if( checkThreeChars( first, c) )
					{
						return true;
					}
				}
			}
			else if( checkThreeChars ( first, compare ) )
			{
				return true;
			}
		}
		
		if( firstName != null && this.middleName != null )
		{
			String middle = this.middleName.toLowerCase();
			String compare = firstName.toLowerCase();
			
			if( compare.contains(" ") )
			{
				String cSplit[] = compare.split(" ");

				for( String c : cSplit )
				{
					if( checkThreeChars( middle, c) )
					{
						return true;
					}
				}
			}
			else if( checkThreeChars ( middle, compare ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isThisPerson( String lastName )
	{		
		if(lastName.equalsIgnoreCase(this.lastName) == false)
		{
			return false;
		}
		
		return true;
	}
	
	private boolean checkThreeChars(String first, String compare)
	{
		if( first.length() > 2 && compare.length() > 2 )
		{
			for( int i=0; i<3; i++ )
			{
				if( first.charAt(i) != compare.charAt(i) )
				{
					return false;
				}
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private String indent = "   ";
	
	public String toString()
	{
		String info = getFullName();
		
		if(info == null)
		{
			info =  "Missing Name info";
		}
		
		if(getFullAddress() != null)
		{
			info += "\n" + getFullAddress();
		}
		else
		{
			info += "\n" + indent + "Address info is missing";
		}
		
		if(getPhone() != null)
		{
			info += "\n" + indent + getPhone();
		}
		else
		{
			info += "\n" + indent + "Phone missing";
		}
		
		if(getEmail() != null)
		{
			info += "\n" + indent + getEmail();
		}
		else
		{
			info += "\n" + indent + "Email missing";
		}
			
		return info;
	}
}
