package alexa.projectcharizard.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


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
     * A reference that communicates with the Storage part of Firebase
     */
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    /**
     * A static instance of the database, making sure that there are not multiple instances of the
     * database in use at the same time.
     *
     * @return the instance of the Database in use
     */
    public static Database getInstance() {
        if (instance == null) {
            return new Database();
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
     * Gets the Storage reference
     *
     * @return StorageReference
     */

    public StorageReference getStorageReference() {
        return storageReference;
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
     *
     * @param user the user which gets saved
     */
    public void saveUser(User user) {
        String tempId = databaseReference.child("Users").push().getKey();
        user.setId(tempId);
        databaseReference.child("Users").child(tempId).setValue(user);
    }


    // A constructor for spots with images, they have already created an ID for the spot
    public Spot saveSpot(String id, String name, Double dblLat, Double dblLng, String description, Category category, Boolean visibility, String userId) {
        Spot spot = new Spot(name, dblLat, dblLng, description, category, visibility, id, userId);
        if (id != null) {
            databaseReference.child("Spots").child(id).setValue(spot);
        }
        currentRun.getSpots().add(spot);
        //currentRun.getActiveUser().getUserSpots().add(spot); //TODO make this work pls lmao
        return spot;
    }

    public Spot saveSpot(String name, Double dblLat, Double dblLng, String description, Category category, Boolean visibility, String userId) {

        String id = databaseReference.child("Spots").push().getKey();
        Spot spot = new Spot(name, dblLat, dblLng, description, category, visibility, id, userId);
        if (id != null) {
            databaseReference.child("Spots").child(id).setValue(spot);
        }
        currentRun.getSpots().add(spot);
        //currentRun.getActiveUser().getUserSpots().add(spot); //TODO make this work pls lmao
        return spot;
    }

    /**
     * A method for removing a spot, also removes from the list of spots
     * Also removes the spot from storage
     * @param id The id of the spot to be removed
     */
    public void remove(String id) {
        databaseReference.child(id).removeValue();
        for (Spot spot : currentRun.getSpots()) {
            if (spot.getId().equals(id)) {
                currentRun.getSpots().remove(spot);
            }
        }
        storageReference.child("images/" + id).delete();
    }

}
