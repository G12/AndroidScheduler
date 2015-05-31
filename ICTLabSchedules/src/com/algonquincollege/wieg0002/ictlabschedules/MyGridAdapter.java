/**
 * Extends BaseAdapter to display a grid of Room/Personal Name and description information
 * When items are clicked the Schedule activity will be called
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

import java.util.List;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import domain.Lab;

public class MyGridAdapter extends BaseAdapter {
	
	private Context _context;
	private List<Lab> _labList;
	
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
	
	private static final int[] pallete = {0xFF0000, 0xFF9900, 0x0099FF, 0xCC00FF, 0x009933, 0x6666FF, 0x00FF00, 0x00FFFF,
		0xFF22FF, 0xCC9922, 0x2299FF, 0x9922FF, 0x229988, 0x2266CC, 0x44FF22, 0x22FFBB,
		0xFF44BB, 0xAA9944, 0x4499BB, 0x7744FF, 0x4499AA, 0x446688, 0x88FF44, 0x44FF88,
		0xFF8888, 0x889988, 0x889988, 0x5588FF, 0x8899FF, 0x886644, 0xAAFF88, 0x88FF44,
		0xFFAAAA, 0x4499AA, 0xAA99AA, 0x33AAFF, 0xAA9908, 0xAA6622, 0x0FFFAA, 0xAAFF22,
		0xFFBBBB, 0x2299BB, 0x0F9900, 0x22BBFF, 0xBB99BB, 0xBB6600, 0x00FFBB, 0xBBFF00
	};

	/**
	 * Display Lab Name and Description
	 */
	public MyGridAdapter(Context context, List<Lab> labList)
	{
		_context = context;
		_labList = labList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null)
		{
			gridView = new View(_context);
			// get layout from my_lab_list.xml
			gridView = inflater.inflate(R.layout.my_lab_list, null);
			// set value into textview
			TextView nameText = (TextView) gridView.findViewById(R.id.lab_name);
			nameText.setText(_labList.get(position).getRoom());
			nameText.setBackground(makeBackground(pallete[position], BACKGROUND_STYLE.gradient));
			
			// set value into textview
			TextView descText = (TextView) gridView.findViewById(R.id.lab_description);
			descText.setText(_labList.get(position).getDescription());
			descText.setBackground(makeBackground(WHITE, BACKGROUND_STYLE.gradient));
			
		} else 
		{
			gridView = (View) convertView;
		}
		return gridView;
	}
	
	private GradientDrawable makeBackground(int color, BACKGROUND_STYLE style)
	{
		GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(0);
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


	@Override
	public int getCount() {
		return _labList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
