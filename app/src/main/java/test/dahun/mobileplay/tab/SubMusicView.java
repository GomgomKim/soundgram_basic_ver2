package test.dahun.mobileplay.tab;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import test.dahun.mobileplay.R;

public class SubMusicView extends LinearLayout {

  ArrayList<Integer> album_arr = new ArrayList<>();
  Context context;
  View view;

  public SubMusicView(Context context, int index, ArrayList<Integer> arr) {
    super(context);

    this.context=context;
    album_arr.addAll(arr);
    LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = li.inflate(R.layout.layout_submusic, this, true);
    initSetting(index);
  }

  public void initSetting(int index){
    ImageView imageView = (ImageView) view.findViewById(R.id.play_2_trackImage);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    switch(index){
      case 0:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(album_arr.get(0)).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 1:
        Glide.with(getContext()).load(album_arr.get(1)).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 2:
        Glide.with(getContext()).load(album_arr.get(2)).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 3:
        Glide.with(getContext()).load(album_arr.get(3)).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 4:
        Glide.with(getContext()).load(album_arr.get(4)).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 5:
        Glide.with(getContext()).load(album_arr.get(5)).apply(new RequestOptions().circleCrop()).into(imageView);
        break;
    }

    //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

  }

}