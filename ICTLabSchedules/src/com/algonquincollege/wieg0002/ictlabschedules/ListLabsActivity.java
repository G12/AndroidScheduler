/**
 * Display List of Room or Personal names to display schedules for.
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

import java.util.Date;
import java.util.List;

import util.ServiceHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import domain.Lab;

/**
 * Display the list of ICT labs.
 *
 * Usage:
 * 1) Click one of the labs from the list to see its schedule.
 *
 * Notes:
 * 1) class ListLabsActivity extends from Android's class ListActivity.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @Version 1.0
 */
public class ListLabsActivity extends Activity implements Constants {

	GridView gridView;
	private static String _lab_list_json = null;
	
	private ArrayAdapter<Lab> labsAdapter;
	private List<Lab> _lab_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_list_labs);

		Date d = new Date();
		long now = d.getTime();
        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        _lab_list_json = settings.getString(LAB_LIST_JSON, null);
        long then = settings.getLong(TIME_STAMP, 0);
        Boolean isStale = now - then > TEN_MINUTES;

        labsAdapter = new ArrayAdapter<Lab>( this, R.layout.lab_view );

		gridView = (GridView) findViewById(R.id.gridView1);
		
		//Get dimensions
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		gridView.setNumColumns(1);
		if(width > height)
		{
			gridView.setNumColumns(2);
		}
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				String selectedLab = ((TextView) v.findViewById(R.id.lab_name)).getText().toString();
			    Intent intent = new Intent( getApplicationContext(), ScheduleActivity.class );
			    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
			    intent.putExtra(ICT_LAB, selectedLab );
			    startActivity( intent );
			}
		});
        
		if(_lab_list_json == null || isStale)
		{
			// Calling async task to get JSON
			new FetchLabs().execute( URL );
		}
		else
		{
			//Use stored value
			_lab_list = Lab.getLabList(_lab_list_json, null);
			labsAdapter.addAll(_lab_list);
			setAdapter();
		}
	}
	
	public void setAdapter()
	{
		gridView.setAdapter(new MyGridAdapter(this, _lab_list));
	}

	@Override
    protected void onStop() {
		Date d = new Date();
		long now = d.getTime();
		SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(LAB_LIST_JSON, _lab_list_json);
        editor.putLong(TIME_STAMP, now);
		editor.commit();
		super.onStop();
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
	private class FetchLabs extends AsyncTask<String, Void, List<Lab>> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog( ListLabsActivity.this );
			pDialog.setMessage( "Please wait..." );
			pDialog.setCancelable( false );
			pDialog.show();

			labsAdapter.clear();
		}

		@Override
		protected List<Lab> doInBackground( String... params ) {

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String json = sh.makeServiceCall( params[0], ServiceHandler.GET ) ;
			Log.d( LAB_LIST_JSON + " Response: ", "> " + _lab_list_json );
			
			List<Lab> labs = Lab.getLabList(json, _lab_list_json);
			if(labs != null)
			{
				_lab_list_json = json;
			}
			return labs;
		}

		@Override
		protected void onPostExecute( List<Lab> result ) {
			super.onPostExecute( result );
			// Dismiss the progress dialog
			if(pDialog.isShowing())pDialog.dismiss();
			if(result != null)
			{
				_lab_list = result;
				labsAdapter.addAll(_lab_list);
				setAdapter();
			}
			else
			{
				Toast.makeText(getBaseContext(), getString(R.string.network_error) + " " + getString(R.string.no_saved_values), Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
