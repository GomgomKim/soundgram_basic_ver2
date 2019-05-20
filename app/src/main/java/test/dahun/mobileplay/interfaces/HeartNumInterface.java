package test.dahun.mobileplay.interfaces;

import java.util.ArrayList;

public interface HeartNumInterface {
    ArrayList<Integer> is_heart = new ArrayList<>();

    static void numSetting(){
        for(int i=0; i<6; i++){
            is_heart.add(0);
        }
    }

    static void setIsHeart(int idx, int flag){
        is_heart.set(idx, flag);
    }

    static int getIsHeart(int idx){
        return is_heart.get(idx);
    }
}
