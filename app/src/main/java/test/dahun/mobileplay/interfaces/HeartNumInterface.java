package test.dahun.mobileplay.interfaces;

import java.util.ArrayList;

public interface HeartNumInterface {
    ArrayList<Integer> heart_num = new ArrayList<>();
    ArrayList<Integer> is_heart = new ArrayList<>();

    static void numSetting(){
        heart_num.add(13);
        heart_num.add(1789);
        heart_num.add(1142);
        heart_num.add(486);
        heart_num.add(992);
        heart_num.add(96);

        for(int i=0; i<heart_num.size(); i++){
            is_heart.add(0);
        }
    }

    static int getHeartNum (int idx){
        return heart_num.get(idx);
    }

    static void setHeartNum (int idx, int num){
        heart_num.set(idx, num);
    }

    static ArrayList<Integer> getHeartArray(){
        return heart_num;
    }

    static void setIsHeart(int idx, int flag){
        is_heart.set(idx, flag);
    }

    static int getIsHeart(int idx){
        return is_heart.get(idx);
    }
}
