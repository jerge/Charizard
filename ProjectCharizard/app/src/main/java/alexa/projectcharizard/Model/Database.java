package alexa.projectcharizard.Model;

import android.graphics.Bitmap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        return spot;
    }

    /**
     * A method for removing a spot, also removes from the list of spots
     *
     * @param id The id of the spot to be removed
     */
    public void removeSpot(String id) {
        Database.getInstance().getDatabaseReference().child("Spots").child(id).removeValue();
        for (Spot spot : currentRun.getSpots()) {
            if (spot.getId().equals(id)) {
                currentRun.getSpots().remove(spot);
                return;
            }
        }
    }

    /**
     * A method for removing the user from the database. Called after the user presses the
     * delete account button/text.
     * @param id The ID of the user
     */
    public void deleteUser(String id) {
        databaseReference.child(id).removeValue();
        for (User user : currentRun.getUsers()) {
            if (user.getId().equals(id)) {
                System.out.println("Removing user " + user.getId());
                Database.getInstance().getDatabaseReference().child("Users").child(id).removeValue();
                currentRun.getUsers().remove(user);
                return;
            }
        }
    }


    /**
     * A method that saves a comment to the database in the specified spot.
     *
     * @param comment The comment to be saved
     * @param spotId The spot the comment is to be saved in
     */
    public void saveComment(Comment comment, String spotId) {
        Database.getInstance().getDatabaseReference().child("Spots").child(spotId)
                .child("comments").push().setValue(comment);
    }
}
