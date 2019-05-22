package alexa.projectcharizard.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.Comment;
import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

/**
 * The activity for the MapView
 */
public abstract class MapParentActivity extends FragmentActivity implements OnMapReadyCallback {

    final protected Database database = Database.getInstance();
    final protected CurrentRun currentRun = CurrentRun.getInstance();
    // The GoogleMap instance
    protected GoogleMap mMap;

    protected float initZoom = 10.0f;

    protected LatLng initLoc = new LatLng(57.7, 11.96);

    final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 47;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker on every 'spot', and center on Gothenburg
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * It also creates the custom info window for the markers.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(200, 50, 0, 0);
        float initialZoomLevel = initZoom;
        LatLng initialLocation = initLoc;

        mMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(initialLocation, initialZoomLevel));
        showUserLocation();

        //Open connection to database and add all existing spots to the spotlist.
        database.getDatabaseReference().child("Spots").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentRun.getSpots().clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Spot spot = data.getValue(Spot.class);

                    // Initializes the comment list.
                    spot.setCommentList(new ArrayList<Comment>());

                    // Checks if the current spot has any comments.
                    if (data.child("comments").getValue() != null) {
                        // Loads the comments separately since it is a different class.
                        for (DataSnapshot d : data.child("comments").getChildren()) {
                            // Saves the comment in a new object.
                            Comment comment = d.getValue(Comment.class);

                            // Saves the comment in a list in the spot.
                            spot.getCommentList().add(comment);
                        }
                    }
                    currentRun.getSpots().add(spot);
                }
                updateMarkers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Add marker on all 'spot's in spots
        updateMarkers();
    }

    /**
     * A method for showing the user's location on the map
     */
    protected void showUserLocation() {
        // Check if app has permission to access fine location
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not request permission to access fine location
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * A method which is called upon getting a result from a permissions request
     * And then it does the thing that required permission
     *
     * @param requestCode  The int corresponding to the permission that's being regarded
     * @param permissions
     * @param grantResults An int array which has the value of PackageManager.PERMISSION_GRANTED on
     *                     a location in the array if that specific permission is granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        mMap.setMyLocationEnabled(true);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    /**
     * Updates the marker options for the map marker.
     * Sets the icon for the map marker to the icon for the corresponding category.
     */
    private void updateMarkers() {
        mMap.clear();
        for (Spot spot : currentRun.getSpots()) {
            //Add all markers
            initMarker(spot);
        }
    }

    /**
     * Initializes a marker and places it on the map
     *
     * @param spot the spot correlated with the new marker
     */
    protected Marker initMarker(Spot spot) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                .title(spot.getName())
                .icon(getMarkerIcon(spot.getCategory())));
    }

    /**
     * Identifies what category the specified spot belongs to and returns the corresponding icon for the category.
     * If the category is OTHER it returns the icon of the map marker.
     *
     * @param category The category of the spot.
     **/
    protected BitmapDescriptor getMarkerIcon(Category category) {
        switch (category) {
            case FRUIT:
                return BitmapDescriptorFactory.fromResource(R.drawable.fruit);
            case VEGETABLE:
                return BitmapDescriptorFactory.fromResource(R.drawable.carrot);
            case BERRY:
                return BitmapDescriptorFactory.fromResource(R.drawable.red_strawberry);
            case MUSHROOM:
                return BitmapDescriptorFactory.fromResource(R.drawable.mushroom);
            default:
                return BitmapDescriptorFactory.fromResource(R.drawable.marker);
        }
    }

    /**
     * Method for checking if connected to internet.
     *
     * @return True if connected to internet, false otherwise
     */
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

}
