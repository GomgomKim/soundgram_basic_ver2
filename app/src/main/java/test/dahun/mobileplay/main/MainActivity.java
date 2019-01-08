package test.dahun.mobileplay.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {
    ViewPager mainPager;
    ViewPagerAdapter viewPagerAdapter;
    SetViewPagerTabListener setViewPagerTabListener;
    public static int position=0;
    public static int state=0;


    public interface SetViewPagerTabListener{
        void setTab(int position);
    }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSetting();
     }

    @Override
    protected void onStart() {
        super.onStart();
        viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void initSetting(){
        mainPager=(ViewPager)findViewById(R.id.mainPager);
        mainPager.setOffscreenPageLimit(4);
        setViewPagerTabListener=new SetViewPagerTabListener() {
            @Override
            public void setTab(int position) {
                switch (position){
                    case 0:
                        mainPager.setCurrentItem(0);
                        break;
                    case 1:
                        mainPager.setCurrentItem(1);
                        break;
                    case 2:
                        mainPager.setCurrentItem(2);
                        break;
                    case 3:
                        mainPager.setCurrentItem(3);
                        break;
                    case 4:
                        mainPager.setCurrentItem(4);
                        break;
                }
            }
        };

        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), setViewPagerTabListener);
        mainPager.setAdapter(viewPagerAdapter);
    }

    public static void setPosition(int p){
        position = p;
    }

    public static int getPosition(){
        return position;
    }

    public static void setState(int s){
        state = s;
    }

    public static int getState(){
        return state;
    }



}
