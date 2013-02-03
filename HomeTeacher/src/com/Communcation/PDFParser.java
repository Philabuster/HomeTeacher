package com.Communcation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFParser {
	
	private String pdfString = null;
	
	public PDFParser(String pdf)
	{
		pdfString = pdf;
	}
	
	public Ward createWard( )
	{
		Ward ward = Ward.getWard();
		
		String[] lines = pdfString.split("\n");
		
		String wardPtrn = "([a-zA-z'\\d ]*)\\((\\d*)\\)\\s+-\\s+([a-zA-Z\\s]*)[0-9]+\\s+of\\s+[0-9]+.*";
		String htPtrn = "Home Teachers";
		String stakePtrn = "([a-zA-Z'\\d\\s]*)Stake \\((\\d*)\\)";
		String orgPtrn = "Organization: ([a-zA-Z\\s]*)/ District: ([a-zA-Z]*|\\s+-\\s+[a-zA-Z\\s,]*)";
		String compHdrPtrn = "[a-zA-Z\\s]+:[a-zA-Z\\s]+:([a-zA-Z\\s]+),([a-zA-Z\\s]+)\\((\\(?\\d{3}\\)?[-\\s]+\\d{3}-\\d{4})\\)";
		String pgEndPtrn = "For Church Use Only.*";
		String hshldHdrPtrn = "Households Given Name Sex Birth Age";
		String individualPtrn = "([A-Za-z']*,?\\s*[A-Za-z'\\s]*|[A-Za-z']*\\s*)([MF])\\s*(\\d{1,2}\\s+[A-Z]{1}[a-z]{2}.*)";
		String ctyStZpPtrn = "([a-zA-Z\\s]*),\\s+([a-zA-Z]{2})\\s+([\\d]{5}.*)";
		String htNmePtrn = "([a-zA-Z\\s]*),([a-zA-Z\\s]*)";
		String strtPtrn = "(\\d* [a-zA-Z\\.#\\d\\s]*)";
		String phnPtrn = "\\d{10,11}|\\d{3,4}[-\\s]*\\d{3}[-\\s]*\\d{4}|\\(\\d{3,4}\\)[-\\s]*\\d{3}[-\\s]*\\d{4}|[\\d-.\\(\\)\\s]{10,18}";
		String emailPtrn = ".*@.*";
		String famNmePtrn = "[A-Za-z]*";
		String prsnInfoPtrn = "(^[MF])(.*)";
		String contFrmPtrn = "\\(Continued from prior page\\)";	

		
		wardMaker:for( int i=0; i<lines.length; i++ )
		{
			Pattern p = null;
			Matcher m = null;
			
			if( lines[i].trim().matches( wardPtrn ) )
			{
				if( ward.hasWardInfo() == false )
				{
					p = Pattern.compile( wardPtrn );
					m = p.matcher(lines[i].trim());
					
					if( m.find() )
					{
						ward.setWardInfo(m.group(1).trim(), m.group(2).trim());
					}
				}
			}
			else if( lines[i].trim().matches( stakePtrn ) )
			{
				if( ward.hasStakeInfo() == false )
				{
					p = Pattern.compile( stakePtrn );
					m = p.matcher(lines[i].trim());
					
					if( m.find() )
					{
						ward.setStakeInfo(m.group(1).trim(), m.group(2).trim());
					}
				}
			}
			else if( lines[i].trim().matches( orgPtrn ) )
			{
				p = Pattern.compile( orgPtrn );
				m = p.matcher(lines[i].trim());
				
				if( m.find() )
				{
					String quorum = m.group(1).trim();
					
					if( ward.hasQuorum( quorum ) == false )
					{
						Quorum q = new Quorum( quorum );
						ward.addQuorum(q);
					}
				}
			}
			else if( lines[i].trim().matches( compHdrPtrn ) )
			{
				p = Pattern.compile( compHdrPtrn );
				m = p.matcher(lines[i].trim());
				
				if( m.find() )
				{
					String lastName = m.group(1).trim();
					String firstName = m.group(2).trim();
					
					for( Quorum q : ward.getQuorums() )
					{
						District d = q.getDistrict(firstName, lastName);
						if( d == null )
						{
							Person districtLeader = ward.getWardMember(firstName, lastName);
							if(districtLeader == null)
							{
								districtLeader = new Person(firstName, lastName);
							}
							
							d= new District( null, districtLeader );
							q.addDistrict(d);
						}
						
						i++;
						
						while( lines[i].trim().matches(htPtrn) )
						{
							i++;
							Companionship comps = new Companionship();
							while(  lines[i].trim().matches(pgEndPtrn) == false &&
									lines[i].trim().matches(hshldHdrPtrn) == false )
							{
								if( lines[i].trim().matches(compHdrPtrn) )
								{
									i--;
									continue wardMaker;
								}
								
								Person homeTeacher = null;
								
								if( lines[i].trim().matches(htNmePtrn) )
								{
									p = Pattern.compile( htNmePtrn );
									m = p.matcher(lines[i].trim());
									
									if(m.find())
									{
										homeTeacher = new Person(m.group(2).trim(), m.group(1).trim());
									}
									i++;
								}
								
								if( lines[i].trim().matches(strtPtrn))
								{
									if(homeTeacher != null)
									{
										homeTeacher.setStreetAddress(lines[i].trim());
									}
									i++;
								}
								
								if( lines[i].trim().matches(ctyStZpPtrn))
								{
									if(homeTeacher != null)
									{
										homeTeacher.setCity(lines[i].trim());
									}
									i++;
								}
								
								if( lines[i].trim().matches(phnPtrn))
								{
									if(homeTeacher != null)
									{
										homeTeacher.setPhone(lines[i].trim());
									}
									i++;
								}
								
								if( lines[i].trim().matches(emailPtrn))
								{
									if(homeTeacher != null)
									{
										homeTeacher.setEmail(lines[i].trim());
									}
									i++;
								}
								
								if(homeTeacher != null)
								{
									comps.addCompanion(homeTeacher);
								}
								else
								{
									i++;
									break;
								}
								
							}
							
							comps = d.addCompanionship(comps);
							
							if( lines[i].trim().matches(hshldHdrPtrn) )
							{
								i++;
								while(lines[i].trim().matches(pgEndPtrn) == false)
								{
									if(lines[i].trim().matches(compHdrPtrn) || lines[i].trim().matches(wardPtrn))
									{
										i--;
										continue wardMaker;
									}
									
									if(lines[i].trim().matches(contFrmPtrn))
									{
										i++;
									}
									
									if(lines[i].trim().matches(hshldHdrPtrn))
									{
										i++;
									}
									
									Families fam = null;
									String famName = null;
									String strAddress = null;
									String cityStZp = null;
									String phone = null;
									String email = null;
									
									if(lines[i].trim().matches(famNmePtrn))
									{
										famName = lines[i].trim();
										i++;
									}
									
									if(lines[i].trim().matches(strtPtrn))
									{
										strAddress = lines[i].trim();
										i++;
									}
									
									if(lines[i].trim().matches(ctyStZpPtrn) ||
											( lines[i].trim().matches("[A-Za-z ]*") &&
											  lines[i+1].trim().matches("[A-Za-z, ]*[\\d- ]*") ) )
									{
										if( lines[i].trim().matches("[A-Za-z ]*") &&
											  lines[i+1].trim().matches("[A-Za-z, ]*[\\d- ]*") )
										{
											cityStZp = lines[i].trim() + " " + lines[++i].trim();
										}
										else
										{
											cityStZp = lines[i].trim();
										}
										i++;
									}
									
									if(lines[i].trim().matches(phnPtrn))
									{
										phone = lines[i].trim();
										i++;
									}
									
									if(lines[i].trim().matches(emailPtrn))
									{
										email = lines[i].trim();
										i++;
									}
									
									
									
									while(lines[i].trim().matches(individualPtrn) || 
											(i+2 < lines.length &&
													lines[i].trim().matches(famNmePtrn) && 
													lines[i+1].trim().matches(famNmePtrn) &&
													lines[i+2].trim().matches(prsnInfoPtrn) ) ||
											(i + 2 < lines.length &&
													lines[i].trim().matches(".*") &&
													lines[i+1].trim().matches(".*") &&
													lines[i+2].trim().matches(prsnInfoPtrn) ) )
									{
										
										if(lines[i].trim().matches(compHdrPtrn) || lines[i].trim().matches(wardPtrn))
										{
											break;
										}
										
										Person peop = null;
										String frstName = null;
										String sex = null;
										String birthInfo = null;
										
										if(lines[i].trim().matches(individualPtrn))
										{
											p = Pattern.compile( individualPtrn );
											m = p.matcher(lines[i].trim());
											
											if(m.find())
											{
												frstName = m.group(1).trim();
												sex = m.group(2).trim();
												birthInfo = m.group(3).trim();
											}
										}
										else if( i+2 < lines.length &&
													lines[i].trim().matches(famNmePtrn) && 
													lines[i+1].trim().matches(famNmePtrn) &&
													lines[i+2].trim().matches(prsnInfoPtrn) )
										{
											frstName = lines[i].trim() + " " + lines[++i].trim();
											
											p = Pattern.compile( prsnInfoPtrn );
											m = p.matcher(lines[++i].trim());
											
											if(m.find())
											{
												sex = m.group(1).trim();
												birthInfo = m.group(2).trim();
											}
										}
										else if( i + 2 < lines.length &&
													lines[i].trim().matches("[A-Za-z\\s,]*") &&
													lines[i+1].trim().matches("[A-Za-z]*\\s*[A-Za-z]*") &&
													lines[i+2].trim().matches(prsnInfoPtrn) )
										{
											
											frstName = lines[i].trim().replace(",","") + " " + lines[++i].trim();
											
											p = Pattern.compile( prsnInfoPtrn );
											m = p.matcher(lines[++i].trim());
											
											if(m.find())
											{
												sex = m.group(1).trim();
												birthInfo = m.group(2).trim();
											}
										}
										else if( lines[i].trim().matches(contFrmPtrn))
										{
											i++;
											break;
										}
												
										peop = ward.getWardMember(frstName, famName);
										
										if(peop == null)
										{
											peop = new Person(frstName, famName);
										}
										
										if(peop.getFullAddress() == null)
										{
											peop.setStreetAddress(strAddress);
											peop.setCity(cityStZp);
										}
										
										if(peop.getEmail() == null)
										{
											peop.setEmail(email);
										}
										
										if(peop.getPhone() == null)
										{
											peop.setPhone(phone);
										}
										
										if(peop.getBirthDate() == null)
										{
											peop.setBirthDate(birthInfo);
										}
										
										if(peop.getGender() == null)
										{
											peop.setGender(sex);
										}
										
										if(fam == null)
										{
											fam = new Families(peop);
										}
										else
										{
											if(peop.getGender().equals("F") && peop.getAge() == null )
											{
												fam.setWife(peop);
											}
											else
											{
												fam.addChild(peop);
											}
										}
										
										i++;
										
										if( i >= lines.length)
										{
											break wardMaker;
										}
									}
									
									if(fam != null)
									{
										comps.addFamily(fam);
									}
								}
								i++;
							}
						}
					}
				}
			}
		}	
		
		for(Quorum q : ward.getQuorums())
		{
			for(District d : q.getDistricts())
			{
				Person dl = d.getDistrictLeader();
				
				if(dl != null)
				{
					dl = ward.getWardMember(dl.getFirstName(), dl.getLastName());
					if(dl != null)
					{
						d.setDistrictLeader(dl);
					}
				}
			}
		}
		
		return ward;
	}
	

}
