package alexa.projectcharizard.ViewModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

public class EditSpotActivity extends MapsActivity {

    private Marker currentMarker;

    private TextView editSpotNameView;
    private TextView editSpotLatView;
    private TextView editSpotLongView;

    private EditText editSpotDescText;

    private Spinner editSpotCatSpinner;

    private Switch editSpotVisSwitch;

    private Button editSpotSaveButton;

    private String currentCategory;
    private String spotId;

    private boolean spotVis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initSpinner();
        initSwitch();
        initId();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        initLocationOnClickListener();
    }

    @Override
    protected void initPlsBtn() {
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
        setContentView(R.layout.activity_edit_spot);
    }


    private void initView() {
        editSpotNameView = findViewById(R.id.editSpotNameView);
        editSpotLatView = findViewById(R.id.editSpotLatView);
        editSpotLongView = findViewById(R.id.editSpotLongView);
        editSpotDescText = findViewById(R.id.editSpotDescText);
        editSpotCatSpinner = findViewById(R.id.editSpotCatSpinner);
        editSpotVisSwitch = findViewById(R.id.editSpotVisSwitch);
        editSpotSaveButton = findViewById(R.id.editSpotSaveButton);

        setInitText();
    }

    private void initSpinner() {
        editSpotCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    currentCategory = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editSpotCatSpinner.setSelection(0);
            }
        });
        editSpotCatSpinner.setSelection(0);
    }

    private void initSwitch() {
        editSpotVisSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spotVis = isChecked;
            }
        });
        editSpotVisSwitch.setChecked(getIntent().getBooleanExtra("SpotVisibility", false));
    }

    private void initId() {
        spotId = getIntent().getStringExtra("SpotId");
    }

    private void setInitText() {
        Intent intent = getIntent();

        editSpotNameView.setText(intent.getStringExtra("SpotName"));
        editSpotLatView.setText(Double.toString(intent.getDoubleExtra("SpotLatitude", 57)));
        editSpotLongView.setText(Double.toString(intent.getDoubleExtra("SpotLongitude", 12)));
        editSpotDescText.setText(intent.getStringExtra("spotDescription"));
    }

    private Category getCategoryEnum(String currentCategory) {
        if (currentCategory.equals("Apple Tree")) {
            return Category.APPLE_TREE;
        } else {
            return Category.OTHER;
        }
    }

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

    public void changeSpotInfoAction(View view) {
        DatabaseReference dataRef = Database.getInstance().getDatabaseReference().child(spotId);
        Category spotCategory = getCategoryEnum(this.currentCategory);
        try {
            dataRef.child("name").setValue(editSpotNameView.getText().toString());
            dataRef.child("latitude").setValue(Double.parseDouble(editSpotLatView.getText().toString()));
            dataRef.child("longitude").setValue(Double.parseDouble(editSpotLongView.getText().toString()));
            dataRef.child("description").setValue(editSpotDescText.getText().toString());
            dataRef.child("category").setValue(spotCategory);
            dataRef.child("visibility").setValue(spotVis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
