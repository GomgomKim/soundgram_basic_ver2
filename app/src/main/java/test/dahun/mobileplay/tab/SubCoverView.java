package test.dahun.mobileplay.tab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.main.MainActivity;

public class SubCoverView extends LinearLayout {
  Context context;
  View view;

  public SubCoverView(Context context, int index) {
    super(context);
    this.context=context;
    LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = li.inflate(R.layout.layout_subcover, this, true);
    initSetting(index);
  }

  public void initSetting(final int index) {
    ImageView imv = (ImageView)view.findViewById(R.id.coverimage);
    switch (index) {
      case 0:
        imv.setImageResource(R.drawable.main_1_bg);
        Log.i("GomgomKim", "SetCoverImg");
        break;
      default:
        break;
    }
  }
}