
package test.dahun.mobileplay.tab;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.MusicCustomPagerAdapter;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.model.ApplicationStatus;
import test.dahun.mobileplay.model.Fan;
import test.dahun.mobileplay.ui.VerticalViewPager;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class MusicFragment extends Fragment
{
    @BindView(R.id.navi) ImageButton navibtn;
    @BindView(R.id.mn_play) ImageButton playbtn;
    @BindView(R.id.mn_movie) ImageButton moviebtn;
    @BindView(R.id.mn_gallery) ImageButton galbtn;
    @BindView(R.id.mn_comm) ImageButton commbtn;
    @BindView(R.id.ic_mn)
    ImageView btn;

    @BindView(R.id.titleLayout) ImageView titleLayout;
    @BindView(R.id.ic_homeBtn) Button ic_homeBtn;
    @BindView(R.id.ic_equalizerBtn) ImageView ic_equalizerBtn;

    @BindView(R.id.musicPager) VerticalViewPager musicPager;


    @BindView(R.id.playBtn) Button playBtn;
    @BindView(R.id.currentTime) TextView currentTime;
    @BindView(R.id.maxTime) TextView maxTime;
    @BindView(R.id.musicProgress) SeekBar seekBar; // 음악 재생위치를 나타내는 시크바
    @BindView(R.id.musictitle) TextView musictitle;//음악 제목

    @BindView(R.id.play_lyrics) Button lyricsBtn;
    @BindView(R.id.play_list) Button playlistBtn;

    @BindView(R.id.play_mode) Button playmodeBtn;




    final String TAG="MusicFragment";
    LinearLayout layout;

    //음악 관련 변수
    int index=0;
    static MediaPlayer mp; // 음악 재생을 위한 객체
    int pos; // 재생 멈춘 시점
    boolean isPlaying = false; // 재생중인지 확인할 변수
    boolean restart = false; // 재생중인지 확인할 변수

    Timer timer=null;
    TimerHandler timerHandler;
    int time=1;
    String totalTime=null;

    //가사 Popup
    TextView lyricsText;

    //음악리스트 Popup
    PopupWindow popup;
    TextView music1;
    TextView music2;
    TextView music3;
    TextView music4;
    TextView music5;
    TextView music6;
    TextView music7;


    //노래 제목 리스트

    ArrayList<String> musicarr = new ArrayList<>();

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_music, container, false);


        ButterKnife.bind(this, layout);

        initSetting();
        lyrics_popupSetting();
        songlist_popupSetting();
        playmode();

        return layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initSetting() {

        musicarr.add("애월");
        musicarr.add("모네");
        musicarr.add("FULL");
        musicarr.add("비행운");
        musicarr.add("디왈리 (With 저수지의 딸들)");
        musicarr.add("");
        musicarr.add("");

        //homebtn
        ic_homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerAdapter.setViewPagerTabListener.setTab(0);
            }
        });
        //
        //equalizer
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ic_equalizerBtn);

        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);

        ic_equalizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerAdapter.setViewPagerTabListener.setTab(1);
            }
        });

        //navibutton
        ViewGroup.LayoutParams params = navibtn.getLayoutParams();
        params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
        params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        navibtn.requestLayout();
        navibtn.setImageResource(R.drawable.mn_default);
        navibtn.setTag(R.drawable.mn_default);

        navibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer)view.getTag() == R.drawable.mn_default){
                    playbtn.setVisibility(View.VISIBLE);
                    moviebtn.setVisibility(View.VISIBLE);
                    galbtn.setVisibility(View.VISIBLE);
                    commbtn.setVisibility(View.VISIBLE);
                    //btn.setVisibility(View.VISIBLE);

                    ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_click);
                    navibtn.setTag(R.drawable.mn_click);
                }else{
                    playbtn.setVisibility(View.GONE);
                    moviebtn.setVisibility(View.GONE);
                    galbtn.setVisibility(View.GONE);
                    commbtn.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                    params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_default);
                    navibtn.setTag(R.drawable.mn_default);
                }
            }
        });

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playbtn.setVisibility(View.GONE);
                moviebtn.setVisibility(View.GONE);
                galbtn.setVisibility(View.GONE);
                commbtn.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);

                ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                navibtn.requestLayout();
                navibtn.setImageResource(R.drawable.mn_default);
                navibtn.setTag(R.drawable.mn_default);
                ViewPagerAdapter.setViewPagerTabListener.setTab(1);
                //Toast.makeText(getContext(), "music", Toast.LENGTH_LONG).show();
            }
        });

        moviebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playbtn.setVisibility(View.GONE);
                moviebtn.setVisibility(View.GONE);
                galbtn.setVisibility(View.GONE);
                commbtn.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);

                ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                navibtn.requestLayout();
                navibtn.setImageResource(R.drawable.mn_default);
                navibtn.setTag(R.drawable.mn_default);
                ViewPagerAdapter.setViewPagerTabListener.setTab(2);
                //      Toast.makeText(getContext(), " movie", Toast.LENGTH_LONG).show();

            }
        });

        galbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playbtn.setVisibility(View.GONE);
                moviebtn.setVisibility(View.GONE);
                galbtn.setVisibility(View.GONE);
                commbtn.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);

                ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                navibtn.requestLayout();
                navibtn.setImageResource(R.drawable.mn_default);
                navibtn.setTag(R.drawable.mn_default);
                ViewPagerAdapter.setViewPagerTabListener.setTab(3);
                //          Toast.makeText(getContext(), "gallery", Toast.LENGTH_LONG).show();

            }
        });

        commbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playbtn.setVisibility(View.GONE);
                moviebtn.setVisibility(View.GONE);
                galbtn.setVisibility(View.GONE);
                commbtn.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);

                ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                navibtn.requestLayout();
                navibtn.setImageResource(R.drawable.mn_default);
                navibtn.setTag(R.drawable.mn_default);
                ViewPagerAdapter.setViewPagerTabListener.setTab(4);
                //          Toast.makeText(getContext(), "community", Toast.LENGTH_LONG).show();

            }
        });
