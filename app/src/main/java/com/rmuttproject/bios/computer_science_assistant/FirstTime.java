package com.rmuttproject.bios.computer_science_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FirstTime extends AppCompatActivity {
        private ViewPager mSlideViewPager;
        private LinearLayout mDotsLayout;
        public SlideAdapter slideAdapter;

        private Button mFinish;

        public int mCurrentPage;

        private Boolean firstTime = null;

        private TextView[] mDots;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_first_time);


            //check is firsttime
            if(!isFirstTime()){

                FirstTime.this.startActivity ( new Intent ( FirstTime.this, LoginActivity.class ) );
                FirstTime.this.finish();
            }

            mSlideViewPager = (ViewPager) findViewById(R.id.SlideViewPager);
            mDotsLayout = (LinearLayout) findViewById(R.id.DotsLayout);
            mFinish = (Button) findViewById(R.id.finishbtn);

            slideAdapter = new SlideAdapter(this);
            mSlideViewPager.setAdapter(slideAdapter);

            addDotsIndicator(0);
            mSlideViewPager.addOnPageChangeListener(viewListener);

            mFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirstTime.this.startActivity ( new Intent ( FirstTime.this, LoginActivity.class ) );
                    FirstTime.this.finish();
                }
            });
        }
        /////code crate dots////
        public void addDotsIndicator(int position){
            /// add new value in TextView[i] for add page
            mDots = new TextView[4];
            mDotsLayout.removeAllViews();
            for(int i = 0 ; i<mDots.length ;i++){
                mDots[i] = new TextView(this);
                mDots[i].setText(Html.fromHtml("&#8226;"));
                mDots[i].setTextSize(35);
                mDots[i].setTextColor(getResources().getColor(R.color.colorGray));
                mDotsLayout.addView(mDots[i]);
            }

            if(mDots.length > 0){
                mDots[position].setTextColor(getResources().getColor(R.color.colorOrange));
            }
        }

        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                addDotsIndicator(i);
                mCurrentPage = i;
                //  หน้าแรก //
                if(i == 0){
                    mFinish.setEnabled(false);
                    mFinish.setVisibility(View.INVISIBLE);

                }else if(i == mDots.length - 1){
                    // หน้าสุดท้าย //
                    mFinish.setEnabled(true);
                    mFinish.setVisibility(View.VISIBLE);
                }else{
                    //หน้าทั่วไป
                    mFinish.setEnabled(false);
                    mFinish.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };
        //medthod check first time return true or false
        private boolean isFirstTime() {
            if (firstTime == null) {
                SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
                firstTime = mPreferences.getBoolean("cs_firstTime", true);
                if (firstTime) {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("cs_firstTime", false);
                    editor.commit();
                }
            }
            return firstTime;
        }
    }
