package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.validator.routines.EmailValidator;

import alexa.projectcharizard.R;

/**
 * An Android activity class for changing the user email. Uses Apache commons validator
 * in order to validate entered email-addresses. Started by AccountPageActivity.
 */
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

    /**
     * Checks so that the new email is valid, not the same as the current one and that
     * the email entered in newEmailField and verifNewEmailField are the same. If the checks pass
     * then calls backend to change the user email
     *
     * @param view
     */
    public void changeEmailButtonAction(View view) {
        String newEmail = newEmailView.getText().toString();
        String verifMail = verifNewEmailView.getText().toString();
        if (newEmail.equals(intent.getStringExtra(AccountPageActivity.EXTRA_MESSAGE))) {
            // Return error message that the new email is the same as the old one
            Toast.makeText(getApplicationContext(), "New email is the same as the old email", Toast.LENGTH_SHORT).show();
        } else if (isValidEmail(newEmail) && newEmail.equals(verifMail)) {
            // Change the users email address and notify the user'
            // TODO: Call backend to change the user email
            currentEmailView.setText(newEmail);
            Toast.makeText(getApplicationContext(), "Email changed", Toast.LENGTH_SHORT).show();
        } else if (isValidEmail(newEmail)) {
            // Return error message that the verification field does not match
            Toast.makeText(getApplicationContext(), "Email does not match", Toast.LENGTH_SHORT).show();
        } else {
            // Return error message that the new email entered is not valid
            Toast.makeText(getApplicationContext(), "Email is not valid", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Uses Apache Commons Validator to determine if a email address is valid
     *
     * @param newEmail the email address to be validated
     * @return boolean
     */
    private boolean isValidEmail(String newEmail) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(newEmail);
    }

}
