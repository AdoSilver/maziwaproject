package com.lastmilesale.android.mobileapps.lastmile.Activities.GetStarted;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lastmilesale.android.mobileapps.lastmile.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class PitchSliderActivity extends Activity {

    private static ViewPager viewPager;
    private static int currentPage = 0;
    private static final Integer[] images= {R.drawable.shopkeeper, R.drawable.suppliers,R.drawable.delivery};
    private ArrayList<Integer> imageArray = new ArrayList<>();
    private ArrayList<String> categoryArray = new ArrayList<>();
    private ArrayList<String> descriptionArray = new ArrayList<>();
    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_slider);

        rootView = findViewById(R.id.pitch_slider_activity_layout);

        //CREATING TRANSPARENT STATUS BAR FOR ANDROID VERSIONS WITH API >= 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        init();
    }

    private void init(){
        //LOADING WELCOME IMAGES
        for(int i=0;i<images.length;i++){
            imageArray.add(images[i]);}

        //LOADING WELCOME CATEGORIES
        categoryArray.add("Order");
        categoryArray.add("Suppliers");
        categoryArray.add("Delivery");

        //LOADING CATEGORIES DESCRIPTIONS
        descriptionArray.add("Fast way to connect directly and order goods from suppliers ");
        descriptionArray.add("A wide range of choices for user to choose the best suppliers");
        descriptionArray.add("Enhanced delivery services of the ordered goods to your shop location ");


        //SETTING UP VIEW PAGER AND VIEW PAGER ADAPTER
        viewPager = (ViewPager) findViewById(R.id.slider_view_pager);
        viewPager.setAdapter(new SliderViewPagerAdapter(PitchSliderActivity.this,rootView,imageArray,categoryArray,descriptionArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.slider_circle_indicator);
        indicator.setViewPager(viewPager);

        //AUTO-START VIEW PAGER,SETTING UP DELAY AND PERIOD TIME
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 10000);
    }
}
