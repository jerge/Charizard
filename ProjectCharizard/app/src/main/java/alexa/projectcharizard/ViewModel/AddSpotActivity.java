package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import alexa.projectcharizard.R;

public class AddSpotActivity extends MapsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initLocationOnClickListener() {
        System.out.println(mMap == null);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        initLocationOnClickListener();
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
}
