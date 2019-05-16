package alexa.projectcharizard.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.User;
import alexa.projectcharizard.R;

/**
 * Activity for signing up an account in the application
 * This is accessed from the sign in activity
 * @author Mathias Lammers
 */

public class SignUpActivity extends Activity {

    private EditText signUpUsername, signUpPassword,signUpEmail;
    private Button signUpButton;
    private TextView alreadySignedUp;
    final Database database = Database.getInstance();
    private CurrentRun currentRun = CurrentRun.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpUIViews();



        //When the 'Sign up' button is clicked, this is what happens
        //'Sign up' doesn't work if there is no internet connection
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    //Checking whether the user has entered information in all the fields
                    if (areFieldsFilledIn()) {

                        String usernameInput = signUpUsername.getText().toString();
                        String passwordInput = signUpPassword.getText().toString();
                        String emailInput = signUpEmail.getText().toString();

                        //Checking if the username and email is already in use
                        if (checkIfUsernameTaken(usernameInput)) {

                            Toast.makeText(getApplicationContext(), "Username already taken, please choose another", Toast.LENGTH_LONG).show();

                        } else if (!isValidEmail(emailInput)) {

                            Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_LONG).show();

                        } else if (checkIfEmailTaken(emailInput)) {

                            Toast.makeText(getApplicationContext(), "Email already taken, please choose another", Toast.LENGTH_LONG).show();

                        } else {
                            // Saving the information the user has entered as a new user in the database
                            User user = new User(usernameInput, emailInput, passwordInput, null);
                            database.saveUser(user);

                            currentRun.setActiveUser(user);

                            // Directing the user to Maps Activity
                            Intent mapActivity = new Intent(SignUpActivity.this, MapsActivity.class);
                            startActivity(mapActivity);
                        }
                    }
                } else{
                    Toast.makeText(getBaseContext(), "You are not connected to internet. " +
                            "Please check your internet connection and try again.", Toast.LENGTH_LONG).show();
                }

            }
        });

        // Allows the user to return to the sign in page
        alreadySignedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

    }

    /**
     * A method to connect all the elements on the page with its corresponding variable
     */
    private void setUpUIViews(){
        signUpUsername = (EditText)findViewById(R.id.signUpUsername);
        signUpPassword = (EditText)findViewById(R.id.signUpPassword);
        signUpEmail = (EditText)findViewById(R.id.signUpEmail);
        signUpButton = (Button)findViewById(R.id.signUpButton);
        alreadySignedUp = (TextView)findViewById(R.id.alreadySignedUp);
    }


    /**
     * A method to determine whether information has been entered in all the fields when signing
     * up for a new account. If not, a toast appears asking the user to enter his or her details.
     * @return True if information has been entered in all the fields, false if not
     */
    private Boolean areFieldsFilledIn(){
        Boolean result = false;
        String name = signUpUsername.getText().toString();
        String password = signUpPassword.getText().toString();
        String email = signUpEmail.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Please enter details", Toast.LENGTH_SHORT).show();
        else
            result = true;
        return result;
    }

    /**
     * Method for checking whether the username a user want to use when signing up is already in
     * use or not.
     * @param usernameInput The username the user want to use for signing up
     * @return True if the username is already in use, false if not
     */
    private Boolean checkIfUsernameTaken(String usernameInput){
        for (User user : currentRun.getCurrentRunUsers()){
            if (user.getUsername().equalsIgnoreCase(usernameInput))
                return true;
        }
        return false;
    }

    /**
     * Method for checking whether the e-mail a user want to use when signing up is already in
     * use or not.
     * @param emailInput The e-mail the user want to use for signing up
     * @return True if the e-mail is already in use, false if not
     */
    private Boolean checkIfEmailTaken(String emailInput){

        for (User user : CurrentRun.getCurrentRunUsers()){
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(emailInput))
                return true;
        }
        return false;
    }

    public static boolean isValidEmail(final String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Method for checking if connected to internet.
     * @return True if connected to internet, false otherwise
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
