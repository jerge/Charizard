package alexa.projectcharizard.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    public void changeUserNameAction(View view) {
        String message = "Trigger works for username";
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    public void changeNameAction(View view) {
        String message = "Trigger works for name";
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    public void changeEmailAction(View view) {
        Intent intent = new Intent(this, ChangeEmailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, email);
        startActivity(intent);
    }

    public void changePasswordAction(View view) {
        String message = "Trigger works for password";
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    public void deleteAccountAction(View view) {
        String message = "Trigger works for deleting account";
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

}
