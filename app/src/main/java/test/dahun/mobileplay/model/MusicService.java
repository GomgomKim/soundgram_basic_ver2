package test.dahun.mobileplay.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.util.ArrayList;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.tab.MusicFragment;

public class MusicService extends Service {

    MediaPlayer mp;
    int pos=0;
    int play_mode=0;
    int music_index=0;

    int current_state=0;

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mp = changeMusicPlayer(0); //mp 초기화
    }

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

    public int getCurrentPosition(){
        return mp.getCurrentPosition();
    }

    public boolean mpIsPlaying(){
        return mp.isPlaying();
    }

    public int getDuration(){
        return mp.getDuration();
    }

    public int getCurrentState(){
        return current_state;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        music_index = intent.getExtras().getInt("index", 0);
        String state = intent.getExtras().getString("state");
        boolean restart = intent.getExtras().getBoolean("restart");
        int seekBarPosition = intent.getExtras().getInt("seekBar_position", -1);

        switch (state){
            case "play":
                showNotification();
                if(restart){
                    mp.seekTo(pos);
                } else{
                    mp.setLooping(false);
                    mp = changeMusicPlayer(music_index);
                    if(seekBarPosition != -1){
                        mp.seekTo(seekBarPosition);
                    }
                }
                mp.start();
                break;
            case "stop":
                if(mp.isPlaying()){
                    mp.stop(); // 멈춤
                    mp.reset();
                    mp.release(); // 자원 해제
                    mp = changeMusicPlayer(music_index);
                }
                break;
            case "pause":
                pos = mp.getCurrentPosition();
                mp.pause();
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
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                switch (play_mode){
                    case 0:
                        /*if(mp.isPlaying()){
                            mp.stop(); // 멈춤
                            mp.reset();
                            mp.release(); // 자원 해제
                            mp = changeMusicPlayer(music_index);
                        }
                        if(music_index == 10){
                            mp = changeMusicPlayer(0);
                            mp.start();
                        } else{
                            mp = changeMusicPlayer(music_index+1);
                            mp.start();
                        }*/
                        current_state = 1; // 다음곡
                        break;
                    case 1:
                        /*mp.stop(); // 멈춤
                        mp.reset();
                        mp.release(); // 자원 해제
                        mp = changeMusicPlayer(music_index);
                        mp.start();*/
                        current_state = 2; // 현재곡
                        break;
                    case 2:
                        if(mp.isPlaying()){
                            mp.stop(); // 멈춤
                            mp.reset();
                            mp.release(); // 자원 해제
                        }
                        break;
                }
            }
        });
    }


    public void showNotification(){
        NotificationCompat.Builder mBuilder = createNotification();

        //커스텀 화면 만들기
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setImageViewResource(R.id.img_noti, R.drawable.albumimg_01);
        remoteViews.setTextViewText(R.id.title_noti, "Big Love");

        //노티피케이션에 커스텀 뷰 장착
        mBuilder.setContent(remoteViews);
        mBuilder.setContentIntent(createPendingIntent());

        //클릭이벤트
        Intent intent_ = new Intent("test.dahun.mobileplay.tab.MusicFragment");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent_,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.music_now, pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }



    private PendingIntent createPendingIntent(){
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }


    private NotificationCompat.Builder createNotification(){
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle("StatusBar Title")
                .setContentText("StatusBar subTitle")
                .setSmallIcon(R.mipmap.ic_launcher/*스와이프 전 아이콘*/)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        return builder;
    }

    public MediaPlayer changeMusicPlayer(int index){
        switch(index){
            case 0:
                mp = MediaPlayer.create(this, R.raw.biglove);
                break;
            case 1:
                mp = MediaPlayer.create(this, R.raw.everything);
                break;
            case 2:
                mp = MediaPlayer.create(this, R.raw.free_land);
                break;
            case 3:
                mp = MediaPlayer.create(this, R.raw.hollywood);
                break;
            case 4:
                mp = MediaPlayer.create(this, R.raw.if_not_me);
                break;
            case 5:
                mp = MediaPlayer.create(this, R.raw.international_love_song);
                break;
            case 6:
                mp = MediaPlayer.create(this, R.raw.kisado);
                break;
            case 7:
                mp = MediaPlayer.create(this, R.raw.love_shine);
                break;
            case 8:
                mp = MediaPlayer.create(this, R.raw.my_home_seoul);
                break;
            case 9:
                mp = MediaPlayer.create(this, R.raw.our_young_love);
                break;
            case 10:
                mp = MediaPlayer.create(this, R.raw.if_not_me);
                break;
        }
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

        setNextPlay();

        return mp;
    }

}
