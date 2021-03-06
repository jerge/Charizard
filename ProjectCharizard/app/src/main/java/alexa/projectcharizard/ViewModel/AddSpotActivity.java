package alexa.projectcharizard.ViewModel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
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
public class AddSpotActivity extends MapParentActivity {

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;
    private final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 42;

    //a Uri object to store file path
    private Uri filePath;

    private Marker currentMarker;

    private EditText txtName;
    private EditText txtDescription;
    private Button btnImage;
    private ImageView addedImage;

    private TextView privateSwitchText;

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

        //Sets the status bar to a white color and the elements in the status bar to a darker color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_add);
    }

    /**
     * Called when the map has loaded and is ready for use.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        initZoom = getIntent().getFloatExtra("ViewedLocationZoom", 12.0f);
        initLoc = new LatLng(getIntent().getDoubleExtra("ViewedLocationLat", 57),
                getIntent().getDoubleExtra("ViewedLocationLong", 12));
        super.onMapReady(googleMap);
        initLocationOnClickListener();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
    }

    /**
     * Couples the GUI with functionality as well as preparing the elements
     */
    private void initView() {
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        btnImage = findViewById(R.id.addImage);
        addedImage = findViewById(R.id.addedImage);

        // Set default parameters
        currentCategory = "Other";

        initSwitch();
        initSpinner();
        initBtnImage();
    }

    /**
     * Sets defaults and connects listener to switch
     */
    private void initSwitch() {
        // Set switch default
        privateSwitch = findViewById(R.id.privateSwitch);
        privateSwitchText = findViewById(R.id.privateSwitchText);
        privateSwitch.setChecked(false);
        privateSwitchText.setText(R.string.private_switch_unchecked);

        privateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    privateSwitchText.setText(R.string.private_switch_checked);
                } else {
                    privateSwitchText.setText(R.string.private_switch_unchecked);
                }
            }
        });
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
            categoryList.add(cat.toString());
        }

        //Set the layout for the category spinner.
        categoryArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryList);
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

    private void initBtnImage() {
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForExternalStoragePermission();
            }
        });
    }
    
    public void addNewSpot(View view) {

        // if no internet connection, show a message
        if (!isOnline()) {
            Toast.makeText(getBaseContext(), "You are not connected to internet. " +
                            "Please check your internet connection and try again.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // If there is an internet connection, check if fields are empty. Then save spot.
        if (latitude == null || longitude == null) {    //creates a toast if no spot location has been chosen
            Toast.makeText(getApplicationContext(), "Choose the location of your spot", Toast.LENGTH_SHORT).show();
            return;
        }

        if (txtName.getText().toString().isEmpty()) {   //creates a toast if no name has been chosen for the spot
            Toast.makeText(getApplicationContext(), "Fill in name", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = txtName.getText().toString();

        if (currentCategory == null) {  //creates a toast if no category has been chosen for the spot
            Toast.makeText(getApplicationContext(), "Select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        Category category = getCategoryEnum(currentCategory);

        String description = txtDescription.getText().toString();

        //Open connection to database and save the spot on the database.
        Database database = Database.getInstance();

        if (filePath != null) {
            // Call the upload file, which finishes the tasks upon completion of upload
            uploadFile(name, latitude, longitude, description, category, privateSwitch.isChecked(), CurrentRun.getActiveUser().getId());
            return;
        }

        // Saving the current Spot and then adding it to a list of Spots added during current run.
        CurrentRun.getCurrentRunAddedSpots().add(database.saveSpot(name, latitude, longitude, description, category, privateSwitch.isChecked(), CurrentRun.getActiveUser().getId()));
        finish();
    }

    /**
     * When the back button gets pressed
     *
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
        for (Category cat : Category.values()) {
            if (currentCategory.equals(cat.toString()))
                return cat;
        }
        return Category.OTHER;
    }


    /**
     * Uploads a file to the firebase storage.
     * It shows a progress bar until done and then finishes the activity
     *
     * @param name
     * @param lat
     * @param lng
     * @param description
     * @param category
     * @param visibility
     * @param userId
     */
    private void uploadFile(final String name, final double lat, final double lng, final String description, final Category category, final Boolean visibility, final String userId) {
        // If the user has selected a file
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            // Gets the ID for the spot that is about to be created
            final String id = Database.getInstance().getDatabaseReference().child("Spots").push().getKey();
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

                            // Open connection to database and save the spot on the database.
                            Database database = Database.getInstance();
                            // Saving the current Spot and then adding it to a list of Spots added during current run.
                            CurrentRun.getCurrentRunAddedSpots().add(database.saveSpot(id, name, lat, lng, description, category, visibility, userId));
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
                            // Finishes the activity
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
     * Checks if the user has granted access to read external storage and starts image selector
     * upon granting access
     */
    private void askForExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // If not request permission to access fine location
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            selectImage();
        }
    }

    /**
     * Starts the image selector
     */
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
                addedImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        selectImage();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }


}
