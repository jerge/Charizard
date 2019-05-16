package alexa.projectcharizard.ViewModel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import alexa.projectcharizard.Model.CurrentRun;
import alexa.projectcharizard.Model.Database;
import alexa.projectcharizard.R;

/**
 * The Android Activity class for the account page where the user can manage their information
 * such as username, email and password. Queries the user through dialogs for username and name,
 * starts a new activity for when changing email or password.
 *
 * @author Stefan Chan
 */

public class AccountPageActivity extends AppCompatActivity {

    // UI references
    private TextView userNameView;
    private TextView nameView;
    private TextView emailView;
    private Button signOutButton;

    // Accessing the current user information
    private CurrentRun currentRun;

    // Accessing the database to update user information
    private DatabaseReference dataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        initViews();
        initTextInViews();
        currentRun = CurrentRun.getInstance();
        dataReference = Database.getInstance().getDatabaseReference().child("Users");

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountPageActivity.this, SignInActivity.class));
            }
        });

    }
    /**
     * Initialises Android view elements
     */
    private void initViews() {
        userNameView = findViewById(R.id.userNameView);
        nameView = findViewById(R.id.nameView);
        emailView = findViewById(R.id.emailView);
        signOutButton = findViewById(R.id.signOutButton);
    }

    /**
     * Sets initial text
     */
    private void initTextInViews() {
        userNameView.setText(CurrentRun.getActiveUser().getUsername());
        nameView.setText(CurrentRun.getActiveUser().getFullName());
        emailView.setText(CurrentRun.getActiveUser().getEmail());
    }

    /**
     * Calls the account class to change the username with the argument, called
     * by showChangeUserNameDialog
     *
     * @param newUserName the new username
     */
    private void changeUserName(String newUserName) {
        dataReference.child(CurrentRun.getActiveUser().getId()).child("username").setValue(newUserName);
        //currentRun.getActiveUser().setUsername(newUserName);
        userNameView.setText(newUserName);
        // For debugging purposes
        Toast.makeText(getApplicationContext(),newUserName, Toast.LENGTH_SHORT).show();
    }

    /**
     * Calls the account class to change the name with the argument, called by
     * showChangeNameDialog
     *
     * @param newName the new name
     */
    private void changeName(String newName) {
        dataReference.child(CurrentRun.getActiveUser().getId()).child("fullname").setValue(newName);
        //currentRun.getActiveUser().setFullName(newName);
        nameView.setText(newName);
        // For debugging purposes
        Toast.makeText(getApplicationContext(), newName, Toast.LENGTH_SHORT).show();
    }

    /**
     * Spawns a dialog where the user can enter in a new username and
     * calls the method changeUserName in order to change user information
     *
     * @param c the context to show this dialog in
     */
    private void showChangeUserNameDialog(Context c) {
        final EditText newUserNameField = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Change username")
                .setMessage("Type in new username")
                .setView(newUserNameField)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUserName = newUserNameField.getText().toString();
                        changeUserName(newUserName);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /**
     * Spawns a dialog where the user can enter in a new name and
     * calls the method changeName in order to change user information
     *
     * @param c the context to shoe this dialog in
     */
    private void showChangeNameDialog(Context c) {
        final EditText newNameField = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Change name")
                .setMessage("Type in new username")
                .setView(newNameField)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = newNameField.getText().toString();
                        changeName(newName);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /**
     * @param view the view which calls this method on click
     */
    public void changeUserNameAction(View view) {
        showChangeUserNameDialog(this);
    }

    /**
     * @param view the view which calls this method on click
     */
    public void changeNameAction(View view) {
        showChangeNameDialog(this);
    }

    /**
     * Opens a new activity for changing email by creating an Intent and passing
     * the email to the newly started activity
     *
     * @param view the view which calls this method on click
     */
    public void changeEmailAction(View view) {
        Intent intent = new Intent(this, ChangeEmailActivity.class);
        intent.putExtra("UserEmail", CurrentRun.getActiveUser().getEmail());
        intent.putExtra("UserId", CurrentRun.getActiveUser().getId());
        startActivity(intent);
    }

    /**
     * Opens a new activity for changing password by creating an Intent and passing
     * the password to the newly started activity
     *
     * @param view the view which calls this method on click
     */
    public void changePasswordAction(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("UserPassword", CurrentRun.getActiveUser().getPassword());
        intent.putExtra("UserId", CurrentRun.getActiveUser().getId());
        startActivity(intent);
    }

    /**
     * @param view the view which calls this method in click
     */
    public void deleteAccountAction(View view) {
        String message = "Execute Order 66";
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

}
