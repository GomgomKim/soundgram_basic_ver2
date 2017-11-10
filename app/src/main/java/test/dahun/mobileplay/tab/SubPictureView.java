package test.dahun.mobileplay.tab;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.File;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.main.MainActivity;

public class SubPictureView extends LinearLayout {

  public SubPictureView(int index) {
    super(MainActivity.context);

    LayoutInflater li = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_subpicture, this, true);

    ImageView imageView=(ImageView)v.findViewById(R.id.image);

  }


}