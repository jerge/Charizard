package alexa.projectcharizard.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.User;
import alexa.projectcharizard.R;

/**
 * Activity for signing in to the application.
 * This is the launcher activity.
 *
 * @author Filip Andréasson
 */

public class SignInActivity extends Activity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView signUpText;
    private TextView credErrorText;
    private ImageView logoImage;
    final Database database = Database.getInstance();
    private CurrentRun currentRun = CurrentRun.getInstance();
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //finds the relevant view for every view object
        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signUpText);
        credErrorText = findViewById(R.id.credErrorText);
        logoImage = findViewById(R.id.logoImage);

        credErrorText.setVisibility(View.INVISIBLE);
        logoImage.setImageResource(R.drawable.project_icon);

        if (!isOnline()) {
            Toast.makeText(getBaseContext(), "You are not connected to internet. ", Toast.LENGTH_LONG).show();

        }

        /**
         * Add listeners for every clickable element
         */
        //Internet connection is required to be able to sign in
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    //if connected to internet, validate username and password
                    validate(username.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(getBaseContext(), "You are not connected to internet. " +
                            "Please check your internet connection and try again.",
                                Toast.LENGTH_LONG).show();
                }
            }
        });

        //Internet connection is required to be able to sign up
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "You are not connected to internet. " +
                            "Please check your internet connection and try again.", Toast.LENGTH_LONG).show();
                }
            }
        });


        /**
         * Add listener to database for Users data. This reads from the database once initially and
         * then every time any data changes in the Users tree
         */
        database.getDatabaseReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentRun.getUsers().clear();                                //clears the outdated user list
                for (DataSnapshot data : dataSnapshot.getChildren()) {      //loops through every entry found in the database
                    User user = data.getValue(User.class);
                    currentRun.getUsers().add(user);                          //adds the updated users to the user list
                }
                CurrentRun.setCurrentRunUsers(currentRun.getUsers());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Validates if there is a user with that name, and if the password matches that particular user
     *
     * @param usernameInput the text from the username field in the gui
     * @param passwordInput the text from the password field in the gui
     */
    private void validate(String usernameInput, String passwordInput) {

        if (usernameInput.equals("")) {           //there is no input in the username field
            credErrorText.setVisibility(View.VISIBLE);
            credErrorText.setText(getString(R.string.missing_credential_username));
        } else if (passwordInput.equals("")) {  //there is no input in the password field
            credErrorText.setVisibility(View.VISIBLE);
            credErrorText.setText(getString(R.string.missing_credential_password));
        }


        for (User user : currentRun.getUsers()) {     //checks every user in the user list
            if (user.getUsername().equals(usernameInput)) {         //if the username matches
                if (passwordInput.equals(user.getPassword())) {     //...and the password matches
                    currentRun.setActiveUser(user);
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                    credErrorText.setVisibility(View.INVISIBLE);
                    break;
                } else {        //if the password doesn't match
                    credErrorText.setVisibility(View.VISIBLE);
                    credErrorText.setText(getString(R.string.wrong_credentials));
                }
            } else {            //if the username doesn't match any users in the user list
                credErrorText.setVisibility(View.VISIBLE);
                credErrorText.setText(getString(R.string.wrong_credentials));
            }
        }
    }

    /**
     * Method for checking if connected to internet.
     *
     * @return True if connected to internet, false otherwise
     */
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    /**
     * Method to avoid the user returning to the previous page. Instead,
     * the back button closes the application if pressed twice quickly.
     */
    @Override
    public void onBackPressed(){

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else { Toast.makeText(getBaseContext(), "Tap button again to exit application", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }


}
