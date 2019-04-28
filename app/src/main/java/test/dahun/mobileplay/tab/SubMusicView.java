package test.dahun.mobileplay.tab;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import test.dahun.mobileplay.R;

public class SubMusicView extends LinearLayout {

  Context context;
  View view;

  public SubMusicView(Context context, int index) {
    super(context);

    this.context=context;
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
        Glide.with(getContext()).load(R.drawable.gallary1).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 1:
        Glide.with(getContext()).load(R.drawable.gallary7).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 2:
        Glide.with(getContext()).load(R.drawable.gallary8).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 3:
        Glide.with(getContext()).load(R.drawable.gallary4).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 4:
        Glide.with(getContext()).load(R.drawable.gallary5).apply(new RequestOptions().circleCrop()).into(imageView);
        break;

      case 5:
        Glide.with(getContext()).load(R.drawable.gallary6).apply(new RequestOptions().circleCrop()).into(imageView);
        break;
    }

    //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

  }

}