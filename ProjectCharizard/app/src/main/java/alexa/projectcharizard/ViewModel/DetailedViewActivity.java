package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

public class DetailedViewActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton editBtn;

    private Spot currentSpot;

    private CurrentRun currentRun = CurrentRun.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        currentSpot = getSpot(getIntent().getStringExtra("SpotId"));

        initView();
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        editBtn = findViewById(R.id.editButton);


        importPicture();

        tabLayout.addTab(tabLayout.newTab().setText("About"));
        tabLayout.addTab(tabLayout.newTab().setText("Comments"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final DetailedViewAdapter adapter = new DetailedViewAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Checks if the spot is owned by the current user, if it is the remove spot button appears and becomes activated
        // Otherwise it will not appear nor be activated
        if (currentSpot.getCreatorId().equals(CurrentRun.getActiveUser().getId())) {
            editBtn.show();
        } else {
            editBtn.hide();
        }
    }

    private void importPicture() {
        final ImageView iv = findViewById(R.id.htab_header);

        // Gets the location in Storage of the picture
        StorageReference imageReference = Database.getInstance().getStorageReference()
                .child("images/" + getIntent().getStringExtra("SpotId"));
        // Tries to download from the url
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(iv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Will throw error, but is fine
            }
        });
    }

    /**
     * Starts EditSpotActivity by getting spot information passed by intent created by MapsActivity
     *
     * @param view the view which this method is called in
     */
    public void openEditSpotActivity(View view) {
        Intent intent = new Intent(DetailedViewActivity.this, EditSpotActivity.class);
        intent.putExtra("SpotId", getIntent().getStringExtra("SpotId"));
        startActivity(intent);
    }

    /**
     * A method that finds Spot in use based on the ID sent from previous activity
     *
     * @param spotId The Spot ID of the Spot to be found
     * @return The Spot found, null if no such Spot is found
     */
    private Spot getSpot(String spotId) {
        for (Spot spot : currentRun.getSpots()) {
            if (spot.getId().equals(spotId)) {
                return spot;
            }
        }
        return null;
    }
}