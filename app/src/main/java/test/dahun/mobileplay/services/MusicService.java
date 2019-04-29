package test.dahun.mobileplay.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.events.DurationEvent;
import test.dahun.mobileplay.events.FinishMusicEvent;
import test.dahun.mobileplay.events.GetSongPlayInfoEvent;
import test.dahun.mobileplay.events.IsPlayEvent;
import test.dahun.mobileplay.events.PositionEvent;
import test.dahun.mobileplay.events.SeekbarEvent;
import test.dahun.mobileplay.events.TimerEvent;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.main.MainActivity;


public class MusicService extends Service {

    MediaPlayer mp;
    int pos=0;
    int play_mode=0;
    int music_index=0;

    Timer timer, timer_update;
    int current_time = 0;
    boolean is_timer_on = false;

    IBinder mBinder = new LocalBinder();

    // notification data
    RemoteViews customView;
    Intent prev_intent, play_intent, next_intent;
    PendingIntent prev_p_intent, play_p_intent, next_p_intent, content_intent;
    NotificationCompat.Builder builder;
    Notification notification;
    NotificationManager notificationManager;
    ArrayList<String> musicarr;
    ArrayList<Integer> albumarr;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return  mBinder;
    }

    public class LocalBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    public MediaPlayer getMp(){
        return mp;
    }

    public int getIndex(){ return music_index; }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = changeMusicPlayer(0); //mp 초기화
        dataSetting();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        music_index = intent.getExtras().getInt("index", 0);
        String state = intent.getExtras().getString("state");
        int seekBarPosition = intent.getExtras().getInt("seekBar_position", -1);

        switch (state){
            case "play":
                mp = changeMusicPlayer(music_index);
                mp.seekTo(pos);
                mp.setLooping(false);
                if(seekBarPosition != -1){
                    mp.seekTo(seekBarPosition);
                    current_time = seekBarPosition/1000;
                }
                if(!mp.isPlaying()) mp.start();

                BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                BusProvider.getInstance().post(new DurationEvent(mp.getDuration()));
                BusProvider.getInstance().post(new PositionEvent(music_index));


                ApplicationStatus.isPlaying = true;

                startNotification();

                break;

            case "stop":
                if(mp.isPlaying()){
                    mp.stop(); // 멈춤
                    mp.reset();
                    mp.release(); // 자원 해제
                    mp = changeMusicPlayer(music_index);
                    pos = 0;
                }
                current_time = 0;
                BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));

                ApplicationStatus.isPlaying = false;

                setNotification();
                break;

            case "pause":
                pos = mp.getCurrentPosition();
                mp.pause();
                BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));

                ApplicationStatus.isPlaying = false;

                setNotification();
                break;

            case "play_mode":
                play_mode = intent.getExtras().getInt("play_mode", 0);
                break;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp.isPlaying()){
            mp.stop(); // 멈춤
            mp.reset();
            mp.release(); // 자원 해제
        }
    }

    public void setNextPlay(){
        mp.setOnCompletionListener(mediaPlayer -> {
            current_time = 0;
            switch (play_mode){
                case 0: // 전체반복
                    mp.stop(); // 멈춤
                    mp.reset();
                    mp.release(); // 자원 해제

                    if(music_index == 7){
                        mp = changeMusicPlayer(0);
                    } else{
                        music_index++;
                        mp = changeMusicPlayer(music_index);
                    }
                    mp.setLooping(false);
                    mp.start();

                    BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                    BusProvider.getInstance().post(new DurationEvent(mp.getDuration()));
                    BusProvider.getInstance().post(new FinishMusicEvent(0));
                    break;

                case 1: // 한곡반복
                    mp.stop(); // 멈춤
                    mp.reset();
                    mp.release(); // 자원 해제
                    mp = changeMusicPlayer(music_index);
                    mp.start();
                    BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                    BusProvider.getInstance().post(new DurationEvent(mp.getDuration()));
                    BusProvider.getInstance().post(new FinishMusicEvent(1));
                    break;

                case 2: // 반복없음

                    mp.stop(); // 멈춤

                    BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                    BusProvider.getInstance().post(new DurationEvent(mp.getDuration()));
                    BusProvider.getInstance().post(new FinishMusicEvent(2));

                    mp.reset();
                    mp.release(); // 자원 해제


                    break;
            }
        });
    }

    public void startNotification(){
        customView = new RemoteViews(getPackageName(), R.layout.layout_notification);
        setNotification();
    }

    public void setNotification() {
        customView.setImageViewResource(R.id.img_noti, albumarr.get(music_index));
        customView.setTextViewText(R.id.title_noti, musicarr.get(music_index));

        if(mp.isPlaying()) customView.setImageViewResource(R.id.play_img, R.drawable.btn_pause);
        else customView.setImageViewResource(R.id.play_img, R.drawable.btn_play);

        content_intent = PendingIntent.getActivity(this, 10, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        customView.setOnClickPendingIntent(R.id.noti_layout, content_intent);

        // click events
        prev_intent = new Intent("music_prev");
        prev_intent.putExtra("id", -1);
        prev_intent.putExtra("music_index", music_index);
        prev_intent.putExtra("is_play", mp.isPlaying());
        prev_p_intent = PendingIntent.getBroadcast(this, -1, prev_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.music_prev, prev_p_intent);

        next_intent = new Intent("music_next");
        next_intent.putExtra("id", 1);
        next_intent.putExtra("music_index", music_index);
        next_intent.putExtra("is_play", mp.isPlaying());
        next_p_intent = PendingIntent.getBroadcast(this, 1, next_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.music_next, next_p_intent);

        play_intent = new Intent("music_play");
        play_intent.putExtra("id", 0);
        play_intent.putExtra("music_index", music_index);
        play_intent.putExtra("is_play", mp.isPlaying());
        play_p_intent = PendingIntent.getBroadcast(this, 0, play_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.music_now, play_p_intent);

        builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.gallary1)
                        .setCustomContentView(customView);

        notification = builder.build();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


    public MediaPlayer changeMusicPlayer(int index){
        switch(index){
            case 0:
                mp = MediaPlayer.create(this, R.raw.track1);
                break;
            case 1:
                mp = MediaPlayer.create(this, R.raw.track2);
                break;
            case 2:
                mp = MediaPlayer.create(this, R.raw.track3);
                break;
            case 3:
                mp = MediaPlayer.create(this, R.raw.track4);
                break;
            case 4:
                mp = MediaPlayer.create(this, R.raw.track5);
                break;
            case 5:
                mp = MediaPlayer.create(this, R.raw.track6);
                break;
        }
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

        setNextPlay();

        return mp;
    }

    public void dataSetting(){
        musicarr = new ArrayList<>();
        albumarr = new ArrayList<>();
        albumarr.add(R.drawable.gallary1); musicarr.add("신현희와김루트");
        albumarr.add(R.drawable.gallary7); musicarr.add("오빠야");
        albumarr.add(R.drawable.gallary8); musicarr.add("Cap Song");
        albumarr.add(R.drawable.gallary4); musicarr.add("집 비던날");
        albumarr.add(R.drawable.gallary5); musicarr.add("편한노래");
        albumarr.add(R.drawable.gallary6); musicarr.add("날개");
    }

}
