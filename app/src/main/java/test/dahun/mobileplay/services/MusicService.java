package test.dahun.mobileplay.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.events.DurationEvent;
import test.dahun.mobileplay.events.FinishMusicEvent;
import test.dahun.mobileplay.events.GetSongPlayInfoEvent;
import test.dahun.mobileplay.events.IsPlayEvent;
import test.dahun.mobileplay.events.SeekbarEvent;
import test.dahun.mobileplay.events.TimerEvent;
import test.dahun.mobileplay.interfaces.ApplicationStatus;


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
//                setNotification();
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


                ApplicationStatus.isPlaying = true;

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
                break;

            case "pause":
                pos = mp.getCurrentPosition();
                mp.pause();
                BusProvider.getInstance().post(new IsPlayEvent(mp.isPlaying()));

                ApplicationStatus.isPlaying = false;
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

    /*public void setNotification() {
        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.layout_notification);
        customView.setImageViewResource(R.id.img_noti, albumarr.get(music_index));
        customView.setTextViewText(R.id.title_noti, musicarr.get(music_index));

        // click events
        Intent prev_intent = new Intent("prev_click");
        prev_intent.putExtra("id", -1);
        prev_intent.putExtra("music_index", music_index);
        prev_intent.putExtra("is_play", mp.isPlaying());
        PendingIntent prev_p_intent = PendingIntent.getBroadcast(this, -1, prev_intent, 0);
        customView.setOnClickPendingIntent(R.id.music_prev, prev_p_intent);

        Intent next_intent = new Intent("next_click");
        next_intent.putExtra("id", 1);
        next_intent.putExtra("music_index", music_index);
        next_intent.putExtra("is_play", mp.isPlaying());
        PendingIntent next_p_intent = PendingIntent.getBroadcast(this, 1, next_intent, 0);
        customView.setOnClickPendingIntent(R.id.music_next, next_p_intent);

        Intent play_intent = new Intent("play_click");
        play_intent.putExtra("id", 0);
        play_intent.putExtra("music_index", music_index);
        play_intent.putExtra("is_play", mp.isPlaying());
        PendingIntent play_p_intent = PendingIntent.getBroadcast(this, 0, play_intent, 0);
        customView.setOnClickPendingIntent(R.id.music_now, play_p_intent);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.booklet_img_01)
                        .setCustomContentView(customView);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }*/


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
        albumarr.add(R.drawable.albumimg_02); musicarr.add("신현희와김루트");
        albumarr.add(R.drawable.albumimg_03); musicarr.add("오빠야");
        albumarr.add(R.drawable.albumimg_04); musicarr.add("Cap Song");
        albumarr.add(R.drawable.albumimg_05); musicarr.add("집 비던날");
        albumarr.add(R.drawable.albumimg_06); musicarr.add("편한노래");
        albumarr.add(R.drawable.albumimg_02); musicarr.add("날개");
    }

}
