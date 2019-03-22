
package test.dahun.mobileplay.tab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.MusicCustomPagerAdapter;
import test.dahun.mobileplay.adapter.MusicInfoItem;
import test.dahun.mobileplay.events.DurationEvent;
import test.dahun.mobileplay.events.EndFragEvent;
import test.dahun.mobileplay.events.FinishMusicEvent;
import test.dahun.mobileplay.events.GetSongPlayInfoEvent;
import test.dahun.mobileplay.events.IsPlayEvent;
import test.dahun.mobileplay.events.SeekbarEvent;
import test.dahun.mobileplay.events.TimerEvent;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.services.BusProvider;
import test.dahun.mobileplay.services.MusicService;
import test.dahun.mobileplay.ui.VerticalViewPager;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;


/**
 * Created by gomgomKim on 2019. 2. 11..
 */

public class MusicFragment extends Fragment
{
    @BindView(R.id.like_gif) ImageView like_gif;

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

    @BindView(R.id.musicPager) VerticalViewPager musicPager;

    @BindView(R.id.musicProgress) SeekBar seekBar; // 음악 재생위치를 나타내는 시크바

    @BindView(R.id.play_btn) ImageButton play_btn;
    @BindView(R.id.home_btn) ImageButton home_btn;
    @BindView(R.id.list_btn) ImageButton list_btn;
    @BindView(R.id.gallery_btn) ImageButton gallery_btn;
    @BindView(R.id.sns_btn) ImageButton sns_btn;




    final String TAG="MusicFragment";
    RelativeLayout layout;

    //음악 관련 변수
    int index=0;
    int pos; // 재생 멈춘 시점
    boolean isPlaying = false; // 재생중인지 확인할 변수

    int time=1;

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

    //서비스 연결
    MusicService mService;
    boolean mBound = false;

    // 자동넘어감
    boolean auto_move = false;

    //하트 애니메이션
    private AnimationDrawable frameAnimation;


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
        eventBus();
        makeData();
        initSetting();
        btnSetting();
        initPlay();
        musicPagerSetting();
        seekBarSetting();
        playBtnSetting();
        lyrics_popupSetting();
        playmode();
        like_music();
        switch_music();
        return layout;
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().post(new EndFragEvent(true));
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    public void eventBus(){
        BusProvider.getInstance().register(this);
    }

    // 초기화
    @Subscribe
    public void FinishLoad(GetSongPlayInfoEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        seekBar.setMax(mEvent.getDuration());
        maxTime.setText(timeTranslation(mEvent.getDuration()/1000));
        isPlaying = mEvent.getIsPlay();

        musicPager.setCurrentItem(mEvent.getPosition());
        if(isPlaying){
            Glide.with(getContext()).load(R.drawable.btn_pause)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(play_btn);
        } else{
            Glide.with(getContext()).load(R.drawable.btn_play)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
            Glide.with(getContext()).load(R.drawable.mn_play_off).into(play_btn);
        }
    }

    // 플레이 이벤트
    @Subscribe
    public void FinishLoad(IsPlayEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        isPlaying = mEvent.isPlaying();
        if(isPlaying == true) { // 재생
            Glide.with(getContext()).load(R.drawable.btn_pause)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(play_btn);
        } else{ // 정지
            Glide.with(getContext()).load(R.drawable.btn_play)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
            Glide.with(getContext()).load(R.drawable.mn_play_off).into(play_btn);
        }
    }

    // 타이머 이벤트
    @Subscribe
    public void FinishLoad(TimerEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        currentTime.setText(timeTranslation(mEvent.getTime()));
    }

    // 음악 길이 이벤트
    @Subscribe
    public void FinishLoad(DurationEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        maxTime.setText(timeTranslation(mEvent.getDuration()/1000));
        seekBar.setMax(mEvent.getDuration());
    }

    // 시크바 이벤트
    @Subscribe
    public void FinishLoad(SeekbarEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        seekBar.setProgress(mEvent.getSeekPosition());
    }

