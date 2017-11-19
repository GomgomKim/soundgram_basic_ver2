

package test.dahun.mobileplay.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.MovieCustomPagerAdapter;
import test.dahun.mobileplay.adapter.PictureCustomPagerAdapter;
import test.dahun.mobileplay.ui.VerticalViewPager;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class VideoFragment extends Fragment
{

    @BindView(R.id.moviePager) VerticalViewPager moviePager;

    final String TAG="VideoFragment";
    LinearLayout layout;

    public VideoFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_video, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {
        moviePager.setAdapter(new MovieCustomPagerAdapter(getContext()));
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            Log.d("SetUserHint","Video ON");
        else
            Log.d("SetUserHint","Video OFF");


    }

}


