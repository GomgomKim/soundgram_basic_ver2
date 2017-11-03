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

public class CommentFragment extends Fragment
{

    final String TAG="CommentFragment";
    LinearLayout layout;

    public CommentFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_comment, container, false);

        initSetting();

        return layout;
    }

    public void initSetting() {




    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            Log.d("SetUserHint","Comment ON");
        else
            Log.d("SetUserHint","Comment OFF");


    }

}

