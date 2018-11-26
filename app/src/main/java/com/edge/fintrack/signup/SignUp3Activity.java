package com.edge.fintrack.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.edge.fintrack.R;
import com.goodiebag.pinview.Pinview;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Objects;

import static com.edge.fintrack.Api_Class.METHOD_NAME_CValidateOTP;
import static com.edge.fintrack.Api_Class.METHOD_NAME_RegenerateOTPClient;
import static com.edge.fintrack.Api_Class.NAMESPACE;
import static com.edge.fintrack.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.Api_Class.URL_ValidateOTP;
import static com.edge.fintrack.Constant.ShowDilog;

public class SignUp3Activity extends AppCompatActivity {
    public final String TAG = SignUp3Activity.class.getSimpleName();
    SoapPrimitive resultString;
    Pinview pin;
    private String RegistrationId = "";
    private TextView tv_main2;
    String EmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_sign_up3);

        Bundle bundle = getIntent().getExtras();
        //20181119112453
        if (bundle == null) {
            RegistrationId = null;
            EmailId = null;
        } else {
            RegistrationId = bundle.getString("RegistrationId");
            EmailId = bundle.getString("EmailId");
            Log.i(TAG, "RegistrationId= " + RegistrationId + " , EmailId= " + EmailId);
        }

        pin = (Pinview) findViewById(R.id.pinview);
        tv_main2 = (TextView) findViewById(R.id.tv_main2);
        tv_main2.setText("We have sent an OTP an your email \n" + EmailId);
        /*pin.setPinBackgroundRes(R.drawable.sample_background);
        pin.setPinHeight(40);
        pin.setPinWidth(40);
        pin.setInputType(Pinview.InputType.NUMBER);
        pin.setValue("1234");
        myLayout.addView(pin);*/

        pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                //Toast.makeText(SignUp3Activity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSignUp4(View view) {
        if (pin.getValue().length() == 6) {
            UserValidateOTPCallWS task = new UserValidateOTPCallWS();
            task.execute();
        } else {
            Toast.makeText(SignUp3Activity.this, "Please Fill OTP Value" + pin.getValue().length(), Toast.LENGTH_SHORT).show();
        }

       /* Intent intentMain = new Intent(SignUp3Activity.this, SignUp2Activity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentMain.putExtra("RegistrationId", RegistrationId);
        startActivity(intentMain);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/

        //  Toast.makeText(SignUp3Activity.this, pin.getValue(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void onResend(View view) {
        RegenerateOTPClient task = new RegenerateOTPClient();
        task.execute();
    }

    private class UserValidateOTPCallWS extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mProgressDialog;
        String responce = "bfdbfh";

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(SignUp3Activity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_CValidateOTP);
                Request.addProperty("Registration_Id", RegistrationId);
                Request.addProperty("OTP", pin.getValue());
                Log.i(TAG, "SoapObject Request " + Request.toString());
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_ValidateOTP);

                transport.call(SOAP_ACTION + METHOD_NAME_CValidateOTP, soapEnvelope);
                //resultString = (SoapPrimitive) soapEnvelope.getResponse();

                responce = soapEnvelope.getResponse().toString();

                Log.i(TAG, "UserValidateOTPCallWS_Result : " + soapEnvelope.getResponse());
            } catch (Exception ex) {
                Log.e(TAG, "UserValidateOTPCallWS Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            try {
                Log.i(TAG, "UserValidateOTPCallWS onPostExecute  : " + responce);
                if (responce.equalsIgnoreCase("success")) {
                    Intent intentMain = new Intent(SignUp3Activity.this, SignUp2Activity.class);
                    intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentMain.putExtra("RegistrationId", RegistrationId);
                    startActivity(intentMain);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    SignUp3Activity.this.finish();
                } else {
                    ShowDilog(SignUp3Activity.this, "" + getString(R.string.app_name), "" + responce);
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "CustomerProfile Error:- " + e.getMessage());
                ShowDilog(SignUp3Activity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class RegenerateOTPClient extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mProgressDialog;
        String responce = "bfdbfh";

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(SignUp3Activity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_RegenerateOTPClient);
                Request.addProperty("Registration_Id", RegistrationId);
                Request.addProperty("Email", EmailId);
                Log.i(TAG, "SoapObject Request " + Request.toString());
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_ValidateOTP);

                transport.call(SOAP_ACTION + METHOD_NAME_RegenerateOTPClient, soapEnvelope);
                //resultString = (SoapPrimitive) soapEnvelope.getResponse();

                responce = soapEnvelope.getResponse().toString();

                Log.i(TAG, "RegenerateOTPClient_Result : " + soapEnvelope.getResponse());
            } catch (Exception ex) {
                Log.e(TAG, "RegenerateOTPClient Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            try {
                Log.i(TAG, "RegenerateOTPClient onPostExecute  : " + responce);
                ShowDilog(SignUp3Activity.this, "" + getString(R.string.app_name), "" + responce);
            } catch (NullPointerException e) {
                Log.d(TAG, "RegenerateOTPClient Error:- " + e.getMessage());
                ShowDilog(SignUp3Activity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }
}

