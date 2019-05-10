package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

/**
 * The activity for the MapView
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final Database database = Database.getInstance();
    // The GoogleMap instance
    protected GoogleMap mMap;
    // All spots that will be added upon map refresh
    // The button for redirecting to Add Spot Activity
    private ImageButton plsBtn;

    //The button for opening the filter
    private ImageButton filterBtn;

    // The list of all checkboxes
    private List<Category> checkBoxes = new ArrayList<>();

    final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 47;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connect to layout file
        contentView();

        initPlsBtn();
        initFilterBtn();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

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
        mMap.setPadding(200, 0, 0, 0);
        float initialZoomLevel = initZoom();
        LatLng initialLocation = initLoc();

        mMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(initialLocation, initialZoomLevel));
        showUserLocation();

        //Open connection to database and add all existing spots to the spotlist.
        database.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                database.getSpots().clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Spot spot = data.getValue(Spot.class);
                    database.getSpots().add(spot);
                }
                updateMarkers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SpotDetailViewAdapter infoWindowAdapter = new SpotDetailViewAdapter(this, database.getSpots());
        mMap.setInfoWindowAdapter(infoWindowAdapter);

        // Add marker on all 'spot's in spots
        updateMarkers();

        // Set a listener to make the RelativeLayout filterBoxes Gone when clicking on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ((RelativeLayout) findViewById(R.id.filterBoxes)).setVisibility(View.GONE);
            }
        });

    }

    /**
     * Returns true if the spot's category is checked true in the checkbox, otherwise false
     *
     * @param s the spot to check
     * @return true if the spot's category is checked true in the checkbox, otherwise false
     */
    private boolean filter(Spot s) {
        return checkBoxes.contains(s.getCategory());
    }

    /**
     * A method for showing the user's location on the map
     */
    protected void showUserLocation() {
        // Check if app has permission to access fine location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not request permission to access fine location
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
            //mMap.setOnMyLocationButtonClickListener(this);
            //mMap.setOnMyLocationClickListener(this);
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
        // Clear all markers
        mMap.clear();
        // Add all markers
        for (final Spot spot : database.getSpots()) {
            if (filter(spot)) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                        .title(spot.getName())
                        .icon(getMarkerIcon(spot.getCategory())));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(MapsActivity.this, DetailedViewActivity.class);
                        intent.putExtra("SpotLatitude", spot.getLatitude());
                        intent.putExtra("SpotLongitude", spot.getLongitude());
                        intent.putExtra("SpotDescription", spot.getDescription());
                        intent.putExtra("SpotName", spot.getName());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    /**
     * Identifies what category the specified spot belongs to and returns the corresponding icon for the category.
     * If the category is OTHER it returns the icon of the map marker.
     *
     * @param category The category of the spot.
     **/
    private BitmapDescriptor getMarkerIcon(Category category) {
        if (category.equals(Category.FRUIT)) {
            return BitmapDescriptorFactory.fromResource(R.drawable.fruit);
        } else if (category.equals(Category.VEGETABLE)) {
            return BitmapDescriptorFactory.fromResource(R.drawable.carrot);
        } else if (category.equals(Category.BERRY)) {
            return BitmapDescriptorFactory.fromResource(R.drawable.red_strawberry);
        } else if (category.equals(Category.MUSHROOM)) {
            return BitmapDescriptorFactory.fromResource(R.drawable.mushroom);
        } else {
            return BitmapDescriptorFactory.fromResource(R.drawable.marker);
        }
    }

    protected void contentView() {
        setContentView(R.layout.activity_maps);
    }

    /**
     * Initializes the plus button to redirect to the AddSpotActivity
     */
    protected void initPlsBtn() {
        // Find the plus button
        plsBtn = (ImageButton) findViewById(R.id.plsbtn);
        // Set a listener
        plsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, AddSpotActivity.class);
                intent.putExtra("ViewedLocationLat", mMap.getCameraPosition().target.latitude);
                intent.putExtra("ViewedLocationLong", mMap.getCameraPosition().target.longitude);
                intent.putExtra("ViewedLocationZoom", mMap.getCameraPosition().zoom);
                startActivity(intent);
            }
        });
    }

    /**
     * Initializes the filter button with all its functionality, besides closing the window upon
     * clicking elsewhere. That is done in onMapReady
     */
    protected void initFilterBtn() {
        RelativeLayout rel = ((RelativeLayout) findViewById(R.id.filterBoxes));
        // Arbitrary number for ID which hopefully doesn't collide with other ID
        int id = 5030201;
        // The amounts of lines currently added
        int counter = 0;
        for (final Category category : Category.values()) {
            checkBoxes.add(category);

            createCheckbox(id, counter, category, rel);
            createTextView(id, counter, category, rel);

            counter++;
        }
        rel.getLayoutParams().height = 50 + 60 * counter;

        // Find the filter button
        filterBtn = (ImageButton) findViewById(R.id.filterbtn);

        // Set a listener to make the RelativeLayout visible
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RelativeLayout) findViewById(R.id.filterBoxes)).setVisibility(View.VISIBLE);
            }
        });

    }

    private void createCheckbox(int id, int counter, final Category category, RelativeLayout rel) {
        // Create parameters for the check box
        RelativeLayout.LayoutParams paramsCB = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(true);
        checkBox.setId(id);
        paramsCB.setMargins(0, 15 + 60 * counter, 0, 0);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxes.add(category);
                    updateMarkers();
                } else {
                    checkBoxes.remove(category);
                    updateMarkers();
                }
            }
        });
        rel.addView(checkBox, paramsCB);
    }

    private void createTextView(int id, int counter, final Category category, RelativeLayout rel) {
        // Create parameters for the text view
        RelativeLayout.LayoutParams paramsTxt = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Create a TextView with the specified margins and
        // an ID which should not collide with the checkbox's ID
        TextView txt = new TextView(this);
        txt.setId(id + 1000);
        paramsTxt.setMargins(100, 30 + 60 * (counter), 0, 0);
        txt.setText(category.toString());
        rel.addView(txt, paramsTxt);
    }

    protected float initZoom() {
        return 10.0f;
    }

    protected LatLng initLoc() {
        return new LatLng(57.7, 11.96);
    }


}