    // 음악종료 이벤트
    @Subscribe
    public void FinishLoad(FinishMusicEvent mEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        switch (mEvent.getState()){
            case 0:
                ApplicationStatus.isPlaying = true;
                auto_move = true;
                if(index == 7){
                    musicPager.setCurrentItem(0);
                } else{
                    musicPager.setCurrentItem(index+1);
                }
                seekBar.setProgress(0);
                currentTime.setText(timeTranslation(0));
                Glide.with(getContext()).load(R.drawable.mn_play_off).into(play_btn);
                break;
            case 1:
                ApplicationStatus.isPlaying = true;
                break;
            case 2:
                seekBar.setProgress(0);
                currentTime.setText(timeTranslation(0));
                Glide.with(getContext()).load(R.drawable.btn_play)
                        .apply(new RequestOptions().fitCenter()).into(btn_play);
                ApplicationStatus.isPlaying = false;
                Glide.with(getContext()).load(R.drawable.mn_play_off).into(play_btn);
                break;
        }
    }

    public void getService(){
        Intent intent = new Intent(getContext(), MusicService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection mConnection = new ServiceConnection() {
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
        singer.setText("검정치마");

        Glide.with(getContext()).load(R.drawable.mn_play_off).into(play_btn);

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

        if(isPlaying){
            Glide.with(getContext()).load(R.drawable.btn_pause)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        } else{
            Glide.with(getContext()).load(R.drawable.btn_play)
                    .apply(new RequestOptions().fitCenter()).into(btn_play);
        }

        currentTime.setText("00:00");
    }

    public void initPlay(){
        auto_move = true;
        if(MainActivity.getState() == 0){
            index = MainActivity.getPosition();
            musicPager.setCurrentItem(index);
            title.setText(musicarr.get(index));
            music_stop();
            music_play();
        }
        MainActivity.setState(1);
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
                index = position;
                if(!auto_move){
                    if(isPlaying) music_stop();
                    music_play();
                }
                currentTime.setText(timeTranslation(0));
                seekBar.setProgress(0);
                auto_move = false;
                changePlay();
                changeLyrics(index);
                setHeartNum(like_count.get(index));
                title.setText(musicarr.get(position));
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

    public void seekBarSetting(){
        // 시크바 초기세팅
        seekBar.setProgress(0);

        // 시크바 색상 변경
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#80f2efe0"), PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        // thumb 수정
        seekBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // custom seekbar thumb
                /*if (seekBar.getHeight() > 0) {
                    Drawable thumb = getResources().getDrawable(R.drawable.play_page_on);
                    int h = seekBar.getMeasuredHeight();
                    int w = h;
                    Bitmap bmpOrg = ((BitmapDrawable) thumb).getBitmap();
                    Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                    Drawable newThumb = new BitmapDrawable(getResources(), bmpScaled);
                    newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                    seekBar.setThumb(newThumb);
                    seekBar.getViewTreeObserver().removeOnPreDrawListener(this);
                }*/
                return true;
            }
        });

        // 시크바 세팅
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) { // 누르고 뗐을 때
                music_seek_play(seekBar.getProgress());
            }
            public void onStartTrackingTouch(SeekBar seekBar) { // 눌러서 움직일 때
                if(isPlaying) music_pause();
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });
    }

    public void playBtnSetting(){ // 재생버튼
        btn_play.setOnClickListener(v -> {
            if(isPlaying) { // 재생중일 때
                music_pause();
            } else { // 정지일 때
                music_play();
            }
        });
    }

    public void btnSetting(){
        home_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(0));
        list_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(1));
        play_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(2));
        gallery_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(3));
        sns_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(4));
    }

    public void music_play(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", index);
        intent.putExtra("state", "play");
        getActivity().startService(intent);
    }

    public void music_pause(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("state", "pause");
        getActivity().startService(intent);
    }

    public void music_stop(){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", index);
        intent.putExtra("state", "stop");
        getActivity().startService(intent);
    }

    public void music_seek_play(int position){
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.putExtra("index", index);
        intent.putExtra("seekBar_position", position);
        intent.putExtra("state", "play");
        getActivity().startService(intent);
    }


    public void lyrics_popupSetting() {

        //팝업으로 띄울 커스텀뷰를 설정하고
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_lyrics, null);

        btn_lyric.setOnClickListener(v -> {

            //클릭시 팝업 윈도우 생성
            popup = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);

            RelativeLayout popupLayout = (RelativeLayout)layout.findViewById(R.id.whole_layout);
            popup.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);

            ImageButton btn_close = (ImageButton) popupView.findViewById(R.id.btn_close);
            TextView title = (TextView) popupView.findViewById(R.id.title);
            TextView info = (TextView) popupView.findViewById(R.id.info);
            title.setText(music_info.get(index).getMusic_title());
            info.setText(
                    "작곡 : "+music_info.get(index).getComposition()+"\n"+
                    "작사 : "+music_info.get(index).getWriter()+"\n"+
                    "편곡 : "+music_info.get(index).getArrangement()+"\n");

            btn_close.setOnClickListener(view -> popup.dismiss());

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


    public void changePlay(){
        switch (index){
            case 0:
                btn_prevplay.setAlpha(0.5f);
                btn_nextplay.setAlpha(1f);
                break;
            case 7:
                btn_prevplay.setAlpha(1f);
                btn_nextplay.setAlpha(0.5f);
                break;
            default:
                btn_prevplay.setAlpha(1f);
                btn_nextplay.setAlpha(1f);
                break;
        }
    }


    // 가사
    public void changeLyrics(int index){
        AssetManager am = getContext().getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read="";
        String lyrics="";
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
        btn_repeat.setOnClickListener(view -> {
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
        });
    }

    // 하트 눌렀을때
    public void like_music(){
        heart_touch_area.setOnClickListener(view -> {
            if((Integer)heart.getTag() == 0){
                heart.setBackgroundResource(R.drawable.like_on);
                int current_like_count = like_count.get(index)+1;
                setHeartNum(current_like_count);
                heart.setTag(1);
                viewGif();
            } else if ((Integer)heart.getTag() == 1){
                heart.setBackgroundResource(R.drawable.like_off);
                int current_like_count = like_count.get(index)-1;
                setHeartNum(current_like_count);
                heart.setTag(0);
            }
        });
    }

    public void viewGif(){
        like_gif.setBackgroundResource(R.drawable.like);
        like_gif.bringToFront();
        frameAnimation = (AnimationDrawable) like_gif.getBackground();
        if(frameAnimation.isRunning()) frameAnimation.stop();
        frameAnimation.start();
    }


    // <- -> 버튼 눌렀을 때
    public void switch_music(){
        //다음
        btn_nextplay.setOnClickListener(view -> {
            if(index != musicarr.size()-1)
                musicPager.setCurrentItem(index+1);
            else
                Toast.makeText(getContext(), "마지막 곡입니다", Toast.LENGTH_SHORT).show();
        });

        //이전
        btn_prevplay.setOnClickListener(view -> {
            if(index !=0)
                musicPager.setCurrentItem(index-1);
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
        musicarr.add("LE Fou Muet");   like_count.add(123);
        musicarr.add("Diamond");       like_count.add(47);
        musicarr.add("난 아니에요");    like_count.add(7);


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
        music_info.add(new MusicInfoItem("LE Fou Muet", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("Diamond", "검정치마", "검정치마", "검정치마"));
        music_info.add(new MusicInfoItem("난 아니에요", "검정치마", "검정치마", "검정치마"));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                initPlay();

                if(refresh == 0){
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();
                    refresh++;
                }
                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                if(ApplicationStatus.isPlaying){
                    Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
                    return;
                }
                else{
                    Glide.with(getContext()).load(R.drawable.mn_play_off).into(imageViewTarget);
                    return;
                }
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }





}
