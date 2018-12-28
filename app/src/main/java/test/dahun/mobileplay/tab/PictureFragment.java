

package test.dahun.mobileplay.tab;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.PictureRecyclerViewAdapter;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.model.ApplicationStatus;
import test.dahun.mobileplay.ui.VerticalViewPager;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class PictureFragment extends Fragment
{

    @BindView(R.id.ic_homeBtn)
    Button ic_homeBtn;
    @BindView(R.id.ic_equalizerBtn) ImageView equalbtn;
    @BindView(R.id.navi)
    ImageButton navibtn;
    @BindView(R.id.mn_play) ImageButton playbtn;
    @BindView(R.id.mn_movie) ImageButton moviebtn;
    @BindView(R.id.mn_gallery) ImageButton galbtn;
    @BindView(R.id.mn_comm) ImageButton commbtn;
    @BindView(R.id.ic_mn)
    ImageView btn;

    @BindView(R.id.num) TextView imgnum;
    @BindView(R.id.totalnum) TextView totalimgnum;

    @BindView(R.id.pictureRecyclerView) RecyclerView pictureRecyclerView;

    final String TAG="PictureFragment";
    LinearLayout layout;

    public PictureFragment() {
        super();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_picture, container, false);

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
            Glide.with(getContext()).load(R.drawable.ic_equalizer).into(imageViewTarget);


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
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
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
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
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
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
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
                    params.height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                    navibtn.requestLayout();
                    navibtn.setImageResource(R.drawable.mn_default);
                    navibtn.setTag(R.drawable.mn_default);
                    ViewPagerAdapter.setViewPagerTabListener.setTab(4);
                    //          Toast.makeText(getContext(), "community", Toast.LENGTH_LONG).show();

                }
            });
/////
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        final PictureRecyclerViewAdapter pictureRecyclerViewAdapter=new PictureRecyclerViewAdapter(getContext());
        pictureRecyclerView.setAdapter(pictureRecyclerViewAdapter);
        pictureRecyclerView.setLayoutManager(layoutManager);
        pictureRecyclerView.setHasFixedSize(true);
        pictureRecyclerView.addOnScrollListener(new CenterScrollListener());

        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
            @Override
            public void onCenterItemChanged(int adapterPosition) {
                imgnum.setText(String.valueOf(adapterPosition+1));
            }
        });

        //
        totalimgnum.setText(Integer.toString(pictureRecyclerViewAdapter.getItemCount()));

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Log.d("SetUserHint","Picture ON");

            View view=layout;
            if(view != null){
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(equalbtn);

                if(ApplicationStatus.isPlaying)
                    Glide.with(getContext()).load(R.raw.ic_equalizer_start).into(imageViewTarget);
                else
                    Glide.with(getContext()).load(R.drawable.ic_equalizer).into(imageViewTarget);
            }
        }
        else
            Log.d("SetUserHint","Picture OFF");


    }

}
