package alexa.projectcharizard.ViewModel;

import android.content.Intent;
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


// Inspiration from https://www.youtube.com/watch?v=xNWcnfQggCk
public class InsertSpotData extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText spotName, spotLatitude, spotLongitude, spotDescription, spotCategory, spotVisibility;
    Button addSpotBtn, allSpotsButton;
    DatabaseReference dbReference;
    EasySpot spot;
    String categoryRecord ="";
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

        allSpotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(InsertSpotData.this, DataRetrieved.class);
                startActivity(next);
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
                        categoryRecord = spinnerlist[0];
                        break;
                    case 1:
                        categoryRecord = spinnerlist[1];
                        break;
                    case 2:
                        categoryRecord = spinnerlist[2];
                        break;
                    case 3:
                        categoryRecord = spinnerlist[3];
                        break;
                    case 4:
                        categoryRecord = spinnerlist[4];
                        break;
                    default:
                        categoryRecord ="Default";
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
        spotLatitude = (EditText) findViewById(R.id.spotLatitude);
        spotLongitude = (EditText) findViewById(R.id.spotLongitude);
        spotDescription = (EditText) findViewById(R.id.spotDescription);
        //  spotCategory = (EditText) findViewById(R.id.spotCategory);
        spotVisibility = (EditText) findViewById(R.id.spotVisibility);
        addSpotBtn = (Button) findViewById(R.id.add_spot_button);
        allSpotsButton = (Button) findViewById(R.id.all_spots_button);
    }

    private void addSpot(){
        String name = spotName.getText().toString().trim();
        String lat = spotLatitude.getText().toString().trim();
        String lon = spotLongitude.getText().toString().trim();
        String desc =spotDescription.getText().toString().trim();
        String vis =spotVisibility.getText().toString().trim();

        String id = dbReference.push().getKey();

        spot = new EasySpot(id, name, lat, lon, desc, categoryRecord, vis);

        dbReference.child(id).setValue(spot);

        spotName.setText("");
        spotLatitude.setText("");
        spotLongitude.setText("");
        spotDescription.setText("");
        spotVisibility.setText("");


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
