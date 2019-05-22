package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.Model.User;
import alexa.projectcharizard.R;

/**
 * Fragment for the about-page of a spot in the detailed view activity
 *
 * @author Filip Andréasson
 */
public class AboutFragment extends Fragment {

    private TextView spotDescription;
    private TextView spotName;
    private TextView spotCategory;
    private TextView spotCreator;
    private Button removeBtn;

    private Database database = Database.getInstance();
    private CurrentRun currentRun = CurrentRun.getInstance();

    private Spot currentSpot;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Set default values to gui elements
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        initFragment(v);

        for (Spot spot: currentRun.getSpots()){
            if (getActivity().getIntent().getStringExtra("SpotId").equals(spot.getId())){
                currentSpot = spot;
                break;
            }
        }



        //Collects extra intent from the previous activity and edits the relevant TextViews
        spotDescription.setText(getActivity().getIntent().getStringExtra("SpotDescription"));
        spotName.setText(getActivity().getIntent().getStringExtra("SpotName"));
        spotCategory.setText(getActivity().getIntent().getStringExtra("SpotCategory"));
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.removeSpot(getActivity().getIntent().getStringExtra("SpotId")); //closes the activity and returns to previous
                getActivity().onBackPressed();
            }
        });

        //Gets the creator of the spot and edits the relevant TextView
        for (User user : currentRun.getUsers()) {
            if (getActivity().getIntent().getStringExtra("SpotCreator").equals(user.getId())) {
                spotCreator.setText(user.getUsername());
                break;
            }
        }
        // Inflate the layout for this fragment
        return v;
    }

    /**
     * Initializes the GUI elements and connects listener to delete button
     */
    private void initFragment(View v) {
        spotDescription = (TextView) v.findViewById(R.id.spotDescription);
        spotName = (TextView) v.findViewById(R.id.spotName);
        spotCreator = (TextView) v.findViewById(R.id.creatorText);
        spotCategory = (TextView) v.findViewById(R.id.spotCategory);
        removeBtn = (Button) v.findViewById(R.id.removeBtn);

/*
        // Checks if the spot is owned by the current user, if it is the remove spot button appears and becomes activated
        // Otherwise it will not appear nor be activated
        if (getActivity().getIntent().getStringExtra("SpotCreator").equals(CurrentRun.getActiveUser().getId())) {
            removeBtn.setActivated(true);
            removeBtn.setVisibility(View.VISIBLE);
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.removeSpot(getActivity().getIntent().getStringExtra("SpotId"));

                    //closes the activity and returns to previous
                    getActivity().onBackPressed();
                }
            });
        } else {
            removeBtn.setActivated(false);
            removeBtn.setVisibility(View.INVISIBLE);
        }*/
    }
}