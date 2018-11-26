package com.edge.fintrack;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static com.edge.fintrack.Constant.ShowDilog;
import static com.edge.fintrack.Constant.isInternetOn;
import static com.edge.fintrack.Constant.isValidEmaillId;

public class ForgotPasswordActivity extends AppCompatActivity {

    public final String TAG = "ForgotPasswordActivity";
    EditText ed_forgot_email;
    RequestQueue queue;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_forgot_password);
        // making notification bar transparent
        changeStatusBarColor();
        queue = Volley.newRequestQueue(this);
        ed_forgot_email = (EditText) findViewById(R.id.ed_forgot_email);
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /*public void OnForgot(View view) {
        if (isValidForm()) {
            mProgressDialog = ProgressDialog.show(ForgotPasswordActivity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            StringRequest postRequest = new StringRequest(Request.Method.POST, Get_City,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // session.createLoginSession("Android Hive", "anroidhive@gmail.com");
                                mProgressDialog.hide();
                                // response
                                Log.d(TAG, "Get_City Response:- " + response);
                            } catch (NullPointerException e) {
                                Log.d(TAG, "Response:- " + e.getMessage());
                                ShowDilog(ForgotPasswordActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                mProgressDialog.hide();
                                // findViewById(R.id.progress_loader).setVisibility(View.GONE);
                                // error
                                Log.d(TAG, "Error.Response:- " + error.getMessage());
                                ShowDilog(ForgotPasswordActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                            } catch (NullPointerException e) {
                                Log.d(TAG, "Error.Response:- " + e.getMessage());
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", "20");
                    return params;
                }
            };
            queue.add(postRequest);
        }

    }*/

    private boolean isValidForm() {   // check the edit text are empty and the spinner are select or not
        if (ed_forgot_email.getText().toString().isEmpty()) {
            ed_forgot_email.requestFocus();
            ed_forgot_email.setError(getString(R.string.email_field_required));
            return false;
        } else if (!isValidEmaillId(ed_forgot_email.getText().toString())) {
            ed_forgot_email.requestFocus();
            ed_forgot_email.setError(getString(R.string.email_field_valid));
            return false;
        } else if (!isInternetOn(ForgotPasswordActivity.this)) {
            ShowDilog(ForgotPasswordActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

}
