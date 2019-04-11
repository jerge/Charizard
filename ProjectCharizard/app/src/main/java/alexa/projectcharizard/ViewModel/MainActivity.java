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

public class MainActivity extends Activity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView attemptsLeftText;
    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.UsernameField);
        password = (EditText)findViewById(R.id.PasswordField);
        attemptsLeftText = (TextView)findViewById(R.id.AttemptsLeftText);
        loginButton = (Button)findViewById(R.id.LoginButton);

        attemptsLeftText.setText("");

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                validate(username.getText().toString(), password.getText().toString());
            }
        });

    }

    private void validate(String usernameInput, String passwordInput){
        if ((usernameInput.equals("Admin")) && (passwordInput.equals("1234"))){
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        } else {
            counter--;

            attemptsLeftText.setText("Number of attempts remaining: " + String.valueOf(counter));

            if (counter <= 0){
                loginButton.setEnabled(false);
            }
        }
    }

}