/////


        musicPager.setAdapter(new MusicCustomPagerAdapter(getContext()));
        musicPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                playBtn.setBackgroundResource(R.drawable.play_ic_pause);
                time=0;
                index=position;

                //노래제목
                musictitle.setText(musicarr.get(position));
                changeMusic(position);
                changeLyrics(position);
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
        final int total = mp.getDuration(); // 노래의 재생시간(miliSecond)
        totalTime=timeTranslation(total/1000);
        maxTime.setText(totalTime);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                playBtn.setBackgroundResource(R.drawable.play_ic_pause);

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
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ic_equalizerBtn);
                Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);

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
                    Toast.makeText(getContext(),"finish",Toast.LENGTH_SHORT).show();


                    isPlaying = false;
                    restart = true;
                    mp.stop();
                    mp.release();

                    time=0;
                    pos=0;
                    seekBar.setProgress(0);
                    currentTime.setText("00:00");
                    playBtn.setBackgroundResource(R.drawable.play_ic_play);

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
                            break;/*
                        case 5:
                            mp = MediaPlayer.create(getContext(), R.raw.sixth);
                            break;
                        case 6:
                            mp = MediaPlayer.create(getContext(), R.raw.seventh);
                            break;*/

                    }

                }
            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPlaying){
                    playBtn.setBackgroundResource(R.drawable.play_ic_play);
                    ApplicationStatus.isPlaying=false;
                   GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ic_equalizerBtn);
                    Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);

                    // 일시중지
                    pos = mp.getCurrentPosition();
                    mp.pause(); // 일시중지
                    isPlaying = false; // 쓰레드 정지
                    restart=true;

                    Log.d(TAG,"INTERRUPT");
                    //timer.interrupt();

                }else{
                    playBtn.setBackgroundResource(R.drawable.play_ic_pause);
                    ApplicationStatus.isPlaying=true;
                   GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ic_equalizerBtn);
                     Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);
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

        autoPlay(total);

    }



    public void lyrics_popupSetting() {

        //팝업으로 띄울 커스텀뷰를 설정하고
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_lyrics, null);



        lyricsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //클릭시 팝업 윈도우 생성
                popup = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, 1200, true);

                RelativeLayout relativeLayout = (RelativeLayout)layout.findViewById(R.id.contentLayout);
                popup.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

            }
        });

        lyricsText=(TextView)popupView.findViewById(R.id.lyricsText);

        AssetManager am = getContext().getAssets();
        try {
            InputStream inputStream = am.open("first.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"euc-kr");
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


    public void songlist_popupSetting() {

        //팝업으로 띄울 커스텀뷰를 설정하고
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_songlist, null);

        playlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //클릭시 팝업 윈도우 생성
                popup = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                //popup = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                RelativeLayout relativeLayout = (RelativeLayout)layout.findViewById(R.id.contentLayout);
                popup.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

            }
        });

        music1=(TextView)popupView.findViewById(R.id.music1);
        music2=(TextView)popupView.findViewById(R.id.music2);
        music3=(TextView)popupView.findViewById(R.id.music3);
        music4=(TextView)popupView.findViewById(R.id.music4);
        music5=(TextView)popupView.findViewById(R.id.music5);
        //music6=(TextView)popupView.findViewById(R.id.music6);
        //music7=(TextView)popupView.findViewById(R.id.music7);

        music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPager.setCurrentItem(0);
                popup.dismiss();
            }
        });
        music2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPager.setCurrentItem(1);
                popup.dismiss();

            }
        });

        music3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPager.setCurrentItem(2);
                popup.dismiss();

            }
        });

        music4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPager.setCurrentItem(3);
                popup.dismiss();

            }
        });

        music5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPager.setCurrentItem(4);
                popup.dismiss();

            }
        });
