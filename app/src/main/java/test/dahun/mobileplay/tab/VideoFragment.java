

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
import android.util.TypedValue;
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
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.MovieRecyclerViewAdapter;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.model.ApplicationStatus;
import test.dahun.mobileplay.ui.VerticalViewPager;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class VideoFragment extends Fragment
{

    @BindView(R.id.ic_homeBtn)
    Button ic_homeBtn;

    @BindView(R.id.ic_equalizerBtn) ImageView equalbtn;

    @BindView(R.id.navi) ImageButton navibtn;
    @BindView(R.id.mn_play) ImageButton playbtn;
    @BindView(R.id.mn_movie) ImageButton moviebtn;
    @BindView(R.id.mn_gallery) ImageButton galbtn;
    @BindView(R.id.mn_comm) ImageButton commbtn;
    @BindView(R.id.ic_mn) ImageView btn;

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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initSetting() {
        //homebtn
        ic_homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerAdapter.setViewPagerTabListener.setTab(0);
            }
        });
        //
        //equalizer
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(equalbtn);

        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);

        equalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerAdapter.setViewPagerTabListener.setTab(1);
            }
        });
        //navibutton
            ViewGroup.LayoutParams params = navibtn.getLayoutParams();
            params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
            params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            navibtn.requestLayout();
            navibtn.setImageResource(R.drawable.mn_default);
            navibtn.setTag(R.drawable.mn_default);

            navibtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if((Integer)view.getTag() == R.drawable.mn_default){
                        playbtn.setVisibility(View.VISIBLE);
                        moviebtn.setVisibility(View.VISIBLE);
                        galbtn.setVisibility(View.VISIBLE);
                        commbtn.setVisibility(View.VISIBLE);
                        //btn.setVisibility(View.VISIBLE);

                        ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
                        navibtn.requestLayout();
                        navibtn.setImageResource(R.drawable.mn_click);
                        navibtn.setTag(R.drawable.mn_click);
                    }else{
                        playbtn.setVisibility(View.GONE);
                        moviebtn.setVisibility(View.GONE);
                        galbtn.setVisibility(View.GONE);
                        commbtn.setVisibility(View.GONE);
                        btn.setVisibility(View.GONE);
                        ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                        params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                        params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                        navibtn.requestLayout();
                        navibtn.setImageResource(R.drawable.mn_default);
                        navibtn.setTag(R.drawable.mn_default);
                    }
                }
            });

            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playbtn.setVisibility(View.GONE);
                    moviebtn.setVisibility(View.GONE);
                    galbtn.setVisibility(View.GONE);
                    commbtn.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);

                    ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                    params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_default);
                    navibtn.setTag(R.drawable.mn_default);
                    ViewPagerAdapter.setViewPagerTabListener.setTab(1);
                    //Toast.makeText(getContext(), "music", Toast.LENGTH_LONG).show();
                }
            });

            moviebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playbtn.setVisibility(View.GONE);
                    moviebtn.setVisibility(View.GONE);
                    galbtn.setVisibility(View.GONE);
                    commbtn.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);

                    ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                    params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_default);
                    navibtn.setTag(R.drawable.mn_default);
                    ViewPagerAdapter.setViewPagerTabListener.setTab(2);
                    //      Toast.makeText(getContext(), " movie", Toast.LENGTH_LONG).show();

                }
            });

            galbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playbtn.setVisibility(View.GONE);
                    moviebtn.setVisibility(View.GONE);
                    galbtn.setVisibility(View.GONE);
                    commbtn.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);

                    ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                    params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_default);
                    navibtn.setTag(R.drawable.mn_default);
                    ViewPagerAdapter.setViewPagerTabListener.setTab(3);
                    //          Toast.makeText(getContext(), "gallery", Toast.LENGTH_LONG).show();

                }
            });

            commbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playbtn.setVisibility(View.GONE);
                    moviebtn.setVisibility(View.GONE);
                    galbtn.setVisibility(View.GONE);
                    commbtn.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);

                    ViewGroup.LayoutParams params = navibtn.getLayoutParams();
                    params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_default);
                    navibtn.setTag(R.drawable.mn_default);
                    ViewPagerAdapter.setViewPagerTabListener.setTab(4);
                    //          Toast.makeText(getContext(), "community", Toast.LENGTH_LONG).show();

                }
            });
/////
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
        if(isVisibleToUser){
            Log.d("SetUserHint","Video ON");

            View view=layout;
            if(view != null){
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(equalbtn);

                if(ApplicationStatus.isPlaying)
                    Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);
                else
                    Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);
            }
        }
        else
            Log.d("SetUserHint","Video OFF");


    }
}


