package wadru.dcapp.Download;

import android.os.Handler;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;
import wadru.dcapp.Activity.MainActivity;
import wadru.dcapp.Fragment.ListDown;

/**
 * Created by hodongkim on 2016. 11. 25..
 */

public class ImageDown extends AsyncHttpResponseHandler {
    String imgTime;
    Handler mHandler;

    public ImageDown(String imgTime, Handler handler) {
        super();
        this.imgTime = imgTime;
        this.mHandler = handler;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            File file = new File(MainActivity.storagePath,imgTime+".PNG");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(responseBody);
            fileOutputStream.flush();
            fileOutputStream.close();
            mHandler.sendEmptyMessage(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }
}
