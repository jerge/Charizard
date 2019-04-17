package alexa.projectcharizard.ViewModel;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alexa.projectcharizard.Model.MyRecyclerViewHolder;
import alexa.projectcharizard.R;

public class MainActivity extends AppCompatActivity {

    TextView name, latlong, description;
    Switch visibility;
    Button save;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<String> options;
    FirebaseRecyclerAdapter<String, MyRecyclerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CHARIZARD_DATABASE");

        showShit();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveShit();
            }
        });
    }

    private void showShit() {
        options = new FirebaseRecyclerOptions.Builder<String>().
                    setQuery(databaseReference,String.class).
                    build();
    }

    private void saveShit (){
        databaseReference.push();
    }

    private void initView() {
        name = (TextView) findViewById(R.id.txtName);
        latlong = (TextView) findViewById(R.id.txtLatLong);
        description = (TextView) findViewById(R.id.txtDescription);
        visibility = (Switch) findViewById(R.id.switchVisibility);
        recyclerView = (RecyclerView) findViewById(R.id.)
    }


}
