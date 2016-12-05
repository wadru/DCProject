package wadru.dcapp.Activity;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.stream.StreamUriLoader;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.loopj.android.http.AsyncHttpClient;

import wadru.dcapp.Dataset.MainData;
import wadru.dcapp.Download.ImageDown;
import wadru.dcapp.Extra.StaticChange;
import wadru.dcapp.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class DetailPage extends AppCompatActivity {
    private LinearLayout linearLayout;
    private TextView title;
    private TextView id;
    private TextView time;
    private TextView viewer;
    private TextView hit;
    private GenericRequestBuilder<Uri, InputStream, BitmapFactory.Options, BitmapFactory.Options> SIZE_REQUEST;
    private MainData data;
    private Handler mHandler;
    private int imgcount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        data = (MainData)getIntent().getSerializableExtra("data");

        Toolbar toolbar = (Toolbar)findViewById(R.id.dt_toolbar);
        setSupportActionBar(toolbar);


        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 바 뛰움
            supportActionBar.setTitle(data.getTitle());
        }

        linearLayout = (LinearLayout) findViewById(R.id.detail_item);

        title = (TextView)findViewById(R.id.txt_title);
        id = (TextView)findViewById(R.id.txt_id);
        time = (TextView)findViewById(R.id.txt_time);
        viewer = (TextView)findViewById(R.id.txt_view);
        hit = (TextView)findViewById(R.id.txt_hit);

        this.mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(getApplicationContext(),"이미지를 저장하고 있습니다",Toast.LENGTH_SHORT).show();
            }
        };


        title.setText(data.getTitle());
        id.setText(data.getId());
        time.setText(data.getTime());
        viewer.setText("조회수 "+data.getView());
        hit.setText("추천수 "+data.getHit());

        int pixel = StaticChange.dpToPixel(this,32);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        final int width = dm.widthPixels-pixel;
        final int height = dm.heightPixels;

        final int[] maxTextureSize = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);

        imgcount = 0;


        for (int i = 0; i < data.getDatas().size(); i++) {

            if(data.getDatas().get(i).equalsIgnoreCase("moo_img")&&!data.getImgUrl().get(imgcount).isEmpty()) {
                ImageView imageView = new ImageView(this);

                final int t = imgcount;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(layoutParams);
                linearLayout.addView(imageView);
                Glide.with(getApplicationContext()).load(data.getImgUrl().get(imgcount)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE).override(width,height).into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("이미지 다운로드").setMessage("위 이미지를 다운로드 하시겠습니까?").setCancelable(false)
                                .setPositiveButton("다운", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            new AsyncHttpClient().get(data.getImgUrl().get(t),new ImageDown(String.valueOf(SystemClock.elapsedRealtime()),mHandler));
                                    }
                                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

                imgcount++;
            }else if(data.getDatas().get(i).equalsIgnoreCase("\n")){
                View view = new View(this);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,10);
                view.setLayoutParams(layoutParams2);

                linearLayout.addView(view);
            }else if(data.getDatas().get(i).equalsIgnoreCase("\n\n")){
                View view = new View(this);
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,15);
                view.setLayoutParams(layoutParams3);

                linearLayout.addView(view);
            }else{
                TextView textView = new TextView(this);
                textView.setTextSize(16);
                textView.setTextColor(Color.DKGRAY);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams1);

                linearLayout.addView(textView);
                textView.setText(data.getDatas().get(i));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
