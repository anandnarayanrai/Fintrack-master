package com.edge.fintrack.ckyc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.edge.fintrack.R;

public class CkycUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cardview_Presonal, cardview_applicable, cardview_PoI, cardview_PoA, cardview_Correspondence, cardview_Contact, cardview_FATCA, cardview_Related, cardview_Pol;
    ImageView iv_menu_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_ckyc_update);
        // making notification bar transparent
        changeStatusBarColor();
        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardview_Presonal = (CardView) findViewById(R.id.cardview_Presonal);
        cardview_Presonal.setOnClickListener(this);

        cardview_applicable = (CardView) findViewById(R.id.cardview_applicable);
        cardview_applicable.setOnClickListener(this);

        cardview_PoI = (CardView) findViewById(R.id.cardview_PoI);
        cardview_PoI.setOnClickListener(this);

        cardview_PoA = (CardView) findViewById(R.id.cardview_PoA);
        cardview_PoA.setOnClickListener(this);

        cardview_Correspondence = (CardView) findViewById(R.id.cardview_Correspondence);
        cardview_Correspondence.setOnClickListener(this);

        cardview_Contact = (CardView) findViewById(R.id.cardview_Contact);
        cardview_Contact.setOnClickListener(this);

        cardview_FATCA = (CardView) findViewById(R.id.cardview_FATCA);
        cardview_FATCA.setOnClickListener(this);

        cardview_Related = (CardView) findViewById(R.id.cardview_Related);
        cardview_Related.setOnClickListener(this);

        cardview_Pol = (CardView) findViewById(R.id.cardview_Pol);
        cardview_Pol.setOnClickListener(this);
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            // window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

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
    public void onClick(View v) {
        Intent intentUpdateProfile = new Intent(CkycUpdateActivity.this, CkycActivity.class);
        switch (v.getId()) {
            case R.id.cardview_Presonal:
                intentUpdateProfile.putExtra("layout", "cardview_Presonal");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.cardview_applicable:
                intentUpdateProfile.putExtra("layout", "cardview_applicable");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.cardview_PoI:
                intentUpdateProfile.putExtra("layout", "cardview_PoI");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.cardview_PoA:
                intentUpdateProfile.putExtra("layout", "cardview_PoA");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.cardview_Correspondence:
                intentUpdateProfile.putExtra("layout", "cardview_Correspondence");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;

            case R.id.cardview_Contact:
                intentUpdateProfile.putExtra("layout", "cardview_Contact");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.cardview_FATCA:
                intentUpdateProfile.putExtra("layout", "cardview_FATCA");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;

            case R.id.cardview_Related:
                intentUpdateProfile.putExtra("layout", "cardview_Related");
                startActivity(intentUpdateProfile);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.cardview_Pol:
                intentUpdateProfile.putExtra("layout", "cardview_Pol");
                startActivity(intentUpdateProfile);
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
