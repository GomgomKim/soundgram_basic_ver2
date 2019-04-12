
package test.dahun.mobileplay.tab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.interfaces.ButtonInterface;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

/**
 * Created by Gomgomkim on 2018. 12. 28..
 */

public class CoverFragment extends Fragment
{
    final String TAG="CoverFragment";

    @BindView(R.id.cover) LinearLayout cover;

    @BindView(R.id.cover_img) ImageView cover_img;
    @BindView(R.id.reflect_img) ImageView reflect_img;
    @BindView(R.id.cover_bottom) ImageView cover_bottom;

    @BindView(R.id.info_btn) ImageButton info_btn;

    RelativeLayout layout = null;


    public CoverFragment() {
        super();
    }


    @Nullable
    @SuppressLint("NewApi")
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_cover, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        return layout;
    }

    public void initSetting() {
        Glide.with(getContext()).load(R.drawable.cover)
                .apply(new RequestOptions().fitCenter()).into(cover_img);

        Glide.with(getContext()).load(R.drawable.bg_home)
                .apply(new RequestOptions().fitCenter()).into(cover_bottom);

        cover.bringToFront();

        Glide.with(getContext()).load(R.drawable.album_reflectlight)
                .apply(new RequestOptions().fitCenter()).into(reflect_img);

        info_btn.setOnClickListener(view -> startActivity(new Intent(getActivity(), SurviceInfoActivity.class)));

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                ((ButtonInterface)getContext()).reset();
                ((ButtonInterface)getContext()).homeOn();
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }


}