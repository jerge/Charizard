package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import alexa.projectcharizard.R;

// https://www.youtube.com/watch?v=lF5m4o_CuNg

public class SignInActivity extends Activity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView attemptsLeftText;
    private TextView signUpText;
    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);
        attemptsLeftText = findViewById(R.id.attemptsLeftText);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signUpText);

        attemptsLeftText.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                validate(username.getText().toString(), password.getText().toString());
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validate(String usernameInput, String passwordInput){
        if ((usernameInput.equals("Admin")) && (passwordInput.equals("1234"))){
            Intent intent = new Intent(SignInActivity.this, MapActivity.class);
            startActivity(intent);
            attemptsLeftText.setVisibility(View.INVISIBLE);
        } else {
            counter--;
            attemptsLeftText.setVisibility(View.VISIBLE);
            attemptsLeftText.setText("Number of attempts remaining: " + String.valueOf(counter));

            if (counter <= 0){
                loginButton.setEnabled(false);
            }
        }
    }

}
