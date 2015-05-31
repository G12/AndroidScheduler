/**
 * This class extends GridLayout and has methods for drawing rows columns and cells
 * on the underlying grid layout
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IctGridLayout extends GridLayout {

	//Note 480 virtual pixels wide
	public static final float DEFAULT_WIDTH = 480;
	public static final float ROW_HEIGHT = 30;
	public static final int CELL_WIDTH = 62;
	public static final int DELTA_COL = 16;
	
	public static final int IS_SPANNED = -1;

	public static final float LARGE_FONT = 7;
	public static final float SMALL_FONT = 5;
	public static final float WIDE_FONT = 10;

	public static final int ALPHA_DARK = 0xFF000000;
	public static final int ALPHA_MID = 0xAA000000;
	public static final int ALPHA_LIGHT = 0x88000000;
	public static final int DARK_LIGHT = 0xAEC161;
	public static final int MID_LIGHT = 0xDEFE5D;
	public static final int HIGH_LIGHT = 0xE2FB7E;
	public static final int PINK_LIGHT = 0xFF66FF;
	public static final int BORDER_GREY = 0xC7C7C7;
	public static final int BORDER_DARK_GREY = 0x555555;
	public static final int BORDER_BLACK = 0x000000;
	public static final int BLACK_STROKE = 0xAA000000;
	public static final int DARK_GREY_STROKE = 0xAA555555;
	public static final int GREY_STROKE = 0xAAC7C7C7;
	public static final int DAY_GREY = 0xF3F3F3;
	public static final int WHITE = 0xFFFFFF;
	public static final int BIG_WORD_COUNT = 5;
	
	private HashMap<String, String> _data;
	private HashMap<String, Integer> _colorMap;
	private String _errMsg;
	private int _screen_width;
	private int _screen_height;
	private float _ratio;
	
	public IctGridLayout(Context context, int width, int height, HashMap<String, Integer> colorMap) {
		super(context);
		init(width, height, colorMap);
	}

	public IctGridLayout(Context context, AttributeSet attrs, int width, int height, HashMap<String, Integer> colorMap) {
		super(context, attrs);
		init(width, height, colorMap);
	}

	public IctGridLayout(Context context, AttributeSet attrs, int defStyle, int width, int height, HashMap<String, Integer> colorMap) {
		super(context, attrs, defStyle);
		_screen_width = width;
		init(width, height, colorMap);
	}
	
	public void addDataMap(HashMap<String, String> data)
	{
		_data = data;
		_errMsg = "";
	}
	
	private void init(int width, int height, HashMap<String, Integer> colorMap)
	{
		_screen_width = width;
		_screen_height = height;
		_colorMap = colorMap;
		_ratio = (float)_screen_width / DEFAULT_WIDTH;
		//If we need to set custom params
		
		ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
		if(layoutParams == null)
		{
			layoutParams = new LayoutParams();
		}
		layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		setLayoutParams(layoutParams);
	}
	
	private String condenseWord(String word)
	{
		if(word.length() > BIG_WORD_COUNT)
		{
			String Cap = word.substring(0, 1);
			String remainder = word.substring(1, 3);
			word = Cap.toUpperCase() + remainder;
		}
		return word;
	}
	
	private int getRowspan(COLUMNS col, ROWS row, String val)
	{
		int count = 1;
		if(row.ordinal() > 0)
		{
			//look for equal value 1st item above
			String row_name = ROWS.values()[row.ordinal()-1].name();
			String test = _data.get(row_name + col.name());
			//if we have a match return -1 indicating not to add a view
			if(test.equals(val)) return IS_SPANNED;
		}
		//Get the count of equal vals imediately below
		for(int i = row.ordinal()+1; i < ROWS.values().length; i++)
		{
			String row_name = ROWS.values()[i].name();
			String test = _data.get(row_name + col.name());
			if(test.equals(val))
			{
				count++;
			}
			else
			{
				return count;
			}
		}
		return count;
	}
	
	private Integer getColor(String key)
	{
		return (int)_colorMap.get(key);
	}
	
	//Draws top row using a range of values
	public void drawTopRow(String rowLabel, int[] range, String caption, int month, int current_day)
	{
		//Add the grid top left corner
		IctGridParams prams = new IctGridParams(rowLabel, IctGridContext.MONTH, _data, month);
		prams = new IctGridParams(prams);
		addCell(prams, 0, 1, 0, 1, CELL_WIDTH - DELTA_COL, BORDER_GREY, BACKGROUND_STYLE.solid);
        for(int i=0; i < range.length; i++)
		{
			Boolean isCurrentDay = current_day == range[i];
			int color = BORDER_GREY;
			if(isCurrentDay)
			{
				color = DARK_LIGHT;
			}
        	String colName = condenseWord(COLUMNS.values()[range[i]].name());
			IctGridParams pram = new IctGridParams(colName, IctGridContext.DAY_NAME, _data, month);
			addCell(pram,0,1,i + 1,1, CELL_WIDTH, color, BACKGROUND_STYLE.solid);
        }
	}

	//Draws top row using start and end values
	public void drawTopRow(String rowLabel, int start, int end, String caption, int month, int current_day)
	{
		//Add the grid top left corner
		IctGridParams prams = new IctGridParams(rowLabel, IctGridContext.MONTH, _data, month);
		addCell(prams, 0, 1, 0, 1, CELL_WIDTH - DELTA_COL, BORDER_GREY, BACKGROUND_STYLE.solid);
		for(COLUMNS column: COLUMNS.values())
        {
			if(column.ordinal() >= start && column.ordinal() <= end)
			{
				Boolean isCurrentDay = current_day == column.ordinal();
				int color = BORDER_GREY;
				if(isCurrentDay)
				{
					color = DARK_LIGHT;
				}
				String colName = condenseWord(column.name());
				IctGridParams pram = new IctGridParams(colName, IctGridContext.DAY_NAME, _data, month, column);
				addCell(pram,0,1,column.ordinal() - start + 1,1, CELL_WIDTH, color, BACKGROUND_STYLE.solid);
			}
        }
	}
	
	//Draw the days of month using a range
	public void drawDaysOfMonth(int[] day_range, IctDay[] days, int week, int month, int current_day)
	{
		//Add the left corner
		IctGridParams prams = new IctGridParams("day", IctGridContext.WEEK_OF_MONTH, _data, week, month);
		addCell(prams, 0, 1, 0, 1, CELL_WIDTH - DELTA_COL, DAY_GREY, BACKGROUND_STYLE.solid);
		for(int i=0; i < day_range.length; i++)
		{
			Boolean isCurrentDay = day_range[i] == current_day;
			int color = DAY_GREY;
			if(isCurrentDay)
			{
				color = HIGH_LIGHT;
			}
			IctGridParams pram = new IctGridParams("" + day_range[i], IctGridContext.DAY_OF_MONTH, _data, week, month, day_range[i]);
			addCell(pram,0,1,i + 1,1, CELL_WIDTH, color, BACKGROUND_STYLE.solid_dark);
        }
	}

	//Draw the days of month
	public void drawCompactDaysOfMonth(String rowLabel, int start, int end, IctDay[] days, int week, int month)
	{
		//Add the left corner
		int i = 0;
		IctGridParams prams = new IctGridParams(rowLabel, IctGridContext.WEEK_OF_MONTH, _data, week, month);
		addCell(prams, 0, 1, 0, 1, CELL_WIDTH - DELTA_COL, DAY_GREY, BACKGROUND_STYLE.solid);
		for(COLUMNS column: COLUMNS.values())
        {
			if(column.ordinal() >= start && column.ordinal() <= end)
			{
				Boolean foundContent = false;
				for(ROWS row: ROWS.values())
				{
					String key = row.name() + column.name();
					String val = _data.get(key);
					if(!val.equals(""))
					{
						foundContent = true;
						break;
					}
				}
				int color = WHITE;
				IctGridContext grid_context = IctGridContext.DAY_OF_MONTH;
				if(foundContent)
				{
					grid_context = IctGridContext.DAY_OF_MONTH_WITH_CONTENT;
					color = MID_LIGHT;
				}
				IctGridParams pram = new IctGridParams("" + days[i].dayOfMonth(), grid_context, _data, week, month, days[i].dayOfMonth());
				addCell(pram,0,1,column.ordinal() - start + 1,1, CELL_WIDTH, color, BACKGROUND_STYLE.solid_dark);
			}
			//Increment index into days array
			i++;
        }
	}

	//Draw days of week using a start and end
	public void drawDaysOfMonth(String rowLabel, int start, int end, IctDay[] days, int week, int month)
	{
		//Add the left corner
		int i = 0;
		IctGridParams prams = new IctGridParams(rowLabel, IctGridContext.WEEK_OF_MONTH, _data, week, month);
		addCell(prams, 0, 1, 0, 1, CELL_WIDTH - DELTA_COL, DAY_GREY, BACKGROUND_STYLE.solid);
		for(COLUMNS column: COLUMNS.values())
        {
			Boolean isCurrentDay = days[i].isCurrentDay();
			if(column.ordinal() >= start && column.ordinal() <= end)
			{
				int color = DAY_GREY;
				BACKGROUND_STYLE style = BACKGROUND_STYLE.solid_dark;
				if(isCurrentDay)
				{
					color = HIGH_LIGHT;
				}
				IctGridParams pram = new IctGridParams("" + days[i].dayOfMonth(), IctGridContext.DAY_OF_MONTH, _data, week, month, days[i].dayOfMonth());
				addCell(pram,0,1,column.ordinal() - start + 1,1, CELL_WIDTH, color, style);
			}
			//Increment index into days array
			i++;
        }
	}

	public void drawLeftRow(int week, int month, int day, int current_hour, Boolean isCurrentWeek)
	{
		for(ROWS row : ROWS.values())
		{
			String name = row.name();
			name = name.substring(1);
			int hour = Integer.parseInt(name);
			int color = BORDER_GREY;
			if(isCurrentWeek)
			{
				int test = current_hour*100;
				if(hour == test)
				{
					color = DARK_LIGHT;
				}
			}
			IctGridParams pram = new IctGridParams(name, IctGridContext.HOUR_OF_DAY, _data, week, month, day, row);
			addCell(pram, row.ordinal() + 1, 1, 0, 1, CELL_WIDTH - DELTA_COL, color, BACKGROUND_STYLE.solid);
		}
	}
	
	public void drawGridRange(int[] range, IctDay[] days, int hour, Boolean isCurrentWeek, int week, int month, int day)
	{
		for(ROWS row : ROWS.values())
		{
			for(int i=0; i < range.length; i++)
            {
				String strHour = "H" + hour + "00";
				Boolean isCurrentDay = days[range[i]].isCurrentDay();
				Boolean isWeekday = COLUMNS.values()[range[i]] != COLUMNS.sunday && COLUMNS.values()[i] != COLUMNS.saturday; 
				String key = row.name() + COLUMNS.values()[range[i]].name();
				String val = _data.get(key);
				int span = 1;
				if(!val.equals(""))
				{
					//Test for rowSpan value
					span = getRowspan(COLUMNS.values()[range[i]], row, val);
				}
				//IS_SPANNED indicates the cell above is spanning into this territory
				//Don't draw anything here
				if(span != IS_SPANNED)
				{
					BACKGROUND_STYLE style = BACKGROUND_STYLE.gradient_light;
					int color = WHITE;
					if(val.equals(""))
					{
						style = BACKGROUND_STYLE.solid_dark;
						if(isCurrentDay && isWeekday)
						{
							color = HIGH_LIGHT;
							if(strHour.equals(row.name()))
							{		
								color = MID_LIGHT;
							}
						}
						else if(strHour.equals(row.name()) && isCurrentWeek)
						{		
							color = HIGH_LIGHT;
						}
						else
						{
							color = WHITE;
						}
					}
					else
					{
						if(isCurrentDay && isWeekday)
						{
							style = BACKGROUND_STYLE.gradient;
							if(isReallyNow(val, strHour, row, COLUMNS.values()[range[i]].name()))
							{
								style = BACKGROUND_STYLE.gradient_dark;
							}
						}
						color = getColor(val);
					}
					IctGridParams pram = new IctGridParams(val, IctGridContext.CELL_TEXT, _data, week, month, day, row, COLUMNS.values()[range[i]]);
					this.addCell(pram, row.ordinal() + 1, span, i + 1, 1, CELL_WIDTH, color, style);
				}
            }
        }
	}
	
	//Deal with cells spanning more than one row
	private Boolean isReallyNow(String current_val, String strHour, ROWS row, String columnName)
	{
		Boolean isNow = false;
		//Check BELOW to see if I'm spanning into the current time zone
		for(int i=row.ordinal(); i < ROWS.values().length; i++)
		{
			String RowVal = ROWS.values()[i].name();
			String key = RowVal + columnName;
			String test_val = _data.get(key);
			//if we have a match test time for a match
			if(test_val.equals(current_val))
			{
				isNow = strHour.equals(RowVal);
				if(isNow)
				{
					//Now we truly can get out of here
					break;
				}
			}
			else
			{
				//Stop testing and get out of here
				break;
			}
		}
		return isNow;
	}
	
	public void drawGrid(int start, int end, IctDay[] days, int hour, Boolean isCurrentWeek, int week, int month, int day)
	{
		for(ROWS row : ROWS.values())
		{
			for(COLUMNS column: COLUMNS.values())
            {
				if(column.ordinal() >= start && column.ordinal() <= end)
				{
					String strHour = "H" + hour + "00";
					Boolean isCurrentDay = days[column.ordinal()].isCurrentDay();
					Boolean isWeekday = column != COLUMNS.sunday && column != COLUMNS.saturday;
					Boolean isNow = strHour.equals(row.name());
					String key = row.name() + column.name();
					String val = _data.get(key);
					int span = 1;
					if(!val.equals(""))
					{
						//Test for rowSpan value
						span = getRowspan(column, row, val);
					}
					//IS_SPANNED indicates the cell above is spanning into this territory
					//Don't draw anything here
					if(span != IS_SPANNED)
					{
						BACKGROUND_STYLE style = BACKGROUND_STYLE.gradient_light;
						int color = WHITE;
						if(val.equals(""))
						{
							style = BACKGROUND_STYLE.solid_dark;
							if(isCurrentDay && isWeekday)
							{
								color = HIGH_LIGHT;
								if(isNow)
								{		
									color = MID_LIGHT;
								}
							}
							else if(isNow && isCurrentWeek)
							{		
								color = HIGH_LIGHT;
							}
							else
							{
								color = WHITE;
							}
						}
						else
						{
							if(isCurrentDay && isWeekday)
							{
								style = BACKGROUND_STYLE.gradient;
								if(isReallyNow(val, strHour, row, column.name()))
								{
									style = BACKGROUND_STYLE.gradient_dark;
								}
							}
							color = getColor(val);
						}
						IctGridParams pram = new IctGridParams(val, IctGridContext.CELL_TEXT, _data, week, month, day, row, column);
						this.addCell(pram, row.ordinal() + 1, span, column.ordinal() - start + 1, 1, CELL_WIDTH, color, style);
					}
				}
            }
        }
	}
	
	private float makeTextSize(String val)
	{
		float text_size;
		if(_screen_width > _screen_height)
		{
			text_size = WIDE_FONT;
		}
		else if(val.length() > 9)
		{
			text_size = SMALL_FONT;
		}
		else
		{
			text_size = LARGE_FONT;
		}
		return text_size;
	}
	
	private int makeCellWidth(int cell_width)
	{
		//Adjust cell sizes to fit screen
		//Should be a better way to do this TODO investigate
		return (int) (cell_width*_ratio);
	}
	
	private int makeRowHeight(int rowSpan)
	{
		return (int) (ROW_HEIGHT*rowSpan*_ratio);
	}
	
	private GradientDrawable makeBackground(int color, BACKGROUND_STYLE style)
	{
		GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(3);
		switch(style)
		{
		case gradient:
			gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	        int[]colors = {ALPHA_DARK + color, ALPHA_MID + color};
	        gd.setColors(colors);
	        gd.setStroke(1, BLACK_STROKE);
	        break;
		case gradient_light:
			gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	        int[]colors2 = {ALPHA_MID + color, ALPHA_LIGHT + color};
	        gd.setColors(colors2);
	        gd.setStroke(1, DARK_GREY_STROKE);
	        break;
		case gradient_dark:
			gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	        int[]colors3 = {ALPHA_DARK + color, ALPHA_MID + color};
	        gd.setColors(colors3);
	        gd.setStroke(4, BLACK_STROKE);
	        break;
		case solid_dark:
	        gd.setColor(ALPHA_DARK + color); 
	        gd.setStroke(1, 0xFFF8F8F8);
	        break;
		case solid_light:
	        gd.setColor(ALPHA_LIGHT + color); 
	        gd.setStroke(1, 0xFFF8F8F8);
	        break;
		default:
	        gd.setColor(ALPHA_MID + color); 
	        gd.setStroke(1, 0xFFF8F8F8);
	        break;
		}
		return gd;
	}
	
	public void addCell(IctGridParams params, int rowPosition, int rowSpan, int colPosition, int colSpan, int cell_width, int color, BACKGROUND_STYLE style)
	{
		
		_errMsg += "val: " + params.getVal() + " row[" + rowPosition + "] column[" + colPosition + "]\n";

		GridLayout.Spec rowSpec;
		GridLayout.Spec colSpec;
		if(rowSpan == 1)
		{
			rowSpec = spec(rowPosition);
		}
		else
		{
			rowSpec  = spec(rowPosition, rowSpan);
		}
		if(colSpan == 1)
		{
			colSpec = spec(colPosition);
		}
		else
		{
			colSpec  = spec(colPosition, colSpan);
		}
		
		GridLayout.LayoutParams lop = new LayoutParams(rowSpec,colSpec);
		TextView txtView = new TextView(this.getContext());
		txtView.setGravity(Gravity.CENTER);
		txtView.setTextSize(makeTextSize(params.getVal()));
		txtView.setWidth(makeCellWidth(cell_width));
		txtView.setHeight(makeRowHeight(rowSpan));
		txtView.setBackground(makeBackground(color, style));
		
		LayoutParams lpView = new LayoutParams();
		lpView.height = LayoutParams.MATCH_PARENT;
        txtView.setLayoutParams(lpView);
        txtView.setTag(params.getVal()); //TODO not used?
		txtView.setText(params.getVal());
		
		txtView.setOnClickListener(new IctTextViewOnClickListener(getContext(), params));
		this.addView(txtView, lop);
	}
	
	public void clear()
	{
		this.removeAllViews();
	}

	public String getErrMsg() {
		return _errMsg;
	}
	
	////////////////////////  Experimental Zone /////////////////////////////
	private TextView makeTextSliver(ROWS row, COLUMNS column, int cell_width)
	{
		TextView txtView = new TextView(getContext());
		txtView.setGravity(Gravity.CENTER);
		txtView.setWidth(makeCellWidth(cell_width));
		float height = makeRowHeight(1)/ROWS.values().length;
		txtView.setHeight((int)height);
		String key = row.name() + column.name();
		String val = _data.get(key);
		int color = WHITE;
		if(val.equals(""))
		{
			//TODO Highlight routine
		}
		else
		{
			color = getColor(val);
		}
		txtView.setBackground(makeBackground(color, BACKGROUND_STYLE.solid_dark));
		LayoutParams lpView = new LayoutParams();
		lpView.height = LayoutParams.WRAP_CONTENT;
        txtView.setLayoutParams(lpView);
		txtView.setText("");
		return txtView;
	}
	
	public void drawCompactDaysOfMonthExperimental(String rowLabel, int start, int end, IctDay[] days, int week, int month)
	{
		//Add the left corner
		int i = 0;
		IctGridParams prams = new IctGridParams(rowLabel, IctGridContext.WEEK_OF_MONTH, _data, week, month);
		addCell(prams, 0, 1, 0, 1, CELL_WIDTH - DELTA_COL, DAY_GREY, BACKGROUND_STYLE.solid);
		for(COLUMNS column: COLUMNS.values())
        {
			if(column.ordinal() >= start && column.ordinal() <= end)
			{
				LinearLayout linLayout = new LinearLayout(getContext());
		        // specifying vertical orientation
		        linLayout.setOrientation(LinearLayout.VERTICAL);
		        // creating LayoutParams  
		        //LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); 
		        // set LinearLayout as a root element of the screen 
		        //setContentView(linLayout, linLayoutParam);
				for(ROWS row: ROWS.values())
				{
					LayoutParams lop = new LayoutParams();
					lop.width = LayoutParams.MATCH_PARENT;
					linLayout.addView(makeTextSliver(row, column, makeCellWidth(CELL_WIDTH)), lop);
				}
				IctGridParams pram = new IctGridParams("row:[" + (week-1) + "] " + days[i].dayOfMonth(), IctGridContext.DAY_OF_MONTH, _data, week, month, days[i].dayOfMonth());
				linLayout.setOnClickListener(new IctTextViewOnClickListener(getContext(), pram));
				GridLayout.Spec rowSpec;
				GridLayout.Spec colSpec;
				rowSpec = spec(week-1);
				colSpec = spec(i+1);
				GridLayout.LayoutParams lop = new LayoutParams(rowSpec,colSpec);
				this.addView(linLayout, lop);
			}
			//Increment index into days array
			i++;
        }
	}

}
