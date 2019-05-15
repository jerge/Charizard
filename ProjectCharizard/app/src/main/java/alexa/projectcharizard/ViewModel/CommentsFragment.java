package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.joda.time.DateTime;

import alexa.projectcharizard.Model.Comment;
import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

public class CommentsFragment extends Fragment {

    private EditText comment;
    private Button send;
    private RecyclerView recyclerView;

    private Spot spot;
    private Database database = Database.getInstance();
    private CurrentRun currentRun = CurrentRun.getInstance();


    public CommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comments, container, false);

        // Initiates the views in the fragment.
        initFragment(v);

        // Finds the current spot among all available spots
        findSpot(getActivity().getIntent().getStringExtra("SpotId"));

        // Creates an adapter for showing the comments connected to the spot
        final CommentsAdapter commentsAdapter = new CommentsAdapter(getContext(), spot.getCommentList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(commentsAdapter);

        // Button that saves and uploads a new comment.
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateTime = new DateTime();
                String tempDate = "20" + dateTime.getYearOfCentury()
                        + "/" + dateTime.getMonthOfYear()
                        + "/" + dateTime.getDayOfMonth()
                        + "  " + dateTime.getHourOfDay()
                        + ":" + dateTime.getMinuteOfHour();
                // The comment that is supposed to be saved.
                Comment tempComment = new Comment(spot.getCreatorId(), comment.getText().toString(),
                        tempDate);
                // Saves comment to the database.
                database.saveComment(tempComment, spot.getId());
                // Saves comment to the commentlist.
                spot.getCommentList().add(tempComment);

                // Resets the text in the comment zone, and resets the focus
                comment.setText("");
                comment.clearFocus();

                // Refills the comments into the list
                CommentsAdapter commentsAdapter = new CommentsAdapter(getContext(), spot.getCommentList());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(commentsAdapter);
            }
        });
        return v;
    }

    /**
     * A method for finding the correct spot among all spots.
     *
     * @param spotId The spot that is to be found.
     */
    private void findSpot(String spotId) {
        for (Spot s : currentRun.getSpots()) {
            if (s.getId().equals(spotId)) {
                spot = s;
                return;
            }
        }

        for (Spot s : CurrentRun.getCurrentRunAddedSpots()) {
            if (s.getId().equals(spotId)) {
                spot = s;
                return;
            }
        }
    }

    /**
     * A method that initialized the fragment.
     *
     * @param v The view where the connections between the things you see and the things
     *          that should be seen are made.
     */
    private void initFragment(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.comment_recyclerView);
        comment = (EditText) v.findViewById(R.id.postTxt);
        send = (Button) v.findViewById(R.id.sentBtn);
    }

}