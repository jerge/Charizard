package alexa.projectcharizard.Model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to retrieve and store data on the Firebase Database.
 */
public class Database {
    private static Database instance = null;

    private DatabaseReference databaseReference;
    private List<Spot> spots = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private User activeUser;

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

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    /**
     * The initiation of the database
     */
    private Database() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Spots");
        databaseReference = firebaseDatabase.getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw new NullPointerException();
            }
        });
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public List<User> getUsers() {
        return users;
    }

    /**
     * A method for saving spots to the database
     *
     * @param name        The name of the spot
     * @param dblLat      The latitude of the spot
     * @param dblLng      The longitude of the spot
     * @param description The description of the spot
     * @param category    The category of the spot
     * @param visibility  The visibility of the spot
     */
    public void saveSpot(String name, Double dblLat, Double dblLng, String description, Category category, Bitmap image, Boolean visibility, User user) {
        Spot spot = new Spot(name, dblLat, dblLng, description, category, image, visibility, user);
        databaseReference.push().setValue(spot);
    }

    /**
     *
     * @param username      The username of the user
     * @param fullName      The full name of the user
     * @param password      The password of the user
     * @param profileImage  The profile image of the user
     * @param spots         The users saved spots
     */
    public void saveUser(String username, String fullName, String password, Bitmap profileImage, List<Spot> spots) {
        User user = new User(username, fullName, password, profileImage, spots);
        databaseReference.push().setValue(user);
    }

    public void addValueEventListener(ValueEventListener valueEventListener) {
    }


    /**
     * Gets the user that is logged in to the application
     * @return the logged in user
     */
    public User getActiveUser() {
        return activeUser;
    }

    /**
     * Sets what user is logged in to the application
     * @param activeUser the user to be set to active
     */
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }
}
