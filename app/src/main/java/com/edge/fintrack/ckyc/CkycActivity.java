package com.edge.fintrack.ckyc;

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

public class CkycActivity extends AppCompatActivity {
    String layout_name;
    ImageView iv_menu_item;
    CardView cardview_UPresonal, cardview_Uapplicable, cardview_UPoI, cardview_UPoA, cardview_UCorrespondence, cardview_UContact, cardview_UFATCA, cardview_URelated, cardview_UPol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_ckyc);
        // making notification bar transparent
        changeStatusBarColor();
        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardview_UPresonal = (CardView) findViewById(R.id.cardview_UPresonal);
        cardview_Uapplicable = (CardView) findViewById(R.id.cardview_Uapplicable);
        cardview_UPoI = (CardView) findViewById(R.id.cardview_UPoI);
        cardview_UPoA = (CardView) findViewById(R.id.cardview_UPoA);
        cardview_UCorrespondence = (CardView) findViewById(R.id.cardview_UCorrespondence);
        cardview_UContact = (CardView) findViewById(R.id.cardview_UContact);
        cardview_UFATCA = (CardView) findViewById(R.id.cardview_UFATCA);
        cardview_URelated = (CardView) findViewById(R.id.cardview_URelated);
        cardview_UPol = (CardView) findViewById(R.id.cardview_UPol);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            layout_name = null;
        } else {
            layout_name = bundle.getString("layout");
            if (layout_name.equalsIgnoreCase("cardview_Presonal")) {
                cardview_UPresonal.setVisibility(View.VISIBLE);
            } else if (layout_name.equalsIgnoreCase("cardview_applicable")) {
                cardview_Uapplicable.setVisibility(View.VISIBLE);
            } else if (layout_name.equalsIgnoreCase("cardview_PoI")) {
                cardview_UPoI.setVisibility(View.VISIBLE);
            } else if (layout_name.equalsIgnoreCase("cardview_PoA")) {
                cardview_UPoA.setVisibility(View.VISIBLE);
            } else if (layout_name.equalsIgnoreCase("cardview_Correspondence")) {
                cardview_UCorrespondence.setVisibility(View.VISIBLE);
            } else if (layout_name.equalsIgnoreCase("cardview_Contact")) {
                cardview_UContact.setVisibility(View.VISIBLE);
            } else if (layout_name.equalsIgnoreCase("cardview_FATCA")) {
                cardview_UFATCA.setVisibility(View.VISIBLE);
            } else if (layout_name.equalsIgnoreCase("cardview_Related")) {
                cardview_URelated.setVisibility(View.VISIBLE);
            } else if (layout_name.equalsIgnoreCase("cardview_Pol")) {
                cardview_UPol.setVisibility(View.VISIBLE);
            }
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}
