
package test.dahun.mobileplay.tab;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

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
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.model.ApplicationStatus;
import test.dahun.mobileplay.ui.VerticalViewPager;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class MusicFragment extends Fragment
{
    @BindView(R.id.whole_layout) RelativeLayout whole_layout;

    @BindView(R.id.album_img) LinearLayout album_img;
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
    static MediaPlayer mp; // 음악 재생을 위한 객체
    int pos; // 재생 멈춘 시점
    boolean isPlaying = false; // 재생중인지 확인할 변수
    boolean restart = false; // 재생중인지 확인할 변수
    int total;

    Timer timer=null;
    TimerHandler timerHandler;
    int time=1;
    String totalTime=null;

    //가사 Popup
    TextView lyricsText;

    //음악리스트 Popup
    PopupWindow popup;


    //노래 제목 리스트
    ArrayList<String> musicarr = new ArrayList<>();

    //like 갯수
    ArrayList<Integer> like_count = new ArrayList<>();

    //auto play 할건지
    boolean isAutoPlay = false;

    //화면 새로고침은 처음만
    int refresh = 0;

    //곡 바로이동
    int music_index = -1;

    //곡 제목, 작곡, 작사, 편곡
    MusicInfoItem musicInfoItem;
    ArrayList<MusicInfoItem> music_info;

    class MusicThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            while(isPlaying) {
                seekBar.setProgress(mp.getCurrentPosition());
            }
        }
    }

    class Timer extends Thread {
        Message message;

        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
           // while (!this.isInterrupted()) {

            while(true){
                 if(mp.isPlaying()){
                     Log.d(TAG,"TIME");
                     // 스레드에게 수행시킬 동작들 구현
                     message=timerHandler.obtainMessage();
                     message.arg1=time;
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
    }

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
        initSetting();
        btnSetting();
        //resizeLayout();
        lyrics_popupSetting();
        playmode();
        like_music();
        switch_music();

        //list - music 바로가기
        //newInstance(-1);
        //getSongIndex();
        return layout;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initSetting() {
        heart.setTag(0);

        // make title data
        musicarr.add("Big Love");  like_count.add(13);
        musicarr.add("좋아해줘");  like_count.add(1789);
        musicarr.add("Dientes");   like_count.add(542);
        musicarr.add("Stand Still"); like_count.add(486);
        musicarr.add("상아");  like_count.add(992);
        musicarr.add("강아지");  like_count.add(9);
        musicarr.add("Antifreeze");  like_count.add(75);

        // make music info
        music_info = new ArrayList<>();
        musicInfoItem = new MusicInfoItem("She's a Baby", "ZICO, Poptime", "ZICO", "ZICO, Poptime");
        music_info.add(musicInfoItem);

        title.setText(musicarr.get(0));

        heart_num.setText(String.valueOf(like_count.get(0)));

        btn_prevplay.setAlpha(0.5f);

        //equalizer
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);

        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.mn_play_on).into(imageViewTarget);

        musicPager.setAdapter(new MusicCustomPagerAdapter(getContext()));
        musicPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                time=0;
                index=position;
                //노래제목
                btn_play.setBackgroundResource(R.drawable.btn_pause);
                title.setText(musicarr.get(position));

                //하트 개수
                String heart_count = "";
                int current_like_count = like_count.get(position);
                if(current_like_count >= 1000)  heart_count = (current_like_count/1000)+"k";
                else heart_count = String.valueOf(current_like_count);
                heart_num.setText(heart_count);
                changeMusic(position);
                changeLyrics(position);
                changePlay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // MediaPlayer 객체 초기화 , 재생
        mp = MediaPlayer.create(
                getContext(), // 현재 화면의 제어권자
                R.raw.first); // 음악파일

        timerHandler=new TimerHandler();

        currentTime.setText("00:00");
        total = mp.getDuration(); // 노래의 재생시간(miliSecond)
        totalTime=timeTranslation(total/1000);
        maxTime.setText(totalTime);

        // 시크바 초기세팅
        seekBar.setProgress(0);

        // 시크바 색상 변경
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                btn_play.setBackgroundResource(R.drawable.btn_pause);
                isPlaying = true;
                int ttt = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                mp.seekTo(ttt);
                mp.start();
                time=mp.getCurrentPosition()/1000;
                currentTime.setText(timeTranslation(time));
                new MusicThread().start();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(timer==null){
                    timer=new Timer();
                    timer.start();
                }
                ApplicationStatus.isPlaying=true;
                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);

                isPlaying = false;

                if(!mp.isPlaying()){

                    mp.setLooping(false); // true:무한반복
                    seekBar.setMax(total);// 씨크바의 최대 범위를 노래의 재생시간으로 설정

                    new MusicThread().start(); // 씨크바 그려줄 쓰레드 시작

                }else
                    mp.pause();
            }
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser) {
                if (seekBar.getMax()==progress) {
                    //Toast.makeText(getContext(),"finish",Toast.LENGTH_SHORT).show();
                    isPlaying = false;
                    restart = true;
                    mp.stop();
                    mp.release();

                    time=0;
                    pos=0;
                    seekBar.setProgress(0);
                    currentTime.setText("00:00");
                    btn_play.setBackgroundResource(R.drawable.btn_play);

                    switch(index){
                        case 0:
                            mp = MediaPlayer.create(getContext(), R.raw.first);
                            break;
                        case 1:
                            mp = MediaPlayer.create(getContext(), R.raw.second);
                            break;
                        case 2:
                            mp = MediaPlayer.create(getContext(), R.raw.third);
                            break;
                        case 3:
                            mp = MediaPlayer.create(getContext(), R.raw.fourth);
                            break;
                        case 4:
                            mp = MediaPlayer.create(getContext(), R.raw.fifth);
                            break;
                        case 5:
                            mp = MediaPlayer.create(getContext(), R.raw.sixth);
                            break;
                        case 6:
                            mp = MediaPlayer.create(getContext(), R.raw.seventh);
                            break;

                    }

                }
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPlaying){
                    btn_play.setBackgroundResource(R.drawable.btn_play);
                    ApplicationStatus.isPlaying=false;
                    DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                    Glide.with(getContext()).load(R.drawable.mn_play_on).into(imageViewTarget);

                    // 일시중지
                    pos = mp.getCurrentPosition();
                    mp.pause(); // 일시중지
                    isPlaying = false; // 쓰레드 정지
                    restart=true;

                    Log.d(TAG,"INTERRUPT");
                    //timer.interrupt();

                }else{
                    btn_play.setBackgroundResource(R.drawable.btn_pause);
                    ApplicationStatus.isPlaying=true;
                    DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                     Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
                    if(restart){
                        Log.d(TAG,"RESTART");
                        // 멈춘 지점부터 재시작
                        mp.seekTo(pos); // 일시정지 시점으로 이동
                        mp.start(); // 시작
                        isPlaying = true; // 재생하도록 flag 변경
                        new MusicThread().start(); // 쓰레드 시작
                    }else{
                        Log.d(TAG,"START");

                        mp.setLooping(false); // true:무한반복
                        mp.start(); // 노래 재생 시작
                        seekBar.setMax(total);// 씨크바의 최대 범위를 노래의 재생시간으로 설정

                        isPlaying = true; // 씨크바 쓰레드 반복 하도록
                        new MusicThread().start(); // 씨크바 그려줄 쓰레드 시작

                        timer=new Timer();
                        timer.start();
                    }
                }


            }
        });



