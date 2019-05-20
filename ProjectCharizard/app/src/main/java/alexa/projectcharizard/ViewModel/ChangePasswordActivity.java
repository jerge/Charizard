package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

/**
 * The Android Activity class for changing the user password. Started by AccountPageActivity
 *
 * @author Stefan Chan
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private EditText newPasswordField;
    private EditText verifNewPasswordField;

    private Intent intent;
    private DatabaseReference dataReference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();

        userId = CurrentRun.getActiveUser().getId();

        intent = getIntent();
        dataReference = Database.getInstance().getDatabaseReference().child("Users")
                                .child(userId);

    }

    /**
     * Initialises Android view elements
     */
    private void initView() {
        newPasswordField = findViewById(R.id.newPasswordField);
        verifNewPasswordField = findViewById(R.id.verifNewPasswordField);
    }

    /**
     * Checks so that the new password is not the same as the old password and
     * that the passwords entered in newPasswordField and verifNewPasswordField
     * match. If checks pass then the user password from backend
     *
     * @param view the view which calls this method on click
     */
    public void changePasswordAction(View view) {
        String newPassword = newPasswordField.getText().toString();
        String verifNewPassword = verifNewPasswordField.getText().toString();
        String currentPassword = CurrentRun.getActiveUser().getPassword();

        if (newPassword.equals(currentPassword)) {
            Toast.makeText(getApplicationContext(), "New password can not be the same as the old password", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(verifNewPassword)) {
            Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
        } else {
            dataReference.child("password").setValue(newPassword);
            CurrentRun.getActiveUser().setPassword(newPassword);
            Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
