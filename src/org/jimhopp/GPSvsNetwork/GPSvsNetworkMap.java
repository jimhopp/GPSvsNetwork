package org.jimhopp.GPSvsNetwork;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.jimhopp.GPSvsNetwork.model.PhoneLocationModel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import org.jimhopp.GPSvsNetwork.R;

public class GPSvsNetworkMap extends MapActivity {
	List<Overlay> mapOverlays;
	Drawable drawable;
	LocationOverlay itemizedOverlay;
	PhoneLocationModel model;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapView mapview;
        setContentView(R.layout.main);
        
        mapview = (MapView) findViewById(R.id.mapview);
        mapview.setBuiltInZoomControls(true);
        mapview.setClickable(true); 
        mapview.setEnabled(true); 
        mapview.setSatellite(false); 
        mapview.setTraffic(false); 
     // start out with a general zoom 
        final MapController mc = mapview.getController();
        mc.setZoom(16);
        mapOverlays = mapview.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        itemizedOverlay = new LocationOverlay(drawable);
        
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        model = new PhoneLocationModel(lm);
        
        Location gps = model.getGPSLocation();
        Location network = model.getNetworkLocation();
        GeoPoint gPoint, nPoint;
        OverlayItem overlayitem;
        
        //GeoPoint point = new GeoPoint((int)(37.65584 * 1e6), (int)(-122.40488 * 1e6));
        if (gps != null) {
        	gPoint = new GeoPoint((int) (gps.getLatitude() * 1e6),
        			(int) (gps.getLongitude() * 1e6));
        	overlayitem = new OverlayItem(gPoint, "", "");
        	itemizedOverlay.addOverlay(overlayitem);
        	mc.setCenter(gPoint);
        }
        if (network != null) {
        	nPoint = new GeoPoint((int) (network.getLatitude() * 1e6),
        						  (int) (network.getLongitude() * 1e6));
        	overlayitem = new OverlayItem(nPoint, "", "");
        	itemizedOverlay.addOverlay(overlayitem);
        }
        
        mapOverlays.add(itemizedOverlay);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	    menu.add(Menu.NONE, 1, Menu.NONE, "Email Location").setAlphabeticShortcut('e');
	    menu.add(Menu.NONE, 2, Menu.NONE, "Exit").setAlphabeticShortcut('x');
	    return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			Location locG = model.getGPSLocation();
			Location locN = model.getNetworkLocation();
			String body;
			if (locG != null && locN != null) {
				String text = "My current locations:\n" +
						"GPS    : "	+ (locG != null ? locG.getLatitude() : -999)
						+ ", " + (locG != null ? locG.getLongitude() : -999) + "\n" 
						+ "Network: "	+ (locN != null ? locN.getLatitude() : -999)
						+ ", " + (locN != null ? locN.getLongitude() : -999);
				String qmap_link;
				try {
					String query_string = "size=512x512"
					     + "&markers=" 
						 + URLEncoder.encode("color:blue|label:G|" 
					             + locG.getLatitude() + "," + locG.getLongitude(), "UTF-8")
					     + "&markers="
					     + URLEncoder.encode("color:green|label:N|" 
					             + locN.getLatitude() + "," + locN.getLongitude(), "UTF-8")
					     + "&sensor=true";
					qmap_link = "http://maps.googleapis.com/maps/api/staticmap?" + query_string;
				} catch (UnsupportedEncodingException e) {
					qmap_link = e.getMessage();
				}
				body = text + "\n " + qmap_link;
			}
			else {
				body = "(location unknown!)";
			}			
			Uri addr = Uri.fromParts("mailto", "jimhopp@gmail.com", null);
			Intent email = new Intent(Intent.ACTION_SENDTO, addr);
			email.putExtra(Intent.EXTRA_TEXT, body);
			email.putExtra(Intent.EXTRA_SUBJECT, "my location");
			PackageManager pm = getPackageManager();
			if (pm.resolveActivity(email, PackageManager.MATCH_DEFAULT_ONLY) != null) {
				startActivity(email);
			}
			else {
				Toast toast = Toast.makeText(this, "Sorry, email not configured on this device",
						Toast.LENGTH_SHORT);
				toast.show();			
			}
			return true;
			
		case 2: 
			finish();
			return true;

		default:
				break;
		}
		return false;
	}
}