package com.Communcation; 

import java.util.ArrayList;
import java.util.List;

public class Companionship {
	
	private List<Person> companionship;
	private List<Families> families;
	
	public Companionship()
	{
		companionship = new ArrayList<Person>();
		families = new ArrayList<Families>();
	}
	
	public void addCompanion(Person homeTeacher)
	{
		companionship.add(homeTeacher);
	}
	
	public void addFamily(Families family)
	{
		Person headOfHouse = family.getHeadOfHouse();
		
		if(headOfHouse != null)
		{
			for(Families f : families)
			{
				if(f.getHeadOfHouse().isThisPerson(headOfHouse))
				{
					return;
				}
			}
			
			families.add(family);
		}
	}
	
	public List<Person> getCompanionship()
	{
		return companionship;
	}
	
	public Person getCompanion(Person p)
	{
		for(Person peops : companionship)
		{
			if(peops.isThisPerson(p) == false)
			{
				return peops;
			}
		}
		
		return null;
	}
	
	public List<Families> getFamilies()
	{
		return families;
	}
	
	public boolean inCompanionShip( Person elder )
	{
		for( Person p : companionship )
		{
			if( p.isThisPerson( elder ))
			{
				return true;
			}
		}
		
		return false;	
	}
	
	public boolean inCompanionShip( String first, String last )
	{
		for( Person p : companionship )
		{
			if( p.isThisPerson( first, last) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean inCompanionShip( String last )
	{
		for( Person p : companionship )
		{
			if( p.isThisPerson( last) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Person isInFamilyTaught( String first, String last )
	{
		for( Families fam : families )
		{
			if(fam.getHeadOfHouse().isThisPerson(first, last))
			{
				return fam.getHeadOfHouse();
			}
			else if(fam.getWife() != null && fam.getWife().isThisPerson(first, last))
			{
				return fam.getWife();
			}
			else if(fam.getChildren().size() > 0)
			{
				for(Person p : fam.getChildren())
				{
					if(p.isThisPerson(first, last))
					{
						return p;
					}
				}
			}
		}
		
		return null;
	}
	
	public Person isInFamilyTaught( String last )
	{
		for( Families fam : families )
		{
			if(fam.getHeadOfHouse().isThisPerson(last))
			{
				return fam.getHeadOfHouse();
			}
			else if(fam.getWife() != null && fam.getWife().isThisPerson(last))
			{
				return fam.getWife();
			}
			else if(fam.getChildren().size() > 0)
			{
				for(Person p : fam.getChildren())
				{
					if(p.isThisPerson(last))
					{
						return p;
					}
				}
			}
		}
		
		return null;
	}
	
	public Person getHomeTeacher( String first, String last )
	{
		for( Person p : companionship )
		{
			if( p.isThisPerson(first, last) )
			{
				return p;
			}
		}
		
		return null;
	}
	
	public Person getHomeTeacher( String last )
	{
		for( Person p : companionship )
		{
			if( p.isThisPerson(last) )
			{
				return p;
			}
		}
		
		return null;
	}
	
	public String toString()
	{
		String info = "Home Teachers\n\n";
		
		if(companionship != null)
		{
			for( Person p : companionship )
			{
				info += p.toString() + "\n\n";

			}
		}
		else
		{
			info += "Missing Companionship Info\n\n";
		}
		
		
		if(families != null)
		{
			int i = 0;
			for(Families f : families)
			{
				info += "Household # " + ++i +"\n";
				info += f.toString() + "\n\n";
			}
		}
		else
		{
			info += "Missing Families Info";
		}
		
		return info;
	}

}
