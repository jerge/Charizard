package alexa.projectcharizard.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A class used to retrieve and store data on the Firebase Database.
 */
public class Database{
    private static Database instance = null;
    private DatabaseReference databaseReference;

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

    /**
     * The initiation of the database
     */
    private Database (){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CHARIZARD_DATABASE");
    }

    /**
     * A method for saving spots to the database
     * @param name The name of the spot
     * @param dblLat The latitude of the spot
     * @param dblLng The longitute of the spot
     * @param description The description of the spot
     * @param visibility The visibility of the spot
     */
    public void saveSpot(String name, Double dblLat, Double dblLng, String description, Boolean visibility){
        Spot spot = new Spot(name, dblLat, dblLng, description, visibility);
        databaseReference.push().setValue(spot);
    }
}
