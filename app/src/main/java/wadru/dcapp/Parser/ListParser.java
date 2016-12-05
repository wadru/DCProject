package wadru.dcapp.Parser;

import android.os.Handler;

import wadru.dcapp.Dataset.MainData;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 2016-06-29.
 */
public class ListParser extends AsyncHttpResponseHandler {
    private Handler h;
    private String rs;
    private Thread thread;
    private ArrayList<MainData> mainDatas;

    public ListParser(Handler h, ArrayList<MainData> mainDatas) {
        super();
        this.h = h;
        this.mainDatas = mainDatas;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        rs = IOUtils.toString(responseBody, "UTF-8");
                        Document dc = Jsoup.parseBodyFragment(rs);
                        Elements el = dc.select("tr.tb");
                        boolean exist = false;
                        for (Element piclt : el) {
                            if (!piclt.getElementsByTag("a").hasClass("icon_notice")&&!piclt.getElementsByTag("a").hasClass("icon_txt_n")) {
                                listadd(mainDatas, piclt);
                                exist = true;
                            }
                        }
                        if(exist) {
                            h.sendEmptyMessage(0);
                        }else{
                            h.sendEmptyMessage(-2);
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        h.sendEmptyMessage(-1);
                    }
                }
            });
            thread.start();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        h.sendEmptyMessage(-1);
        error.printStackTrace();
    }

    // 전체게시판 번호, 제목, 글쓴이, 조회수 등등 ArrayList<MainData> 에 저장
    public void listadd(ArrayList<MainData> datas, Element piclt){
        datas.add(new MainData(
                piclt.getElementsByClass("t_notice").text(),
                piclt.getElementsByTag("a").first().attr("href"),
                piclt.getElementsByTag("a").attr("class"),
                piclt.getElementsByTag("a").first().text(),
                piclt.getElementsByTag("span").text(),
                piclt.getElementsByClass("t_date").attr("title"),
                piclt.select(".t_hits").first().text(),
                piclt.select(".t_hits").last().text(),null,null));

    }
}
