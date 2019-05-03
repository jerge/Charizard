package alexa.projectcharizard.Model;

import android.support.annotation.NonNull;

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
public class Database{
    private static Database instance = null;

    private DatabaseReference databaseReference;
    private List<Spot> spots = new ArrayList<>();
    /**
     * A static instance of the database, making sure that there are not muliple instances of the
     * database in use at the same time.
     * @return the instance of the Database in use
     */
    public static Database getInstance(){
        if(instance == null){
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
    private Database (){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CHARIZARD_DATABASE");

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

    /**
     * A method for saving spots to the database
     * @param name The name of the spot
     * @param dblLat The latitude of the spot
     * @param dblLng The longitute of the spot
     * @param description The description of the spot
     * @param category The category of the spot
     * @param visibility The visibility of the spot
     */
    public void saveSpot(String name, Double dblLat, Double dblLng, String description, Category category, Boolean visibility){
        Spot spot = new Spot(name, dblLat, dblLng, description, category, visibility);
        databaseReference.push().setValue(spot);
    }

    public void addValueEventListener(ValueEventListener valueEventListener) {
    }
}
