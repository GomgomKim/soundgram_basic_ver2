package test.dahun.mobileplay.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import test.dahun.mobileplay.R;

public class SubMovieView extends LinearLayout {

  Context context;
  View view;

  public SubMovieView(Context context, int index) {
    super(context);

    this.context=context;

    LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = li.inflate(R.layout.layout_submovie, this, true);

    initSetting(index);

  }

  public void initSetting(int index){

    //동영상 2개 있을거라고 가정하고 이렇게 했음 SubPictureView 참조해서 ㄱ

    switch(index){
      case 0:
        break;
      case 1:
        break;

    }

  }


}