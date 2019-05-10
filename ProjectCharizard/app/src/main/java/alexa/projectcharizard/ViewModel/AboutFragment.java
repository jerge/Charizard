package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

public class AboutFragment extends Fragment {

    TextView spotDescription;
    TextView spotName;
    private Button removeBtn;
    private Database database;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        initFragment(v);

        spotDescription.setText(getActivity().getIntent().getStringExtra("SpotDescription"));
        spotName.setText(getActivity().getIntent().getStringExtra("SpotName"));
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.remove(getActivity().getIntent().getStringExtra("SpotId"));

                //closes the activity and returns to previous
                getActivity().onBackPressed();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void initFragment(View v) {
        spotDescription = (TextView) v.findViewById(R.id.spotDescription);
        spotName = (TextView) v.findViewById(R.id.spotName);
        removeBtn = (Button) v.findViewById(R.id.removeBtn);
        database = Database.getInstance();
    }

}