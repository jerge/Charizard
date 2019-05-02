package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

public class AddSpotActivity extends MapsActivity {

    private CheckBox visibilityCheckbox;

    private EditText txtLat;
    private EditText txtLong;
    private EditText txtName;
    private EditText txtDescription;

    private String currentCategory;
    private String currentProperty;

    private Spinner categorySpinner;
    private Spinner propertySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //contentView();

        /*mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        txtLat = findViewById(R.id.txtLat);
        txtLong = findViewById(R.id.txtLong);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);

        currentCategory = "Other";
        currentProperty = "Public";

        visibilityCheckbox = findViewById(R.id.visibilityCheckbox);
        if (visibilityCheckbox.isChecked()) {
            visibilityCheckbox.setChecked(false);
        }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        initLocationOnClickListener();
    }

    private void initLocationOnClickListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                EditText lat = (EditText) findViewById(R.id.txtLat);
                lat.setText(Double.toString(latLng.latitude));
                EditText lng = (EditText) findViewById(R.id.txtLong);
                lng.setText(Double.toString(latLng.longitude));
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
        //startActivity(new Intent(AddSpotActivity.this, MapsActivity.class));
        // Send the return to user model
    }


    private Category getCategoryEnum(String currentCategory) {
        if (currentCategory.equals("Apple Tree")) {
            return Category.APPLE_TREE;
        } else {
            return Category.OTHER;
        }
    }
}
