package com.algonquincollege.mad9132.ictlabschedules;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import domain.Schedule;

import util.ServiceHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
	private String theLab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		// Get the bundle of extras that was sent to this activity
		Bundle bundle = getIntent().getExtras();
		if ( bundle != null ) {
			theLab = bundle.getString( THE_LAB ).toLowerCase( Locale.getDefault() );
			// Calling async task to get json
			new FetchSchedule().execute( URL, theLab );
		}
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
	private class FetchSchedule extends AsyncTask<String, Void, Void> {
		private ProgressDialog pDialog;
		private Schedule theSchedule;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog( ScheduleActivity.this );
			pDialog.setMessage( "Please wait..." );
			pDialog.setCancelable( false);
			pDialog.show();

			theSchedule = new Schedule();
		}

		@Override
		protected Void doInBackground( String... params ) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall( params[0] + params[1], ServiceHandler.GET );

			Log.d( TAG + " Response: ", "> " + jsonStr );

			if ( jsonStr != null ) {
				try {
					JSONObject jsonObj = new JSONObject( jsonStr );

					JSONObject jsonLab = jsonObj.getJSONObject( params[1] );
                    JSONObject periodRow = jsonLab.getJSONObject( Periods.H0800.name() );
                    System.out.println( "periodRow: " + periodRow.toString() );
                    String scheduledClass = periodRow.getString( DOW.friday.name() );
                    System.out.println( "Friday at 0800: " + scheduledClass );
                    TextView cell = (TextView) findViewById( R.id.H0800friday );
                    cell.setText( scheduledClass );
				} catch ( JSONException e ) {
					e.printStackTrace();
				}
			} else {
				Log.e( TAG + " ServiceHandler", "Couldn't get any data from the url" );
			}

			return null;
		}

		@Override
		protected void onPostExecute( Void result ) {
			super.onPostExecute( result );
			// Dismiss the progress dialog
			if ( pDialog.isShowing() )
				pDialog.dismiss();

			//TODO: cell
		}

	}

}
