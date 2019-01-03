package test.dahun.mobileplay.adapter;

public class GallaryListItem {
    private int type; // 사진 : 0, 동영상 : 1
    private String title;
    private int img_resource;
    private String video_code;

    public GallaryListItem(){}

    public void setType(int type){ this.type = type; }
    public int getType() { return this.type; }

    public void setTitle(String title){ this.title = title; }
    public String getTitle() { return this.title; }

    public void setImg_resource(int img_resource){ this.img_resource = img_resource; }
    public int getImg_resource() { return this.img_resource; }

    public void setVideo_code(String video_code){ this.video_code = video_code; }
    public String getVideo_code() { return this.video_code; }

}
