package wadru.dcapp.Endless;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import wadru.dcapp.Activity.DetailPage;
import wadru.dcapp.Activity.MainActivity;
import wadru.dcapp.Dataset.MainData;
import wadru.dcapp.Download.ImageDown;
import wadru.dcapp.Download.MediaScanning;
import wadru.dcapp.Extra.StaticChange;
import wadru.dcapp.R;


/**
 * Created by hodongkim on 2016. 11. 10..
 */
//

// ViewHolder 클래스 타입의 RecyclerView를 상속받는 클래스
public class DetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MainData> datas;
    private Context context;
    private int height, width;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Handler mHandler;

    public DetailListAdapter(ArrayList<MainData> datas, final Context context) {
        super();
        this.datas = datas;
        this.context = context;
        this.height = StaticChange.dpToPixel(context, 200);
        this.width = context.getResources().getDisplayMetrics().widthPixels - StaticChange.dpToPixel(context, 32);

        this.mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(context,"이미지를 저장하고 있습니다",Toast.LENGTH_SHORT).show();
            }
        };


    }

    // RecyclerView에 ViewHolder를 상속받아서 ViewHolder 객체에 들어갈 아이템을 커스터마이징한다.
    // static 처리해서 상속받을 때 데이터 타입으로 한다?
     class DetailViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout img_linear;
        public ImageView iconview;
        public TextView titleview;
        public TextView userid;
        public TextView time;
        public TextView viewer;
        public TextView choose;
        public ImageButton downbtn;


        public DetailViewHolder(View itemView) {
            super(itemView);
            this.iconview = (ImageView) itemView.findViewById(R.id.iconview);
            this.titleview = (TextView) itemView.findViewById(R.id.titleview);
            this.userid = (TextView) itemView.findViewById(R.id.userid);
            this.time = (TextView) itemView.findViewById(R.id.timeview);
            this.viewer = (TextView) itemView.findViewById(R.id.viewer);
            this.choose = (TextView) itemView.findViewById(R.id.choose);

            img_linear = (LinearLayout) itemView.findViewById(R.id.img_linear);

            downbtn = (ImageButton) itemView.findViewById(R.id.fab);
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.ProgressBar_recycler);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (datas.size() == 0) {
            return VIEW_PROG;
        } else {
            return datas.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        }
    }

    // 제네릭 형식의 변수로 ViewHolder 생성, 위에 생성한 ViewHolder클래스를 리턴
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            View detailView = layoutInflater.inflate(R.layout.recycle_item, parent, false);
            viewHolder = new DetailViewHolder(detailView);
        } else {
            View detailView = layoutInflater.inflate(R.layout.recycler_progressbar, parent, false);
            viewHolder = new ProgressViewHolder(detailView);
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof DetailViewHolder) {
            ((DetailViewHolder) holder).img_linear.removeAllViews();

            for (int i = 0; i < datas.get(position).getImgUrl().size(); i++) {
                    ImageView imageview = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                    layoutParams.setMargins(0, 0, 10, 0);
                    imageview.setLayoutParams(layoutParams);
                    ((DetailViewHolder) holder).img_linear.addView(imageview);

                    Glide.with(context).load(datas.get(position).getImgUrl().get(i)).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT).override(width,height).into(imageview);
            }

            switch (datas.get(position).getIcon()) {
                case "icon_pic_b":
                    ((DetailViewHolder) holder).iconview.setImageResource(R.drawable.choo_1);
                    break;
                case "sec_icon":
                    ((DetailViewHolder) holder).iconview.setImageResource(R.drawable.choo_3);
                    break;
                case "icon_txt_b":
                    ((DetailViewHolder) holder).iconview.setImageResource(R.drawable.choo_2);
                    break;
                default:
                    ((DetailViewHolder) holder).iconview.setImageResource(R.drawable.nomal);
                    break;
            }


            ((DetailViewHolder) holder).titleview.setText(" " + StaticChange.calculString(datas.get(position).getTitle()));
            ((DetailViewHolder) holder).userid.setText(datas.get(position).getId());

            final String date = datas.get(position).getTime();
            try {
                Date d = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").parse(date);
                Date now = new Date(System.currentTimeMillis());

                if (now.getDate() == d.getDate()) {
                    String curTime = String.format("%02d:%02d", d.getHours(), d.getMinutes());
                    ((DetailViewHolder) holder).time.setText(curTime);
                } else {
                    String curTime = String.format("%02d.%02d.%02d",d.getYear()-100,d.getMonth()+1,d.getDate());
                    ((DetailViewHolder) holder).time.setText(curTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((DetailViewHolder) holder).viewer.setText(" | 조회 " + datas.get(position).getView());
            ((DetailViewHolder) holder).choose.setText(" | 추천 " + datas.get(position).getHit());

            ((DetailViewHolder) holder).downbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("전체 다운로드").setMessage(String.valueOf(datas.get(position).getImgUrl().size())+"장의 이미지를 다운로드 하시겠습니까?").setCancelable(false)
                    .setPositiveButton("다운", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int i = 0; i < datas.get(position).getImgUrl().size(); i++){
                                new AsyncHttpClient().get(datas.get(position).getImgUrl().get(i),new ImageDown(String.valueOf(SystemClock.elapsedRealtime()),mHandler));
                            }
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

            ((DetailViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.back));
                    Intent intent = new Intent(context, DetailPage.class);
                    intent.putExtra("data",datas.get(position));
                    context.startActivity(intent);
                }
            });

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    // 데이터 개수
    @Override
    public int getItemCount() {

        if (datas.size() == 0) {
            return 1;
        } else {
            return datas.size();
        }
    }
}

