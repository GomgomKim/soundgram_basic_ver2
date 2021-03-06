package test.dahun.mobileplay.main;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.BackPressCloseHandler;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.database.DBOpenHelper;
import test.dahun.mobileplay.database.NetworkTask;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.interfaces.AutoUiInterface;
import test.dahun.mobileplay.interfaces.ButtonInterface;
import test.dahun.mobileplay.interfaces.GetSongDataInterface;
import test.dahun.mobileplay.interfaces.HeartNumInterface;
import test.dahun.mobileplay.interfaces.ServiceStateInterface;
import test.dahun.mobileplay.services.MusicService;

import test.dahun.mobileplay.services.MusicService.LocalBinder;
import test.dahun.mobileplay.tab.MusicFragment;

public class MainActivity extends AppCompatActivity implements ButtonInterface, ServiceStateInterface, GetSongDataInterface {
    ViewPager mainPager;
    ViewPagerAdapter viewPagerAdapter;
    SetViewPagerTabListener setViewPagerTabListener;
    public static int position=0;
    public static int state=1;
    ImageButton home_btn, list_btn, play_btn, gallery_btn, sns_btn;
    BackPressCloseHandler backPressCloseHandler;

    //서비스 연결
    MusicService mService;
    boolean isService = false;
    ServiceConnection conn;

    private AutoUiInterface autoUiInterface;

    DBOpenHelper mDbOpenHelper;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    public MusicService getServiceState() {
        return mService;
    }

    public interface SetViewPagerTabListener{
        void setTab(int position);
    }

    //noti 누르면 바로 뮤직화면으로
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String noti_massage = getIntent().getStringExtra("notificationMessage");
        Log.i("main_test", ""+noti_massage);
        mainPager.setCurrentItem(2);
    }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getService();
        initSetting();
        btnSetting();
        setIsNetwork();
        getSongdata();
        interfaceSetting();
        createSQL();

        Log.i("main_test", "one");
        String noti_massage = getIntent().getStringExtra("notificationMessage");
         Log.i("main_test", ""+noti_massage);
        if(noti_massage != null) {
            ((MusicFragment)new MusicFragment()).setTrueAutoMove();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                mainPager.setCurrentItem(2);
            }, 100);
        }
     }

     public void setMusicUI(AutoUiInterface autoUiInterface){
        this.autoUiInterface = autoUiInterface;
     }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        mDbOpenHelper.close();
    }

    public void getService(){
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocalBinder binder = (LocalBinder) service;
                mService = binder.getService();
                isService = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isService = false;
            }
        };

        Intent intent = new Intent(MainActivity.this, MusicService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

     public void setIsNetwork(){
         connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
         networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

        } else{
            // alert
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
            alert.setMessage("네트워크가 연결되지 않았습니다. 일부 기능이 제한될 수 있습니다.");
            alert.show();
        }
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

    @Override
    public void getSongdata(){
        if(networkInfo != null && networkInfo.isConnected()){
            String url = "http://211.251.236.130:9000/API/song/getList/";

            ContentValues param = new ContentValues();
            param.put("albumID", 3); // 신루트 앨범

            // AsyncTask를 통해 HttpURLConnection 수행.
            NetworkTask networkTask = new NetworkTask(url, param, "song_data");
            networkTask.execute();
        } else{
        }
    }

    public void interfaceSetting(){
        HeartNumInterface.numSetting();
    }

    public void createSQL(){
        mDbOpenHelper = new DBOpenHelper(this);
        mDbOpenHelper.open();

        int count = mDbOpenHelper.getCount();
        Log.i("count_check", "count : "+count);
        if(count == 0){
            mDbOpenHelper.create();
            for(int i=0; i<6; i++){
                mDbOpenHelper.insertColumn(i, 0);
            }
        }
    }

    @Override
    public ArrayList<Integer> getIsLike(){
        ArrayList<Integer> is_like_arr = new ArrayList<>();

        Cursor iCursor = mDbOpenHelper.selectColumns();
        while(iCursor.moveToNext()){
            int song_id = iCursor.getInt(iCursor.getColumnIndex("song_id"));
            int is_like = iCursor.getInt(iCursor.getColumnIndex("is_like"));
            Log.i("is_likeSQLTest", "song_id : "+song_id);
            Log.i("is_likeSQLTest", "is like : "+is_like);
            is_like_arr.add(is_like);
        }

        return is_like_arr;
    }

    @Override
    public void updateSQLDB(int position, int is_like){
        mDbOpenHelper.updateColumn(position, is_like);
    }

}
