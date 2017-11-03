package test.dahun.mobileplay.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import test.dahun.mobileplay.R;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class VideoFragment extends Fragment
{

    final String TAG="VideoFragment";
    LinearLayout layout;

    public VideoFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_video, container, false);

        initSetting();

        return layout;
    }

    public void initSetting() {




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
