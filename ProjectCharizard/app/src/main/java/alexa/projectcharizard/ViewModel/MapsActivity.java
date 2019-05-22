package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

/**
 * The activity for the MapView
 */
public class MapsActivity extends MapParentActivity {

    // All spots that will be added upon map refresh
    // The button for redirecting to Add Spot Activity
    private ImageButton plsBtn;

    //The button for opening the filter
    private ImageButton filterBtn;

    // The list of all checkboxes
    private List<Category> checkBoxes = new ArrayList<>();
    private boolean checkBoxOnlyPrivate = false; //local variable for when the checkbox for privacy is checked or not

    private SpotDetailViewAdapter spotDetailViewAdapter;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initPlsBtn();
        initFilterBtn();
        initTmpAccountBtn();

        //Sets the status bar to a white color and the elements in the status bar to a darker color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_maps);
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
        super.onMapReady(googleMap);
        spotDetailViewAdapter = new SpotDetailViewAdapter(this, currentRun.getSpots());
        mMap.setInfoWindowAdapter(spotDetailViewAdapter);

        // Set a listener to make the RelativeLayout filterBoxes Gone when clicking on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ((RelativeLayout) findViewById(R.id.filterBoxes)).setVisibility(View.GONE);
            }
        });
    }

    /**
     * Method to avoid the user returning to the sign in / sign up page from the map view. Instead,
     * the back button closes the application if pressed twice quickly.
     */
    @Override
    public void onBackPressed(){

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else { Toast.makeText(getBaseContext(), "Tap button again to exit application", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    /**
     * Initializes a marker and places it on the map
     *
     * @param spot the spot correlated with the new marker
     */
    @Override
    protected Marker initMarker(Spot spot) {
        if ((filter(spot) && privacyVisible(spot)) &&
                (spot.getPrivacy() || !checkBoxOnlyPrivate)) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                    .title(spot.getName())
                    .icon(getMarkerIcon(spot.getCategory())));

            // Saves the id of the spot in the snippet so that it can be accessed in the next
            // activity
            marker.setSnippet(spot.getId());
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(MapsActivity.this, DetailedViewActivity.class);
                    intent.putExtra("SpotId", spotDetailViewAdapter.getSpot().getId());
                    startActivity(intent);
                }
            });
            return marker;
        }
        return null;
    }


    /**
     * Initializes the plus button to redirect to the AddSpotActivity
     * Redirection requires an internet connection
     */
    protected void initPlsBtn() {
        // Find the plus button
        plsBtn = (ImageButton) findViewById(R.id.plsbtn);
        // Set a listener on the plus button
        plsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if no internet connection, show a message
                if (!isOnline()) {
                    Toast.makeText(getBaseContext(), "You are not connected to internet. " +
                                    "Please check your internet connection and try again.",
                            Toast.LENGTH_LONG).show();
                }
                //If there is an internet connection, redirect to the AddSpotActivity
                else {
                    Intent intent = new Intent(MapsActivity.this, AddSpotActivity.class);
                    intent.putExtra("ViewedLocationLat", mMap.getCameraPosition().target.latitude);
                    intent.putExtra("ViewedLocationLong", mMap.getCameraPosition().target.longitude);
                    intent.putExtra("ViewedLocationZoom", mMap.getCameraPosition().zoom);
                    startActivity(intent);
                }

            }
        });
    }

    protected void initTmpAccountBtn() {
        // Find the plus button
        Button accountbtn = (Button) findViewById(R.id.accountPageBtn);
        // Set a listener on the plus button
        accountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if no internet connection, show a message
                Intent intent = new Intent(MapsActivity.this, AccountPageActivity.class);
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

        createCheckbox(id, counter, null, rel, true);
        createTextView(id, counter, null, rel, true);
        counter++;

        for (final Category category : Category.values()) {
            checkBoxes.add(category);

            createCheckbox(id, counter, category, rel, false);
            createTextView(id, counter, category, rel, false);

            counter++;
        }
        rel.getLayoutParams().height = 50 + 60 * counter;

        // Find the filter button
        filterBtn = (ImageButton) findViewById(R.id.filterbtn);

        // Set a listener to make the RelativeLayout visible
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.filterBoxes).getVisibility() == View.VISIBLE) {
                    ((RelativeLayout) findViewById(R.id.filterBoxes)).setVisibility(View.INVISIBLE);
                    return;
                }
                ((RelativeLayout) findViewById(R.id.filterBoxes)).setVisibility(View.VISIBLE);
            }
        });


    }

    private void createCheckbox(int id, int counter, final Category category, RelativeLayout rel, final boolean isPrivateBox) {
        // Create parameters for the check box
        RelativeLayout.LayoutParams paramsCB = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        CheckBox checkBox = new CheckBox(this);
        if (isPrivateBox) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
        }
        checkBox.setId(id);
        checkBox.setButtonTintList(new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}, // checked
                },
                new int[]{
                        Color.parseColor("#FF6E73"),
                        Color.parseColor("#FF6E73"),
                }
        ));
        paramsCB.setMargins(0, 15 + 60 * counter, 0, 0);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !isPrivateBox) {
                    checkBoxes.add(category);
                    updateMarkers();
                } else if (isChecked && isPrivateBox) {
                    checkBoxOnlyPrivate = true;
                    updateMarkers();
                } else if (isPrivateBox) {
                    checkBoxOnlyPrivate = false;
                    updateMarkers();
                } else {
                    checkBoxes.remove(category);
                    updateMarkers();
                }
            }
        });
        rel.addView(checkBox, paramsCB);
    }

    private void createTextView(int id, int counter, final Category category, RelativeLayout rel, boolean isPrivateBox) {
        // Create parameters for the text view
        RelativeLayout.LayoutParams paramsTxt = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Create a TextView with the specified margins and
        // an ID which should not collide with the checkbox's ID
        TextView txt = new TextView(this);
        txt.setId(id + 1000);
        paramsTxt.setMargins(100, 30 + 60 * (counter), 0, 0);

        if (isPrivateBox) {
            txt.setText("Private");
        } else {
            txt.setText(category.toString());
        }
        rel.addView(txt, paramsTxt);
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

    private boolean privacyVisible(Spot s) {
        if (!s.getPrivacy() || s.getCreatorId().equals(CurrentRun.getActiveUser().getId())) {
            return true;
        } else {
            return false;
        }
    }

}