/*
        music6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPager.setCurrentItem(5);
                popup.dismiss();


            }
        });

        music7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPager.setCurrentItem(6);
                popup.dismiss();

            }
        });*/
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
                break;/*
            case 5:
                mp = MediaPlayer.create(getContext(), R.raw.sixth);
                break;
            case 6:
                mp = MediaPlayer.create(getContext(), R.raw.seventh);
                break;*/

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
                    inputStreamReader = new InputStreamReader(inputStream,"euc-kr");
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
                    inputStreamReader = new InputStreamReader(inputStream,"euc-kr");
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
                    inputStreamReader = new InputStreamReader(inputStream,"euc-kr");
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
                    inputStreamReader = new InputStreamReader(inputStream,"euc-kr");
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
                    inputStreamReader = new InputStreamReader(inputStream,"euc-kr");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;/*
            case 5:
                try {
                    inputStream = am.open("sixth.txt");
                    inputStreamReader = new InputStreamReader(inputStream,"euc-kr");
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
                    inputStreamReader = new InputStreamReader(inputStream,"euc-kr");
                    br = new BufferedReader(inputStreamReader);

                    while((read=br.readLine())!=null){
                        lyrics+=read;
                        lyrics+="\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;*/
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
                GlideDrawableImageViewTarget
                        imageViewTarget = new GlideDrawableImageViewTarget(ic_equalizerBtn);
                if (ApplicationStatus.isPlaying)
                    Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);
                else
                    Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);

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

        playmodeBtn.setTag(R.drawable.play_ic_random);

        playmodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer)view.getTag()==R.drawable.play_ic_random){
                    playmodeBtn.setBackgroundResource(R.drawable.play_ic_repeat);
                    playmodeBtn.setTag(R.drawable.play_ic_repeat);
                }else if((Integer)view.getTag()==R.drawable.play_ic_repeat){
                    playmodeBtn.setBackgroundResource(R.drawable.play_ic_repeatone);
                    playmodeBtn.setTag(R.drawable.play_ic_repeatone);
                }else if((Integer)view.getTag()==R.drawable.play_ic_repeatone){
                    playmodeBtn.setBackgroundResource(R.drawable.play_ic_random);
                    playmodeBtn.setTag(R.drawable.play_ic_random);
                }
            }
        });

    }

}
