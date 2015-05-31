/**
 * Contains information related to a day of week number
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

public class IctDay {
	
	public Boolean isCurrentDay() {
		return _isCurrentDay;
	}

	public int dayOfMonth() {
		return _day_of_month;
	}

	public Boolean isInCurrentMonth() {
		return _isInCurrentMonth;
	}

	private int _day_of_month;
	private Boolean _isInCurrentMonth;
	private Boolean _isCurrentDay;
	
	public IctDay(int day_of_month, Boolean isInCurrentMonth, Boolean isCurrentDay)
	{
		_day_of_month = day_of_month;
		_isInCurrentMonth = isInCurrentMonth;
		_isCurrentDay = isCurrentDay;
	}
}
