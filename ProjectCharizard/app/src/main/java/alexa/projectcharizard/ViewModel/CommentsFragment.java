package alexa.projectcharizard.ViewModel;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
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
    private Button createCommentButton;
    private RecyclerView recyclerView;

    private Spot spot;
    private Database database = Database.getInstance();
    private CurrentRun currentRun = CurrentRun.getInstance();

    private String commentInput = ""; // The input for comment from user


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

        // Create an onClickListener for createCommentButton
        createCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDialog();
            }
        });

        return v;
    }

    private void addComment() {
        DateTime dateTime = new DateTime();
        String dateString = "20" + dateTime.getYearOfCentury()
                + "/" + dateTime.getMonthOfYear()
                + "/" + dateTime.getDayOfMonth()
                + "  " + addZero(dateTime.getHourOfDay())
                + ":" + addZero(dateTime.getMinuteOfHour());
        // The comment that is supposed to be saved.
        Comment newComment = new Comment(CurrentRun.getActiveUser().getUsername(), commentInput,
                dateString);
        // Saves comment to the database.
        database.saveComment(newComment, spot.getId());
        // Saves comment to the commentlist.
        spot.getCommentList().add(newComment);

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
        createCommentButton = (Button) v.findViewById(R.id.createCommentBtn);
    }

    /**
     * The AlertDialog box that allows the user to leave a comment.
     * Pops up after the user presses the FAB button
     */
    private void commentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
        builder.setTitle("Leave a comment");

        // Set up the input
        final EditText input = new EditText(getContext());

        // Allowing multi-line for text input when leaving a comment
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setSingleLine(false);
        input.setLines(5);
        input.setMaxLines(5);
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commentInput = input.getText().toString();

                // Adding comment to spot
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Set the send button to initially disabled
        final Button sendButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        sendButton.setEnabled(false);

        // Adding textChangedListener to Send button - not possible to send message if
        // text field is empty
        input.addTextChangedListener(new TextWatcher() {
            private void handleText() {
                if(input.getText().length() == 0) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                handleText();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing to do
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing to do
            }
        });

    }
}