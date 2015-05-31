package domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Model an Algonquin College <em>Lab</em>.
 *
 * A lab has the following attributes:
 * 1) a room
 * 2) a description
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @Version 1.0
 */
public class Lab{
	private String description;
	private String room;
	
	// JSON Node names
	public static final String TAG_ICT_LABS    = "ict-labs";
	public static final String TAG_ROOM        = "room";
	public static final String TAG_DESCRIPTION = "description";


	@SuppressWarnings("unused")
	private Lab() {
		// NOOP
	}

	public Lab( String room, String description ) {
		super();

		this.room = room;
		this.description = description;
	}

	public String getDescription() { return description; }
	public String getRoom()        { return room; }

	public void setDescription( String description ) { this.description = description; }
	public void setRoom( String room )               { this.room = room; }

	@Override
	public String toString() {
		return room;
	}
	
	public static List<Lab> getLabList(String json, String defaultJson)
	{
		if(json == null )
		{
			//Use the stored value for off line
			json = defaultJson;
		}
		if(json != null)
		{
			List<Lab> labs = new ArrayList<Lab>();
			try
			{
				JSONObject jsonObj = new JSONObject(json);

				// Getting JSON Array node
				JSONArray jsonLabs = jsonObj.getJSONArray( TAG_ICT_LABS );

				//labs.empty();
				// looping through each Lab, one at a time
				for (int i = 0; i < jsonLabs.length(); i++) {
					JSONObject jsonLab = jsonLabs.getJSONObject( i );

					String room = jsonLab.getString( TAG_ROOM );
					String description = jsonLab.getString( TAG_DESCRIPTION );

					// add this lab to the list of labs
					labs.add( new Lab(room, description) );
				}
				return labs;
			} catch ( JSONException e )
			{
				e.printStackTrace();
			}
		} else
		{
			Log.e( "Lab " + " ServiceHandler", "Couldn't get any data from the url" );
		}
		return null;
	}
}
