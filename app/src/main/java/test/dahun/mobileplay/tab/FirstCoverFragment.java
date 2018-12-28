package test.dahun.mobileplay.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import test.dahun.mobileplay.R;

public class FirstCoverFragment extends Fragment {


    public FirstCoverFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_cover, container, false);
    }

}
