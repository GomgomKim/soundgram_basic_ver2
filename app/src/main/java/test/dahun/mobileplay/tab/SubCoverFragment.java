package test.dahun.mobileplay.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.adapter.CoverPagerAdapter;
import test.dahun.mobileplay.adapter.CoverVerticalViewPager;
import test.dahun.mobileplay.R;

public class SubCoverFragment extends Fragment {
    @BindView(R.id.coverpager) CoverVerticalViewPager coverPager;
    CoverPagerAdapter coverPagerAdapter;
    LinearLayout layout = null;

    public SubCoverFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.layout = (LinearLayout) inflater.inflate(R.layout.fragment_sub_cover, container, false);
        ButterKnife.bind(this, this.layout);
        initSetting();
        return this.layout;
    }

    public void initSetting(){
        coverPagerAdapter = new CoverPagerAdapter(getChildFragmentManager());
        coverPager.setAdapter(coverPagerAdapter);
    }

}
