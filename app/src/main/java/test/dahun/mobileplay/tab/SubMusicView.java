package test.dahun.mobileplay.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.listener.ChangeMusicListener;
import test.dahun.mobileplay.main.MainActivity;

public class SubMusicView extends LinearLayout {

  View view;
  ChangeMusicListener changeMusicListener;

  public SubMusicView(int index, ChangeMusicListener changeMusicListener) {
    super(MainActivity.context);

    this.changeMusicListener=changeMusicListener;
    LayoutInflater li = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = li.inflate(R.layout.layout_submusic, this, true);

    initSetting(index);


  }

  public void initSetting(int index){

    ImageView imageView=(ImageView)view.findViewById(R.id.play_2_trackImage);

    switch(index){
      case 0:
        imageView.setImageResource(R.drawable.play_2_trackimg_01);
        break;
      case 1:
        imageView.setImageResource(R.drawable.play_2_trackimg_02);
        break;
      case 2:
        imageView.setImageResource(R.drawable.play_2_trackimg_03);
        break;
      case 3:
        imageView.setImageResource(R.drawable.play_2_trackimg_04);
        break;
      case 4:
        imageView.setImageResource(R.drawable.play_2_trackimg_05);
        break;
      case 5:
        imageView.setImageResource(R.drawable.play_2_trackimg_06);
        break;
      case 6:
        imageView.setImageResource(R.drawable.play_2_trackimg_07);
        break;
    }


    changeMusicListener.chageMusic(index);
  }

}