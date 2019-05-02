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
