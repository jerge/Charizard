package alexa.projectcharizard.Model;

import java.io.Serializable;

/**
 * @author Alexander Selmanovic
 *
 * A class that holds all the information needed for a Comment
 */
public class Comment implements Serializable {

    private String username;
    private String comment;
    private String dateTime;
    private String commentId;

    public Comment(String username, String comment, String dateTime, String commentId) {
        this.username = username;
        this.comment = comment;
        this.dateTime = dateTime;
        this.commentId = commentId;
    }

    public Comment() {
        // Required for Firebase to create new Comments
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCommentId() {
        return commentId;
    }
}
