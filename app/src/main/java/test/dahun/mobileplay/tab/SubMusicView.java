package test.dahun.mobileplay.tab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import test.dahun.mobileplay.BuildConfig;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.main.MainActivity;

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

    CircleImageView imageView=(CircleImageView) view.findViewById(R.id.play_2_trackImage);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    switch(index){
      case 0:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(R.drawable.albumimg_02).into(imageView);
        break;

      case 1:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(R.drawable.albumimg_03).into(imageView);
        break;

      case 2:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(R.drawable.albumimg_04).into(imageView);
        break;

      case 3:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(R.drawable.albumimg_05).into(imageView);
        break;

      case 4:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(R.drawable.albumimg_06).into(imageView);
        break;

      case 5:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(R.drawable.albumimg_01).into(imageView);
        break;

      case 6:
//        path = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.play_2_trackimg_01);
//        imageView.setImageURI(path);
        Glide.with(getContext()).load(R.drawable.albumimg_02).into(imageView);
        break;
    }

    //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

  }

}