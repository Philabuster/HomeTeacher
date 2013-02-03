package com.Communcation;

import java.util.ArrayList;
import java.util.List;

public class Ward {
	
	private static volatile Ward instance = null;
	private String wardName = null;
	private String wardNumber = null;
	private String stakeName = null;
	private String stakeNumber = null;
	private List<Quorum> quorums = null;
	
	private Ward( )	
	{ 
		quorums = new ArrayList<Quorum>();
	}
	
	public static Ward getWard( )
	{
		 if (instance == null) 
		 {
             synchronized (Ward.class)
             {
            	 if (instance == null) 
            	 {
            		 instance = new Ward();
                 }
             }
		 }
		 
		 return instance;
	}
	
	public boolean hasWardInfo()
	{
		if( wardName == null || wardNumber == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean hasStakeInfo()
	{
		if( stakeName == null || stakeNumber == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void setWardInfo(String name, String number)
	{
		wardName = name;
		wardNumber = number;
	}
	
	public void setStakeInfo(String name, String number)
	{
		stakeName = name;
		wardNumber = number;
	}
	
	public String getWardName()
	{
		return wardName;
	}
	
	public String getWardNumber()
	{
		return wardNumber;
	}
	
	public String getStakeName()
	{
		return stakeName;
	}
	
	public String getStakeNumber()
	{
		return stakeNumber;
	}
	
	public List<Quorum> getQuorums()
	{
		return quorums;
	}
	
	public boolean hasQuorum(String name)
	{
		if( quorums.isEmpty() )
		{
			return false;
		}
		
		for( Quorum q : quorums)
		{
			if(q.getQuorumName().equalsIgnoreCase(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean addQuorum(Quorum q)
	{
		if(hasQuorum(q.getQuorumName()) == false)
		{
			quorums.add(q);
			return true;
		}
		
		return false;
	}
	
	public Companionship getCompanionShip(Person p)
	{
		for(Quorum q : quorums)
		{
			if(q.isInQuorum(p))
			{
				for(District d : q.getDistricts())
				{
					if(d.isInDistrict(p))
					{
						return d.getCompanionship(p);
					}
				}
			}
		}
		
		return null;
	}
	
	public Person getWardMember(String firstName, String lastName)
	{
		Person wardMember = null;
		
		for(Quorum q : quorums)
		{
			if(q.isInQuorum(firstName, lastName))
			{
				wardMember = q.getHomeTeacher(firstName, lastName);
			}
		}
		
		return wardMember;
	}
	
	public Person getWardMember(String lastName)
	{
		Person p = null;
		
		for(Quorum q : quorums)
		{
			p = q.getHomeTeacher(lastName);
		}
		
		return p;
	}
	
	public Person[] getAllHomeTeachers()
	{
		ArrayList<Person> peops = new ArrayList<Person>();
		
		for(Quorum q: quorums)
		{
			for(District d : q.getDistricts())
			{
				for(Companionship comps : d.getCompanionships())
				{
					for(Person p : comps.getCompanionship())
					{
						peops.add(p);
					}
				}
			}
		}
		
		if(peops.size() > 0)
		{
			Person[] people = new Person[peops.size()];
			
			for(int i=0; i<peops.size(); i++)
			{
				people[i] = peops.get(i);
			}
			
			return people;
		}
		else
		{
			return null;
		}
	}
	
	public String[] getAllHomeTeachersNames()
	{
		ArrayList<String> peops = new ArrayList<String>();
		
		for(Quorum q: quorums)
		{
			for(District d : q.getDistricts())
			{
				for(Companionship comps : d.getCompanionships())
				{
					for(Person p : comps.getCompanionship())
					{
						peops.add(p.getFullName());
					}
				}
			}
		}
		
		if(peops.size() > 0)
		{
			String[] people = new String[peops.size()];
			
			for(int i=0; i<peops.size(); i++)
			{
				people[i] = peops.get(i);
			}
			
			return people;
		}
		else
		{
			return null;
		}
	}
}
