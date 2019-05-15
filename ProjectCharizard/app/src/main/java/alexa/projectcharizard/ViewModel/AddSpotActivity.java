package alexa.projectcharizard.ViewModel;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

/**
 * An android activity class for adding a spot through an user interface defined
 * by activity_add.xml. Gets the data from the user interface, verifies that it is not
 * empty, creates a new spot from the data and adds it to the database.
 */
public class AddSpotActivity extends MapsActivity {

    private Marker currentMarker;

    private EditText txtName;
    private EditText txtDescription;

    private ImageView backButton;

    private String currentCategory;

    private Spinner categorySpinner;

    private Switch privateSwitch;

    private Double latitude;
    private Double longitude;

    private ArrayAdapter<String> categoryArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * Called when the map has loaded and is ready for use.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        initLocationOnClickListener();
    }

    /**
     * Saves a new spot to the database
     * starts by checking if fields are empty
     *
     * @param view the view that contains the values for the spot
     */
    public void addNewSpot(View view) {

        if (txtName.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in name", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = txtName.getText().toString();

        if (txtDescription.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in description", Toast.LENGTH_SHORT).show();
            return;
        }
        String description = txtDescription.getText().toString();

        if (currentCategory == null) {
            Toast.makeText(getApplicationContext(), "Select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        Category category = getCategoryEnum(currentCategory);

        Bitmap image = null;

        //Open connection to database and save the spot on the database.
        Database database = Database.getInstance();

        // Saving the current Spot and then adding it to a list of Spots added during current run.
        CurrentRun.getCurrentRunAddedSpots().add(database.saveSpot(name, latitude, longitude, description, category,
                image, CurrentRun.getActiveUser().getId(), privateSwitch.isChecked()));
        finish();
    }

    /**
     * When the back button gets pressed
     * @param view the view from which this function takes you away from
     */
    public void backButtonOnClick(View view) {
        finish();
    }

    /**
     * Sets what category the spot should have.
     *
     * @param currentCategory The category of the spot.
     */
    private Category getCategoryEnum(String currentCategory) {

        if (currentCategory.equals("FRUIT")) {
            return Category.FRUIT;
        } else if (currentCategory.equals("VEGETABLE")) {
            return Category.VEGETABLE;
        } else if (currentCategory.equals("BERRY")) {
            return Category.BERRY;
        } else if (currentCategory.equals("MUSHROOM")) {
            return Category.MUSHROOM;
        } else {
            return Category.OTHER;
        }
    }

    private void initView() {

        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);

        backButton = findViewById(R.id.backArrow);

        // Set default parameters
        currentCategory = "Other";

        // Set checkbox default
        privateSwitch = findViewById(R.id.privateSwitch);
        privateSwitch.setChecked(false);
        privateSwitch.setText(R.string.private_switch_unchecked);

        initSpinner();
    }

    /**
     * Set listeners to spinners
     */
    private void initSpinner() {
        categorySpinner = findViewById(R.id.categorySpinner);

        //Create a list for all categories and add "choose category"
        // as the first item that should be shown in the dropdown
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Choose category");

        //loop through all existing categories and add it to the list.
        for (Category cat : Category.values()) {
            categoryList.add(cat.name());
        }

        //Set the layout for the category spinner.
        categoryArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryArrayAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    currentCategory = selectedCategory;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorySpinner.setSelection(0);
            }
        });

    }


    /**
     * Sets the fields latitude and longitude when clicking on the map to correspond to the latitude and longitude
     */
    private void initLocationOnClickListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;

                // Set out a marker on the spot selected
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_marker)));
            }
        });

        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                // Set out a marker on the spot selected
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_marker)));
            }
        });
    }


    /**
     * Removes functionality of overridden parent class
     */
    @Override
    protected void initPlsBtn() {
    }

    /**
     * Removes functionality of overridden parent class
     */
    @Override
    protected void initFilterBtn() {
    }

    @Override
    protected float initZoom() {
        return getIntent().getFloatExtra("ViewedLocationZoom", 15.0f);
    }

    @Override
    protected LatLng initLoc() {
        return new LatLng(getIntent().getDoubleExtra("ViewedLocationLat", 57),
                getIntent().getDoubleExtra("ViewedLocationLong", 12));
    }


    @Override
    protected void contentView() {
        setContentView(R.layout.activity_add);
    }

    /**
     * Method to make back button behave as it normally would
     */
    /*@Override
    public void onBackPressed() {

    }*/ //TODO This method may need to be changed for parent method to behave correctly


}
