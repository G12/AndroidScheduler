/**
 * Select and Display Lab schedules.
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import util.ServiceHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import domain.MySchedule;

/**
 * Display the the schedule for the selected lab.
 *
 * A schedule is a grid of rows and columns,
 * where the rows are periods of time (based on 24-hour clock): 0800, 0900, etc.
 * and where the columns are the days of the week: monday, tuesday, ... friday
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @Version 1.0
 */
public class ScheduleActivity extends Activity implements Constants {

	private MySchedule _mySchedule;
	private IctCalendar _ict_calendar;
	private Calendar _calendar; 
	private LinearLayout _container;
	
	private static String _the_lab_name = null;
	//private static String _lab_list_json = null;
	private static String _jsonSchedule = null;
	
	private SharedPreferences _settings;
	
	private static int calendar_choice = R.id.WeekButton;
	
	private boolean isDead = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.my_activity_schedule);
		
		Date d = new Date();
		long now = d.getTime();
        _settings = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        //_lab_list_json = _settings.getString(LAB_LIST_JSON, null);
        long then = _settings.getLong(TIME_STAMP2, 0);
        Boolean isStale = now - then > TEN_MINUTES;
		
		//Get dimensions
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		_calendar = new GregorianCalendar();
		_mySchedule = new MySchedule();
		_container = (LinearLayout) findViewById(R.id.layout);
		_ict_calendar = new IctCalendar(this, _container, width, height);
		
		// Get the bundle of extras that was sent to this activity
		Bundle bundle = getIntent().getExtras();
		if ( bundle != null )
		{
			_the_lab_name = bundle.getString(ICT_LAB).toLowerCase( Locale.getDefault() );
			//_lab_list_json = bundle.getString(LAB_LIST_JSON);
			_jsonSchedule = _settings.getString(_the_lab_name, null);
			if(isStale)
			{
				getDataFromNetwork();
			}
			else if(_jsonSchedule != null)
			{
				if(populateMySchedule(_jsonSchedule, null, _the_lab_name))
				{
					_ict_calendar.setSchedule(_mySchedule);
					ChangeCalendar(calendar_choice);
				}
				else
				{
					isDead = true;
					Toast.makeText(this, getString(R.string.no_saved_values), Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				getDataFromNetwork();
			}
		}
	}
	
	private void getDataFromNetwork()
	{
		if(isNetworkConnectionAvailable())
		{
			// Calling async task to get json
			new FetchSchedule().execute(URL, _the_lab_name);
		}
		else
		{
			String msg = "";
			String err = getString(R.string.network_error);
			if(_jsonSchedule != null)
			{
				if(populateMySchedule(_jsonSchedule, null, _the_lab_name))
				{
					_ict_calendar.setSchedule(_mySchedule);
					ChangeCalendar(calendar_choice);
					msg = err + " " + getString(R.string.using_saved_values);
				}
				else
				{
					msg = err + " " + getString(R.string.no_saved_values);
				}
			}
			else
			{
				msg = err + " " + getString(R.string.no_saved_values);
				isDead = true;
			}
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
    protected void onStop() {
		Date d = new Date();
		long now = d.getTime();
		SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = settings.edit();
		//editor.putString(LAB_LIST_JSON, _lab_list_json);
        editor.putLong(TIME_STAMP2, now);
		editor.commit();
		super.onStop();
    }
	
	public void storeData()
	{
		SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(_the_lab_name, _jsonSchedule);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_author) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class FetchSchedule extends AsyncTask<String, Void, Boolean>
	{
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog( ScheduleActivity.this );
			pDialog.setMessage( "Please wait..." );
			pDialog.setCancelable( false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground( String... params ) {
			
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			
			String jsonSchedule = sh.makeServiceCall( params[0] + params[1], ServiceHandler.GET );
			//Log.d( TAG + " Response: ", "> " + jsonStr );
			
			if(populateMySchedule(jsonSchedule, _jsonSchedule, params[1]))
			{
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result ) {
			super.onPostExecute( result );
			// Dismiss the progress dialog
			if ( pDialog.isShowing() )
				pDialog.dismiss();
			if(result)
			{
				_ict_calendar.setSchedule(_mySchedule);
				ChangeCalendar(calendar_choice);
			}
			else
			{
				Toast.makeText(getBaseContext(), getString(R.string.no_data), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	//Button Click Listener
	public void ChangeCalendar(View v)
	{
		ChangeCalendar(v.getId());
	}
	
	private void ChangeCalendar(int id)
	{
		if(isDead)
		{
			Toast.makeText(getBaseContext(), getString(R.string.no_data), Toast.LENGTH_SHORT).show();
			return;
		}
		calendar_choice = id;
		int week =  _calendar.get(Calendar.WEEK_OF_MONTH);
		int month = _calendar.get(Calendar.MONTH);
		int day_of_week = _calendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch(id) 
		{
	        case R.id.DayButton:
				_container.removeAllViews();
				_ict_calendar.drawWeek(day_of_week, day_of_week, week, month);
	          break;
	        case R.id.DayPlusButton:
	        	int start = day_of_week - 1;
	        	if(start < 0) start = 6;
	        	int mid = start+1;
	        	if(mid > 6) mid = 0;
	        	int end = mid + 1;
				if(end > 6) end = 0;
				int[] range = new int[3];
				range[0] = start;
				range[1] = mid;
				range[2] = end;
	        	_container.removeAllViews();
				_ict_calendar.drawWeek(range, week, month);
	          break;
	        case R.id.WeekButton:
				_container.removeAllViews();
				_ict_calendar.drawWeek(0, 6, week, month);
	          break;
	        case R.id.MonthButton:
				_container.removeAllViews();
				_ict_calendar.drawMonth(0, 6, month);
	          break;
	        case R.id.CompactMonthButton:
	        	_container.removeAllViews();
				_ict_calendar.drawCompactMonth(0, 6, month);
			 break;
		}
	}
	
	private boolean populateMySchedule(String jsonSchedule, String defaultJson, String the_lab_name )
	{
		boolean retVal = false;
		if(jsonSchedule == null )
		{
			//Use the stored value for off line
			jsonSchedule = defaultJson;
		}
		if ( jsonSchedule != null ) {
			_jsonSchedule = jsonSchedule;
			try {
				JSONObject jsonObj = new JSONObject( jsonSchedule );

				JSONObject jsonLab = jsonObj.getJSONObject(the_lab_name); // params[1] );
                for ( ROWS row : ROWS.values() ) {
                    JSONObject periodRow = jsonLab.getJSONObject( row.name() );
                    for ( COLUMNS column : COLUMNS.values() ) {
                        String scheduledClass = periodRow.getString( column.name() );
                        _mySchedule.atPut(row.name(), column.name(), scheduledClass);
                    }
                }
                retVal = true;
                storeData();
			} catch ( JSONException e ) {
				e.printStackTrace();
			}
		}		
		return retVal;
	}
	
	private boolean isNetworkConnectionAvailable() {  
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();     
	    if (info == null) return false;
	    State network = info.getState();
	    return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
	}  
}
