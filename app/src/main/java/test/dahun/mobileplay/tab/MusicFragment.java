package test.dahun.mobileplay.tab;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.framed.FrameReader;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.MusicCustomPagerAdapter;
import test.dahun.mobileplay.adapter.PictureCustomPagerAdapter;
import test.dahun.mobileplay.listener.ChangeMusicListener;
import test.dahun.mobileplay.ui.VerticalViewPager;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class MusicFragment extends Fragment
{
    @BindView(R.id.ic_homeBtn) Button ic_homeBtn;
    @BindView(R.id.ic_equalizerBtn) Button ic_equalizerBtn;

    @BindView(R.id.musicPager) VerticalViewPager musicPager;


    @BindView(R.id.playBtn) Button playBtn;
    @BindView(R.id.currentTime) TextView currentTime;
    @BindView(R.id.maxTime) TextView maxTime;
    @BindView(R.id.musicProgress) SeekBar seekBar; // 음악 재생위치를 나타내는 시크바
    @BindView(R.id.navi) ImageButton navibtn;

    @BindView(R.id.play_list) Button playlistBtn;



    final String TAG="MusicFragment";
    LinearLayout layout;
    ChangeMusicListener changeMusicListener;

    //음악 관련 변수
    static MediaPlayer mp; // 음악 재생을 위한 객체
    int pos; // 재생 멈춘 시점
    boolean isPlaying = false; // 재생중인지 확인할 변수
    boolean restart = false; // 재생중인지 확인할 변수

    Timer timer;
    TimerHandler timerHandler;
    int time=0;

    //Popup
    PopupWindow popup;
    TextView music1;
    TextView music2;
    TextView music3;
    TextView music4;
    TextView music5;
    TextView music6;
    TextView music7;


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
            while (!this.isInterrupted()) {
                try {
                    Log.d(TAG,"TIME");
                    // 스레드에게 수행시킬 동작들 구현
                    Thread.sleep(1000); // 1초간 Thread를 잠재운다
                    message=timerHandler.obtainMessage();
                    message.arg1=time;
                    timerHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time++;
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
        popupSetting();

        return layout;
    }

    public void initSetting() {

        changeMusicListener=new ChangeMusicListener() {
            @Override
            public void chageMusic(int index) {
                // 음악 종료
                isPlaying = false; // 쓰레드 종료
                restart = false;
                mp.stop(); // 멈춤
                mp.release(); // 자원 해제
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
        };

        musicPager.setAdapter(new MusicCustomPagerAdapter(changeMusicListener));

        // MediaPlayer 객체 초기화 , 재생
        mp = MediaPlayer.create(
                getContext(), // 현재 화면의 제어권자
                R.raw.first); // 음악파일

        timerHandler=new TimerHandler();

        final int total = mp.getDuration(); // 노래의 재생시간(miliSecond)
        String time=timeTranslation(total/1000);
        maxTime.setText(time);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
                int ttt = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                mp.seekTo(ttt);
                mp.start();
                new MusicThread().start();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                mp.pause();
            }
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser) {
                if (seekBar.getMax()==progress) {
                    isPlaying = false;
                    mp.stop();
                }
            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPlaying){
                    playBtn.setBackgroundResource(R.drawable.play_ic_play);
                    // 일시중지
                    pos = mp.getCurrentPosition();
                    mp.pause(); // 일시중지
                    isPlaying = false; // 쓰레드 정지
                    restart=true;

                    Log.d(TAG,"INTERRUPT");
                    //timer.interrupt();

                }else{
                    playBtn.setBackgroundResource(R.drawable.play_ic_pause);

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



    }


    public void popupSetting() {

        //팝업으로 띄울 커스텀뷰를 설정하고
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_window, null);

        playlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //클릭시 팝업 윈도우 생성
                popup = new PopupWindow(popupView, 1200, WindowManager.LayoutParams.WRAP_CONTENT, true);
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
        music6=(TextView)popupView.findViewById(R.id.music6);
        music7=(TextView)popupView.findViewById(R.id.music7);

        music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();
            }
        });
        music2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT).show();

            }
        });

        music3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"3",Toast.LENGTH_SHORT).show();

            }
        });

        music4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"4",Toast.LENGTH_SHORT).show();

            }
        });

        music5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"5",Toast.LENGTH_SHORT).show();

            }
        });

        music6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"6",Toast.LENGTH_SHORT).show();

            }
        });

        music7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"7",Toast.LENGTH_SHORT).show();

            }
        });
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
        if(isVisibleToUser)
            Log.d("SetUserHint","Music ON");
        else
            Log.d("SetUserHint","Music OFF");

    }

}
