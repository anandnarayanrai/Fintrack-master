package com.edge.fintrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edge.fintrack.anim_viewpager.ZoomOutPageTransformer;

public class WelcomeActivity extends AppCompatActivity {

    ImageView iv_backgraund, iv_think;
    TextView tv_tag;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
           /* Animation imgAnimationIn = AnimationUtils.
                    loadAnimation(WelcomeActivity.this, R.anim.anim_fade_in);
            iv_backgraund.startAnimation(imgAnimationIn);
            switch (position) {
                case 0:
                    iv_backgraund.setImageResource(R.drawable.intro2_bg);
                    iv_think.startAnimation(imgAnimationIn);
                    tv_tag.startAnimation(imgAnimationIn);
                    break;
                case 1:
                    iv_backgraund.setImageResource(R.drawable.intro3_bg);
                    iv_think.startAnimation(imgAnimationIn);
                    tv_tag.startAnimation(imgAnimationIn);
                    break;
                case 2:
                    iv_backgraund.setImageResource(R.drawable.intro1_bg);
                    iv_think.startAnimation(imgAnimationIn);
                    tv_tag.startAnimation(imgAnimationIn);
                    break;
            }*/
            // changing the next button text 'NEXT' / 'GOT IT'
           /* if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }*/

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private Button btnSkip, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        iv_backgraund = (ImageView) findViewById(R.id.iv_backgraund);

        iv_think = (ImageView) findViewById(R.id.iv_think);

        tv_tag = (TextView) findViewById(R.id.tv_tag);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                /*int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }*/
                launchHomeScreen();
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(45);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        Intent mainIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        finish();
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

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            ImageView iv_think = (ImageView) view.findViewById(R.id.iv_think);

            TextView tv_tag = (TextView) view.findViewById(R.id.tv_tag);

            if (position == 0) {
                Animation imgAnimationIn = AnimationUtils.
                        loadAnimation(WelcomeActivity.this, R.anim.anim_slide_in_left);
                iv_think.startAnimation(imgAnimationIn);
                tv_tag.startAnimation(imgAnimationIn);
            } else if (position == 1) {
                Animation imgAnimationIn = AnimationUtils.
                        loadAnimation(WelcomeActivity.this, R.anim.anim_slide_in_left);
                iv_think.startAnimation(imgAnimationIn);
                tv_tag.startAnimation(imgAnimationIn);
            } else if (position == 2) {
                Animation imgAnimationIn = AnimationUtils.
                        loadAnimation(WelcomeActivity.this, R.anim.anim_slide_in_left);
                iv_think.startAnimation(imgAnimationIn);
                tv_tag.startAnimation(imgAnimationIn);
            }

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
