package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import alexa.projectcharizard.Model.EasySpot;
import alexa.projectcharizard.R;

public class DataRetrieved extends AppCompatActivity {


    private ListView listView;
    DatabaseReference databaseReference;
    List<EasySpot> spotList;

    String categoryRecord ="";
    Spinner spinner1;
    String spinnerlist[] = {"Choose category", "Äppelträd", "Päronträd", "Körsbärsträd", "Annat"};
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_retrieved);
       // prepareSpinner();

        listView = findViewById(R.id.list_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("Spots");
        spotList = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        super.onStart();
      //  Query query = databaseReference.orderByChild("category").equalTo(categoryRecord);
      //  query.addValueEventListener(new ValueEventListener() {
        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //    spotList.clear();

                for(DataSnapshot spotSnapshot : dataSnapshot.getChildren()){
                    EasySpot spot = spotSnapshot.getValue(EasySpot.class);
                    spotList.add(spot);
                }
                SpotInfoAdapter spotInfoAdapter = new SpotInfoAdapter(DataRetrieved.this, spotList);
                listView.setAdapter(spotInfoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        prepareSpinner();
    }

    private void prepareSpinner(){
        spinner1 = (Spinner) findViewById(R.id.categorySpinner);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, spinnerlist);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    showAllSpots();
                }
                else {

                    switch (position) {
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
                            categoryRecord = "Default";
                            break;
                    }
                    showCategorisedSpots();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showAllSpots(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    spotList.clear();

                for(DataSnapshot spotSnapshot : dataSnapshot.getChildren()){
                    EasySpot spot = spotSnapshot.getValue(EasySpot.class);
                    spotList.add(spot);
                }
                SpotInfoAdapter spotInfoAdapter = new SpotInfoAdapter(DataRetrieved.this, spotList);
                listView.setAdapter(spotInfoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showCategorisedSpots(){
        Query query = databaseReference.orderByChild("category").equalTo(categoryRecord);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                spotList.clear();

                for(DataSnapshot spotSnapshot : dataSnapshot.getChildren()){
                    EasySpot spot = spotSnapshot.getValue(EasySpot.class);
                    spotList.add(spot);
                }
                SpotInfoAdapter spotInfoAdapter = new SpotInfoAdapter(DataRetrieved.this, spotList);
                listView.setAdapter(spotInfoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
