package org.jimhopp.GPSvsNetwork.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class LocationContentProvider implements BaseColumns {
	public static String AUTHORITY = "org.jimhopp.GPSvsNetwork.provider.LocationsContentProvider";
	public static String LOCATION_PATH = "location";
	public static final Uri LOCATIONS_URI = Uri.parse("content://" +
            AUTHORITY + "/" + LOCATION_PATH);
}
