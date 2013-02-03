package com.Communcation;

import java.util.ArrayList;
import java.util.List;

public class District 
{

	private Person districtLeader;
	private List<Companionship> companions;
	private String districtName;
	
	public District( String name, Person leader )
	{
		districtName = name;
		districtLeader = leader;
		companions = new ArrayList<Companionship>();
	}
	
	public Companionship addCompanionship( Companionship comp )
	{
		if(compAlreadyEntered(comp) == false)
		{
			companions.add(comp);
			return comp;
		}
		
		for(Companionship c : companions)
		{
			for(Person p : comp.getCompanionship())
			{
				if(c.inCompanionShip(p))
				{
					return c;
				}
			}
		}
		
		return null;
	}
	
	public List<Companionship> getCompanionships( )
	{
		return companions;
	}
	
	public String getDistrictName()
	{
		return districtName;
	}
	
	public boolean isInDistrict( String first, String last )
	{
		for( Companionship comp : companions )
		{
			if( comp.inCompanionShip( first, last ))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isInDistrict( String last )
	{
		for( Companionship comp : companions )
		{
			if( comp.inCompanionShip( last ))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isInDistrict( Person elder )
	{
		for( Companionship comp : companions )
		{
			if( comp.inCompanionShip( elder ))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Person getHomeTeacher( String first, String last )
	{
		if( isInDistrict( first, last ))
		{
			for( Companionship comp : companions )
			{
				if( comp.inCompanionShip(first, last))
				{
					return comp.getHomeTeacher(first, last);
				}
				
				if( comp.isInFamilyTaught(first, last) != null)
				{
					return comp.isInFamilyTaught(first, last);
				}
			}
		}
		
		return null;
	}
	
	public Person getHomeTeacher( String last )
	{
		if( isInDistrict( last ))
		{
			for( Companionship comp : companions )
			{
				if( comp.inCompanionShip(last))
				{
					return comp.getHomeTeacher(last);
				}
				
				if( comp.isInFamilyTaught(last) != null)
				{
					return comp.isInFamilyTaught(last);
				}
			}
		}
		
		return null;
	}
	
	public Companionship getCompanionship( String first, String last )
	{
		if( isInDistrict( first, last ))
		{
			for( Companionship comp : companions )
			{
				if( comp.inCompanionShip(first, last) )
				{
					return comp;
				}
			}
		}
		
		return null;
	}
	
	public Companionship getCompanionship( Person elder )
	{
		if( isInDistrict( elder ))
		{
			for( Companionship comp : companions )
			{
				if( comp.inCompanionShip(elder) )
				{
					return comp;
				}
			}
		}
		
		return null;
	}
	
	public boolean compAlreadyEntered(Companionship comp)
	{
		for(Person p : comp.getCompanionship())
		{
			if(isInDistrict(p))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Person getDistrictLeader()
	{
		return districtLeader;
	}
	
	public void setDistrictLeader(Person dl)
	{
		if(dl != null)
		{
			districtLeader = dl;
		}
	}
	
	public String toString()
	{
		String info = "District ";
		if(districtName != null)
		{
			info += districtName + "\n";
		}
		
		info +=	"Supervisor : " + districtLeader.getFullName() + " " + districtLeader.getPhone() + "\n";
	
		if(companions != null)
		{
			for(Companionship comp : companions)
			{
				info += comp.toString() + "\n";
			}
		}
		
		return info;
	}
	
}
