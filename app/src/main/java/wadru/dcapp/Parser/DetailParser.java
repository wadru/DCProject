package wadru.dcapp.Parser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import wadru.dcapp.Dataset.MainData;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import wadru.dcapp.Extra.Network;


/**
 * Created by user on 2016-06-28.
 */
public class DetailParser extends AsyncHttpResponseHandler {
    private MainData aLists;
    private Handler h;
    private String res;
    private int countT;

    public DetailParser() {
        super();
    }

    public DetailParser(MainData aLists, Handler h, int countT) {
        super();
        this.aLists = aLists;
        this.h = h;
        this.countT = countT;

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        res = IOUtils.toString(responseBody, "UTF-8");

                        String src = res.replaceFirst("<html>(.|[\\r\\n])*<pre></pre>","");
//                        src = src.replaceFirst("<div id[^>]*>(.|[\\r\\n])*<pre></pre>","");
                        src = src.replaceFirst("<div class=\"con_banner[^>]*>(.|[\\r\\n])*</html>","");

                        InputSource inputSource = new InputSource(new StringReader(src));
                        SAXHandler saxHandler = new SAXHandler();

                        SAXParserImpl.newInstance(null).parse(
                                inputSource, saxHandler
                        );

                        detail_add(saxHandler.getDatas(), saxHandler.getImgUrl(), saxHandler.getImg_ex());
                    } catch (Exception e) {
                        // 실패했을 때
                        h.sendEmptyMessage(-4);
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // 실패했을 때
        h.sendEmptyMessage(-4);
        error.printStackTrace();
    }

    public void detail_add(ArrayList<String> data, ArrayList<String> imgUrl, boolean img) {

        if (img) {
            Message msg =h.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data",new MainData(aLists.getNotice(), aLists.gettUrl(), aLists.getIcon(), aLists.getTitle(), aLists.getId(), aLists.getTime(),
                    aLists.getView(), aLists.getHit(), data, imgUrl));
                 msg.setData(bundle);
            if(countT==0){
                // 마지막 글 이미지가 있는 글이고 성공했을 때
                msg.what=-1;
                h.sendMessage(msg);
            }else {
                // 이미지가 있는 글이고 성공했을 때
                msg.what=0;
                h.sendMessage(msg);
            }

        } else {
            if(countT==0) {
                // 마지막 글 이미지가 아닌 경우 그냥 제거
                h.sendEmptyMessage(-3);
            }else{
                // 이미지 글 아닌 경우 다음 글 파싱
                h.sendEmptyMessage(-2);
            }
        }
    }

}

class SAXHandler extends DefaultHandler {
    private StringBuffer txt = new StringBuffer();
    private ArrayList<String> datas = new ArrayList<String>();
    private ArrayList<String> imgUrl = new ArrayList<String>();
    private boolean img_ex;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        img_ex = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (qName.equalsIgnoreCase("img")) {
            datas.add("moo_img");
            imgUrl.add(attributes.getValue("src"));
            img_ex = true;
        } else if (qName.equalsIgnoreCase("br")) {
            if (txt.toString().trim().length() != 0) {
                datas.add(txt.toString().trim());
            }
            txt.setLength(0);
            datas.add("\n");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        txt.append(ch, start, length);

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if(qName.equalsIgnoreCase("p")||qName.equalsIgnoreCase("td")){
            if (txt.toString().trim().length() != 0) {
                datas.add(txt.toString().trim());
            }
            txt.setLength(0);
            datas.add("\n\n");
        }

    }

    public ArrayList<String> getDatas() {
        return datas;
    }

    public ArrayList<String> getImgUrl() {return imgUrl;}

    public boolean getImg_ex() {
        return img_ex;
    }


}
