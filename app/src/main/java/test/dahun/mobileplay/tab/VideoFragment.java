

package test.dahun.mobileplay.tab;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.MovieRecyclerViewAdapter;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.interfaces.ApplicationStatus;

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

    YoutubeListener youtubeListener;

    public interface YoutubeListener{
        void startYoutube(int position);
    }
    public VideoFragment() {
        super();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        ic_homeBtn.setOnClickListener(view -> ViewPagerAdapter.setViewPagerTabListener.setTab(0));
        //
        //equalizer
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(equalbtn);

        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);

        equalbtn.setOnClickListener(view -> ViewPagerAdapter.setViewPagerTabListener.setTab(1));
        //navibutton
            ViewGroup.LayoutParams params = navibtn.getLayoutParams();
            params.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
            params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            navibtn.requestLayout();
            navibtn.setImageResource(R.drawable.mn_default);
            navibtn.setTag(R.drawable.mn_default);

            navibtn.setOnClickListener(view -> {
                if((Integer)view.getTag() == R.drawable.mn_default){
                    playbtn.setVisibility(View.VISIBLE);
                    moviebtn.setVisibility(View.VISIBLE);
                    galbtn.setVisibility(View.VISIBLE);
                    commbtn.setVisibility(View.VISIBLE);
                    //btn.setVisibility(View.VISIBLE);

                    ViewGroup.LayoutParams params13 = navibtn.getLayoutParams();
                    params13.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    params13.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_click);
                    navibtn.setTag(R.drawable.mn_click);
                }else{
                    playbtn.setVisibility(View.GONE);
                    moviebtn.setVisibility(View.GONE);
                    galbtn.setVisibility(View.GONE);
                    commbtn.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params13 = navibtn.getLayoutParams();
                    params13.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    params13.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_default);
                    navibtn.setTag(R.drawable.mn_default);
                }
            });

            playbtn.setOnClickListener(view -> {
                playbtn.setVisibility(View.GONE);
                moviebtn.setVisibility(View.GONE);
                galbtn.setVisibility(View.GONE);
                commbtn.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);

                ViewGroup.LayoutParams params1 = navibtn.getLayoutParams();
                params1.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                params1.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                navibtn.requestLayout();
                navibtn.setImageResource(R.drawable.mn_default);
                navibtn.setTag(R.drawable.mn_default);
                ViewPagerAdapter.setViewPagerTabListener.setTab(1);
                //Toast.makeText(getContext(), "music", Toast.LENGTH_LONG).show();
            });

            moviebtn.setOnClickListener(view -> {
                playbtn.setVisibility(View.GONE);
                moviebtn.setVisibility(View.GONE);
                galbtn.setVisibility(View.GONE);
                commbtn.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);

                ViewGroup.LayoutParams params12 = navibtn.getLayoutParams();
                params12.width =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                params12.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                navibtn.requestLayout();
                navibtn.setImageResource(R.drawable.mn_default);
                navibtn.setTag(R.drawable.mn_default);
                ViewPagerAdapter.setViewPagerTabListener.setTab(2);
                //      Toast.makeText(getContext(), " movie", Toast.LENGTH_LONG).show();

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
        youtubeListener= position -> {
            Intent intent=new Intent(getContext(),VideoActivity.class);
            startActivity(intent);
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
                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(equalbtn);

                if(ApplicationStatus.isPlaying)
                    Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
                else
                    Glide.with(getContext()).load(R.drawable.ic_equalizer_stop).into(imageViewTarget);
            }
        }
        else
            Log.d("SetUserHint","Video OFF");


    }
}


