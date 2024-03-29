package com.eightpeak.salakafarm.views.welcomeActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.eightpeak.salakafarm.R;
import com.eightpeak.salakafarm.views.home.HomeActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    TextView btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;
     View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), HomeActivity.class );
            startActivity(mainActivity);
            finish();


        }else {
            setContentView(R.layout.activity_intro);
            view = findViewById(R.id.view);


            // hide the action bar

//        getSupportActionBar().hide();

            // ini views
            btnNext = findViewById(R.id.btn_next);
            btnGetStarted = findViewById(R.id.btn_get_started);
            tabIndicator = findViewById(R.id.tab_indicator);
            btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
            tvSkip = findViewById(R.id.tv_skip);

            // fill list screen

            final List<ScreenItem> mList = new ArrayList<>();
            mList.add(new ScreenItem("Quality Products", " Ensuring your products are of high quality.\n Fulfilling everyone's Good quality needs.", R.drawable.intro3));
            mList.add(new ScreenItem("Fast Delivery", "We bring the store to your door.\n Save your precious time", R.drawable.intro2));
            mList.add(new ScreenItem("Easy Payment", "The greatest journey of online shop. \n Life is hard enough already. Let us make it a little easier.", R.drawable.intro1));

            // setup viewpager
            screenPager = findViewById(R.id.screen_viewpager);
            introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
            screenPager.setAdapter(introViewPagerAdapter);

            // setup tablayout with viewpager

            tabIndicator.setupWithViewPager(screenPager);

            // next button click Listner

            btnNext.setOnClickListener(v -> {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);


                }

                if (position == mList.size() - 1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button

                    loaddLastScreen();


                }


            });

            // tablayout add change listener


            tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    if (tab.getPosition() == mList.size() - 1) {

                        loaddLastScreen();

                    }


                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


            // Get Started button click listener

            btnGetStarted.setOnClickListener(v -> {
                //open main activity
                Intent mainActivity = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(mainActivity);
                // also we need to save a boolean value to storage so next time when the user run the app
                // we could know that he is already checked the intro screen activity
                // i'm going to use shared preferences to that process
                savePrefsData();
                finish();


            });

            // skip button click listener

            tvSkip.setOnClickListener(v -> screenPager.setCurrentItem(mList.size()));
        }
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend",false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.apply();
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);

    }
}
