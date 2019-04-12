package test.dahun.mobileplay.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.youtube.player.internal.v;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.BackPressCloseHandler;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.interfaces.ButtonInterface;

public class MainActivity extends AppCompatActivity implements ButtonInterface {
    ViewPager mainPager;
    ViewPagerAdapter viewPagerAdapter;
    SetViewPagerTabListener setViewPagerTabListener;
    public static int position=0;
    public static int state=1;
    ImageButton home_btn, list_btn, play_btn, gallery_btn, sns_btn;
    BackPressCloseHandler backPressCloseHandler;

    public interface SetViewPagerTabListener{
        void setTab(int position);
    }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSetting();
        btnSetting();
     }

    @Override
    protected void onStart() {
        super.onStart();
//        viewPagerAdapter.notifyDataSetChanged();
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
        backPressCloseHandler = new BackPressCloseHandler(this);

        mainPager=(ViewPager)findViewById(R.id.mainPager);
        mainPager.setOffscreenPageLimit(4);
        setViewPagerTabListener= position -> {
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
        };

        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), setViewPagerTabListener);
        mainPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
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

    public void btnSetting(){
        home_btn = (ImageButton)findViewById(R.id.home_btn);
        list_btn = (ImageButton)findViewById(R.id.list_btn);
        play_btn = (ImageButton)findViewById(R.id.play_btn);
        gallery_btn = (ImageButton)findViewById(R.id.gallery_btn);
        sns_btn = (ImageButton)findViewById(R.id.sns_btn);
        home_btn.setOnClickListener(v -> {  mainPager.setCurrentItem(0); });
        list_btn.setOnClickListener(v -> {  mainPager.setCurrentItem(1); });
        play_btn.setOnClickListener(v -> { mainPager.setCurrentItem(2); });
        gallery_btn.setOnClickListener(v -> { mainPager.setCurrentItem(3); });
        sns_btn.setOnClickListener(v -> { mainPager.setCurrentItem(4); });

        Glide.with(this).load(R.drawable.mn_home_off2).apply(new RequestOptions().fitCenter()).into(home_btn);
        Glide.with(this).load(R.drawable.mn_list_off2).apply(new RequestOptions().fitCenter()).into(list_btn);
        Glide.with(this).load(R.drawable.mn_play_off2).apply(new RequestOptions().fitCenter()).into(play_btn);
        Glide.with(this).load(R.drawable.mn_gallery_off2).apply(new RequestOptions().fitCenter()).into(gallery_btn);
        Glide.with(this).load(R.drawable.mn_sns_off2).apply(new RequestOptions().fitCenter()).into(sns_btn);
    }

    @Override
    public void reset() {
        Glide.with(this).load(R.drawable.mn_home_off2).apply(new RequestOptions().fitCenter()).into(home_btn);
        Glide.with(this).load(R.drawable.mn_list_off2).apply(new RequestOptions().fitCenter()).into(list_btn);
        if(!ApplicationStatus.isPlaying) Glide.with(this).load(R.drawable.mn_play_off2).apply(new RequestOptions().fitCenter()).into(play_btn);
        Glide.with(this).load(R.drawable.mn_gallery_off2).apply(new RequestOptions().fitCenter()).into(gallery_btn);
        Glide.with(this).load(R.drawable.mn_sns_off2).apply(new RequestOptions().fitCenter()).into(sns_btn);
    }

    @Override
    public void homeOn() {
        Glide.with(this).load(R.drawable.mn_home_on2).apply(new RequestOptions().fitCenter()).into(home_btn);
    }

    @Override
    public void listOn() {
        Glide.with(this).load(R.drawable.mn_list_on2).apply(new RequestOptions().fitCenter()).into(list_btn);
    }

    @Override
    public void playMusic() {
        Glide.with(this).load(R.raw.mn_equalizer).apply(new RequestOptions().fitCenter()).into(play_btn);
    }

    @Override
    public void playOn() {
        Glide.with(this).load(R.drawable.mn_play_on3).apply(new RequestOptions().fitCenter()).into(play_btn);
    }

    @Override
    public void playOff() {
        Glide.with(this).load(R.drawable.mn_play_off2).apply(new RequestOptions().fitCenter()).into(play_btn);
    }

    @Override
    public void galleryOn() {
        Glide.with(this).load(R.drawable.mn_gallery_on2).apply(new RequestOptions().fitCenter()).into(gallery_btn);
    }

    @Override
    public void snsOn() {
        Glide.with(this).load(R.drawable.mn_sns_on2).apply(new RequestOptions().fitCenter()).into(sns_btn);
    }

}
