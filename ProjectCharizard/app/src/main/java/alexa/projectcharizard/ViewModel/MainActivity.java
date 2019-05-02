package alexa.projectcharizard.ViewModel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

public class MainActivity extends AppCompatActivity {

    Database database = Database.getInstance();
    EditText name, latitiude, longitude, description;
    Switch visibility;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double dblLat = Double.parseDouble(latitiude.getText().toString());
                Double dblLng = Double.parseDouble(longitude.getText().toString());

                database.saveSpot(name.getText().toString(), dblLat, dblLng, description.getText().toString(), visibility.isChecked());
            }
        });
    }

    private void initView() {
        name = (EditText) findViewById(R.id.txtName);
        latitiude = (EditText) findViewById(R.id.txtLat);
        longitude = (EditText) findViewById(R.id.txtLong);
        description = (EditText) findViewById(R.id.txtDescription);
        visibility = (Switch) findViewById(R.id.switchVisibility);
        save = (Button) findViewById(R.id.btnSave);
    }
}
