package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.Model.User;
import alexa.projectcharizard.R;

// https://www.youtube.com/watch?v=lF5m4o_CuNg

public class SignInActivity extends Activity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView signUpText;
    private TextView incorrectCredText;
    private ImageView logoImage;
    final Database database = Database.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signUpText);
        incorrectCredText = findViewById(R.id.incorrectCredText);
        logoImage = findViewById(R.id.logoImage);

        incorrectCredText.setVisibility(View.INVISIBLE);
        logoImage.setImageResource(R.drawable.project_icon);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(username.getText().toString(), password.getText().toString());
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Validates if there is a user with that name, and if the password matches that particular user
     * @param usernameInput
     * @param passwordInput
     */
    private void validate(String usernameInput, String passwordInput) {
        for (User user : database.getUsers()) {
            if (user.getUsername().equals(usernameInput)) {
                if (usernameInput.equals(user.getUsername()) && passwordInput.equals(user.getPassword())) {
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                    incorrectCredText.setVisibility(View.INVISIBLE);
                    break;
                } else {
                    incorrectCredText.setVisibility(View.VISIBLE);
                }
            } else {
                incorrectCredText.setVisibility(View.VISIBLE);
            }
        }
    }
}
