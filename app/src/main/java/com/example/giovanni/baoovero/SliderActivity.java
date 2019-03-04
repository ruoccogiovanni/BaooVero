package com.example.giovanni.baoovero;

import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SliderActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private SlideAdapter myadapter;
    private LinearLayout Dots_Layout;
    private TextView[] mDots;
    private ImageView[] dots;
    private Button BnNext;
    private Vibrator myVib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        myadapter = new SlideAdapter(this);
        viewPager.setAdapter(myadapter);
        addDotsIndicator(0);
        BnNext = (Button) findViewById(R.id.iniziamoButton);
        BnNext.setOnClickListener(this);
        myVib=(Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void addDotsIndicator(int current_position) {
        mDots = new TextView[3];
        if(Dots_Layout!=null)
            Dots_Layout.removeAllViews();
        dots= new ImageView[4];
        for (int i=0;i<dots.length;i++)
        {
            dots[i]= new ImageView(this);
            if(i==current_position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i],params);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iniziamoButton:
                myVib.vibrate(25);
                startActivity(new Intent((this),MainActivity.class));
                break;
        }
    }
}