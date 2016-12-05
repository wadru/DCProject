package wadru.dcapp.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;

import wadru.dcapp.Database.GalleryDB;
import wadru.dcapp.Database.GalleryDBColumns;
import wadru.dcapp.Database.GalleryDBList;
import wadru.dcapp.Parser.GalleryParser;
import wadru.dcapp.R;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;


/**
 * Created by hodongkim on 2016. 11. 8..
 */

public class SplashPage extends GalleryDB {
    private ArrayList<GalleryDBList> galleryDBLists;
    private int count;
    private TextView textView;
    private Handler gHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        count = 0;
        textView = (TextView)findViewById(R.id.loading);
        galleryDBLists = new ArrayList<GalleryDBList>();

        Handler sHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                finish();
            }
        };

        gHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ++count;
                switch (count){
                    case 1:
                        textView.setText("갤러리 목록을 가져왔습니다");
                        break;
                    case 2:
                        textView.setText("데이터베이스에 저장 중입니다");
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initDB(gHandler);
                            }
                        });
                        thread.start();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    textView.setText("데이터베이스에 저장시 다소 시간이 소요됩니다");
                                }catch(Exception e){
                                        e.printStackTrace();
                                }
                            }
                        },3000);
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),GalleryList.class));
                        finish();
                        break; // 필요한가?
                }
            }
        };

        if(getIntent().getBooleanExtra("data",true)) {
            String gal = MainActivity.sharedPreferences.getString("Title","최근 접속");
            textView.setText(gal+" 갤러리에 접속합니다");
            sHandler.sendEmptyMessageDelayed(0, 2000);
        }else{
            textView.setText("갤러리 목록을 가져옵니다");
              new AsyncHttpClient().get("http://wstatic.dcinside.com/gallery/gallindex_iframe_new_gallery.html",new GalleryParser(gHandler,galleryDBLists));
              new AsyncHttpClient().get("http://wstatic.dcinside.com/gallery/mgallindex_iframe.html",new GalleryParser(gHandler,galleryDBLists));
        }


    }

    public void initDB(Handler gHandler){
        try{
            String DROP_SQL = "drop table if exists "+ GalleryDBColumns.GalleryDBCol.TABLE_NAME;
            mDB.execSQL(DROP_SQL);
        }catch (Exception e){
            e.printStackTrace();
        }

        mDB.execSQL("CREATE TABLE " + GalleryDBColumns.GalleryDBCol.TABLE_NAME + " ("+
                GalleryDBColumns.GalleryDBCol._ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + GalleryDBColumns.GalleryDBCol.COLUMN_NAME_URL+" TEXT, "+
                GalleryDBColumns.GalleryDBCol.COLUMN_NAME_GID + " TEXT"+");");


        for(int i=0; i<galleryDBLists.size(); i++){
            ContentValues recordValues = new ContentValues();
            recordValues.put(GalleryDBColumns.GalleryDBCol.COLUMN_NAME_URL,galleryDBLists.get(i).getgUrl());
            recordValues.put(GalleryDBColumns.GalleryDBCol.COLUMN_NAME_GID,galleryDBLists.get(i).getGid());
            mDB.insert(GalleryDBColumns.GalleryDBCol.TABLE_NAME,null,recordValues);
        }

        gHandler.sendEmptyMessage(0);

    }
}
