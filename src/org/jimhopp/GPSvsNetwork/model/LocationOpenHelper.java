package org.jimhopp.GPSvsNetwork.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationOpenHelper extends SQLiteOpenHelper {
	

	private static final int DATABASE_VERSION = 4;
	private static String DATABASE_NAME = "location_db";
    public static final String LOCATIONS_TABLE_NAME = "locations";
    public static String TIME_COL = "time";
    public static String TYPE_COL = "type";
    public static String LAT_COL = "lat";
    public static String LON_COL = "lon";
    public static String ACCURACY_COL = "accuracy";
    public static String[][] ALL_COLS = { {TIME_COL, "INTEGER"},
    	                                  {TYPE_COL, "TEXT"},
    	                                  {LAT_COL, "REAL"},
    	                                  {LON_COL, "REAL"},
    	                                  {ACCURACY_COL, "REAL"}
    	                                }; 
    public static String[] COL_NAMES = {
    	                                ALL_COLS[0][0],
    	                                ALL_COLS[1][0],
    	                                ALL_COLS[2][0],
    	                                ALL_COLS[3][0],
    	                                ALL_COLS[4][0]
    	                               };
    private static final String LOCATIONS_TABLE_CREATE = "CREATE TABLE " 
    		+ LOCATIONS_TABLE_NAME + " (" 
            + ALL_COLS[0][0] + " " + ALL_COLS[0][1] + ", " 
            + ALL_COLS[1][0] + " " + ALL_COLS[1][1] + ", " 
            + ALL_COLS[2][0] + " " + ALL_COLS[2][1] + ", "
            + ALL_COLS[3][0] + " " + ALL_COLS[3][1] + ", " 
            + ALL_COLS[4][0] + " " + ALL_COLS[4][1] + ");";

    public LocationOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(this.getClass().getSimpleName(), "instantiated");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.i(this.getClass().getSimpleName(), "creating database");
        db.execSQL(LOCATIONS_TABLE_CREATE);
    	Log.i(this.getClass().getSimpleName(), "created database");

    }


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
    	Log.i(this.getClass().getSimpleName(), "upgrading database");
    	db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME + ";");
    	onCreate(db);
		
	}
}

