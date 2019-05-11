package test.dahun.mobileplay.tab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.otto.Subscribe;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.events.GetSongPlayInfoEvent;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.interfaces.ServiceStateInterface;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.services.BusProvider;
import test.dahun.mobileplay.services.MusicService;
import test.dahun.mobileplay.services.MusicService.LocalBinder;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener listener;
    ImageButton mv_close_btn;
    TextView mv_pop_title;

    public static final String API_KEY = "AIzaSyDi54gnjDssDmXAfG1X-rJs4OmuYjH8iGM";

    public String video_id = "";
    public String video_name = "";

    private boolean isPlaying = false;
    private boolean musicPlayed = false;

    int music_index =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
//        getService();
        getVideoInfo();
        initSetting();
        musicSetting();
        closeActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(musicPlayed){
            Log.i("videoooo", "index / play : "+music_index);
            Log.i("videoooo", "play!");
            Intent intent_service = new Intent(this, MusicService.class);
            intent_service.putExtra("index", music_index);
            intent_service.putExtra("state", "play");
            startService(intent_service);
        }*/
    }


    public void getVideoInfo(){
        Intent intent = getIntent();
        video_id = intent.getExtras().getString("video_id");
        video_name = intent.getExtras().getString("video_name");
        music_index = intent.getExtras().getInt("music_index", 0);
        Log.i("gomgom", "id : "+video_id);
        Log.i("gomgom", "name : "+video_name);
    }

    public void initSetting(){
        youTubePlayerView=(YouTubePlayerView) findViewById(R.id.youtube);
        mv_close_btn=(ImageButton) findViewById(R.id.mv_close_btn);
        mv_pop_title=(TextView) findViewById(R.id.mv_pop_title);
        if(mv_pop_title != null) mv_pop_title.setText(video_name);


        //리스너 등록부분
        listener = new YouTubePlayer.OnInitializedListener(){

            //초기화 성공시
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(video_id); //url의 맨 뒷부분 ID값만 넣으면 됨
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }

        };

        youTubePlayerView.initialize(API_KEY,this);

    }

    public void musicSetting(){
        if(ApplicationStatus.isPlaying){
            Log.i("videoooo", "pause!");
            Intent intent_service = new Intent(this, MusicService.class);
            intent_service.putExtra("state", "pause");
            startService(intent_service);
            musicPlayed = true;
        }
    }

    public void closeActivity(){
         if(mv_close_btn != null) {
//             mv_close_btn.setOnClickListener(view -> finish());
             mv_close_btn.setOnClickListener(v -> {
                 if(musicPlayed){
                     Intent intent_service = new Intent(getApplicationContext(), MusicService.class);
                     intent_service.putExtra("index", music_index);
                     intent_service.putExtra("state", "play");
                     startService(intent_service);
                     musicPlayed = false;
                 }
                 finish();
             });
         }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(null== youTubePlayer) return;

        //Start buffering
        if (!b) {
            //youTubePlayer.cueVideo(video_id); // 자동재생 X
            youTubePlayer.loadVideo(video_id); // 자동재생 O
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();


    }
}
