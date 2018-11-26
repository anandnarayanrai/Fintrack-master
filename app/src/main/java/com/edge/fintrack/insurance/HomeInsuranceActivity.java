package com.edge.fintrack.insurance;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.edge.fintrack.R;
import com.edge.fintrack.SessionManager;

import java.util.HashMap;

public class HomeInsuranceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public final String TAG = "HelathInsuranceActivity";
    Spinner sp_policy_type;
    ImageView iv_menu_item;
    // Session Manager Class
    SessionManager session;
    String sp_policy_item = "Structure & Contents";

    LinearLayout layout_structure, layout_contents;
    EditText et_mobile, et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_home_insurance);

        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Session class instance
        session = new SessionManager(HomeInsuranceActivity.this);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        Log.d(TAG, "Registration_Id " + user.get(SessionManager.KEY_Registration_Id));
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_mobile.setText(user.get(SessionManager.KEY_Mobile_No));
        et_email = (EditText) findViewById(R.id.et_email);
        et_email.setText(user.get(SessionManager.KEY_EMAIL));

        sp_policy_type = (Spinner) findViewById(R.id.sp_policy_type);
        sp_policy_type.setOnItemSelectedListener(this);
        layout_structure = (LinearLayout) findViewById(R.id.layout_structure);
        layout_contents = (LinearLayout) findViewById(R.id.layout_contents);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_policy_type:
                sp_policy_item = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "sp_policy_item" + sp_policy_item);
                if (sp_policy_item.equalsIgnoreCase("Structure")) {
                    layout_structure.setVisibility(View.VISIBLE);
                    layout_contents.setVisibility(View.GONE);
                } else if (sp_policy_item.equalsIgnoreCase("Contents")) {
                    layout_structure.setVisibility(View.GONE);
                    layout_contents.setVisibility(View.VISIBLE);
                } else {
                    layout_structure.setVisibility(View.VISIBLE);
                    layout_contents.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}
