
package test.dahun.mobileplay.tab;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.MusicCustomPagerAdapter;
import test.dahun.mobileplay.adapter.MusicInfoItem;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.model.ApplicationStatus;
import test.dahun.mobileplay.model.ICounterService;
import test.dahun.mobileplay.model.MusicService;
import test.dahun.mobileplay.ui.VerticalViewPager;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class MusicFragment extends Fragment
{
    @BindView(R.id.whole_layout) RelativeLayout whole_layout;

    @BindView(R.id.album_img) LinearLayout album_img;
    @BindView(R.id.bg_img) ImageView bg_img;
    @BindView(R.id.heart_touch_area) LinearLayout heart_touch_area;

    @BindView(R.id.title) TextView title;
    @BindView(R.id.singer) TextView singer;

    @BindView(R.id.btn_prevplay) ImageButton btn_prevplay;
    @BindView(R.id.btn_play) ImageButton btn_play;
    @BindView(R.id.btn_nextplay) ImageButton btn_nextplay;

    @BindView(R.id.btn_lyric) ImageButton btn_lyric;
    @BindView(R.id.heart) ImageButton heart;
    @BindView(R.id.heart_num) TextView heart_num;
    @BindView(R.id.btn_repeat) ImageButton btn_repeat;

    @BindView(R.id.currentTime) TextView currentTime;
    @BindView(R.id.maxTime) TextView maxTime;

    @BindView(R.id.home_btn) ImageButton home_btn;
    @BindView(R.id.list_btn) ImageButton list_btn;
    @BindView(R.id.play_btn) ImageButton play_btn;
    @BindView(R.id.gallery_btn) ImageButton gallery_btn;
    @BindView(R.id.sns_btn) ImageButton sns_btn;

    @BindView(R.id.musicPager) VerticalViewPager musicPager;

    @BindView(R.id.musicProgress) SeekBar seekBar; // 음악 재생위치를 나타내는 시크바

    final String TAG="MusicFragment";
    RelativeLayout layout;

    //음악 관련 변수
    int index=0;
    int pos; // 재생 멈춘 시점
    boolean isPlaying = false; // 재생중인지 확인할 변수
    boolean restart = false; // 재생중인지 확인할 변수
    int total;

    Timer timer=null;
    TimerHandler timerHandler;
    int time=1;
    String totalTime="";

    //가사 Popup
    TextView lyricsText;

    //음악리스트 Popup
    PopupWindow popup;

    //노래 제목 리스트
    ArrayList<String> musicarr;

    //like 갯수
    ArrayList<Integer> like_count;

    //auto play 할건지
    boolean isAutoPlay = false;

    //화면 새로고침은 처음만
    int refresh = 0;

    //곡 제목, 작곡, 작사, 편곡
    ArrayList<MusicInfoItem> music_info;

    //플레이모드 (0: 전체재생 1: 한곡재생 2: 반복없음)
    int play_mode = 0;

    //사용자 스크롤 방향
    boolean checkDirection, scrollStarted = false;

    boolean running = true;

    //서비스 연결
    MusicService mService;
    boolean mBound = false;

    class MusicThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            while(isPlaying) {
                int progress = mService.getCurrentPosition();
//                seekBar.setProgress(mp.getCurrentPosition());
                seekBar.setProgress(progress);
            }

            try {
                Thread.sleep(1000); // 1초간 Thread를 잠재운다
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 현재 재생시간 표시 위해 만듦
    // handler로 현재 재생시간 전달
    class Timer extends Thread {
        Message message;

        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            while (running) {
                // 스레드에게 수행시킬 동작들 구현
                message = timerHandler.obtainMessage();
                message.arg1 = time;
                timerHandler.sendMessage(message);
                time++;

                try {
                    Thread.sleep(1000); // 1초간 Thread를 잠재운다
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 현재 재생시간 표시
    class TimerHandler extends Handler{
        public void handleMessage(Message msg) {
            currentTime.setText(timeTranslation(msg.arg1));
        }
    }

    public MusicFragment() {
        super();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this, layout);
        getService();
        makeData();
        initSetting();
        musicPagerSetting();
        seekBarSetting();
        playBtnSetting();
        btnSetting();
        lyrics_popupSetting();
        playmode();
        like_music();
        switch_music();
        return layout;
    }

    public void getService(){
        Intent intent = new Intent(getContext(), MusicService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private  ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initSetting() {
        heart.setTag(0); // 하트의 상태 / 0 : off / 1 : on
        title.setText(musicarr.get(0)); // 첫번째 노래제목
        heart_num.setText(String.valueOf(like_count.get(0))); // 첫번째 노래 하트개수

        Glide.with(getContext()).load(R.drawable.btn_prevplay)
                .apply(new RequestOptions().fitCenter()).into(btn_prevplay);
        Glide.with(getContext()).load(R.drawable.btn_nextplay)
                .apply(new RequestOptions().fitCenter()).into(btn_nextplay);
        btn_prevplay.setAlpha(0.5f); // 첫번째곡 이전버튼 반투명

        Glide.with(getContext()).load(R.drawable.btn_lyrics)
                .apply(new RequestOptions().fitCenter()).into(btn_lyric);

        DrawableImageViewTarget imageViewTarget2 = new DrawableImageViewTarget(btn_play);
        Glide.with(getContext()).load(R.drawable.btn_play)
                .apply(new RequestOptions().fitCenter()).into(imageViewTarget2);

        Glide.with(getContext()).load(R.drawable.bg_play1)
                .apply(new RequestOptions().fitCenter()).into(bg_img);


        // equalizer setting
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.mn_play_on).into(imageViewTarget);

        timerHandler = new TimerHandler();
        currentTime.setText("00:00");

        if(isAutoPlay){
            autoPlay(total);
        }
    }

    public void musicPagerSetting(){
        musicPager.setAdapter(new MusicCustomPagerAdapter(getContext()));
        musicPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(checkDirection) {
                    if(positionOffset < 0.5f){ // 사용자가 아래서 위로 스와이프
                        if(position == musicarr.size()-1){ // 마지막 곡일 때
                            Toast.makeText(getContext(), "마지막 곡입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else { // 사용자가 위에서 아래로 스와이프

                    }
                    checkDirection = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                //change icon
                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);

                //초기화
                time=0;
                index=position;
                DrawableImageViewTarget imageViewTarget2 = new DrawableImageViewTarget(btn_play);
                Glide.with(getContext()).load(R.drawable.btn_pause)
                        .apply(new RequestOptions().fitCenter()).into(imageViewTarget2);
//                btn_play.setBackgroundResource(R.drawable.btn_pause);
                seekBar.setProgress(0);

                //노래제목
                title.setText(musicarr.get(position));

                //하트 개수
                setHeartNum(like_count.get(position));

                //music control
                changeMusic(position);
                changeLyrics(position);
                changePlay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if( !scrollStarted && state == VerticalViewPager.SCROLL_STATE_DRAGGING){
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });
    }

    public VerticalViewPager getViewPager(){
        return musicPager;
    }

    public void seekBarSetting(){
        // 시크바 초기세팅
        seekBar.setProgress(0);

        // 시크바 색상 변경
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#80f2efe0"), PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        // 시크바 세팅
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) { // 누르고 뗐을 때
                DrawableImageViewTarget imageViewTarget2 = new DrawableImageViewTarget(btn_play);
                Glide.with(getContext()).load(R.drawable.btn_pause)
                        .apply(new RequestOptions().fitCenter()).into(imageViewTarget2);
//                btn_play.setBackgroundResource(R.drawable.btn_pause);

                isPlaying = true;
                ApplicationStatus.isPlaying=true;

                int seekBar_position = seekBar.getProgress(); // 사용자가 움직여놓은 위치
//                mp.seekTo(seekBar_position);
//                mp.start();
                Intent intent = new Intent(getContext(), MusicService.class);
                intent.putExtra("index", index);
                intent.putExtra("seekBar_position", seekBar_position);
                intent.putExtra("state", "play");
                getActivity().startService(intent);

                time = seekBar_position/1000;
//                time=mp.getCurrentPosition()/1000;
                currentTime.setText(timeTranslation(time));
                new MusicThread().start();

                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
            }
            public void onStartTrackingTouch(SeekBar seekBar) { // 눌러서 움직일 때
                if(timer==null){
                    timer=new Timer();
                    timer.start();
                }

                isPlaying = false;

                if(!mService.mpIsPlaying()){
                    seekBar.setMax(total);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
                    new MusicThread().start(); // 씨크바 그려줄 쓰레드 시작
                } else{
//                    mp.pause();
                    Intent intent = new Intent(getContext(), MusicService.class);
                    intent.putExtra("index", index);
                    intent.putExtra("state", "pause");
                    getActivity().startService(intent);
                }
                int seekBar_position = seekBar.getProgress();
                time = seekBar_position/1000;
//                time=mp.getCurrentPosition()/1000;
                currentTime.setText(timeTranslation(time));
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });
    }

    public void playBtnSetting(){
        btn_play.setOnClickListener(new View.OnClickListener() { // 재생버튼
            @Override
            public void onClick(View v) {
                if(isPlaying) { // 재생중일 때
                    DrawableImageViewTarget imageViewTarget2 = new DrawableImageViewTarget(btn_play);
                    Glide.with(getContext()).load(R.drawable.btn_play)
                            .apply(new RequestOptions().fitCenter()).into(imageViewTarget2);
                    ApplicationStatus.isPlaying=false;
                    DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                    Glide.with(getContext()).load(R.drawable.mn_play_on).into(imageViewTarget);

                    // 일시중지
                    Intent intent = new Intent(getContext(), MusicService.class);
                    intent.putExtra("index", index);
                    intent.putExtra("state", "pause");
                    getActivity().startService(intent);
                    running = false;

                    isPlaying = false; // 쓰레드 정지
                    restart=true;

                } else { // 정지일 때
                    DrawableImageViewTarget imageViewTarget2 = new DrawableImageViewTarget(btn_play);
                    Glide.with(getContext()).load(R.drawable.btn_pause)
                            .apply(new RequestOptions().fitCenter()).into(imageViewTarget2);
                    ApplicationStatus.isPlaying=true;
                    DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                    Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
                    if(restart){
                        // 멈춘 지점부터 재시작
                        Intent intent = new Intent(getContext(), MusicService.class);
                        intent.putExtra("index", index);
                        intent.putExtra("state", "play");
                        intent.putExtra("restart", true);
                        getActivity().startService(intent);
                        running = true;
                        timer=new Timer();
                        timer.start();

                        isPlaying = true; // 재생하도록 flag 변경
                        new MusicThread().start(); // 쓰레드 시작

                    } else {
                        Intent intent = new Intent(getContext(), MusicService.class);
                        intent.putExtra("index", index);
                        intent.putExtra("state", "play");
                        intent.putExtra("restart", false);
                        getActivity().startService(intent);
                        running = true;

                        seekBar.setMax(total);// 씨크바의 최대 범위를 노래의 재생시간으로 설정

                        isPlaying = true; // 씨크바 쓰레드 반복 하도록
                        new MusicThread().start(); // 씨크바 그려줄 쓰레드 시작

                        timer=new Timer();
                        timer.start();

                    }
                }
            }
        });
    }


    public void btnSetting(){
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(0);
            }
        });
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(1);
            }
        });
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(2);
            }
        });
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(3);
            }
        });
        sns_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(4);
            }
        });
    }


    public void lyrics_popupSetting() {

        //팝업으로 띄울 커스텀뷰를 설정하고
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_lyrics, null);



        btn_lyric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //클릭시 팝업 윈도우 생성
                popup = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);

                RelativeLayout popupLayout = (RelativeLayout)layout.findViewById(R.id.whole_layout);
                popup.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);

                ImageButton btn_close = (ImageButton) popupView.findViewById(R.id.btn_close);
                TextView title = (TextView) popupView.findViewById(R.id.title);
                TextView info = (TextView) popupView.findViewById(R.id.info);
                title.setText(music_info.get(index).getMusic_title());
                info.setText("작곡 : "+music_info.get(index).getComposition()+"\n"+
                        "작사 : "+music_info.get(index).getWriter()+"\n"+
                        "편곡 : "+music_info.get(index).getArrangement()+"\n");

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                    }
                });

            }
        });

        lyricsText=(TextView)popupView.findViewById(R.id.lyricsText);

        AssetManager am = getContext().getAssets();
        try {
            InputStream inputStream = am.open("first.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader br = new BufferedReader(inputStreamReader);

            String read=null;
            String lyrics="";

            while((read=br.readLine())!=null){
                lyrics+=read;
                lyrics+="\n";
            }

            lyricsText.setText(lyrics);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void autoPlay(int total){
        seekBar.setMax(total);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
        isPlaying = true; // 씨크바 쓰레드 반복 하도록
        new MusicThread().start(); // 씨크바 그려줄 쓰레드 시작
        timer=new Timer();
        timer.start();
    }

    public void changePlay(int index){
        switch (index){
            case 0:
                btn_prevplay.setAlpha(0.5f);
                btn_nextplay.setAlpha(1f);
                break;
            case 10:
                btn_prevplay.setAlpha(1f);
                btn_nextplay.setAlpha(0.5f);
                break;
            default:
                btn_prevplay.setAlpha(1f);
                btn_nextplay.setAlpha(1f);
                break;
        }
    }

    public void changeMusic(final int index){
        endMusic();

        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", index);
        intent.putExtra("state", "play");
        getActivity().startService(intent);

        int total = mService.getDuration(); // 노래의 재생시간(miliSecond)
        String time=timeTranslation(total/1000);
        maxTime.setText(time);
        seekBar.setMax(total);// 씨크바의 최대 범위를 노래의 재생시간으로 설정

        isPlaying = true; // 씨크바 쓰레드 반복 하도록
        ApplicationStatus.isPlaying = true;
        new MusicThread().start(); // 씨크바 그려줄 쓰레드 시작
    }

    // 가사
    public void changeLyrics(int index){
        AssetManager am = getContext().getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read="";
        String lyrics="";

        switch(index){
            case 0:
                try {
                    inputStream = am.open("first.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    inputStream = am.open("second.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    inputStream = am.open("third.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    inputStream = am.open("fourth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    inputStream = am.open("fifth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    inputStream = am.open("sixth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    inputStream = am.open("seventh.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    inputStream = am.open("first.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    inputStream = am.open("second.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                try {
                    inputStream = am.open("third.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    inputStream = am.open("fourth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        lyricsText.setText(lyrics);


    }

    // 재생중인 음악 종료
    public void endMusic(){
        isPlaying = false; // 쓰레드 종료
        restart = false;
        /*if(mp.isPlaying()){
            mp.stop(); // 멈춤
            mp.reset();
            mp.release(); // 자원 해제
        }
        mp = changeMusicPlayer(index);*/

        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", index);
        intent.putExtra("state", "stop");
        getActivity().startService(intent);

        seekBar.setProgress(0); // 씨크바 초기화
        currentTime.setText("00:00");
        time = 0;
    }

    // 현재시간 나타내기
    public String timeTranslation(int time){
        int minutes=time/60;
        int second=time-minutes*60;
        String result="";
        result+=String.format("%02d", minutes);
        result+=":";
        result+=String.format("%02d", second);
        return result;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("SetUserHint","Music ON");
            total = mService.getDuration(); // 노래의 재생시간(miliSecond)
            totalTime = timeTranslation(total/1000); // minute - second
            maxTime.setText(totalTime);

            View view = layout;
            if (view != null) {
                if(refresh == 0){
                    getFragmentManager().beginTransaction()
                            .detach(this)
                            .attach(this)
                            .commit();
                    refresh++;
                }

                // 음악 바로가기
                if(MainActivity.getState() == 0){
                    int position = MainActivity.getPosition();
                    musicPager.setCurrentItem(position);
                    MainActivity.setState(1);
                }

                DrawableImageViewTarget
                        imageViewTarget = new DrawableImageViewTarget(play_btn);
                if (ApplicationStatus.isPlaying)
                    Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
                else
                    Glide.with(getContext()).load(R.drawable.mn_play_on).into(imageViewTarget);
            }
        }
        else
            Log.d("SetUserHint","Music OFF");

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mBound){
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    // 전체재생, 1곡재생 눌렀을 때
    public void playmode(){
        btn_repeat.setTag("all");
        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag().equals("all")){
                    btn_repeat.setBackgroundResource(R.drawable.btn_repeatone);
                    btn_repeat.setTag("one");
                    Intent intent = new Intent(getContext(), MusicService.class);
                    intent.putExtra("play_mode", 1);
                    intent.putExtra("state", "play_mode");
                    getActivity().startService(intent);
                }else if(view.getTag().equals("one")){
                    btn_repeat.setBackgroundResource(R.drawable.btn_repeatall);
                    btn_repeat.setAlpha(0.5f);
                    btn_repeat.setTag("none");
                    Intent intent = new Intent(getContext(), MusicService.class);
                    intent.putExtra("play_mode", 2);
                    intent.putExtra("state", "play_mode");
                    getActivity().startService(intent);
                }else if(view.getTag().equals("none")){
                    btn_repeat.setAlpha(1f);
                    btn_repeat.setTag("all");
                    Intent intent = new Intent(getContext(), MusicService.class);
                    intent.putExtra("play_mode", 0);
                    intent.putExtra("state", "play_mode");
                    getActivity().startService(intent);
                }
            }
        });
    }

    // 하트 눌렀을때
    public void like_music(){
        heart_touch_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer)heart.getTag() == 0){
                    heart.setBackgroundResource(R.drawable.like_on);
                    int current_like_count = like_count.get(index)+1;
                    setHeartNum(current_like_count);
                    heart.setTag(1);
                } else if ((Integer)heart.getTag() == 1){
                    heart.setBackgroundResource(R.drawable.like_off);
                    int current_like_count = like_count.get(index)-1;
                    setHeartNum(current_like_count);
                    heart.setTag(0);
                }
            }
        });
    }

    // <- -> 버튼 눌렀을 때
    public void switch_music(){
        //다음
        btn_nextplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index != musicarr.size()-1)
                    musicPager.setCurrentItem(index+1);
                else
                    Toast.makeText(getContext(), "마지막 곡입니다", Toast.LENGTH_SHORT).show();
            }
        });

        //이전
        btn_prevplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index !=0)
                    musicPager.setCurrentItem(index-1);
            }
        });
    }

    // 하트갯수 수정
    public void setHeartNum(int current_like_count){
        String heart_count = "";
        if(current_like_count >= 1000)  heart_count = (current_like_count/1000)+"k";
        else heart_count = String.valueOf(current_like_count);
        like_count.set(index, current_like_count);
        heart_num.setText(heart_count);
    }

    public void makeData(){
        // make title data - database 연동예정
        musicarr = new ArrayList<>();
        like_count = new ArrayList<>();
        musicarr.add("Big Love");  like_count.add(13);
        musicarr.add("좋아해줘");  like_count.add(1789);
        musicarr.add("Dientes");   like_count.add(542);
        musicarr.add("Stand Still"); like_count.add(486);
        musicarr.add("상아");  like_count.add(992);
        musicarr.add("강아지");  like_count.add(96);
        musicarr.add("Antifreeze");  like_count.add(9);
        musicarr.add("Kiss And Tell");  like_count.add(75);
        musicarr.add("LE Fu Muet");  like_count.add(123);
        musicarr.add("Diamond");  like_count.add(47);
        musicarr.add("난 아니에요");  like_count.add(7);

        // make music info - database 연동예정
        music_info = new ArrayList<>();
        music_info.add(new MusicInfoItem("Big Love", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("좋아해줘", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("Dientes", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("Stand Still", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("상아", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("강아지", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("Antifreeze", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("Kiss And Tell", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("Le Fou Muet", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("Diamond", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("난 아니에요", "검정치마", "검정치마", "검정치마"));
    }


}
