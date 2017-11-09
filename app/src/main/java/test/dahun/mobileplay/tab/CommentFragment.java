package test.dahun.mobileplay.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.FanRecyclerviewAdapter;
import test.dahun.mobileplay.model.Fan;

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


    //
    FanRecyclerviewAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    private ArrayList<Fan> mItems = new ArrayList<>();
    Fan fan = new Fan();
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_comment, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {

        //List<Fan> items = new ArrayList<>();
        Fan[] item = new Fan[5];
        item[0] = new Fan("fan1","010-1111-2222","2017.11.08 3:46","a");
        item[1] = new Fan("fan2","010-2222-2222","2017.11.08 3:46","b");
        item[2] = new Fan("fan3","010-3333-2222","2017.11.08 3:46","c");
        item[3] = new Fan("fan4","010-4444-2222","2017.11.08 3:46","d");
        item[4] = new Fan("fan5","010-5555-2222","2017.11.08 3:46","e");

        for(int i=0;i<5;i++){
            mItems.add(item[i]);
        }


        //mItems.add(fan);
        adapter = new FanRecyclerviewAdapter(mItems);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

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

