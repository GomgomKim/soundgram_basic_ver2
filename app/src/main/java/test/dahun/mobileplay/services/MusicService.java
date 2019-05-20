package test.dahun.mobileplay.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
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
    boolean is_noti_broad = false;
    String channel_id="";

    //Image
    ArrayList<Integer> album_arr = new ArrayList<>();


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
        is_noti_broad = intent.getExtras().getBoolean("is_broad", false);

        switch (state){
            case "play":
                try{
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
                    if(is_noti_broad) BusProvider.getInstance().post(new PositionEvent(music_index));


                    ApplicationStatus.isPlaying = true;

                    startNotification();
                } catch (IllegalStateException e){

                }
                break;

            case "stop":
                try {
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

                    if(customView != null) setNotification();

                } catch (IllegalStateException e){
                    Log.i("gomgomKim", "illegal_stop");
                }
                break;

            case "pause":
                try {
                    pos = mp.getCurrentPosition();
                    mp.pause();
                    BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));

                    ApplicationStatus.isPlaying = false;

                    if(customView != null) setNotification();
                } catch (IllegalStateException e){

                }
                break;

            case "play_mode":
                play_mode = intent.getExtras().getInt("play_mode", 0);
                break;

            case "arr":
                album_arr = (ArrayList<Integer>) intent.getSerializableExtra("album_arr");
                albumarr = new ArrayList<>();
                albumarr.addAll(album_arr);
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
                    try {
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
                    } catch (IllegalStateException e){

                    }

                    break;

                case 1: // 한곡반복

                    try {
                        mp.stop(); // 멈춤
                        mp.reset();
                        mp.release(); // 자원 해제
                        mp = changeMusicPlayer(music_index);

                        mp.start();
                        BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                        BusProvider.getInstance().post(new DurationEvent(mp.getDuration()));
                        BusProvider.getInstance().post(new FinishMusicEvent(1));
                    } catch (IllegalStateException e){

                    }
                    break;

                case 2: // 반복없음
                    try {
                        mp.stop(); // 멈춤

                        BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));
                        BusProvider.getInstance().post(new DurationEvent(mp.getDuration()));
                        BusProvider.getInstance().post(new FinishMusicEvent(2));

                        mp.reset();
                        mp.release(); // 자원 해제
                    } catch (IllegalStateException e){

                    }

                    break;
            }
        });
    }

    public void startNotification(){
        customView = new RemoteViews(getPackageName(), R.layout.layout_notification);
        setNotification();
    }

    public void setNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        customView.setImageViewResource(R.id.img_noti, albumarr.get(music_index));
        customView.setTextViewText(R.id.title_noti, musicarr.get(music_index));

        if(mp.isPlaying()) customView.setImageViewResource(R.id.play_img, R.drawable.btn_pause);
        else customView.setImageViewResource(R.id.play_img, R.drawable.btn_play);

        // 안드로이드 8.0.0 이상은 채널 설정 해줘야함
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("MusicService", "is phone version");
            // 채널의 ID
            channel_id = "soundgram_channel";
            // 사용자에게 보이는 채널의 이름
            CharSequence name = getString(R.string.channel_name);
            // 사용자에게 보이는 채널의 설명
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channel_id, name, importance);
            // 알림 채널 설정
            mChannel.setDescription(description);
            /*mChannel.enableLights(true);
            // 기기가 이 기능을 지원한다면, 이 채널에 게시되는 알림에 대한 알림 불빛 색상을 설정
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});*/
            notificationManager.createNotificationChannel(mChannel);
        }


        content_intent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.noti_layout, content_intent);

        // click events
        prev_intent = new Intent(getApplicationContext(), NotificationIntentService.class);
        prev_intent.putExtra("id", -1);
        prev_intent.putExtra("music_index", music_index);
        prev_intent.putExtra("is_play", mp.isPlaying());
        prev_p_intent = PendingIntent.getBroadcast(this, -1, prev_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.music_prev, prev_p_intent);

        next_intent = new Intent(getApplicationContext(), NotificationIntentService.class);
        next_intent.putExtra("id", 1);
        next_intent.putExtra("music_index", music_index);
        next_intent.putExtra("is_play", mp.isPlaying());
        next_p_intent = PendingIntent.getBroadcast(this, 1, next_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.music_next, next_p_intent);

        play_intent = new Intent(getApplicationContext(), NotificationIntentService.class);
        play_intent.putExtra("id", 0);
        play_intent.putExtra("music_index", music_index);
        play_intent.putExtra("is_play", mp.isPlaying());
        play_p_intent = PendingIntent.getBroadcast(this, 0, play_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.music_now, play_p_intent);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("notificationMessage", "message");
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.gallary1)
                .setCustomContentView(customView)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setFullScreenIntent(pendingIntent,true)
                .setContentIntent(pendingIntent);

        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        notification = builder.build();

        notificationManager.notify(0, notification);
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
