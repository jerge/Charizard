package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import alexa.projectcharizard.R;

public class DetailedViewActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);


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
        intent.putExtra("SpotVisibility", getIntent().getStringExtra("SpotVisibility"));
        intent.putExtra("SpotImage", getIntent().getStringExtra("SpotImage"));

        startActivity(intent);
    }
}