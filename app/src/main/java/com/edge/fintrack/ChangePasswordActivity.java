package com.edge.fintrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Objects;

import static com.edge.fintrack.Api_Class.METHOD_NAME_InvestorProfileChangeOldPassword;
import static com.edge.fintrack.Api_Class.METHOD_NAME_InvestorProfileOldPasswordBind;
import static com.edge.fintrack.Api_Class.NAMESPACE;
import static com.edge.fintrack.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.Api_Class.URL_InvestorProfileChangePassword;
import static com.edge.fintrack.Constant.ShowDilog;
import static com.edge.fintrack.Constant.isInternetOn;

public class ChangePasswordActivity extends AppCompatActivity {

    public final String TAG = "ChangePasswordActivity";

    TextView tv_layout_header;
    SoapPrimitive resultString;
    EditText ed_oldpassword, ed_newpassword, ed_conpassword;
    Button bt_updatePass;
    String oldpassword, Registration_Id;
    // Session Manager Class
    SessionManager session;
    private ProgressDialog mProgressDialog;

    /*[{"UserId":"FIN2018717753833"}]*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_change_password);
        tv_layout_header = (TextView) findViewById(R.id.tv_layout_header);
        tv_layout_header.setText("Change Password");

        ed_oldpassword = (EditText) findViewById(R.id.ed_oldpassword);
        ed_newpassword = (EditText) findViewById(R.id.ed_newpassword);
        ed_conpassword = (EditText) findViewById(R.id.ed_conpassword);

        bt_updatePass = (Button) findViewById(R.id.bt_updatePass);

        bt_updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidForm()) {
                    ChangePassword task = new ChangePassword();
                    task.execute();
                }
            }
        });
        getpaswordCallWS task = new getpaswordCallWS();
        task.execute();
        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        Registration_Id = user.get(SessionManager.KEY_Registration_Id);
    }

    private boolean isValidForm() {   // check the edit text are empty and the spinner are select or not
        if (ed_oldpassword.getText().toString().isEmpty()) {
            ed_oldpassword.requestFocus();
            ed_oldpassword.setError(getString(R.string.password_field_required));
            return false;
        } else if (!ed_oldpassword.getText().toString().equalsIgnoreCase(oldpassword)) {
            ed_oldpassword.requestFocus();
            ed_oldpassword.setError(getString(R.string.old_password_field_match));
            return false;
        } else if (ed_newpassword.getText().toString().isEmpty()) {
            ed_newpassword.requestFocus();
            ed_newpassword.setError(getString(R.string.password_field_required));
            return false;
        } else if (ed_conpassword.getText().toString().isEmpty()) {
            ed_conpassword.requestFocus();
            ed_conpassword.setError(getString(R.string.password_field_required));
            return false;
        } else if (!ed_conpassword.getText().toString().equalsIgnoreCase(ed_newpassword.getText().toString())) {
            ed_conpassword.requestFocus();
            ed_conpassword.setError(getString(R.string.password_field_match));
            return false;
        } else if (!isInternetOn(ChangePasswordActivity.this)) {
            ShowDilog(ChangePasswordActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
            return false;
        } else {
            return true;
        }
    }

    public void Onforgot(View view) {
       /* Intent intentMain = new Intent(ChangePasswordActivity.this, ForgotPasswordActivity.class);
        //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intentMain);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/

        Intent intentWebView = new Intent(ChangePasswordActivity.this, WebViewActivity.class);
        intentWebView.putExtra("url", "http://fintrackindia.com/forgotPassword.aspx");
        intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentWebView);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    private class getpaswordCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_InvestorProfileOldPasswordBind);
                Request.addProperty("Registration_Id", Registration_Id);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorProfileChangePassword);

                transport.call(SOAP_ACTION + METHOD_NAME_InvestorProfileOldPasswordBind, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "InvestorProfileOldPasswordBind_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "InvestorProfileOldPasswordBind Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "InvestorProfileOldPasswordBind Result " + resultString.toString());
                JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    oldpassword = jsonObject.getString("Password");

                }
            } catch (NullPointerException e) {
                Log.d(TAG, "CustomerProfile Error:- " + e.getMessage());
                ShowDilog(ChangePasswordActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ChangePassword extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(ChangePasswordActivity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_InvestorProfileChangeOldPassword);
                Request.addProperty("Registration_Id", Registration_Id);
                Request.addProperty("Password", ed_conpassword.getText().toString());

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorProfileChangePassword);

                transport.call(SOAP_ACTION + METHOD_NAME_InvestorProfileChangeOldPassword, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "InvestorProfileChangeOldPassword Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "InvestorProfileChangeOldPassword Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            try {
                Log.d(TAG, "InvestorProfileChangeOldPassword " + resultString.toString());
                if (resultString.toString().equalsIgnoreCase("success")) {
                    oldpassword = ed_conpassword.getText().toString();
                    ed_oldpassword.setText("");
                    ed_newpassword.setText("");
                    ed_conpassword.setText("");
                    ShowDilog(ChangePasswordActivity.this, "" + getString(R.string.app_name), "Password Update Successfully");
                } else {
                    ShowDilog(ChangePasswordActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "Error.Response:- " + e.getMessage());
                ShowDilog(ChangePasswordActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }
}
