package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import alexa.projectcharizard.R;

public class AboutFragment extends Fragment {

    TextView spotDescription;
    TextView spotName;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        spotDescription = (TextView) v.findViewById(R.id.spotDescription);
        spotName = (TextView) v.findViewById(R.id.spotName);

        spotDescription.setText(getActivity().getIntent().getStringExtra("SpotDescription"));
        spotName.setText(getActivity().getIntent().getStringExtra("SpotName"));
        // Inflate the layout for this fragment
        return v;
    }

}