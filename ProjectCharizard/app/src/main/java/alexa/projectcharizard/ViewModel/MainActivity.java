package alexa.projectcharizard.ViewModel;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alexa.projectcharizard.Model.MyRecyclerViewHolder;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

public class MainActivity extends AppCompatActivity {

    EditText name, latitiude, longitude, description;
    Switch visibility;
    Button save;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Spot> options;
    FirebaseRecyclerAdapter<Spot, MyRecyclerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CHARIZARD_DATABASE");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSpot();
            }
        });

        showSpot();
    }



    private void saveSpot(){
        Double dblLat = Double.parseDouble(latitiude.getText().toString());
        Double dblLng = Double.parseDouble(longitude.getText().toString());
        Spot spot = new Spot(name.getText().toString(), dblLat, dblLng, description.getText().toString(), visibility.getShowText());

        databaseReference.push().setValue(spot);

        adapter.notifyDataSetChanged();
    }

    private void showSpot() {
        options = new FirebaseRecyclerOptions.Builder<Spot>().
                setQuery(databaseReference,Spot.class).build();

        adapter =
                new FirebaseRecyclerAdapter<Spot, MyRecyclerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(
                            @NonNull MyRecyclerViewHolder holder,
                            int position,
                            @NonNull Spot model) {
                        holder.name.setText(model.getName());
                        holder.latitude.setText(Double.toString(model.getLatitude()));
                        holder.longitude.setText(Double.toString(model.getLongitude()));
                        holder.description.setText(model.getDescription());
                        holder.visibility.setText(String.valueOf(model.isVisibility()));
                    }

                    @NonNull
                    @Override
                    public MyRecyclerViewHolder onCreateViewHolder(
                            @NonNull ViewGroup viewGroup,
                            int i) {
                        View itemview = LayoutInflater.from(getBaseContext()).inflate(R.layout.show_spot,viewGroup,false);
                        return new MyRecyclerViewHolder(itemview);
                    }
                };

        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    private void initView() {
        name = (EditText) findViewById(R.id.txtName);
        latitiude = (EditText) findViewById(R.id.txtLat);
        longitude = (EditText) findViewById(R.id.txtLong);
        description = (EditText) findViewById(R.id.txtDescription);
        visibility = (Switch) findViewById(R.id.switchVisibility);
        save = (Button) findViewById(R.id.btnSave);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }


}
