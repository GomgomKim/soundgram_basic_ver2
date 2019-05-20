package test.dahun.mobileplay.database;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseGallaryData {

    String json_data = "";

    public ParseGallaryData(String data){
        this.json_data = data;
    }

    public void parseData(){
        try {
            JSONObject jsonObject = new JSONObject(json_data);

            String id = jsonObject.getString("id");
            int age = jsonObject.getInt("age");
            boolean flag = jsonObject.getBoolean("flag");
            double weight = jsonObject.getDouble("weight");


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
