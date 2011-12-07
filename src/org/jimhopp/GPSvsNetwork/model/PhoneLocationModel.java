package org.jimhopp.GPSvsNetwork.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class PhoneLocationModel {
	//maintains time and current location
	//has callbacks for new fixes, time update
	//calculates age of fixes
	//notifies view to update fields?
	
	public Location lastLocGPS;
	public Location lastLocNetwork;
	public LocationOpenHelper dbh;
	
	
	public PhoneLocationModel(LocationManager lm, Context ctxt) {
        dbh = new LocationOpenHelper(ctxt);
        Log.i(this.getClass().getSimpleName(), "instantiated db handler");		
		LocationListener locationGPS = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) { updateGPS(location); }
			@Override
			public void onProviderDisabled(String providerName) { }
			@Override
			public void onProviderEnabled(String providerName) { }
			@Override
			public void onStatusChanged(String providerName, int providerStatus,
					Bundle extras) { }
        };
        updateGPS(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000l, Float.valueOf("10.0"),
        	locationGPS);
        
		LocationListener locationNetwork = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) { updateNetwork(location); }
			@Override
			public void onProviderDisabled(String providerName) { }
			@Override
			public void onProviderEnabled(String providerName) { }
			@Override
			public void onStatusChanged(String providerName, int providerStatus,
					Bundle extras) { }
        };
        updateNetwork(lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000l, Float.valueOf("10.0"),
        	locationNetwork);

	}
	
	public Location getGPSLocation() { return lastLocGPS; }
	
	public Location getNetworkLocation() { return lastLocNetwork; }
	void updateGPS(Location loc) {
		lastLocGPS = loc;
		recordLocation(loc, "GPS");
	}
	
	void updateNetwork(Location loc) {
		lastLocNetwork = loc;
		recordLocation(loc, "Network");
	}
	
	void recordLocation(Location loc, String tag) {
		ContentValues map = new ContentValues(); 
		map.put(LocationOpenHelper.TIME_COL, loc != null ? loc.getTime() : System.currentTimeMillis()); 
		map.put(LocationOpenHelper.TYPE_COL, tag);
		map.put(LocationOpenHelper.LAT_COL, loc != null ? loc.getLatitude() : 0);
		map.put(LocationOpenHelper.LON_COL, loc != null ? loc.getLongitude() : 0);
		map.put(LocationOpenHelper.ACCURACY_COL, loc != null ? loc.getAccuracy(): 10000000.0);
		if (dbh == null) {
			throw new RuntimeException("dbh is null");
		}
		Log.i(this.getClass().getSimpleName(), "inserting " + map.toString());
		try{ dbh.getWritableDatabase().insert(LocationOpenHelper.LOCATIONS_TABLE_NAME, null, map);
		} catch (SQLException e) { Log.e("Error writing new location", e.toString());
		}
	}

	public String dumpLocations() {
		// TODO Auto-generated method stub
		try {
			Cursor cursor = dbh.getReadableDatabase().query(
					LocationOpenHelper.LOCATIONS_TABLE_NAME,     //table name   
					LocationOpenHelper.COL_NAMES,                 //columns in SELECT clause
					null,                                        //selection, we want all rows 
					null,                                        //selection args
					null,                                        //group by
					null,                                        //having
					LocationOpenHelper.TIME_COL + " ASC"); //sort order
			Log.i(this.getClass().getSimpleName(), "dumpLocations(): got " 
			     + cursor.getCount() + " rows");
			
			int nCols = LocationOpenHelper.ALL_COLS.length;
			StringBuilder strbuf = new StringBuilder("Locations: " + cursor.getCount() 
					+ " rows\n| ");
			boolean more = cursor.moveToNext();
			for (int i=0;i<nCols;i++) {
				strbuf.append(cursor.getColumnName(i) + " |");
			}
			while (more) {
				strbuf.append("\n| ");
				for (int i=0;i<nCols;i++) {
					if (LocationOpenHelper.ALL_COLS[i][1] == "INTEGER") {
						strbuf.append(cursor.getInt(i));
					} else if (LocationOpenHelper.ALL_COLS[i][1] == "REAL") {
						strbuf.append(cursor.getFloat(i));
					} else if (LocationOpenHelper.ALL_COLS[i][1] == "TEXT") {
						strbuf.append(cursor.getString(i));
					} else {
						strbuf.append("(null or blob)");
					}
					strbuf.append(" | ");
				}
				more = cursor.moveToNext();
			}
			return strbuf.toString();
		} catch (SQLException e) {
			Log.e("Error trying to dump locations", e.toString());
			return "Error trying to dump locations " + e.toString();
		}
	}
}