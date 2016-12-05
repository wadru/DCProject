package wadru.dcapp.Dataset;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 2016-06-29.
 */
public class MainData implements Serializable {
    private String notice;
    private String tUrl;
    private String icon;
    private String title;
    private String id;
    private String time;
    private String view;
    private String hit;
    private ArrayList<String> datas;
    private ArrayList<String> imgUrl;

    public MainData(String notice, String url, String icon, String title, String id, String time, String view, String hit, ArrayList<String> datas, ArrayList<String> imgUrl) {
        this.notice = notice;
        this.tUrl = url;
        this.icon = icon;
        this.title = title;
        this.id = id;
        this.time = time;
        this.view = view;
        this.hit = hit;
        this.datas = datas;
        this.imgUrl = imgUrl;
    }

    public String getNotice() {
        return notice;
    }

    public String gettUrl() {
        return tUrl;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getView() {
        return view;
    }

    public String getHit() {
        return hit;
    }

    public ArrayList<String> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<String> qUrl) {
        this.datas = qUrl;
    }

    public ArrayList<String> getImgUrl() {
        return imgUrl;
    }
}
