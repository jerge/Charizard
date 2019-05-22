package alexa.projectcharizard.ViewModel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alexa.projectcharizard.Model.Category;
import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

/**
 * An activity class that implements functionality for changing the information about a spot.
 * Gets information from the GUI and changes the interface to display preliminary changes. Sends
 * the changed information to the database in order to update the spot.
 *
 * @Author Stefan Chan
 */

public class EditSpotActivity extends MapsActivity {

    // a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    private final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 42;
    // a Uri object to store file path
    private Uri filePath;

    private Marker currentMarker;

    private TextView editSpotNameView;
    private TextView editSpotLatView;
    private TextView editSpotLongView;

    private EditText editSpotDescText;
    private Spinner editSpotCatSpinner;

    private Switch editSpotPrivacySwitch;
    private ImageButton currentImage;

    private String currentCategory;

    private boolean spotPrivacy;

    private Spot currentSpot;

    private CurrentRun currentRun = CurrentRun.getInstance();

    // Override super class methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentSpot = getSpot(getIntent().getStringExtra("SpotId"));

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
        initSpotLocationOnMap();
    }

    @Override
    protected void initPlsBtn() {
    }

    @Override
    protected void initFilterBtn() {

    }

    /**
     * Removes functionality of overridden parent class
     */
    @Override
    protected void initTmpAccountBtn() {
    }

    @Override
    protected float initZoom() {
        return getIntent().getFloatExtra("ViewedLocationZoom", 12.0f);
    }

    @Override
    protected LatLng initLoc() {
        return new LatLng(currentSpot.getLatitude(),
                currentSpot.getLongitude());
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
        currentImage = findViewById(R.id.addedImage);
        setInitText();
        initImageButton();
        importPicture();
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

        editSpotCatSpinner.setSelection(categoryAdapter.getPosition(currentSpot.getCategory().toString()));
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
        editSpotPrivacySwitch.setChecked(currentSpot.getPrivacy());
    }

    /**
     * Sets the textviews with appropriate spot information
     */
    private void setInitText() {
        Intent intent = getIntent();

        editSpotNameView.setText(currentSpot.getName());
        editSpotLatView.setText(Double.toString(currentSpot.getLatitude()));
        editSpotLongView.setText(Double.toString(currentSpot.getLongitude()));
        editSpotDescText.setText(currentSpot.getDescription());
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
     * Sets a initial marker on map
     */
    private void initSpotLocationOnMap() {
        if (currentMarker != null) {
            currentMarker.remove();
        }

        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(initLoc())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_marker)));
    }

    /**
     * Calls the database to change update the spot with new information from the textviews.
     * Triggers when pressing "Save" on the graphical interface. Sends feedback if the update
     * to the database is successful or fails
     *
     * @param view the view which this action takes place in
     */
    public void changeSpotInfoAction(View view) {
        String id = currentSpot.getId();
        DatabaseReference dataRef = Database.getInstance().getDatabaseReference().child("Spots")
                .child(id);
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

        // Uploads a file before switching activity if a file has been changed, otherwise just start new activity
        Intent intent = new Intent(EditSpotActivity.this, MapsActivity.class);
        if (filePath != null) {
            uploadFile(intent, id);
        } else {
            startActivity(intent);
            // Ends activity after saving
            finish();
        }
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

    /**
     * Makes the button for selecting image clickable
     */
    private void initImageButton() {
        currentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForExternalStoragePermission();
            }
        });
    }

    private void importPicture() {
        // Gets the location in Storage of the picture
        StorageReference imageReference = Database.getInstance().getStorageReference()
                .child("images/" + getIntent().getStringExtra("SpotId"));
        // Tries to download from the url
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(currentImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Will throw error, but is fine
            }
        });
    }

    /**
     * Uploads and replaces a file in the firebase storage.
     * It shows a progress bar until done and then finishes the activity
     */
    private void uploadFile(final Intent intent, String id) {
        // If the user has selected a file
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            // Names the image the same as the spot ID and puts it in folder /images/
            StorageReference imageRef = Database.getInstance().getStorageReference().child("images/" + id);
            imageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successful
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            // and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            // Starts next activity
                            startActivity(intent);
                            // Ends activity after saving
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // if the upload is not successful
                            // hiding the progress dialog
                            progressDialog.dismiss();

                            // and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage() + " . Spot not saved, please try again", Toast.LENGTH_LONG).show();

                            // Starts next activity
                            startActivity(intent);
                            // Ends activity after saving
                            finish();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
    }


    /**
     * Upon choosing a file, this method is called to update the filePath.
     * Also sets the picture of addedImage to the choosen file.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                currentImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
