package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alexa.projectcharizard.R;

public class ChangePasswordActivity extends Activity {

    private TextView currentPasswordView;
    private EditText newPasswordField;
    private EditText verifNewPasswordField;
    private Button changePasswordButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        intent = getIntent();
        String message = intent.getStringExtra(AccountPageActivity.EXTRA_MESSAGE);

        currentPasswordView = (TextView) findViewById(R.id.currentPasswordView);
        newPasswordField = (EditText) findViewById(R.id.newPasswordField);
        verifNewPasswordField = (EditText) findViewById(R.id.verifNewPasswordField);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);

        currentPasswordView.setText(message);
    }

    public void changePasswordAction(View view) {
        String newPassword = newPasswordField.getText().toString();
        String verifNewPassword = verifNewPasswordField.getText().toString();
        if (newPassword.equals(intent.getStringExtra(AccountPageActivity.EXTRA_MESSAGE))) {
            Toast.makeText(getApplicationContext(), "New password is the same as the old password", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(verifNewPassword)) {
            Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
        } else {
            currentPasswordView.setText(newPassword);
            Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_SHORT).show();
        }
    }

}
