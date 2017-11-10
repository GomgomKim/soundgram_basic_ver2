package test.dahun.mobileplay.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.PictureCustomPagerAdapter;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.ui.VerticalViewPager;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class PictureFragment extends Fragment
{
    @BindView(R.id.picturePager) VerticalViewPager picturePager;

    final String TAG="PictureFragment";
    LinearLayout layout;

    public PictureFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_picture, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {
        MainActivity.context=getContext();
        picturePager.setAdapter(new PictureCustomPagerAdapter());
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            Log.d("SetUserHint","Picture ON");
        else
            Log.d("SetUserHint","Picture OFF");


    }

}
