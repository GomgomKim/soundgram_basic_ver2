package test.dahun.mobileplay.interfaces;

import java.util.ArrayList;

public interface GetSongDataInterface {
    void getSongdata();

    ArrayList<Integer> getIsLike();

    void updateSQLDB(int position, int is_like);
}
