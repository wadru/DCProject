package wadru.dcapp.Endless;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import wadru.dcapp.Activity.MainActivity;
import wadru.dcapp.Download.MediaScanning;
import wadru.dcapp.Extra.StaticChange;
import wadru.dcapp.R;

/**
 * Created by hodongkim on 2016. 11. 25..
 */

public class DownListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> list;
    private Context mContext;
    private DisplayMetrics dm;
    private int width;
    private int height;
    private boolean isData;


    public static class DownViewHolder extends RecyclerView.ViewHolder {
        public ImageView downimg;

        public DownViewHolder(View itemView) {
            super(itemView);
            downimg = (ImageView) itemView.findViewById(R.id.down_img);
        }

    }

    public DownListAdapter(ArrayList<String> list, Context context, boolean isData) {
        super();
        this.list = list;
        this.mContext = context;
        this.isData = isData;

        int pixel = StaticChange.dpToPixel(context,8);
        dm = mContext.getResources().getDisplayMetrics();
        width = dm.widthPixels/2-pixel;
        height = dm.heightPixels-pixel;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

        String fileName = list.get(position);
        final File file = new File(MainActivity.storagePath,fileName);
        Uri imgUri = Uri.fromFile(file);
        Glide.with(mContext).load(imgUri).asBitmap().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESULT).override(width,height).into(((DownViewHolder)holder).downimg);
        ((DownViewHolder)holder).downimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(v.getContext());
                builder.setTitle("이미지 삭제").setMessage("위 이미지를 삭제하시겠습니까?").setCancelable(false).setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        file.delete();
                        list.clear();
                        File file1 = MainActivity.storagePath;
                        String[] mPreList = file1.list();
                        for(int i=mPreList.length-1; i>-1; i--){
                            list.add(mPreList[i]);
                        }
                        DownListAdapter.this.notifyDataSetChanged();
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

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;

        View detailView = layoutInflater.inflate(R.layout.down_gall_item,parent,false);
        viewHolder = new DownViewHolder(detailView);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if(isData){
            return list.size();
        }else{
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
