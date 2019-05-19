package alexa.projectcharizard.ViewModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

/**
 * An activity class that implements functionality for changing the information about a spot.
 * Gets information from the GUI and changes the interface to display preliminary changes. Sends
 * the changed information to the database in order to update the spot.
 *
 * @Author Stefan Chan
 */

public class EditSpotActivity extends MapsActivity {

    private Marker currentMarker;

    private TextView editSpotNameView;
    private TextView editSpotLatView;
    private TextView editSpotLongView;

    private EditText editSpotDescText;

    private Spinner editSpotCatSpinner;

    private Switch editSpotPrivacySwitch;

    private String currentCategory;

    private boolean spotPrivacy;

    // Override super class methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initSpinner();
        initSwitch();
    }

    /**
     * Called when the map is ready for use
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        initLocationOnClickListener();
    }

    @Override
    protected void initPlsBtn() {
    }

    @Override
    protected void initFilterBtn() {

    }

    @Override
    protected float initZoom() {
        return getIntent().getFloatExtra("ViewedLocationZoom", 15.0f);
    }

    @Override
    protected LatLng initLoc() {
        return new LatLng(getIntent().getDoubleExtra("SpotLatitude", 57),
                getIntent().getDoubleExtra("SpotLongitude", 12));
    }

    @Override
    protected void contentView() {
        setContentView(R.layout.activity_edit_spot);
    }

    /**
     * Initialises text components
     */
    private void initView() {
        editSpotNameView = findViewById(R.id.editSpotNameView);
        editSpotLatView = findViewById(R.id.editSpotLatView);
        editSpotLongView = findViewById(R.id.editSpotLongView);
        editSpotDescText = findViewById(R.id.editSpotDescText);
        editSpotCatSpinner = findViewById(R.id.editSpotCatSpinner);
        editSpotPrivacySwitch = findViewById(R.id.editSpotPrivacySwitch);
        setInitText();
    }

    /**
     * Sets listener and populates the spinner with all spot categories, sets the current spot
     * category as the initial selected option
     */
    private void initSpinner() {
        editSpotCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editSpotCatSpinner.setSelection(0);
            }
        });

        List<String> categoryElements = new ArrayList<String>();
        for (Category c : Category.values()) {
            categoryElements.add(c.toString());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryElements);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editSpotCatSpinner.setAdapter(categoryAdapter);

        String spotCategory = (getIntent().getStringExtra("SpotCategory"));
        editSpotCatSpinner.setSelection(categoryAdapter.getPosition(spotCategory));
    }

    /**
     * Sets the switch depending on the visibility of the spot
     */
    private void initSwitch() {
        editSpotPrivacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (editSpotPrivacySwitch.isChecked()) {
                    spotPrivacy = true;
                } else {
                    spotPrivacy = false;
                }
            }
        });
        editSpotPrivacySwitch.setChecked(getIntent().getBooleanExtra("SpotVisibility", false));
    }

    /**
     * Sets the textviews with appropriate spot information
     */
    private void setInitText() {
        Intent intent = getIntent();

        editSpotNameView.setText(intent.getStringExtra("SpotName"));
        editSpotLatView.setText(Double.toString(intent.getDoubleExtra("SpotLatitude", 57)));
        editSpotLongView.setText(Double.toString(intent.getDoubleExtra("SpotLongitude", 12)));
        editSpotDescText.setText(intent.getStringExtra("SpotDescription"));
    }

    /**
     * Converts the string to a Category enum equivalent, giving "OTHER" back if no equivalent
     * exist
     *
     * @param currentCategory the string to be converted
     * @return the enum, or OTHER if it no equivalent exists
     */
    private Category getCategoryEnum(String currentCategory) {
        try {
            return Category.valueOf(currentCategory);
        } catch (IllegalArgumentException e) {
            return Category.OTHER;
        }
    }


    /**
     * Sets the textviews when clicking on the map to correspond with the latitude and longitude
     */
    private void initLocationOnClickListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                editSpotLatView.setText(Double.toString(latLng.latitude));
                editSpotLongView.setText(Double.toString(latLng.longitude));
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
                editSpotLatView.setText(Double.toString(location.getLatitude()));
                editSpotLongView.setText(Double.toString(location.getLongitude()));
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
     * Spawns a dialog in which the user can type in the new spot name, changes the textview to
     * display the new name
     *
     * @param view the view to show the dialog in
     */
    public void showChangeSpotNameDialog(View view) {
        final EditText newSpotNameField = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Change spot name")
                .setMessage("Type in new spot name")
                .setView(newSpotNameField)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editSpotNameView.setText(newSpotNameField.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /**
     * Calls the database to change update the spot with new information from the textviews.
     * Triggers when pressing "Save" on the graphical interface. Sends feedback if the update
     * to the database is successful or fails
     *
     * @param view the view which this action takes place in
     */
    public void changeSpotInfoAction(View view) {
        DatabaseReference dataRef = Database.getInstance().getDatabaseReference().child("Spots")
                                    .child(getIntent().getStringExtra("SpotId"));
        Category spotCategory = getCategoryEnum(this.currentCategory);
        try {
            dataRef.child("name").setValue(editSpotNameView.getText().toString());
            dataRef.child("latitude").setValue(Double.parseDouble(editSpotLatView.getText().toString()));
            dataRef.child("longitude").setValue(Double.parseDouble(editSpotLongView.getText().toString()));
            dataRef.child("description").setValue(editSpotDescText.getText().toString());
            dataRef.child("category").setValue(spotCategory);
            dataRef.child("privacy").setValue(spotPrivacy);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Details changed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditSpotActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    /**
     * Notifies the user via Toast to use the map fragment to change latitude or longitude values
     *
     * @param view the view which this action takes place in
     */
    public void notifyUserToUseMap(View view) {
        Toast.makeText(this, "Please use the map below to change latitude/longitude",
                        Toast.LENGTH_SHORT).show();
    }

}
