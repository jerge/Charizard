package alexa.projectcharizard.ViewModel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import alexa.projectcharizard.R;

public class AccountPageActivity extends AppCompatActivity {

    // public constant key
    public static final String EXTRA_MESSAGE = "alexa.projectcharizard.ViewModel.MESSAGE";

    // UI references
    private TextView userNameView;
    private TextView nameView;
    private ImageView emailView;
    private ImageView passwordView;
    private ImageView deleteView;

    // Hardcoded user informationA
    // TODO: Replace these with user information from userAccount
    private String userName = "Inte_Semlan_420";
    private String name = "Mathias Bammers";
    private String email = "420@swag.it";
    // TODO: Replace with something that is not a primitive type
    private String password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        userNameView = (TextView) findViewById(R.id.userNameView);
        nameView = (TextView) findViewById(R.id.nameView);
        emailView = (ImageView) findViewById(R.id.emailView);
        passwordView = (ImageView) findViewById(R.id.passwordView);
        deleteView = (ImageView) findViewById(R.id.deleteView);

        userNameView.setText(userName);
        nameView.setText(name);
    }

    private void changeUserName(String newUserName) {
        // TODO: Add call to change username in backend, preferably before setting userNameView
        this.userName = newUserName;
        userNameView.setText(newUserName);
        // For debugging purposes
        Toast.makeText(getApplicationContext(),newUserName, Toast.LENGTH_SHORT).show();
    }

    private void changeName(String newName) {
        // TODO: Add call to change name in backend, preferably before setting nameView
        this.name = newName;
        nameView.setText(newName);
        // For debugging purposes
        Toast.makeText(getApplicationContext(), newName, Toast.LENGTH_SHORT).show();
    }

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

    public void changeUserNameAction(View view) {
        showChangeUserNameDialog(this);
    }

    public void changeNameAction(View view) {
        showChangeNameDialog(this);
    }

    public void changeEmailAction(View view) {
        Intent intent = new Intent(this, ChangeEmailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, email);
        startActivity(intent);
    }

    public void changePasswordAction(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra(EXTRA_MESSAGE, password);
        startActivity(intent);
    }

    public void deleteAccountAction(View view) {
        String message = "Execute Order 66";
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

}
