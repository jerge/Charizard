package alexa.projectcharizard.ViewModel;

import android.graphics.Color;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

public class DetailedViewActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        //Sets the status bar to a white color and the elements in the status bar to a darker color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

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

        intent.putExtra("SpotName", getIntent().getStringExtra("SpotName"));
        intent.putExtra("SpotId", getIntent().getStringExtra("SpotId"));
        intent.putExtra("SpotLatitude", getIntent().getDoubleExtra("SpotLatitude", 57.0));
        intent.putExtra("SpotLongitude", getIntent().getDoubleExtra("SpotLongitude", 12.0));
        intent.putExtra("SpotDescription", getIntent().getStringExtra("SpotDescription"));
        intent.putExtra("SpotCategory", getIntent().getStringExtra("SpotCategory"));
        intent.putExtra("SpotPrivacy", getIntent().getStringExtra("SpotPrivacy"));

        startActivity(intent);
    }
}