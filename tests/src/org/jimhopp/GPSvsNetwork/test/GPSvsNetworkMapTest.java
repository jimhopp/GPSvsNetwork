package org.jimhopp.GPSvsNetwork.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import org.jimhopp.GPSvsNetwork.GPSvsNetworkMap;

import com.google.android.maps.MapView;

public class GPSvsNetworkMapTest extends
		ActivityInstrumentationTestCase2<GPSvsNetworkMap> {
	GPSvsNetworkMap mActivity;
	MapView mView;
	String resourceString;

	public GPSvsNetworkMapTest(Class<GPSvsNetworkMap> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	public GPSvsNetworkMapTest() {
		super("org.jimhopp.GPSvsNetwork", GPSvsNetworkMap.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
		mView = (MapView) mActivity
				.findViewById(org.jimhopp.GPSvsNetwork.R.id.mapview);
		resourceString = mActivity
				.getString(org.jimhopp.GPSvsNetwork.R.string.app_name);
	}

	public void testPreconditions() {
		assertNotNull(mView);
	}

	public void testName() {
		assertEquals(resourceString, "GPS vs. Network Location");
	}
	
	public void testMenuExit() {
		this.sendKeys(KeyEvent.KEYCODE_MENU);
		this.sendKeys(KeyEvent.KEYCODE_X);
		assertTrue(mActivity.isFinishing());
	}
}