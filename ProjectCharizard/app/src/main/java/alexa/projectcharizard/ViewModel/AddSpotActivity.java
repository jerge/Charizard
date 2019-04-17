package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import alexa.projectcharizard.R;

public class AddSpotActivity extends MapsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void contentView() {
        setContentView(R.layout.activity_add);
    }

    @Override
    protected float initZoom(){
        return 17.0f;
    }
}
