package wadru.dcapp.Parser;

import android.os.Handler;
import android.util.Log;

import wadru.dcapp.Database.GalleryDBList;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 2016-07-05.
 */
public class GalleryParser extends AsyncHttpResponseHandler {
    private Handler n;
    private ArrayList<GalleryDBList> galleryDBLists;
    private String rs;

    public GalleryParser(Handler n, ArrayList<GalleryDBList> galleryDBLists) {
        super();
        this.n = n;
        this.galleryDBLists = galleryDBLists;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rs = IOUtils.toString(responseBody, "UTF-8");
                    Document dc = Jsoup.parseBodyFragment(rs);
                    Elements el = dc.select("a.list_title, a.list_title1, ul.list_category li a");
                    String gallName = null;
                    for (Element gal : el) {
                        if (gal.attr("href").contains("http://gall"))
                            if (gal.hasText()) {
                                gallName = gal.text().replaceFirst("-", "").trim();
                                galleryDBLists.add(new GalleryDBList(gal.attr("href"), gallName));
                                Log.d("Log yes",gallName);
                            } else {
                                Element span = gal.getElementsByTag("span").get(0);
                                gallName = span.text().replaceFirst("-", "");
                                galleryDBLists.add(new GalleryDBList(gal.attr("href"), gallName));
                                Log.d("Log no",gallName);
                            }
                    }

                    if (galleryDBLists != null) {
                        n.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }


}
