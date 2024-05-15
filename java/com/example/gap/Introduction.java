package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.gap.misc.IntroModalClass;
import com.example.gap.misc.ViewPagerAdapter;

import java.util.ArrayList;

public class Introduction extends AppCompatActivity {

    ViewPager2 viewPager;
    ArrayList<IntroModalClass> arrayList=new ArrayList<>();
    Button btnnext,btnprev,btnreg,btnskip;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        viewPager=findViewById(R.id.viewPager);
        btnnext=findViewById(R.id.intro_btn_next);
        btnprev=findViewById(R.id.intro_btn_prev);
        btnreg=findViewById(R.id.intro_btn_reg);
        btnskip=findViewById(R.id.btn_skip);

        animation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.get_registered_button_anim);

        arrayList.add(new IntroModalClass(R.drawable.servicetime,"24 X 7 Service","You can avail 24x7 hours of services without any problem\nyou can perform banking operation in one click"));
        arrayList.add(new IntroModalClass(R.drawable.secure,"Safe and Secure","Your data and money will be safe under the RBI regulation act.\nThere is a negligible amount of data loss"));
        arrayList.add(new IntroModalClass(R.drawable.userfriend,"User friendly App","Are u new to mobile banking?\n then dont worry\n Our app is built with the user friendly interface"));
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this,arrayList);

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(position==arrayList.size()-1) {
                    btnnext.setVisibility(View.INVISIBLE);
                    btnprev.setVisibility(View.INVISIBLE);
                    btnreg.setVisibility(View.VISIBLE);
                    btnskip.setVisibility(View.INVISIBLE);
                    btnreg.startAnimation(animation);
                }
                else if(position==0)
                    btnprev.setVisibility(View.INVISIBLE);
                if(position<arrayList.size()-1 && position>0)
                {
                    btnnext.setVisibility(View.VISIBLE);
                    btnprev.setVisibility(View.VISIBLE);
                    btnskip.setVisibility(View.VISIBLE);
                    btnreg.setVisibility(View.INVISIBLE);
                }

            }

            // triggered when you select a new page
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    public void onClickIntro(View v){
        int position=viewPager.getCurrentItem();
        if(v.getId()==btnnext.getId())
            position++;
        else if(v.getId()==btnprev.getId())
            position--;
        else
            position=arrayList.size()-1;
        viewPager.setCurrentItem(position);
    }

    public void onClickGetStart(View v)
    {
        startActivity(new Intent(this,Registration.class));
        finish();
    }
}