package test.dahun.mobileplay.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.tab.CallbackEvent;
import test.dahun.mobileplay.tab.CoverFragment;
import test.dahun.mobileplay.tab.SubCoverView;
import test.dahun.mobileplay.ui.VerticalViewPager;

public class MainActivity extends AppCompatActivity {
    ViewPager mainPager;
    ViewPagerAdapter viewPagerAdapter;
    SetViewPagerTabListener setViewPagerTabListener;


    public interface SetViewPagerTabListener{
        void setTab(int position);
    }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSetting();
        callback();
     }

     // 기연 추가
     public void callback(){
        CallbackEvent callbackEvent = new CallbackEvent() {
            @Override
            public void callbackMethod() {
                Log.i("GomgomKim", "callback_main");
            }
        };
        CoverFragment coverFragment = new CoverFragment();
        coverFragment.doWork(callbackEvent);
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

    //앱 재시작
    public static void restartApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }


}
