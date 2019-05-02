package alexa.projectcharizard.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database{
    private static Database instance = null;
    private DatabaseReference databaseReference;

    public static Database getInstance(){
        if(instance == null){
            return new Database();
        }
        return instance;
    }

    private Database (){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CHARIZARD_DATABASE");
    }

    public void saveSpot(String name, Double dblLat, Double dblLng, String description, Boolean visibility){
        Spot spot = new Spot(name, dblLat, dblLng, description, visibility);
        databaseReference.push().setValue(spot);
    }
}
