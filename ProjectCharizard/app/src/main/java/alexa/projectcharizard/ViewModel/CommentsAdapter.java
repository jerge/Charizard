package alexa.projectcharizard.ViewModel;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import alexa.projectcharizard.Model.Comment;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

/**
 * @author Alexander Selmanovic
 *
 * A class that makes a list of all comments that is shown in CommentsFragment
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private Dialog dialog;

    public CommentsAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(v);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_comment);

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button deleteBtn = (Button) dialog.findViewById(R.id.deleteComentBtn);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Database.getInstance().removeComment(commentList.get(myViewHolder.getAdapterPosition()).getCommentId());
                        //commentList.remove(commentList.get(myViewHolder.getAdapterPosition()));
                        // TODO: Remove from database
                        // TODO: Remove from current spot
                        dialog.dismiss();
                    }
                });

                Button cancelBtn = (Button) dialog.findViewById(R.id.cancelDeleteCommentBtn);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        return myViewHolder;
    }

    /**
     * @param myViewHolder The viewholder where text should be shown
     * @param i The current position in the list to be shown
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textBody.setText(commentList.get(i).getComment());
        myViewHolder.name.setText(commentList.get(i).getUsername());
        myViewHolder.date.setText(commentList.get(i).getDateTime());

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
        private ConstraintLayout delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            delete = (ConstraintLayout) itemView.findViewById(R.id.deleteCommentConstraint);
            textBody = (TextView) itemView.findViewById(R.id.commentText);
            name = (TextView) itemView.findViewById(R.id.usernameText);
            date = (TextView) itemView.findViewById(R.id.timeStampTxt);
        }
    }
}
