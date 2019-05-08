package alexa.projectcharizard.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.User;
import alexa.projectcharizard.R;

public class SignUpActivity extends Activity {

    private EditText signUpUsername, signUpPassword,signUpEmail;
    private Button signUpButton;
    private TextView alreadySignedUp;

    final Database database = Database.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpUIViews();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){

                    String usernameInput = signUpUsername.getText().toString();
                    String passwordInput = signUpPassword.getText().toString();
                    String emailInput = signUpEmail.getText().toString();

                    if(checkIfUsernameTaken(usernameInput) || checkIfEmailTaken(emailInput)){
                        Toast.makeText(getApplicationContext(), "Username or email already taken", Toast.LENGTH_LONG).show();
                    }else{
                        User user = new User(usernameInput, emailInput, passwordInput, null);

                        database.saveUser(user);

                        Intent mapActivity = new Intent(SignUpActivity.this, MapsActivity.class);
                        startActivity(mapActivity);
                    }
                }
            }
        });

        alreadySignedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

    }

    private void setUpUIViews(){
        signUpUsername = (EditText)findViewById(R.id.signUpUsername);
        signUpPassword = (EditText)findViewById(R.id.signUpPassword);
        signUpEmail = (EditText)findViewById(R.id.signUpEmail);
        signUpButton = (Button)findViewById(R.id.signUpButton);
        alreadySignedUp = (TextView)findViewById(R.id.alreadySignedUp);
    }

    private Boolean validate(){

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

    private Boolean checkIfUsernameTaken(String usernameInput){
        for (User user : database.getUsers()){
            if (user.getUsername().toLowerCase().equals(usernameInput.toLowerCase()))
                return true;
        }
        return false;
    }

    private Boolean checkIfEmailTaken(String emailInput){
        for (User user : database.getUsers()){
            if (user.getEmail().toLowerCase().equals(emailInput.toLowerCase()))
                return true;
        }
        return false;
    }

}
