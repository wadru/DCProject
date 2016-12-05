package wadru.dcapp.Database;

/**
 * Created by user on 2016-07-05.
 */
public class GalleryDBList {

    private String gUrl;
    private String gid;

    public GalleryDBList(String gUrl, String gid) {
        this.gUrl = gUrl;
        this.gid = gid;
    }

    public String getgUrl() {
        return gUrl;
    }

    public String getGid() {
        return gid;
    }

}
