package alexa.projectcharizard.ViewModel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import alexa.projectcharizard.Model.Comment;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.Spot;
import alexa.projectcharizard.R;

/**
 * @author Alexander Selmanovic
 * <p>
 * A class that makes a list of all comments that is shown in CommentsFragment
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private Spot currentSpot;
    private CommentsFragment commentsFragment;

    public CommentsAdapter(Context context, List<Comment> commentList, Spot spot, CommentsFragment commentsFragment) {
        this.context = context;
        this.commentList = commentList;
        this.currentSpot = spot;
        this.commentsFragment = commentsFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment, viewGroup, false);
        v.findViewById(R.id.deleteComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new MyViewHolder(v);
    }

    /**
     * @param myViewHolder The viewholder where text should be shown
     * @param i            The current position in the list to be shown
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final int nr = i;
        System.out.println("\n hej \n");
        System.out.println(nr);
        myViewHolder.textBody.setText(commentList.get(i).getComment());
        myViewHolder.name.setText(commentList.get(i).getUsername());
        myViewHolder.date.setText(commentList.get(i).getDateTime());
        myViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.getInstance().deleteComment(commentList.get(nr).getId(), currentSpot);
                commentsFragment.refreshComments();
            }
        });

    }

    /**
     * A method for getting the size of the list to be shown in the adapter
     *
     * @return Size of the list
     */
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /**
     * A class for holding the views for the comments
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textBody, name, date;
        private ImageButton deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textBody = itemView.findViewById(R.id.commentText);
            name = itemView.findViewById(R.id.usernameText);
            date = itemView.findViewById(R.id.timeStampTxt);
            deleteBtn = itemView.findViewById(R.id.deleteComment);
        }
    }
}
