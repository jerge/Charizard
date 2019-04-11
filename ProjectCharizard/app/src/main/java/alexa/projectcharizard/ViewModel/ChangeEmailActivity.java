package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.validator.routines.EmailValidator;

import alexa.projectcharizard.R;


public class ChangeEmailActivity extends AppCompatActivity {

    private TextView currentEmailView;
    private EditText newEmailView;
    private EditText verifNewEmailView;
    private Button changeEmailButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        intent = getIntent();
        String message = intent.getStringExtra(AccountPageActivity.EXTRA_MESSAGE);

        currentEmailView = (TextView) findViewById(R.id.currentEmailView);
        newEmailView = (EditText) findViewById(R.id.newEmailView);
        verifNewEmailView = (EditText) findViewById(R.id.verifNewEmailView);
        changeEmailButton = (Button) findViewById(R.id.changeEmailButton);

        currentEmailView.setText(message);
    }

    public void changeEmailButtonAction(View view) {
        String newEmail = newEmailView.getText().toString();
        String verifMail = verifNewEmailView.getText().toString();
        if (newEmail == intent.getStringExtra(AccountPageActivity.EXTRA_MESSAGE)) {
            // Return error message that the new email is the same as the old one
        } else if (isValidEmail(newEmail) && newEmail.equals(verifMail)) {
            // Change the users email address and notify the user
        } else if (isValidEmail(newEmail)) {
            // Return error message that the verification field does not match
        } else {
            // Return error message that the new email entered is not valid
        }
        // Return general error message
    }

    /**
     * Use Apache Commons Validator to determine if a email address is valid
     *
     * @param newEmail the email address to be validated
     * @return boolean
     */
    private boolean isValidEmail(String newEmail) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(newEmail);
    }

}
