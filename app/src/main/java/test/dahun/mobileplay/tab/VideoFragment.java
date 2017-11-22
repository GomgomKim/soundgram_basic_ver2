

package test.dahun.mobileplay.tab;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.MovieRecyclerViewAdapter;
import test.dahun.mobileplay.ui.VerticalViewPager;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class VideoFragment extends Fragment
{

    @BindView(R.id.movieRecyclerView) RecyclerView movieRecyclerView;

    final String TAG="VideoFragment";
    LinearLayout layout;

    View mvpopupView;
    PopupWindow mvpw;

    YoutubeListener youtubeListener;

    public interface YoutubeListener{
        void startYoutube(int position);
    }
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

        youtubeListener=new YoutubeListener() {
            @Override
            public void startYoutube(int position) {
                Intent intent=new Intent(getContext(),VideoActivity.class);
                startActivity(intent);
            }
        };

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        movieRecyclerView.setAdapter(new MovieRecyclerViewAdapter(getContext(), youtubeListener));
        movieRecyclerView.setLayoutManager(linearLayoutManager);
        //

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


