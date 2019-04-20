package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import alexa.projectcharizard.Model.EasySpot;
import alexa.projectcharizard.R;

public class DataRetrieved extends AppCompatActivity {


    private ListView listView;
    DatabaseReference databaseReference;
    List<EasySpot> spotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_retrieved);

        listView = findViewById(R.id.list_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("Spots");
        spotList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
