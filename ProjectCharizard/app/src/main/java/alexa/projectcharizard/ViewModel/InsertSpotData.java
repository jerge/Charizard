package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alexa.projectcharizard.Model.EasySpot;
import alexa.projectcharizard.R;

public class InsertSpotData extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText spotName, spotLatitud, spotLongitud, spotDescription, spotCategory, spotVisibility;
    Button addSpotBtn;
    DatabaseReference dbReference;
    EasySpot spot;

    String record ="";
    Spinner spinner1;
    String spinnerlist[] = {"Okategoriserat", "Äppelträd", "Päronträd", "Körsbärsträd", "Annat"};
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_spot_data);
        prepareSpinner();
        layoutSetup();

        dbReference = FirebaseDatabase.getInstance().getReference().child("Spots");
        addSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpot();
                Toast.makeText(InsertSpotData.this, "data inserted successfully",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void prepareSpinner(){
        spinner1 = (Spinner) findViewById(R.id.spinner);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, spinnerlist);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        record = spinnerlist[0];
                        break;
                    case 1:
                        record = spinnerlist[1];
                        break;
                    case 2:
                        record = spinnerlist[2];
                        break;
                    case 3:
                        record = spinnerlist[3];
                        break;
                    case 4:
                        record = spinnerlist[4];
                        break;
                    default:
                        record ="Default";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void layoutSetup(){
        spotName = (EditText) findViewById(R.id.spotName);
        spotLatitud = (EditText) findViewById(R.id.spotLatitud);
        spotLongitud = (EditText) findViewById(R.id.spotLongitud);
        spotDescription = (EditText) findViewById(R.id.spotDescription);
        //  spotCategory = (EditText) findViewById(R.id.spotCategory);
        spotVisibility = (EditText) findViewById(R.id.spotVisibility);
        addSpotBtn = (Button) findViewById(R.id.add_spot_button);
    }

    private void addSpot(){
        String name = spotName.getText().toString().trim();
        String lat = spotLatitud.getText().toString().trim();
        String lon = spotLongitud.getText().toString().trim();
        String desc =spotDescription.getText().toString().trim();
        String vis =spotVisibility.getText().toString().trim();

        spot = new EasySpot(name, lat, lon, desc, record, vis);

        dbReference.push().setValue(spot);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
