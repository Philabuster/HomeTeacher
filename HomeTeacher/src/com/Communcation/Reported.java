package com.Communcation;

public class Reported {
	Months reportingMonth = null;
	
	public Reported(String month)
	{
		switch(month.toLowerCase())
		{
			case ("january"):
				reportingMonth = Months.JANUARY;
				break;
			case("february"):
				reportingMonth = Months.FEBRUARY;
				break;
			case("march"):
				reportingMonth = Months.MARCH;
				break;
			case("april"):
				reportingMonth = Months.APRIL;
				break;
			case("may"):
				reportingMonth = Months.MAY;
				break;
			case("june"):
				reportingMonth = Months.JUNE;
				break;
			case("july"):
				reportingMonth = Months.JULY;
				break;
			case("august"):
				reportingMonth = Months.AUGUST;
				break;
			case("september"):
				reportingMonth = Months.SEPTEMBER;
				break;
			case("october"):
				reportingMonth = Months.OCTOBER;
				break;
			case("november"):
				reportingMonth = Months.NOVEMBER;
				break;
			case("december"):
				reportingMonth = Months.DECEMBER;
				break;
		}
	}
}
