package com.edge.fintrack;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {
    public final String TAG = "ChangePasswordActivity";
    String url;
    RelativeLayout app_header, app_header2;
    ImageView iv_menu_item;
    LinearLayout layout_Investror, layout_Address, layout_Nominee;
    private WebView mWebview;
    private ProgressDialog mProgressDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_web_view);
        // making notification bar transparent
        changeStatusBarColor();

        app_header = (RelativeLayout) findViewById(R.id.app_header);
        //app_header.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        app_header2 = (RelativeLayout) findViewById(R.id.app_header2);
        // app_header2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            url = "http://www.fintrackindia.com/default.aspx";
        } else {
            url = bundle.getString("url");
        }

        mProgressDialog = ProgressDialog.show(WebViewActivity.this, null, null, true);
        mProgressDialog.setContentView(R.layout.layout_progressdialog);
        Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.setCancelable(true);

        WebView htmlWebView = (WebView) findViewById(R.id.webView);
        htmlWebView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = htmlWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        htmlWebView.loadUrl(url);

        //htmlWebView.loadUrl("file:///android_asset/about_us.html");   // fails here
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.colorWhite));

            /*// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));*/

            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                mProgressDialog.show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            view.loadUrl(url);
            return true;
        }

        //Show loader on url load
        public void onLoadResource(WebView view, String url) {
            Log.i(TAG, "onLoadResource : " + url);

        }

        public void onPageFinished(WebView view, String url) {
            Log.i(TAG, "onPageFinished : " + url);
            try {
                mProgressDialog.dismiss();
                mProgressDialog.hide();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }

}
