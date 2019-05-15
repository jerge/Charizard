package alexa.projectcharizard.ViewModel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
public class AddSpotActivity extends MapsActivity {

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;
    //a Uri object to store file path
    private Uri filePath;

    private Marker currentMarker;

    private CheckBox visibilityCheckbox;

    private TextView txtLat;
    private TextView txtLong;
    private EditText txtName;
    private EditText txtDescription;
    private Button btnImage;
    private ImageView addedImage;

    private String currentCategory;
    private String currentProperty;

    private Spinner categorySpinner;
    private Spinner propertySpinner;

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

        if (filePath != null) {
            // Call the upload file, which finishes the tasks upon completion of upload
            uploadFile(name, lat, lng, description, category, visibility, CurrentRun.getActiveUser().getId());
            return;
        }



        Bitmap image = null;

        //Open connection to database and save the spot on the database.
        Database database = Database.getInstance();

        // Saving the current Spot and then adding it to a list of Spots added during current run.
        CurrentRun.getCurrentRunAddedSpots().add(database.saveSpot(name, lat, lng, description, category, image, visibility, CurrentRun.getActiveUser().getId()));
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
        txtLat = findViewById(R.id.txtLat);
        txtLong = findViewById(R.id.txtLong);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        btnImage = findViewById(R.id.addImage);
        addedImage = findViewById(R.id.addedImage);

        // Set default parameters
        currentCategory = "Other";
        currentProperty = "Public";

        // Set checkbox default
        visibilityCheckbox = findViewById(R.id.visibilityCheckbox);
        if (visibilityCheckbox.isChecked()) {
            visibilityCheckbox.setChecked(false);
        }
        initSpinner();
        initBtnImage();
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
     * Sets the TextViews when clicking on the map to correspond to the latitude and longitude
     */
    private void initLocationOnClickListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                txtLat.setText(Double.toString(latLng.latitude));
                txtLong.setText(Double.toString(latLng.longitude));
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
                txtLat.setText(Double.toString(location.getLatitude()));
                txtLong.setText(Double.toString(location.getLongitude()));
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

    private void initBtnImage() {
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    //handling the image chooser activity result
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

    private void uploadFile(final String name, final double lat, final double lng, final String description, final Category category, final Boolean visibility, String userId) {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            //String id = databaseReference.child("Spots").push().getKey();

            StorageReference riversRef = Database.getInstance().getStorageReference().child("images/pic.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            //Open connection to database and save the spot on the database.
                            Database database = Database.getInstance();

                            // Saving the current Spot and then adding it to a list of Spots added during current run.
                            CurrentRun.getCurrentRunAddedSpots().add(database.saveSpot(name, lat, lng, description, category, null, visibility, CurrentRun.getActiveUser().getId()));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

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
        //if there is not any file
        else {
            //you can display an error toast
        }
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
