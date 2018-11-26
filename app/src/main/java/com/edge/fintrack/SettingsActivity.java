package com.edge.fintrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_changePass, tv_aboutus, tv_privacy_policy, tv_logout, tv_layout_header;
    Intent browserIntent;

    // Session Manager Class
    SessionManager session;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_settings);

        tv_layout_header = (TextView) findViewById(R.id.tv_layout_header);
        tv_layout_header.setText(getResources().getString(R.string.action_settings));

        tv_changePass = (TextView) findViewById(R.id.tv_changePass);
        tv_changePass.setOnClickListener(this);

        tv_privacy_policy = (TextView) findViewById(R.id.tv_privacy_policy);
        tv_privacy_policy.setOnClickListener(this);

        tv_aboutus = (TextView) findViewById(R.id.tv_aboutus);
        tv_aboutus.setOnClickListener(this);

        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(this);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        ((TextView) findViewById(R.id.sUserFullname)).setText(user.get(SessionManager.KEY_NAME));
        // Mobile_No
        ((TextView) findViewById(R.id.sUserPhone)).setText(user.get(SessionManager.KEY_Mobile_No));
        // email
        ((TextView) findViewById(R.id.semail)).setText(user.get(SessionManager.KEY_EMAIL));

    }

    @Override
    public void onClick(View v) {
        Intent intentWebView = new Intent(SettingsActivity.this, WebViewActivity.class);
        switch (v.getId()) {
            case R.id.tv_changePass:
                Intent intentUpdateProfile = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_aboutus:
                intentWebView.putExtra("url", "http://fintrackindia.com/about-us.html");
                // intentWebView.putExtra("url", "https://www.google.com/");
                startActivity(intentWebView);
                /*browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://fintrackindia.com/about-us.html"));
                startActivity(browserIntent);*/
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_privacy_policy:
                intentWebView.putExtra("url", "http://fintrackindia.com/privacy.html");
                //intentWebView.putExtra("url", "https://www.google.com/");
                startActivity(intentWebView);
                /*browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://fintrackindia.com/privacy.html"));
                startActivity(browserIntent);*/
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_logout:
                OnLogoutDilog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void OnLogoutDilog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle(getString(R.string.app_name));
        dialog.setMessage("Are you sure for logout Edge Fintrack ?");
        dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
                alert.dismiss();
            }
        });
        dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                session.logoutUser();
                SettingsActivity.this.finish();
            }
        });
        alert = dialog.create();
        alert.show();
    }

}