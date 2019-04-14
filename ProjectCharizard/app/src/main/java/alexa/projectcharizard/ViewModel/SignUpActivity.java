package alexa.projectcharizard.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alexa.projectcharizard.R;

// https://www.youtube.com/watch?v=zKBGjGoeid0&t=3s
// https://www.youtube.com/watch?v=-r59HK5zyhk

public class SignUpActivity extends Activity {

    private EditText signUpName, signUpPassword,signUpEmail;
    private Button signUpButton;
    private TextView alreadySignedUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpUIViews();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    //TODO connect to database and upload
                    Intent mapActivity = new Intent(SignUpActivity.this, MapActivity.class);
                    startActivity(mapActivity);
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
        signUpName = (EditText)findViewById(R.id.signUpName);
        signUpPassword = (EditText)findViewById(R.id.signUpPassword);
        signUpEmail = (EditText)findViewById(R.id.signUpEmail);
        signUpButton = (Button)findViewById(R.id.signUpButton);
        alreadySignedUp = (TextView)findViewById(R.id.alreadySignedUp);
    }

    private Boolean validate(){

        Boolean result = false;
        String name = signUpName.getText().toString();
        String password = signUpPassword.getText().toString();
        String email = signUpEmail.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Please enter details", Toast.LENGTH_SHORT).show();
        else
            result = true;
        return result;
    }

}
