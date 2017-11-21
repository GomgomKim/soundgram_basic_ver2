package test.dahun.mobileplay.tab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import test.dahun.mobileplay.R;

public class SubMovieView extends LinearLayout {

  Context context;
  View view;

  //
  View mvpopupView;
  PopupWindow mvpw;

  public SubMovieView(Context context, int index) {
    super(context);

    this.context=context;

    LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = li.inflate(R.layout.layout_submovie, this, true);

    initSetting(index);

  }

  public void initSetting(final int index){

    //
    final TextView mv_title = (TextView)view.findViewById(R.id.movie_title);
    VideoView mv_video = (VideoView)view.findViewById(R.id.movie_video);
    TextView mv_exp = (TextView)view.findViewById(R.id.movie_exp);
    Button mv_share_btn = (Button)view.findViewById(R.id.movie_share_btn);
    Button mv_play_btn = (Button)view.findViewById(R.id.movie_play_btn);

    //동영상 2개 있을거라고 가정하고 이렇게 했음 SubPictureView 참조해서 ㄱ

    switch(index){
      case 0:
        mv_title.setText("[Teaser 1] IU(아이유)_The shower(푸르던)");
        mv_exp.setText("Album_ CHAT-SHIRE\n" +
                "Release Date_ 2017.03.21\n" +
                "Play Time_ 00:45");
        break;
      case 1:
        mv_title.setText("[Teaser 1] IU(아이유)_The shower(푸르던2)");
        mv_exp.setText("Album_ CHAT-SHIRE\n" +
                "Release Date_ 2017.03.21\n" +
                "Play Time_ 00:45");
///////
        break;

    }

    mv_play_btn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
       // Toast.makeText(context,"비디오재생"+ index,Toast.LENGTH_SHORT).show();
        //mv_popup(view, "비디오 경로",mv_title.getText().toString());
        mv_dialog(mv_title.getText().toString());
      }
    });

    mv_share_btn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(context,"공유"+ index,Toast.LENGTH_SHORT).show();
      }
    });

  }

  public void mv_dialog(String title){
      final Dialog dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
      //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.setContentView(R.layout.popup_video);
      dialog.show();
      WindowManager.LayoutParams lp = new WindowManager.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
      lp.copyFrom(dialog.getWindow().getAttributes());
      dialog.getWindow().setAttributes(lp);
      //내용
    Button mvclosebtn = (Button)dialog.findViewById(R.id.mv_close_btn);
    TextView mvpoptitle = (TextView)dialog.findViewById(R.id.mv_pop_title);
    final Button mvstopbtn = (Button)dialog.findViewById(R.id.mv_pop_stopbtn);
    final VideoView videoView = (VideoView)dialog.findViewById(R.id.mv_view);
    //제목
    mvpoptitle.setText(title);
    //비디오
    mvclosebtn.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        //비디오 정지
        mvpw.dismiss();
      }
    });

    //비디오

    final int[] videoTime = {0};

      /*
      int type = URL;
        switch (type) {
            case URL:https://github.com/oguzbilgener/CircularFloatingActionMenu
                //동영상 경로가 URL일 경우
                videoView.setVideoURI(Uri.parse(VIDEO_URL));
                break;

            case SDCARD:
                //동영상 경로가 SDCARD일 경우
                String path = Environment.getExternalStorageDirectory()
                        + "/TestVideo.mp4";
                videoView.setVideoPath(path);
                break;
        }

       */
    String url = "https://www.youtube.com/watch?v=42Gtm4-Ax2U";
    Uri uri = Uri.parse(url);
    videoView.setVideoURI(uri);
    //videoView.setZOrderOnTop(true);

    final MediaController controller = new MediaController(context);
    videoView.setMediaController(controller);

    videoView.requestFocus();
    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      //동영상 재생 준비완료!
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
        videoView.seekTo(0);
        videoView.start();
        /*videoView.postDelayed(new Runnable() {
          @Override
          public void run() {
            controller.show(0);
            videoView.pause();
          }
        },100);*/
      }
    });

    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        Toast.makeText(context, "동영상 재생 완료", Toast.LENGTH_LONG).show();
      }
    });

    mvstopbtn.setOnClickListener(new OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
      @Override
      public void onClick(View view) {
        if(videoView.isPlaying()){
          //videoTime[0] = videoView.getCurrentPosition();
          videoView.pause();
          mvstopbtn.setBackground(getContext().getDrawable(R.drawable.play_ic_play));
        }else{
          videoView.start();
          mvstopbtn.setBackground(getContext().getDrawable(R.drawable.play_ic_pause));
        }
      }
    });


  }

  public void mv_popup(View v, String vidnum,String title){
    LayoutInflater inff = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mvpopupView =inff.inflate(R.layout.popup_video,null);

    mvpw = new PopupWindow(v);

    mvpw.setContentView(mvpopupView);
    mvpw.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
    // pw.setTouchable(true);
    mvpw.setFocusable(true);
    mvpw.showAtLocation(v, Gravity.CENTER, 0, 0);



    //popup 내용
    Button mvclosebtn = (Button)mvpopupView.findViewById(R.id.mv_close_btn);
    TextView mvpoptitle = (TextView)mvpopupView.findViewById(R.id.mv_pop_title);
    final Button mvstopbtn = (Button)mvpopupView.findViewById(R.id.mv_pop_stopbtn);
    final VideoView videoView = (VideoView)mvpopupView.findViewById(R.id.mv_view);

    mvpoptitle.setText(title);

    mvclosebtn.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        //비디오 정지
        mvpw.dismiss();
      }
    });

    //비디오

      final int[] videoTime = {0};

      /*
      int type = URL;
        switch (type) {
            case URL:https://github.com/oguzbilgener/CircularFloatingActionMenu
                //동영상 경로가 URL일 경우
                videoView.setVideoURI(Uri.parse(VIDEO_URL));
                break;

            case SDCARD:
                //동영상 경로가 SDCARD일 경우
                String path = Environment.getExternalStorageDirectory()
                        + "/TestVideo.mp4";
                videoView.setVideoPath(path);
                break;
        }

       */
    String url = "https://www.youtube.com/watch?v=d9IxdwEFk1c";
    videoView.setVideoURI(Uri.parse(url));

    final MediaController controller = new MediaController(context);
    videoView.setMediaController(controller);

    videoView.requestFocus();
    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      //동영상 재생 준비완료!
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
          videoView.seekTo(0);
          videoView.start();
        /*videoView.postDelayed(new Runnable() {
          @Override
          public void run() {
            controller.show(0);
            videoView.pause();
          }
        },100);*/
      }
    });

    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        Toast.makeText(context, "동영상 재생 완료", Toast.LENGTH_LONG).show();
      }
    });

    mvstopbtn.setOnClickListener(new OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
      @Override
      public void onClick(View view) {
        if(videoView.isPlaying()){
            //videoTime[0] = videoView.getCurrentPosition();
          videoView.pause();
          mvstopbtn.setBackground(getContext().getDrawable(R.drawable.play_ic_play));
        }else{
            videoView.start();
            mvstopbtn.setBackground(getContext().getDrawable(R.drawable.play_ic_pause));
        }
      }
    });


    mvpw.showAsDropDown(v);
  }



}