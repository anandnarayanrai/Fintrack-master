package com.edge.fintrack.insurance;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edge.fintrack.R;

public class InsuranceActivity extends AppCompatActivity implements View.OnClickListener {
    public final String TAG = "InsuranceActivity";
    ImageView iv_menu_item;
    TextView tv_term_insu, tv_health_insu, tv_motor_insu, tv_travel_insu, tv_home_insu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_insurance);
        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(this);

        tv_term_insu = (TextView) findViewById(R.id.tv_term_insu);
        tv_term_insu.setOnClickListener(this);
        tv_health_insu = (TextView) findViewById(R.id.tv_health_insu);
        tv_health_insu.setOnClickListener(this);
        tv_motor_insu = (TextView) findViewById(R.id.tv_motor_insu);
        tv_motor_insu.setOnClickListener(this);
        tv_home_insu = (TextView) findViewById(R.id.tv_home_insu);
        tv_home_insu.setOnClickListener(this);
        tv_travel_insu = (TextView) findViewById(R.id.tv_travel_insu);
        tv_travel_insu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intentTInsurance = new Intent(InsuranceActivity.this, TermInsuranceActivity.class);
        switch (v.getId()) {
            case R.id.iv_menu_item:
                onBackPressed();
                break;
            case R.id.tv_term_insu:
                // tv_insurance.setClickable(false);
                // Intent intentInsurance = new Intent(InsuranceActivity.this, TermInsuranceActivity.class);
                intentTInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentTInsurance);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_health_insu:
                Intent intentHInsurance = new Intent(InsuranceActivity.this, HelathInsuranceActivity.class);
                intentHInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHInsurance);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_motor_insu:
                Intent intentMInsurance = new Intent(InsuranceActivity.this, MotorInsuranceActivity.class);
                intentMInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMInsurance);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_home_insu:
                Intent intentHomeInsurance = new Intent(InsuranceActivity.this, HomeInsuranceActivity.class);
                intentHomeInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHomeInsurance);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_travel_insu:
                Intent intentTaveInsurance = new Intent(InsuranceActivity.this, TravelInsuranceActivity.class);
                intentTaveInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentTaveInsurance);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}
