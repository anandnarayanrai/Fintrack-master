package com.edge.fintrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.edge.fintrack.signup.SignUp2Activity;
import com.edge.fintrack.signup.SignUp3Activity;
import com.edge.fintrack.signup.SignUpActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Objects;

import static com.edge.fintrack.Api_Class.METHOD_NAME_CustomerLogin;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getInvestorProfile;
import static com.edge.fintrack.Api_Class.NAMESPACE;
import static com.edge.fintrack.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.Api_Class.URL_CustomerLogin;
import static com.edge.fintrack.Api_Class.URL_InvestorViewProfile;
import static com.edge.fintrack.Constant.ShowDilog;
import static com.edge.fintrack.Constant.isInternetOn;
import static com.edge.fintrack.Constant.isValidEmaillId;

public class LoginActivity extends AppCompatActivity {

    public final String TAG = "LoginActivity";
    EditText ed_login_email, ed_login_password;
    RequestQueue queue;
    SessionManager session;
    SoapPrimitive resultString;
    private ProgressDialog mProgressDialog;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);
        // making notification bar transparent
        changeStatusBarColor();
        ed_login_email = (EditText) findViewById(R.id.ed_login_email);
        //ed_login_email.setText("anandnrai@gmail.com");
        ed_login_password = (EditText) findViewById(R.id.ed_login_password);
        //ed_login_password.setText("123456789");
        queue = Volley.newRequestQueue(this);
        // progress_loader= (AVLoadingIndicatorView) findViewById(R.id.progress_loader);
        // findViewById(R.id.progress_loader).setVisibility(View.VISIBLE);
        // Session Manager
        session = new SessionManager(getApplicationContext());
    }

    public void onSignUp(View view) {
        Intent intentSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
        intentSignUp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentSignUp);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
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

    /*public void OnLogin(View view) {
        if (isValidForm()) {
            try {
                mProgressDialog = ProgressDialog.show(LoginActivity.this, null, null, true);
                mProgressDialog.setContentView(R.layout.layout_progressdialog);
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                JSONObject jsonLoginObject = new JSONObject();
                jsonLoginObject.put("Email_Id", ed_login_email.getText().toString());
                jsonLoginObject.put("Password", ed_login_password.getText().toString());
                JsonObjectRequest LoginRequest = new JsonObjectRequest(Request.Method.POST, GetCustomerLogin,
                        jsonLoginObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, response.toString());
                            String d = response.getString("d");
                            JSONArray jsonArray = new JSONArray(d);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String UserId = jsonObject.getString("UserId");
                                mProgressDialog.hide();
                                if (UserId.equalsIgnoreCase("-1")) {
                                    ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "User are not activated !! We have send an email, please activated account first.");
                                } else if (UserId.equalsIgnoreCase("-2")) {
                                    ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "Email and Password is incorrect");
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                    Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                                    //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                                    startActivity(intentMain);
                                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                    LoginActivity.this.finish();
                                    //  Toast.makeText(LoginActivity.this, "" + UserId, Toast.LENGTH_SHORT).show();

                                }
                            }

                        } catch (NullPointerException e) {
                            Log.d(TAG, "Error.Response:- " + e.getMessage());
                            ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            mProgressDialog.hide();
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            Log.e(TAG, "Site Info Error: " + error.getMessage());
                            ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                        } catch (NullPointerException e) {
                            Log.d(TAG, "Error.Response:- " + e.getMessage());
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json; charset=utf-8");
                        params.put("Accept", "application/json");
                        return params;
                    }

                    *//*@Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Email_Id", "anandnrai@gmail.com");
                        params.put("Password", "123456789");
                        return params;
                    }*//*
                };
                LoginRequest.setRetryPolicy(
                        new DefaultRetryPolicy(5000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                );

                queue.add(LoginRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InflateException e) {
                e.printStackTrace();
            }
        }
    }*/


    public void OnLogin(View view) {
        if (isValidForm()) {
            LogingCallWS task = new LogingCallWS();
            task.execute();
        }
    }

    private boolean isValidForm() {   // check the edit text are empty and the spinner are select or not
        if (ed_login_email.getText().toString().isEmpty()) {
            ed_login_email.requestFocus();
            ed_login_email.setError(getString(R.string.email_field_required));
            return false;
        } else if (!isValidEmaillId(ed_login_email.getText().toString())) {
            ed_login_email.requestFocus();
            ed_login_email.setError(getString(R.string.email_field_valid));
            return false;
        } else if (ed_login_password.getText().toString().isEmpty()) {
            ed_login_password.requestFocus();
            ed_login_password.setError(getString(R.string.password_field_required));
            return false;
        } else if (!isInternetOn(LoginActivity.this)) {
            ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast toast = Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT);
        View view = toast.getView();
        toast.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void Onforgot(View view) {
        /*Intent intentMain = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMain);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/

        Intent intentWebView = new Intent(LoginActivity.this, WebViewActivity.class);
        intentWebView.putExtra("url", "http://fintrackindia.com/forgotPassword.aspx");
        intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentWebView);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    private class LogingCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(LoginActivity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_CustomerLogin);
                Request.addProperty("Email_Id", ed_login_email.getText().toString());
                Request.addProperty("Password", ed_login_password.getText().toString());

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_CustomerLogin);

                transport.call(SOAP_ACTION + METHOD_NAME_CustomerLogin, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "CustomerLogin Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "CustomerLogin Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //mProgressDialog.dismiss();
            try {
                Log.d(TAG, resultString.toString());
                JSONArray jsonArray = new JSONArray(resultString.toString());
                Log.i(TAG, "CustomerLogin Result : " + resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String UserId = jsonObject.getString("UserId");
                    //mProgressDialog.hide();
                    if (UserId.equalsIgnoreCase("-2")) {
                        mProgressDialog.dismiss();
                        ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "Email and Password is incorrect");
                    } else if (UserId.equalsIgnoreCase("-3")) {
                        mProgressDialog.dismiss();
                        ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "User Not Register");
                    } else if (UserId.equalsIgnoreCase("-1")) {
                        /*[{"UserId":"-1","RegID":"20181119112453"}]*/
                        mProgressDialog.dismiss();
                        Intent intentMain = new Intent(LoginActivity.this, SignUp3Activity.class);
                        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentMain.putExtra("RegistrationId", jsonObject.getString("RegID"));
                        intentMain.putExtra("EmailId", ed_login_email.getText().toString());
                        startActivity(intentMain);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    } else if (UserId.equalsIgnoreCase("-4")) {
                        mProgressDialog.dismiss();
                        Intent intentMain = new Intent(LoginActivity.this, SignUp2Activity.class);
                        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentMain.putExtra("RegistrationId", jsonObject.getString("RegID"));
                        startActivity(intentMain);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    } else {

                        UserprofilegCallWS userprofilegCallWS = new UserprofilegCallWS(UserId);
                        userprofilegCallWS.execute();

                       /* Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                        //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intentMain);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        LoginActivity.this.finish();*/
                        //  Toast.makeText(LoginActivity.this, "" + UserId, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NullPointerException e) {
                mProgressDialog.dismiss();
                Log.d(TAG, "Error.Response:- " + e.getMessage());
                ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class UserprofilegCallWS extends AsyncTask<Void, Void, Void> {
        String UserId;

        UserprofilegCallWS(String UserId) {
            this.UserId = UserId;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getInvestorProfile);
                Request.addProperty("Registration_Id", UserId);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorViewProfile);

                transport.call(SOAP_ACTION + METHOD_NAME_getInvestorProfile, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "InvestorProfileAddUpdateBind_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "InvestorProfileAddUpdateBind Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            try {

                /**
                 * [{"Registration_Id":"FIN2018717753833","Customer_Name":"Anand Rai","Gender":"M","DOB":"17/07/2018",
                 "PanCard_No":"GJHGJHG23f","PanKYC_Status":"","Occupation":"","Annual_Income":"","Country_Permanent":"",
                 "Fathers_Name":"","Mothers_Name":"","Communication_Address":"","Communication_AddressLine2":"","Country_Communication":"",
                 "State_Communication":"","City_Communication":"","Mobile_No":"9569702714","Email_Id":"anandnrai@gmail.com","Zip_Communication":"",
                 "Telephone_Communication":"","Cust_Bankname":"","Cust_AccountType":"","Cust_HoldingMode":"","Cust_AccountNumber":"","Cust_IFSCCode":"","Cust_BankCity":"","Cust_BranchAddress":""}] */

                JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Log.d(TAG, "Registration_Id Result " + jsonObject.getString("Registration_Id"));
                    /*(String Registration_Id,String name,String Gender,String DOB,String PanCard_No,String PanKYC_Status,
                                   String Occupation,String Annual_Income,String Fathers_Name,String Mothers_Name,String Mobile_No,String Email_Id)*/
                    session.createUserInfoSession(
                            jsonObject.getString("Registration_Id"),
                            jsonObject.getString("Customer_Name"),
                            jsonObject.getString("Gender"),
                            jsonObject.getString("DOB"),
                            jsonObject.getString("PanCard_No"),
                            jsonObject.getString("PanKYC_Status"),
                            jsonObject.getString("Occupation"),
                            jsonObject.getString("Annual_Income"),
                            jsonObject.getString("Fathers_Name"),
                            jsonObject.getString("Mothers_Name"),
                            jsonObject.getString("Mobile_No"),
                            jsonObject.getString("Email_Id"));

                  /*(String Country_Permanent, String Communication_Address,String Communication_AddressLine2,String Country_Communication,
                                   String State_Communication,String City_Communication,String Zip_Communication)*/

                    session.createUserCommunicationSession(jsonObject.getString("Country_Permanent"),
                            jsonObject.getString("Communication_Address"),
                            jsonObject.getString("Communication_AddressLine2"),
                            jsonObject.getString("Country_Communication"),
                            jsonObject.getString("State_Communication"),
                            jsonObject.getString("City_Communication"),
                            jsonObject.getString("Zip_Communication"));

                  /*createUserBankInfoSession(String Cust_Bankname, String Cust_AccountType, String Cust_HoldingMode, String Cust_AccountNumber, String Cust_IFSCCode,
                                   String Cust_BranchAddress)*/

                    session.createUserBankInfoSession(jsonObject.getString("Cust_Bankname"),
                            jsonObject.getString("Cust_AccountType"),
                            jsonObject.getString("Cust_HoldingMode"),
                            jsonObject.getString("Cust_AccountNumber"),
                            jsonObject.getString("Cust_IFSCCode"),
                            jsonObject.getString("Cust_BranchAddress"));

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    LoginActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    LoginActivity.this.finish();
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "CustomerProfile Error:- " + e.getMessage());
                ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

       /*StringRequest postRequest = new StringRequest(Request.Method.POST, GetCustomerLogin,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // session.createLoginSession("fintrack", "fintrack@gmail.com");
                                mProgressDialog.hide();
                                // response
                                Log.d(TAG, "Get_City Response:- " + response);
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                                //intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                                startActivity(intentMain);
                                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                LoginActivity.this.finish();
                            } catch (NullPointerException e) {
                                Log.d(TAG, "Response:- " + e.getMessage());
                                ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
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
                                ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                            } catch (NullPointerException e) {
                                Log.d(TAG, "Error.Response:- " + e.getMessage());
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Email_Id", "anandnrai@gmail.com");
                    params.put("Password", "123456789");
                    return params;
                }

            };
            queue.add(postRequest);*/

}
