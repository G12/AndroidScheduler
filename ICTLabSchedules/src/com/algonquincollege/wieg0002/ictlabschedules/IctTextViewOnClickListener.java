/**
 * Listens for clicks on individual cells of the calendar grid.
 * Processes clicks according to context - see IctGridContext
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IctTextViewOnClickListener implements OnClickListener {

	private Context _context;
	private IctGridParams _parms;
	/**
	 *  Used to edit text in cell
	 */
	public IctTextViewOnClickListener(Context context, IctGridParams parms)
	{
		_context = context;
		_parms = parms;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		final TextView txtView = (TextView)v;
		
		//Display edit Box
		String title = "Edit: " + _parms.getType().name();
		String msg = "Type: " + _parms.getType().name() + "\nValue" + _parms.getVal();
		Boolean canEdit = false;
		switch(_parms.getType())
		{
			//TODO do what you wanta do
			case DAY_OF_MONTH_WITH_CONTENT: //Can't do this with C# eh!
			case DAY_OF_MONTH:
				canEdit = true;
				break;
			case CELL_TEXT:
				canEdit = true;
				msg = "hour:" + _parms.getRow().name() + "\nweek:" + _parms.getWeek() + "\nselected day:" + _parms.getColumn().name() 
					+ "\nmonth:" + _parms.getMonth() + "\ntoday:" + _parms.getDay() + "\n";
						
				break;
			case HOUR_OF_DAY:
				title = "Information: " + _parms.getType().name();
				msg = "hour:" + _parms.getRow().name() + "\nweek:" + _parms.getWeek() + "\ntoday:" + _parms.getDay();
				break;
			case WEEK_OF_MONTH:
			case MONTH:
			case DAY_NAME:
			case CALENDAR_VALUES:
				title = "Information: " + _parms.getType().name();
				msg = "Do something with these values month:" + _parms.getMonth() + " week:" + _parms.getWeek() + " etc.";
				break;
		}
		
		AlertDialog.Builder alert = new AlertDialog.Builder(_context);

		alert.setTitle(title);
		alert.setMessage(msg);
		final EditText input;
		if(canEdit)
		{
			// Set an EditText view to get user input 
			input = new EditText(_context);
			input.setText(_parms.getVal());
			alert.setView(input);
		}
		else
		{
			input = null;
		}
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				if(input != null)
				{
					String text = input.getText().toString();
					Toast.makeText(_context, "You entered: " + text, Toast.LENGTH_SHORT).show();
					txtView.setText(text);
					if(_parms.getType().ordinal() == IctGridContext.CELL_TEXT.ordinal())
					{
						//String key = _parms.getRow().name() + _parms.getColumn().name();
						//TODO this kills getColor Map which relies on the value of a cell to set the color
						//Much more work needed to develop a viable two way data scheme
						//_parms.getData().put(key, text);
					}
				}
			}
		});
		if(canEdit)
		{
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
				}
			});
		}
		alert.show();
	}
}
