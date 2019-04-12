package test.dahun.mobileplay.tab;

import android.content.Intent;
import android.os.Bundle;
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
import test.dahun.mobileplay.services.BusProvider;
import test.dahun.mobileplay.services.MusicService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        eventBus();
        getVideoInfo();
        initSetting();
        closeActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(musicPlayed){
            Intent intent_service = new Intent(this, MusicService.class);
            intent_service.putExtra("state", "play");
            startService(intent_service);

        }
        BusProvider.getInstance().unregister(this);
    }

    public void eventBus(){
        BusProvider.getInstance().register(this);
    }

    // 초기화
    @Subscribe
    public void FinishLoad(GetSongPlayInfoEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        isPlaying = mEvent.getIsPlay();
        if(isPlaying && !musicPlayed){
            Intent intent_service = new Intent(this, MusicService.class);
            intent_service.putExtra("state", "pause");
            startService(intent_service);
            musicPlayed = true;
        }
    }

    public void getVideoInfo(){
        Intent intent = getIntent();
        video_id = intent.getExtras().getString("video_id");
        video_name = intent.getExtras().getString("video_name");
        Log.i("gomgom", "id : "+video_id);
        Log.i("gomgom", "name : "+video_name);
    }

    public void initSetting(){
        youTubePlayerView=(YouTubePlayerView) findViewById(R.id.youtube);
        mv_close_btn=(ImageButton) findViewById(R.id.mv_close_btn);
        mv_pop_title=(TextView) findViewById(R.id.mv_pop_title);
        mv_pop_title.setText(video_name);


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

    public void closeActivity(){
        mv_close_btn.setOnClickListener(view -> finish());
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
