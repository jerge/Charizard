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
    private TextView deleteAccountView;
    private Button signOutButton;

    // Accessing the database to update user information
    private DatabaseReference dataReference;
    private Database database = Database.getInstance();;

    Boolean deleteUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        initViews();
        initTextInViews();
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
        deleteAccountView = findViewById(R.id.deleteAccountText);
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
        userNameView.setText(newUserName);
    }

    /**
     * Calls the account class to change the name with the argument, called by
     * showChangeNameDialog
     *
     * @param newName the new name
     */
    private void changeName(String newName) {
        dataReference.child(CurrentRun.getActiveUser().getId()).child("fullname").setValue(newName);
        nameView.setText(newName);
    }

    /**
     * Spawns a dialog where the user can enter in a new username and
     * calls the method changeUserName in order to change user information
     */
    private void showChangeUserNameDialog() {
        final EditText newUserNameField = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setView(newUserNameField);
        builder.setTitle("Change username");
        builder.setMessage("Type in new name");
        builder.setCancelable(false);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUserName = newUserNameField.getText().toString();
                changeUserName(newUserName);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    /**
     * Spawns a dialog where the user can enter in a new name and
     * calls the method changeName in order to change user information
     */
    private void showChangeNameDialog() {
        final EditText newNameField = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setView(newNameField);
        builder.setTitle("Change name");
        builder.setMessage("Type in new name");
        builder.setCancelable(false);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = newNameField.getText().toString();
                changeName(newName);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    /**
     * @param view the view which calls this method on click
     */
    public void changeUserNameAction(View view) {
        showChangeUserNameDialog();
    }

    /**
     * @param view the view which calls this method on click
     */
    public void changeNameAction(View view) {
        showChangeNameDialog();
    }

    /**
     * Opens a new activity for changing email by creating an Intent and passing
     * the email to the newly started activity
     *
     * @param view the view which calls this method on click
     */
    public void changeEmailAction(View view) {
        Intent intent = new Intent(this, ChangeEmailActivity.class);
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
        startActivity(intent);
    }

    /**
     * Method called when the user presses delete account button/text. Starts with asking for a
     * confirmation from user.
     * @param view the view which calls this method in click
     */
    public void deleteAccountAction(View view) {

        confirmDialog();

        if (deleteUser){
            database.deleteUser(CurrentRun.getActiveUser().getId());
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Your account has been deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A dialog box that shows up when the user clicks "Delete user" button, asking the user
     * to confirm the choice.
     * @return True if the user says yes, otherwise no
     */
    private Boolean confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle("Delete account");
        builder.setMessage("You are about to delete your account. Do you want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User chose yes, returns the user to the log in page
                deleteUser = true;
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User chose no, returning to account page
                deleteUser = false;
            }
        });

        builder.show();
        return deleteUser;
    }

}