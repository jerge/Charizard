package alexa.projectcharizard.Model;

import android.graphics.Bitmap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * A class used to retrieve and store data on the Firebase Database.
 */
public class Database {
    private static Database instance = null;
    private CurrentRun currentRun = CurrentRun.getInstance();

    /**
     * A reference that communicates with the Firebase database
     */
    private DatabaseReference databaseReference;

    /**
     * A static instance of the database, making sure that there are not multiple instances of the
     * database in use at the same time.
     *
     * @return the instance of the Database in use
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
            return instance;
        }
        return instance;
    }

    /**
     * Gets the database reference
     *
     * @return Databasereference
     */
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    /**
     * The initiation of the database
     */
    private Database() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /**
     * Function that saves user in the database
     * The user receives an id and gets saved at that unique id in the database
     * @param user the user which gets saved
     */
    public void saveUser(User user) {
        String tempId = databaseReference.child("Users").push().getKey();
        user.setId(tempId);
        databaseReference.child("Users").child(tempId).setValue(user);
    }

    public Spot saveSpot(String name, Double dblLat, Double dblLng, String description, Category category, Bitmap image, Boolean visibility, String userId) {
        String id = databaseReference.child("Spots").push().getKey();
        Spot spot = new Spot(name, dblLat, dblLng, description, category, image, visibility, id, userId);
        if (id != null) {
            databaseReference.child("Spots").child(id).setValue(spot);
        }
        currentRun.getSpots().add(spot);
        //currentRun.getActiveUser().getUserSpots().add(spot); //TODO make this work pls lmao
        return spot;
    }

    /**
     * A method for removing a spot, also removes from the list of spots
     *
     * @param id The id of the spot to be removed
     */
    public void removeSpot(String id) {
        Database.getInstance().getDatabaseReference().child("Spots").child(id).removeValue();
        Spot spotToRemove = findSpot(id);
        currentRun.getSpots().remove(spotToRemove);
    }

    public Comment saveComment(String userName, String commentText, String dateTime, String spotId) {
        String id = Database.getInstance().getDatabaseReference().child("Spots").child(spotId).push().getKey();
        Comment comment = new Comment(userName, commentText, dateTime, id);
        if (id != null) {
            Database.getInstance().getDatabaseReference().child("Spots").child(spotId)
                    .child("comments").child(id).setValue(comment);
        }

        return comment;
    }

    public void removeComment (String commentId){
        Spot spot = findSpotWithComment(commentId);
        System.out.println("Spot id: " + spot.getId());
        System.out.println("Comment id: " + commentId);
        Database.getInstance().getDatabaseReference()
                .child("Spot").child(spot.getId()).child(commentId).removeValue();
        Comment comment = findComment(spot.getCommentList(), commentId);
        int index = findSpotIndex(spot.getId());
        currentRun.getSpots().get(index).getCommentList().remove(comment);
    }

    private Spot findSpotWithComment(String commentId) {
        for (Spot spot: currentRun.getSpots()){
            if (findComment(spot.getCommentList(), commentId) != null){
                return spot;
            }
        }
        return null;
    }

    private int findSpotIndex(String spotId) {
        for (int i = 0; i < currentRun.getSpots().size(); i++){
            if (currentRun.getSpots().get(i).getId().equals(spotId)){
                return i;
            }
        }
        return 0;
    }

    private Spot findSpot(String id){
        for (Spot spot: currentRun.getSpots()){
            if (spot.getId().equals(id)){
                return spot;
            }
        }
        return null;
    }

    private Comment findComment (List<Comment> commentList, String commentId){
        for (Comment comment: commentList){
            if (comment.getCommentId().equals(commentId)){
                return comment;
            }
        }
        return null;
    }
}
