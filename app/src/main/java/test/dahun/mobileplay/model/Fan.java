package test.dahun.mobileplay.model;

/**
 * Created by gonghojin on 2017. 11. 8..
 */
////////
public class Fan {
    public String id;
    public String phone;
    public String date;
    public String text;

    public Fan(String id, String phone, String date, String text){
        this.id = id;
        this.phone = phone;
        this.date = date;
        this.text = text;
    }

    public Fan(){

    }

    public String getid(){
        return this.id;
    }

    public String getphone(){
        return this.phone;
    }

    public String getdate(){
        return this.date;
    }

    public String gettext(){
        return this.text;
    }


}
