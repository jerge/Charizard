package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import alexa.projectcharizard.R;

public class AccountPageActivity extends Activity {

    // Account model
    // private Account userAccount;

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

}
