/**
 * Contains methods for drawing different types calendars
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.widget.LinearLayout;
import domain.MySchedule;

public class IctCalendar {

	//TODO create repeatable color generator class
	//Always generates the same order of colors no limit
	private static final int[] pallete = {0xFF0000, 0xFF9900, 0x0099FF, 0xCC00FF, 0x009933, 0x6666FF, 0x00FF00, 0x00FFFF,
		0xFF22FF, 0xCC9922, 0x2299FF, 0x9922FF, 0x229988, 0x2266CC, 0x44FF22, 0x22FFBB,
		0xFF44BB, 0xAA9944, 0x4499BB, 0x7744FF, 0x4499AA, 0x446688, 0x88FF44, 0x44FF88,
		0xFF8888, 0x889988, 0x889988, 0x5588FF, 0x8899FF, 0x886644, 0xAAFF88, 0x88FF44,
		0xFFAAAA, 0x4499AA, 0xAA99AA, 0x33AAFF, 0xAA9908, 0xAA6622, 0x0FFFAA, 0xAAFF22,
		0xFFBBBB, 0x2299BB, 0x0F9900, 0x22BBFF, 0xBB99BB, 0xBB6600, 0x00FFBB, 0xBBFF00
	};
	public static final String TIME = "Time";
	public static final String DAY = "day";
	
	private Context _context;
	private LinearLayout _container;
	private int _width;
	private int _height;
	private MySchedule _schedule;
	private Calendar _calendar;
	private HashMap<String, Integer> _colorMap;

	public IctCalendar(Context context, LinearLayout container, int width, int height)
	{
		_context = context;
		_container = container;
		_width = width;
		_height = height;
		_calendar = new GregorianCalendar();
		_colorMap = new HashMap<String, Integer>();
	}	
	
	public void drawGenericMonth(int startDay, int endDay, int month)
	{
		if(_schedule != null)
		{
			_calendar.set(Calendar.MONTH, month);
			String caption = _calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
			int max_number_of_weeks_in_month = _calendar.getMaximum(Calendar.WEEK_OF_MONTH);
			int day = _calendar.get(Calendar.DAY_OF_MONTH);
			int current_day_of_week = _calendar.get(Calendar.DAY_OF_WEEK) - 1;
			IctGridLayout header_grid = new IctGridLayout(_context, _width, _height, _colorMap);
			header_grid.addDataMap(_schedule);
			header_grid.drawTopRow("Week", startDay,endDay, caption, month, current_day_of_week);
			_container.addView(header_grid);
			for(int week=1; week <= max_number_of_weeks_in_month; week++)
			{
				drawGenericWeek(week, month, day, startDay, endDay);
			}
		}
	}
	
	public void drawCompactMonth(int startDay, int endDay, int month)
	{
		if(_schedule != null)
		{
			_calendar.set(Calendar.MONTH, month);
			String caption = _calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
			int max_number_of_weeks_in_month = _calendar.getMaximum(Calendar.WEEK_OF_MONTH);
			int day = _calendar.get(Calendar.DAY_OF_MONTH);
			int current_day_of_week = _calendar.get(Calendar.DAY_OF_WEEK) - 1;
			IctGridLayout header_grid = new IctGridLayout(_context, _width, _height, _colorMap);
			header_grid.addDataMap(_schedule);
			header_grid.drawTopRow("Week", startDay,endDay, caption, month, current_day_of_week);
			_container.addView(header_grid);
			for(int week=1; week <= max_number_of_weeks_in_month; week++)
			{
				drawCompactWeek(week, month, day, startDay, endDay);
			}
		}
	}

	public void drawMonth(int startDay, int endDay, int month)
	{
		if(_schedule != null)
		{
			_calendar.set(Calendar.MONTH, month);
			String caption = _calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
			int max_number_of_weeks_in_month = _calendar.getMaximum(Calendar.WEEK_OF_MONTH);
			int day = _calendar.get(Calendar.DAY_OF_MONTH);
			int current_day_of_week = _calendar.get(Calendar.DAY_OF_WEEK) - 1;
			IctGridLayout header_grid = new IctGridLayout(_context, _width, _height, _colorMap);
			header_grid.addDataMap(_schedule);
			header_grid.drawTopRow(DAY, startDay,endDay, caption, month, current_day_of_week);
			_container.addView(header_grid);
			for(int week=1; week <= max_number_of_weeks_in_month; week++)
			{
				drawWeek(week, month, day, startDay, endDay);
			}
		}
	}
	
	public void drawMonth(int startDay, int endDay)
	{
		drawMonth(startDay, endDay, _calendar.get(Calendar.MONTH));
	}
	
	public void drawWeek(int[] range, int week, int month)
	{
		_calendar.set(Calendar.MONTH, month);
		_calendar.set(Calendar.WEEK_OF_MONTH, week);
		String caption = _calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
		int day = _calendar.get(Calendar.DAY_OF_MONTH);
		
		Calendar temp = new GregorianCalendar();
		temp.set(_calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH), _calendar.get(Calendar.DAY_OF_MONTH));
		int current_day = temp.get(Calendar.DAY_OF_WEEK) - 1;
		int[] day_range = new int[3];
		temp.add(Calendar.DAY_OF_MONTH, -1);
		day_range[0] = temp.get(Calendar.DAY_OF_MONTH);
		temp.add(Calendar.DAY_OF_MONTH, 1);
		day_range[1] = temp.get(Calendar.DAY_OF_MONTH);
		temp.add(Calendar.DAY_OF_MONTH, 1);
		day_range[2] = temp.get(Calendar.DAY_OF_MONTH);
		
		IctGridLayout header_grid = new IctGridLayout(_context, _width, _height, _colorMap);
		header_grid.addDataMap(_schedule);
		header_grid.drawTopRow(TIME, range, caption, month, current_day);
		_container.addView(header_grid);
		drawWeek(week, month, day, range, day_range);
	}
	
	//TODO Remove comment on the unlikely chance this type of functionality is required 
	/*
	private void drawCompactWeek(int week, int month, int day, int[] range, int[] day_range)
	{
		int hour = _calendar.get(Calendar.HOUR_OF_DAY);
		Boolean isCurrentWeek = _calendar.get(Calendar.WEEK_OF_MONTH) == week;
		IctDay[] days = makeDaysOfMonthFor(week, month, day);
		IctGridLayout grid = new IctGridLayout(_context, _width, _height, _colorMap);
		grid.addDataMap(_schedule);
		grid.drawDaysOfMonth(day_range,days, week, month);
		_container.addView(grid);
	}
	*/

	private void drawGenericWeek(int week, int month, int day, int startDay, int endDay)
	{
		//Boolean isCurrentWeek = _calendar.get(Calendar.WEEK_OF_MONTH) == week;
		IctDay[] days = makeDaysOfMonthFor(week, month, day);
		IctGridLayout grid = new IctGridLayout(_context, _width, _height, _colorMap);
		grid.addDataMap(_schedule);
		grid.drawDaysOfMonth("" + week, startDay,endDay,days, week, month);
		_container.addView(grid);
	}

	private void drawCompactWeek(int week, int month, int day, int startDay, int endDay)
	{
		//Boolean isCurrentWeek = _calendar.get(Calendar.WEEK_OF_MONTH) == week;
		IctDay[] days = makeDaysOfMonthFor(week, month, day);
		IctGridLayout grid = new IctGridLayout(_context, _width, _height, _colorMap);
		grid.addDataMap(_schedule);
		grid.drawCompactDaysOfMonth("" + week, startDay,endDay,days, week, month);
		_container.addView(grid);
	}

	private void drawWeek(int week, int month, int day, int[] range, int[] day_range)
	{
		int hour = _calendar.get(Calendar.HOUR_OF_DAY);
		int current_day = _calendar.get(Calendar.DAY_OF_MONTH);
		Boolean isCurrentWeek = _calendar.get(Calendar.WEEK_OF_MONTH) == week;
		IctDay[] days = makeDaysOfMonthFor(week, month, day);
		IctGridLayout grid = new IctGridLayout(_context, _width, _height, _colorMap);
		grid.addDataMap(_schedule);
		grid.drawDaysOfMonth(day_range,days, week, month, current_day);
		grid.drawLeftRow(week, month, day, hour, isCurrentWeek);
		grid.drawGridRange(range, days, hour, isCurrentWeek, week, month, day);
		_container.addView(grid);
	}

	public void drawWeek(int startDay, int endDay)
	{
		int week = _calendar.get(Calendar.WEEK_OF_MONTH);
		int month = _calendar.get(Calendar.MONTH);
		drawWeek(week, month, startDay, endDay);
	}
	
	public void drawWeek(int startDay, int endDay, int week)
	{
		int month = _calendar.get(Calendar.MONTH);
		drawWeek(week, month, startDay, endDay);
	}
	
	public void drawWeek(int startDay, int endDay, int week, int month)
	{
		_calendar.set(Calendar.MONTH, month);
		_calendar.set(Calendar.WEEK_OF_MONTH, week);
		String caption = _calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
		int day = _calendar.get(Calendar.DAY_OF_MONTH);
		int current_day_of_week = _calendar.get(Calendar.DAY_OF_WEEK) -1;
		IctGridLayout header_grid = new IctGridLayout(_context, _width, _height, _colorMap);
		header_grid.addDataMap(_schedule);
		header_grid.drawTopRow(TIME, startDay,endDay, caption, month, current_day_of_week);
		_container.addView(header_grid);
		drawWeek(week, month, day, startDay, endDay);
	}
	
	private void drawWeek(int week, int month, int day, int startDay, int endDay)
	{
		int hour = _calendar.get(Calendar.HOUR_OF_DAY);
		Boolean isCurrentWeek = _calendar.get(Calendar.WEEK_OF_MONTH) == week;
		IctDay[] days = makeDaysOfMonthFor(week, month, day);
		IctGridLayout grid = new IctGridLayout(_context, _width, _height, _colorMap);
		grid.addDataMap(_schedule);
		grid.drawDaysOfMonth("day", startDay,endDay,days, week, month);
		grid.drawLeftRow(week, month, day, hour, isCurrentWeek);
		grid.drawGrid(startDay,endDay, days, hour, isCurrentWeek, week, month, day);
		_container.addView(grid);
	}
	
	private IctDay[] makeDaysOfMonthFor(int week, int month, int day)
	{
		IctDay[] days = new IctDay[7];//(day_of_month, isInCurrentMonth)[7];
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.WEEK_OF_MONTH, week);
		cal.set(Calendar.DAY_OF_WEEK,1);
		for(int i=0; i < 7; i++)
		{
			int day_of_the_month = cal.get(Calendar.DAY_OF_MONTH);
			Boolean isInCurrentMonth = cal.get(Calendar.MONTH) == month;
			Boolean isCurrentDay = false;
			if(isInCurrentMonth)
			{
				isCurrentDay = cal.get(Calendar.DAY_OF_MONTH) == day;
			}
			days[i] = new IctDay(day_of_the_month, isInCurrentMonth, isCurrentDay);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return days;
	}

	public MySchedule getSchedule() {
		return _schedule;
	}

	public void setSchedule(MySchedule schedule) {
		_schedule = schedule;
		//Create Color Map
		_colorMap.clear();
		int i = 0;
		for(ROWS row : ROWS.values())
		{
			for(COLUMNS column: COLUMNS.values())
            {
				String key = row.name() + column.name();
				String val = _schedule.get(key);
				if(!val.equals(""))
				{
					if(!_colorMap.containsKey(val))
					{
						int color = pallete[i++];
						_colorMap.put(val, color);
					}
				}
            }
		}
	}

}