//        bStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 음악 종료
//                isPlaying = false; // 쓰레드 종료
//                mp.stop(); // 멈춤
//                mp.release(); // 자원 해제
//                sb.setProgress(0); // 씨크바 초기화
//            }
//        });

        if(isAutoPlay){
            autoPlay(total);
        }
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

    public void resizeLayout(){
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float context_height = pxToDp(dm.heightPixels);
        final int layout_height = dpToPx(context_height - 314);
        album_img.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams position = new LinearLayout.LayoutParams(
                        album_img.getWidth(), layout_height
                );
                album_img.setLayoutParams(position);
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
                title.setText(music_info.get(0).getMusic_title());
                info.setText("작곡 : "+music_info.get(0).getComposition()+"\n"+
                                "작사 : "+music_info.get(0).getWriter()+"\n"+
                                "편곡 : "+music_info.get(0).getArrangement()+"\n");

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

    void autoPlay(int total){
        mp.setLooping(false); // true:무한반복
         mp.start(); // 노래 재생 시작
        seekBar.setMax(total);// 씨크바의 최대 범위를 노래의 재생시간으로 설정

         isPlaying = true; // 씨크바 쓰레드 반복 하도록
        new MusicThread().start(); // 씨크바 그려줄 쓰레드 시작
       timer=new Timer();
        timer.start();
    }

    void changePlay(int index){
        switch (index){
            case 0:
                btn_prevplay.setAlpha(0.5f);
                btn_nextplay.setAlpha(1f);
                break;
            case 6:
                btn_prevplay.setAlpha(1f);
                btn_nextplay.setAlpha(0.5f);
                break;
            default:
                btn_prevplay.setAlpha(1f);
                btn_nextplay.setAlpha(1f);
                break;
        }
    }

    void changeMusic(int index){

        // 음악 종료
        isPlaying = false; // 쓰레드 종료
        restart = false;
        if(mp.isPlaying()){
            mp.stop(); // 멈춤
            mp.release(); // 자원 해제
        }

        seekBar.setProgress(0); // 씨크바 초기화

        switch(index){
            case 0:
                mp = MediaPlayer.create(getContext(), R.raw.first);
                break;
            case 1:
                mp = MediaPlayer.create(getContext(), R.raw.second);
                break;
            case 2:
                mp = MediaPlayer.create(getContext(), R.raw.third);
                break;
            case 3:
                mp = MediaPlayer.create(getContext(), R.raw.fourth);
                break;
            case 4:
                mp = MediaPlayer.create(getContext(), R.raw.fifth);
                break;
            case 5:
                mp = MediaPlayer.create(getContext(), R.raw.sixth);
                break;
            case 6:
                mp = MediaPlayer.create(getContext(), R.raw.seventh);
                break;

        }

        Log.d(TAG,"START");

        currentTime.setText("00:00");
        int total = mp.getDuration(); // 노래의 재생시간(miliSecond)
        String time=timeTranslation(total/1000);
        maxTime.setText(time);
        seekBar.setMax(total);// 씨크바의 최대 범위를 노래의 재생시간으로 설정

        mp.setLooping(false); // true:무한반복
        mp.start(); // 노래 재생 시작

        isPlaying = true; // 씨크바 쓰레드 반복 하도록
        new MusicThread().start(); // 씨크바 그려줄 쓰레드 시작

//        timer=new Timer();
//        timer.start();
    }

    public void changeLyrics(int index){
        AssetManager am = getContext().getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader br;
        String read=null;
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
        }

        lyricsText.setText(lyrics);


    }

    String timeTranslation(int time){
        int minutes=time/60;
        int second=time-minutes*60;

        String result="0";
        result+=String.valueOf(minutes);
        result+=":";
        if(second<10)
            result+="0";
        result+=String.valueOf(second);

        return result;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("SetUserHint","Music ON");
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
        mp.stop();
    }

    public void playmode(){
        btn_repeat.setTag("all");
        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("gomgom", String.valueOf(view.getTag()));
                if(view.getTag().equals("all")){
                    btn_repeat.setBackgroundResource(R.drawable.btn_repeatone);
                    btn_repeat.setTag("one");
                }else if(view.getTag().equals("one")){
                    btn_repeat.setBackgroundResource(R.drawable.btn_repeatall);
                    btn_repeat.setAlpha(0.5f);
                    btn_repeat.setTag("none");

                    mp.setLooping(true);
                }else if(view.getTag().equals("none")){
                    btn_repeat.setAlpha(1f);
                    btn_repeat.setTag("all");

                    mp.setLooping(false);
                }
            }
        });
    }

    public void like_music(){
        heart_touch_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer)heart.getTag() == 0){
                    heart.setBackgroundResource(R.drawable.like_on);
                    int current_like_count = like_count.get(index)+1;
                    like_count.set(index, current_like_count);
                    String new_like_count = String.valueOf(current_like_count);
                    heart_num.setText(new_like_count);
                    heart.setTag(1);
                } else if ((Integer)heart.getTag() == 1){
                    heart.setBackgroundResource(R.drawable.like_off);
                    int current_like_count = like_count.get(index)-1;
                    like_count.set(index, current_like_count);
                    String new_like_count = String.valueOf(current_like_count);
                    heart_num.setText(new_like_count);
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
                if(index != musicarr.size())
                    musicPager.setCurrentItem(index+1);
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

    //dp를 px로
    public int dpToPx(float dp){
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return px;
    }

    //px를 dp로
    public float pxToDp(float px){
        float density = getContext().getResources().getDisplayMetrics().density;

        if(density == 1.0) density *= 4.0;
        else if(density == 1.5) density *= (8/3);
        else if(density == 2.0) density *= 2.0;

        float dp = px/density;
        return dp;
    }

   /* public void getSongIndex(){
        music_index = getArguments().getInt("music_index", -1);
        if(music_index != -1){
            musicPager.setCurrentItem(music_index);
        }
    }*/


}
