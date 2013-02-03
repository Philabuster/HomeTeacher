package com.Communcation;

import java.util.ArrayList;
import java.util.List;

public class Quorum {
	
	private List<District> districts;
	private String quorumName = null;
	
	public Quorum( String name )
	{
		quorumName = name;
		districts = new ArrayList<District>();
	}
	
	public String getQuorumName()
	{
		return quorumName;
	}
	
	public boolean addDistrict(District district)
	{
		for( District d : districts )
		{
			if( d.getDistrictName() != null )
			{
				if( d.getDistrictName().equals( district.getDistrictName() ) )
				{
					return false;
				}
			}
			else
			{
				Person dl = d.getDistrictLeader();
				Person dlCompare = district.getDistrictLeader();
				if( dl != null && dlCompare != null )
				{
					if( dl.isThisPerson( dlCompare.getFirstName(), dlCompare.getLastName() ) )
					{
						return false;
					}
				}
			}
		}
		
		districts.add(district);
		return true;
	}
	
	public boolean isInQuorum( Person elder )
	{
		for( District d : districts )
		{
			for( Companionship comp : d.getCompanionships() )
			{
				if( comp.inCompanionShip(elder))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isInQuorum( String first, String last )
	{
		for( District d : districts )
		{
			for( Companionship comp : d.getCompanionships() )
			{
				if( comp.inCompanionShip(first, last))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Person getHomeTeacher( String first, String last )
	{
		if( isInQuorum( first, last ))
		{
			for( District d : districts )
			{
				if( d.isInDistrict( first, last ))
				{
					return d.getHomeTeacher(first, last);
				}
			}
		}
		
		return null;
	}
	
	public Person getHomeTeacher(String last)
	{
		
		for( District d : districts )
		{
			if( d.isInDistrict( last ))
			{
				return d.getHomeTeacher( last );
			}
		}
		
		return null;
	}
	
	public Companionship getCompanionship( String first, String last )
	{
		if( isInQuorum( first, last ))
		{
			for( District d : districts )
			{
				if( d.isInDistrict( first, last ))
				{
					return d.getCompanionship(first, last);
				}
			}
		}
		
		return null;
	}
	
	public boolean hasDistricts()
	{
		if(districts.isEmpty())
			return false;
		else
			return true;
	}
	
	public District getDistrict(String first, String last)
	{
		if( hasDistricts() )
		{
			for(District d : districts)
			{
				Person dl = d.getDistrictLeader();
				if(dl.isThisPerson(first, last) )
				{
					return d;
				}
			}
		}
		
		return null;
	}
	
	public List<District> getDistricts()
	{
		return districts;
	}

}
