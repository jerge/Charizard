package alexa.projectcharizard.ViewModel;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

/**
 * An android activity class for adding a spot through an user interface defined
 * by activity_add.xml. Gets the data from the user interface, verifies that it is not
 * empty, creates a new spot from the data and adds it to the database.
 */
public class AddSpotActivity extends MapsActivity {

    private CheckBox visibilityCheckbox;

    private TextView txtLat;
    private TextView txtLong;
    private EditText txtName;
    private EditText txtDescription;

    private String currentCategory;
    private String currentProperty;

    private Spinner categorySpinner;
    private Spinner propertySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtLat = findViewById(R.id.txtLat);
        txtLong = findViewById(R.id.txtLong);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);

        // Set default parameters
        currentCategory = "Other";
        currentProperty = "Public";

        // Set checkbox default
        visibilityCheckbox = findViewById(R.id.visibilityCheckbox);
        if (visibilityCheckbox.isChecked()) {
            visibilityCheckbox.setChecked(false);
        }

        // Set listeners to spinners
        categorySpinner = findViewById(R.id.categorySpinner);
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

        propertySpinner = findViewById(R.id.propertySpinner);
        propertySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProperty = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    currentProperty = selectedProperty;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                propertySpinner.setSelection(0);
            }
        });

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
     * Sets the TextViews when clicking on the map to correspond to the latitude and longitude
      */
    private void initLocationOnClickListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                txtLat.setText(Double.toString(latLng.latitude));
                txtLong.setText(Double.toString(latLng.longitude));
            }
        });
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                txtLat.setText(Double.toString(location.getLatitude()));
                txtLong.setText(Double.toString(location.getLongitude()));
            }
        });
    }


    // Do not create plsBtn
    @Override
    protected void initPlsBtn() {}

    @Override
    protected void contentView() {
        setContentView(R.layout.activity_add);
    }

    @Override
    protected float initZoom(){
        return 17.0f;
    }

    /**
     * Saves a new spot to the database
     * starts by checking if fields are empty
     * @param view the view that contains the values for the spot
     */
    public void addNewSpot(View view) {
        if (txtLat.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in latitude", Toast.LENGTH_SHORT).show();
            return;
        }
        Double lat = Double.valueOf(txtLat.getText().toString());
        if (txtLong.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in longitude", Toast.LENGTH_SHORT).show();
            return;
        }
        Double lng = Double.valueOf(txtLong.getText().toString());
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
        boolean visibility = visibilityCheckbox.isChecked();

        Database database = Database.getInstance();
        database.saveSpot(name, lat, lng, description, visibility);
        finish();
    }


    private Category getCategoryEnum(String currentCategory) {
        if (currentCategory.equals("Apple Tree")) {
            return Category.APPLE_TREE;
        } else {
            return Category.OTHER;
        }
    }
}
