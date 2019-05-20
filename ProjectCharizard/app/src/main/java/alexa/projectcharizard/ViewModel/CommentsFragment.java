package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        initButton();


        return v;
    }

    private void initButton() {
        // Button that saves and uploads a new comment.
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Spot s: currentRun.getSpots()){
                    if (s.getId().equals(spot.getId())){
                        addComment();
                        return;
                    }
                }
                // If spot isn't found in currentRun, a Toast appears with this text.
                Toast toast = Toast.makeText(getContext(),"The spot might not exist anymore, try reloading the spot", Toast.LENGTH_LONG);
                TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
                if (t != null) t.setGravity(Gravity.CENTER);
                toast.show();
            }
        });
    }

    private void addComment() {
        DateTime dateTime = new DateTime();
        String dateString = "20" + dateTime.getYearOfCentury()
                + "/" + dateTime.getMonthOfYear()
                + "/" + dateTime.getDayOfMonth()
                + "  " + addZero(dateTime.getHourOfDay())
                + ":" + addZero(dateTime.getMinuteOfHour());
        // The comment that is supposed to be saved.
        Comment newComment = new Comment(CurrentRun.getActiveUser().getUsername(), comment.getText().toString(),
                dateString);
        // Saves comment to the database.
        database.saveComment(newComment, spot.getId());
        // Saves comment to the commentlist.
        spot.getCommentList().add(newComment);

        // Resets the text in the comment zone, and resets the focus
        comment.setText("");
        comment.clearFocus();

        // Refills the comments into the list
        CommentsAdapter commentsAdapter = new CommentsAdapter(getContext(), spot.getCommentList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(commentsAdapter);
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

    private String addZero(int dateTime) {
        if (dateTime < 10){
            return "0"+dateTime;
        }
        return dateTime + "";
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
        // Starts the button as not enabled since there is no text to be used as a comment
        send.setEnabled(false);
        initCommentSection();
    }

    /**
     * A method that lets tells the send-button if there is text to be sent as a comment
     */
    private void initCommentSection() {
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}