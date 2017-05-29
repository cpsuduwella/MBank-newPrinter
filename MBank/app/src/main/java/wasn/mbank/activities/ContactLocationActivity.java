package wasn.mbank.activities;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * activity class for contact location
 * @author eranga
 */
public class ContactLocationActivity extends MapActivity{

	//map view
	MapView mapView;
	
	//map controller
	MapController mapController;
    
	//geo point
	GeoPoint geoPoint;
    
	
	/**
	 * when create activity
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.contact_location_layout);
    	
    	//get map view
    	mapView = (MapView) findViewById(R.id.mapView);

    	//get linear layout
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);
        
        //zoom view
        View zoomView = mapView.getZoomControls(); 
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT));
        
        //display
        mapView.displayZoomControls(true);
    	
        //get map controller
        mapController=mapView.getController();
        
        //latitude and longtude
        String coordinates[] = {"1.352566007", "103.78921587"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
 
        //create new geo point
        geoPoint = new GeoPoint((int) (lat * 1E6),(int) (lng * 1E6)); 
         
        //navigate to location
        mapController.animateTo(geoPoint);
        
        //zoom
        mapController.setZoom(17);
        
        mapView.invalidate();
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
		
	}
	
}
