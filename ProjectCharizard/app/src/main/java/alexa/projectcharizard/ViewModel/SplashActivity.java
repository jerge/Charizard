package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.User;
import alexa.projectcharizard.R;

/**
 * @author Alexander Selmanovic
 *
 * A class that shows a splashimage when opening the app, and signs in a user in case they have
 * logged in on the device before.
 */
public class SplashActivity extends AppCompatActivity {

    private Database database = Database.getInstance();
    private CurrentRun currentRun = CurrentRun.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Loads all users from the database into currentRun.
        database.getDatabaseReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentRun.getUsers().clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    currentRun.getUsers().add(user);
                }
                CurrentRun.setCurrentRunUsers(currentRun.getUsers());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    // Shows a splashimage for 1500 milliseconds.
                    sleep(1500);
                    if (!checkCredentials(loadLocalUsername(), loadLocalPassword())){
                        // If there is no previously saved local user, takes the user to the sign-in
                        // page
                        Intent intent  = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    /**
     * A method that checks if the previously saved username and password matches a user saved
     * in the database, in which case it signs in with that user. If a match is found, returns true.
     *
     * @param usernameInput The username to be checked.
     * @param passwordInput The password to be checked.
     * @return Returns true if match found, otherwise false.
     */
    public boolean checkCredentials (String usernameInput, String passwordInput) {
        for (User user : currentRun.getUsers()) {     //checks every user in the user list
            if (user.getUsername().equals(usernameInput)) {         //if the username matches
                if (passwordInput.equals(user.getPassword())) {     //...and the password matches
                    currentRun.setActiveUser(user);
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A method that fetches previously saved username.
     *
     * @return The previously saved username.
     */
    private String loadLocalUsername(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString("username", "default value");
    }

    /**
     * A method that fetches previously saved password.
     *
     * @return The previously saved password.
     */
    private String loadLocalPassword(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString("password", "default value");
    }
}
