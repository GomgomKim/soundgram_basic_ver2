package test.dahun.mobileplay.tab;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.main.MainActivity;

public class SubPictureView extends LinearLayout {

  Context context;
  View view;

  public SubPictureView(Context context, int index) {
    super(context);

    this.context=context;

    LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = li.inflate(R.layout.layout_subpicture, this, true);

    initSetting(index);

  }

  public void initSetting(int index){

    ImageView photoView=(ImageView)view.findViewById(R.id.image);

    switch(index){
      case 0:
        Glide.with(getContext()).load(R.drawable.photo_img_01).into(photoView);
        break;
      case 1:
        Glide.with(getContext()).load(R.drawable.photo_img_02).into(photoView);
        break;
      case 2:
        Glide.with(getContext()).load(R.drawable.photo_img_03).into(photoView);
        break;
      case 3:
        Glide.with(getContext()).load(R.drawable.photo_img_04).into(photoView);
        break;
      case 4:
        Glide.with(getContext()).load(R.drawable.photo_img_05).into(photoView);
        break;
      case 5:
        Glide.with(getContext()).load(R.drawable.photo_img_06).into(photoView);
        break;
    }

    //imageView.setScaleType(ImageView.ScaleType.FIT_XY);


  }


}