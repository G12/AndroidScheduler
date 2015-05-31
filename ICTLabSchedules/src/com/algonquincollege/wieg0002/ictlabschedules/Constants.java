/**
 * Global constants - implement this class to get access
 * 
 * @author Thomas Wiegand (wieg0002)
 * @version 1.0 
 * 
 */

package com.algonquincollege.wieg0002.ictlabschedules;

public interface Constants {

	public static final String DOMAIN = "com.algonquincollege.ictlabs.";
	public static final String TIME_STAMP = "TIME_STAMP";
	public static final String TIME_STAMP2 = "TIME_STAMP2";
	public static final String LAB_LIST_JSON = DOMAIN + "LAB_LIST_JSON";
	public static final String ICT_LAB = DOMAIN + "ICT_LAB";

	public static final long TEN_MINUTES = 600000;

	// http://developer.android.com/tools/devices/emulator.html#networkaddresses
	
	// LOCALHOST
	//public static final String URL     = "http://10.0.2.2:8888/ict/tt/";
	// LIVE @ EduMedia
	public static final String URL     = "http://faculty.edumedia.ca/hurdleg/ict/tt/";
}