package test.dahun.mobileplay.tab;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import test.dahun.mobileplay.R;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class MusicFragment extends Fragment
{

    final String TAG="MusicFragment";
    LinearLayout layout;

    //음악 관련 변수
    static MediaPlayer mp; // 음악 재생을 위한 객체
    int pos; // 재생 멈춘 시점
    Button bStart;
    Button bPause;
    Button bRestart;
    Button bStop;
    SeekBar sb; // 음악 재생위치를 나타내는 시크바
    boolean isPlaying = false; // 재생중인지 확인할 변수


    class MyThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            while(isPlaying) {
                sb.setProgress(mp.getCurrentPosition());
            }
        }
    }




    public MusicFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_music, container, false);

        initSetting();

        return layout;
    }

    public void initSetting() {

        sb = (SeekBar)layout.findViewById(R.id.musicProgress);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
                int ttt = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                mp.seekTo(ttt);
                mp.start();
                new MyThread().start();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                mp.pause();
            }
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser) {
                if (seekBar.getMax()==progress) {
                    bStart.setVisibility(View.VISIBLE);
                    bStop.setVisibility(View.INVISIBLE);
                    bPause.setVisibility(View.INVISIBLE);
                    bRestart.setVisibility(View.INVISIBLE);
                    isPlaying = false;
                    mp.stop();
                }
            }
        });

        bStart = (Button)layout.findViewById(R.id.startBtn);
        bPause = (Button)layout.findViewById(R.id.pauseBtn);
        bRestart=(Button)layout.findViewById(R.id.restartBtn);
        bStop  = (Button)layout.findViewById(R.id.stopBtn);

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MediaPlayer 객체 초기화 , 재생
                mp = MediaPlayer.create(
                        getContext(), // 현재 화면의 제어권자
                        R.raw.konan); // 음악파일
                mp.setLooping(false); // true:무한반복
                mp.start(); // 노래 재생 시작

                int a = mp.getDuration(); // 노래의 재생시간(miliSecond)
                sb.setMax(a);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
                new MyThread().start(); // 씨크바 그려줄 쓰레드 시작
                isPlaying = true; // 씨크바 쓰레드 반복 하도록

                bStart.setVisibility(View.INVISIBLE);
                bStop.setVisibility(View.VISIBLE);
                bPause.setVisibility(View.VISIBLE);
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 음악 종료
                isPlaying = false; // 쓰레드 종료

                mp.stop(); // 멈춤
                mp.release(); // 자원 해제
                bStart.setVisibility(View.VISIBLE);
                bPause.setVisibility(View.INVISIBLE);
                bRestart.setVisibility(View.INVISIBLE);
                bStop.setVisibility(View.INVISIBLE);

                sb.setProgress(0); // 씨크바 초기화
            }
        });

        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 일시중지
                pos = mp.getCurrentPosition();
                mp.pause(); // 일시중지

                bPause.setVisibility(View.INVISIBLE);
                bRestart.setVisibility(View.VISIBLE);
                isPlaying = false; // 쓰레드 정지
            }
        });

        bRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 멈춘 지점부터 재시작
                mp.seekTo(pos); // 일시정지 시점으로 이동
                mp.start(); // 시작
                bRestart.setVisibility(View.INVISIBLE);
                bPause.setVisibility(View.VISIBLE);
                isPlaying = true; // 재생하도록 flag 변경
                new MyThread().start(); // 쓰레드 시작
            }
        });



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
