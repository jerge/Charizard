package alexa.projectcharizard.Model;

import java.io.Serializable;

/**
 * @author Alexander Selmanovic
 * <p>
 * A class that holds all the information needed for a Comment
 */
public class Comment implements Serializable {

    private String username;
    private String comment;
    private String dateTime;
    private String id;
    private String creatorId;

    public Comment(String username, String comment, String dateTime, String id, String creatorId) {
        this.username = username;
        this.comment = comment;
        this.dateTime = dateTime;
        this.id = id;
        this.creatorId = creatorId;
    }

    public Comment(String username, String comment, String dateTime, String creatorId) {
        this.username = username;
        this.comment = comment;
        this.dateTime = dateTime;
        this.creatorId = creatorId;
    }

    public Comment() {
        // Required for Firebase to create new Comments
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }
}
