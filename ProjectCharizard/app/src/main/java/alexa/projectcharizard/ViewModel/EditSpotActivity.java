package alexa.projectcharizard.ViewModel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import alexa.projectcharizard.R;

public class EditSpotActivity extends AppCompatActivity {

    private TextView editSpotNameView;
    private TextView editSpotLatView;
    private TextView editSpotLongView;

    private EditText editSpotDescText;

    private Spinner editSpotCatSpinner;

    private Switch editSpotVisSwitch;

    private Button editSpotSaveButton;

    private String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        editSpotNameView = findViewById(R.id.editSpotNameView);
        editSpotLatView = findViewById(R.id.editSpotLatView);
        editSpotLongView = findViewById(R.id.editSpotLongView);
        editSpotDescText = findViewById(R.id.editSpotDescText);
        editSpotCatSpinner = findViewById(R.id.editSpotCatSpinner);
        editSpotVisSwitch = findViewById(R.id.editSpotVisSwitch);
        editSpotSaveButton = findViewById(R.id.editSpotSaveButton);

        editSpotCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    currentCategory = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editSpotCatSpinner.setSelection(0);
            }
        });

        setContentView(R.layout.activity_edit_spot);
    }

}
