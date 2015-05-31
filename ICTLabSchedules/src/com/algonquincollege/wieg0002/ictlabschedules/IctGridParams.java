/**
 * Properties for specifying information about a cell in the grid
 * contains a reference to the underlying data object (HashMap).
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

import java.util.HashMap;

public class IctGridParams {
	
	private IctGridContext _type;
	private String _val;
	private HashMap<String, String> _data;

	private int _day;
	private int _week;
	private int _month;
	private ROWS _row;
	private COLUMNS _column;
	/**
	 *  Used for passing information around
	 */
	public IctGridParams(String val, IctGridContext type, HashMap<String, String> data) {
		_type = type;
		_val = val;
		_data = data;
	}

	public IctGridParams(String val, IctGridContext type, HashMap<String, String> data, int month) {
		_type = type;
		_val = val;
		_data = data;
		_month = month;
	}

	public IctGridParams(String val, IctGridContext type, HashMap<String, String> data, int month, COLUMNS col) {
		_type = type;
		_val = val;
		_data = data;
		_month = month;
		_day = col.ordinal();
	}

	public IctGridParams(String val, IctGridContext type, HashMap<String, String> data,int week, int month) {
		_type = type;
		_val = val;
		_data = data;
		_week = week;
		_month = month;
	}

	public IctGridParams(String val, IctGridContext type, HashMap<String, String> data,int week, int month, int day) {
		_type = type;
		_val = val;
		_data = data;
		_week = week;
		_month = month;
		_day = day;
	}

	public IctGridParams(String val, IctGridContext type, HashMap<String, String> data,int week, int month, int day, ROWS row) {
		_type = type;
		_val = val;
		_data = data;
		_week = week;
		_month = month;
		_day = day;
		_row = row;
	}

	public IctGridParams(String val, IctGridContext type, HashMap<String, String> data,int week, int month, int day, ROWS row, COLUMNS column) {
		_type = type;
		_val = val;
		_data = data;
		_week = week;
		_month = month;
		_day = day;
		_row = row;
		_column = column;
	}

	public IctGridParams(int day, int week, int month, IctGridContext type) {
		_type = type;
		_day = day;
		_week = week;
		_month = month;
	}
	
	//Copy constructor
	public IctGridParams(IctGridParams prams)
	{
		_type = prams.getType();
		_val = prams.getVal();
		_data = prams.getData();
		_day = prams.getDay();
		_week = prams.getWeek();
		_month = prams.getMonth();
		_row = prams.getRow();
		_column = prams.getColumn();
	}

	public String getVal() {
		return _val;
	}
	public void setVal(String _val) {
		this._val = _val;
	}
	public int getWeek() {
		return _week;
	}
	public void setWeek(int _week) {
		this._week = _week;
	}
	public int getMonth() {
		return _month;
	}
	public void setMonth(int _month) {
		this._month = _month;
	}
	public int getDay() {
		return _day;
	}
	public void setDay(int _day) {
		this._day = _day;
	}
	public ROWS getRow() {
		return _row;
	}
	public void setRow(ROWS _row) {
		this._row = _row;
	}
	public COLUMNS getColumn() {
		return _column;
	}
	public void setColumn(COLUMNS _column) {
		this._column = _column;
	}
	public IctGridContext getType() {
		return _type;
	}
	public HashMap<String, String> getData() {
		return _data;
	}
}
