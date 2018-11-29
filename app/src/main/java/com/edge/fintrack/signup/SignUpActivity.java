package com.edge.fintrack.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.edge.fintrack.R;
import com.edge.fintrack.SessionManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Objects;

import static com.edge.fintrack.utility.Api_Class.METHOD_NAME_GetUser_AlreadyRegisterdEmail;
import static com.edge.fintrack.utility.Api_Class.METHOD_NAME_GetUser_AlreadyRegisterdMobile;
import static com.edge.fintrack.utility.Api_Class.METHOD_NAME_OpenCustomerAccountFirstStep;
import static com.edge.fintrack.utility.Api_Class.NAMESPACE;
import static com.edge.fintrack.utility.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.utility.Api_Class.URL_CheckAvailabilityEmailMobile;
import static com.edge.fintrack.utility.Api_Class.URL_OpenCustomerAccountFirstStep;
import static com.edge.fintrack.utility.Constant.ShowDilog;
import static com.edge.fintrack.utility.Constant.isInternetOn;
import static com.edge.fintrack.utility.Constant.isValidEmaillId;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public final String TAG = "SignUpActivity";
    EditText et_sname, et_semail, et_smobile, ed_login_password, ed_partnerCode;
    RequestQueue queue;
    SessionManager session;
    SoapPrimitive resultString;
    CheckBox ck_tram;
    private ProgressDialog mProgressDialog;
    Spinner spinner_Prefixn;
    private String IntrestedIn = "All";
    private boolean isAlreadyRegisterdEmail;
    private boolean isAlreadyRegisterdMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_sign_up);
        // making notification bar transparent
        changeStatusBarColor();
        queue = Volley.newRequestQueue(this);
        et_sname = (EditText) findViewById(R.id.et_sname);
        et_semail = (EditText) findViewById(R.id.et_semail);
        et_smobile = (EditText) findViewById(R.id.et_smobile);
        ed_login_password = (EditText) findViewById(R.id.ed_login_password);
        ed_partnerCode = (EditText) findViewById(R.id.ed_partnerCode);
        session = new SessionManager(getApplicationContext());
        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollview));
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        ck_tram = (CheckBox) findViewById(R.id.ck_tram);
        TextView tv_by_continuing = (TextView) findViewById(R.id.tv_by_continuing_str);
        tv_by_continuing.setText(Html.fromHtml(getString(R.string.privacy_term)));
        tv_by_continuing.setMovementMethod(LinkMovementMethod.getInstance());

        spinner_Prefixn = (Spinner) findViewById(R.id.spinner_Prefixn);
        spinner_Prefixn.setOnItemSelectedListener(this);

        et_semail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (!isValidEmaillId(et_semail.getText().toString())) {
                        et_semail.requestFocus();
                        et_semail.setError(getString(R.string.email_field_valid));
                    } else {
                        CheckAvailabilityCallWS callWS = new CheckAvailabilityCallWS(METHOD_NAME_GetUser_AlreadyRegisterdEmail, "Email_Id", et_semail.getText().toString());
                        callWS.execute();
                    }
                }
            }
        });

        et_smobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 9) {
                    CheckAvailabilityCallWS callWS = new CheckAvailabilityCallWS(METHOD_NAME_GetUser_AlreadyRegisterdMobile, "mobile", et_smobile.getText().toString());
                    callWS.execute();
                } else {
                    et_smobile.requestFocus();
                    et_smobile.setError(getString(R.string.email_field_valid));
                }
            }
        });
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

    public void onSignUp(View view) {
        if (isValidForm()) {
            OpenCustomerAccountCallWS task = new OpenCustomerAccountCallWS();
            task.execute();
        }

       /* Intent intentMain = new Intent(SignUpActivity.this, SignUp3Activity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentMain.putExtra("RegistrationId", "446446416161");
        intentMain.putExtra("EmailId", et_semail.getText().toString());
        startActivity(intentMain);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    private boolean isValidForm() {   // check the edit text are empty and the spinner are select or not
        if (et_sname.getText().toString().isEmpty()) {
            et_sname.requestFocus();
            et_sname.setError(getString(R.string.name_field_required));
            return false;
        } else if (et_semail.getText().toString().isEmpty()) {
            et_semail.requestFocus();
            et_semail.setError(getString(R.string.email_field_required));
            return false;
        } else if (!isValidEmaillId(et_semail.getText().toString())) {
            et_semail.requestFocus();
            et_semail.setError(getString(R.string.email_field_valid));
            return false;
        } else if (isAlreadyRegisterdEmail) {
            et_semail.requestFocus();
            et_semail.setError(getString(R.string.email_AlreadyReg));
            return false;
        } else if (et_smobile.getText().toString().isEmpty()) {
            et_smobile.requestFocus();
            et_smobile.setError(getString(R.string.name_field_required));
            return false;
        } else if (isAlreadyRegisterdMobile) {
            et_smobile.requestFocus();
            et_smobile.setError(getString(R.string.mobile_AlreadyReg));
            return false;
        } else if (ed_login_password.getText().toString().isEmpty()) {
            ed_login_password.requestFocus();
            ed_login_password.setError(getString(R.string.password_field_required));
            return false;
        } else if (!ck_tram.isChecked()) {
            Toast.makeText(this, "Please Check Terms and Conditions", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isInternetOn(SignUpActivity.this)) {
            ShowDilog(SignUpActivity.this, "" + getString(R.string.Internet_not_found), "" + getString(R.string.Internet_not_found));
            return false;
        } else {
            return true;
        }
    }

    public void onLogin(View view) {
        onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_Prefixn:
                // ResidentialStatus = ResidentialStatus_id.get(spinner_ResidentialStatus.getItemAtPosition(position).toString());
                IntrestedIn = spinner_Prefixn.getItemAtPosition(position).toString();
                // Toast.makeText(this, "" + spinner_Prefixn.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class CheckAvailabilityCallWS extends AsyncTask<Void, Void, Void> {
        private String METHOD_NAME;
        private String RequestKey;
        private String RequestVelue;

        public CheckAvailabilityCallWS(String METHOD_NAME, String requestKey, String requestVelue) {
            this.METHOD_NAME = METHOD_NAME;
            this.RequestKey = requestKey;
            this.RequestVelue = requestVelue;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty(RequestKey, RequestVelue);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_CheckAvailabilityEmailMobile);

                transport.call(SOAP_ACTION + METHOD_NAME, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, METHOD_NAME + " Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, METHOD_NAME + " Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                //mProgressDialog.dismiss();
                Log.d(TAG, "CheckAvailabilityCallWS onPostExecute Response:- " + resultString.toString());
                if (RequestKey.equalsIgnoreCase("Email_Id")) {
                    isAlreadyRegisterdEmail = Boolean.parseBoolean(resultString.toString());
                    if (isAlreadyRegisterdEmail)
                        et_semail.setError(getString(R.string.email_AlreadyReg));
                } else {
                    isAlreadyRegisterdMobile = Boolean.parseBoolean(resultString.toString());
                    if (isAlreadyRegisterdMobile)
                        et_smobile.setError(getString(R.string.mobile_AlreadyReg));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }

    private class OpenCustomerAccountCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(SignUpActivity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_OpenCustomerAccountFirstStep);
                Request.addProperty("RegistrationId", "");
                Request.addProperty("CustomerName", et_sname.getText().toString());
                Request.addProperty("MobileNo", et_smobile.getText().toString());
                Request.addProperty("EmailId", et_semail.getText().toString());
                Request.addProperty("Password", ed_login_password.getText().toString());
                Request.addProperty("IntrestedIn", IntrestedIn);
                Request.addProperty("PartnerCode", ed_partnerCode.getText().toString());
                Request.addProperty("Entry_Plateform", "AndroidApplication");
                Request.addProperty("PageName", "OpenCustomerAccountRegistration");

                Log.e(TAG, "OpenCustomerAccountCallWS  SoapObject Request " + Request.toString());

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_OpenCustomerAccountFirstStep);

                transport.call(SOAP_ACTION + METHOD_NAME_OpenCustomerAccountFirstStep, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "Result OpenCustomerAccountFirstStep: " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, resultString.toString());
                mProgressDialog.dismiss();
                if (resultString.toString().equalsIgnoreCase("Invalid")) {
                    Log.d(TAG, "Error.Response:- " + resultString.toString());
                    ShowDilog(SignUpActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                } else {
                    Intent intentMain = new Intent(SignUpActivity.this, SignUp3Activity.class);
                    intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentMain.putExtra("RegistrationId", resultString.toString());
                    intentMain.putExtra("EmailId", et_semail.getText().toString());
                    startActivity(intentMain);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    SignUpActivity.this.finish();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

         /*   try {
                JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String UserId = jsonObject.getString("UserId");
                    mProgressDialog.hide();
                    if (UserId.equalsIgnoreCase("-1")) {
                        ShowDilog(SignUpActivity.this, "" + getString(R.string.app_name), "User are not activated !! We have send an email, please activated account first.");
                    } else if (UserId.equalsIgnoreCase("-2")) {
                        ShowDilog(SignUpActivity.this, "" + getString(R.string.app_name), "Email and Password is incorrect");
                    } else {
                        Toast.makeText(SignUpActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intentMain = new Intent(SignUpActivity.this, MainActivity.class);
                        //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intentMain);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        SignUpActivity.this.finish();
                        //  Toast.makeText(LoginActivity.this, "" + UserId, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "Error.Response:- " + e.getMessage());
                ShowDilog(SignUpActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }

    }

}
