package wadru.dcapp.Fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

import wadru.dcapp.Activity.MainActivity;
import wadru.dcapp.Endless.DownListAdapter;
import wadru.dcapp.Endless.EndlessRecyclerViewScrollListener;
import wadru.dcapp.Extra.SmoothScroll;
import wadru.dcapp.Parser.ListParser;
import wadru.dcapp.R;

/**
 * Created by user on 2016-07-03.
 */
public class ListDown extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ArrayList<String> imgList;
    private String[] preList;
    private File list;
    private DownListAdapter adapter;
    private static boolean mloadMore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            mloadMore = true;

            list = MainActivity.storagePath;
            imgList = new ArrayList<String>();
            preList = list.list();
            if(preList.length==0) {
                adapter = new DownListAdapter(imgList, getContext(),false);
            }else {
                for(int i=preList.length-1; i>-1; i--){
                    imgList.add(preList[i]);
                }
                adapter = new DownListAdapter(imgList, getContext(),true);
            }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.down_gallery,container,false);
        recyclerView =(RecyclerView)view.findViewById(R.id.down_gall);
        recyclerView.setAdapter(adapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setHasFixedSize(true);
        // Set padding for Tiles
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.refresh_down_gall);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mloadMore) {
                    mloadMore =false;
                        String[] mPreList = list.list();
                        imgList.clear();
                        for(int i=mPreList.length-1; i>-1; i--){
                        imgList.add(mPreList[i]);
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    mloadMore = true;
                }else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        return view;

    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }
}
