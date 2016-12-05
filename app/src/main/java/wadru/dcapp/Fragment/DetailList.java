package wadru.dcapp.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import wadru.dcapp.Activity.MainActivity;
import wadru.dcapp.Dataset.MainData;
import wadru.dcapp.Endless.DetailListAdapter;
import wadru.dcapp.Endless.EndlessRecyclerViewScrollListener;
import wadru.dcapp.Extra.Compare;
import wadru.dcapp.Extra.Network;
import wadru.dcapp.Extra.SmoothScroll;
import wadru.dcapp.Parser.DetailParser;
import wadru.dcapp.Parser.ListParser;
import wadru.dcapp.R;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

/**
 * Created by user on 2016-07-03.
 */
public class DetailList extends Fragment {

    private Bundle bundle;
    private ArrayList<MainData> dtList, mdList, rdList, ldList;
    private AsyncHttpClient asyncHttpClient;
    private DetailListAdapter adapter;
    private String url, galleryName, bool_rec;
    private String rec = "&exception_mode=recommend";
    private int itemCount, pageCount, lastitem, firstitem, adapteritem;
    private boolean handlercount, pageNext;
    private Handler mHandler, dHandler, cHandler;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int exlastitem;
    public static String T = "Log";
    public static boolean loadMore;


    public DetailList() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        asyncHttpClient = new AsyncHttpClient();
        galleryName = MainActivity.sharedPreferences.getString("Main", "http://gall.dcinside.com/board/lists/?id=mamamoo");
        url = galleryName + "&page=";
        dtList = new ArrayList<MainData>();
        mdList = new ArrayList<MainData>();
        rdList = new ArrayList<MainData>();
        ldList = new ArrayList<MainData>();
        adapter = new DetailListAdapter(dtList, getContext());
        itemCount = 0;
        pageCount = 1;
        lastitem = 0;
        firstitem = 0;
        handlercount = true;
        loadMore = true;
        pageNext = true;
        exlastitem = 0;
        adapteritem = 0;

        if (bundle.getBoolean("bundle") == true) {
            bool_rec = "";
        } else {
            bool_rec = rec;
        }

        dHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    // 성공했을 때
                    case 0:
                        firstitem++;
                        rdList.add((MainData) msg.getData().getSerializable("data"));
                        if (firstitem > 1) {
                            Compare.compareChange(rdList);
                            dtList.addAll(rdList);
                            rdList.clear();
                            pageNext = true;
                            adapter.notifyItemRangeInserted(adapter.getItemCount(), firstitem - 1);
                            loadMore = true;
                            firstitem = 0;
                        }
                        break;
                    // 마지막 아이템 성공했을 때
                    case -1:
                        Log.d("Log msg", String.valueOf(msg.what));
                        ldList.add((MainData) msg.getData().getSerializable("data"));
                        dtList.addAll(ldList);
                        ldList.clear();
                        adapter.notifyItemRangeInserted(adapter.getItemCount(), 0);
                        break;
                    // 이미지 없는 글인 경우 다음 글 파싱
                    case -2:
                        Log.d("Log msg", String.valueOf(msg.what));
                        if (itemCount == lastitem + 1) {
                            pageCount++;
                            handlercount = true;
                            asyncHttpClient.get(url + String.valueOf(pageCount) + bool_rec, new ListParser(mHandler, mdList));
                        } else {
                            asyncHttpClient.get("http://gall.dcinside.com" + mdList.get(itemCount).gettUrl(), new DetailParser(mdList.get(itemCount), dHandler, 1));
                            itemCount++;
                        }
                        break;
                    // 마지막 글인데 이미지 없는 글
                    case -3:
                        break;
                    // 인터넷 연결 실패나, thread 실패일 때
                    case -4:
                        if (Network.isNetwork(getActivity())) {

                        } else {

                        }
                        break;
                }

            }
        };

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // loadMore 중지
                loadMore = false;
                // ListParser 실패
                if (msg.what == -1) {
                    Log.d("Log msg", "m fail");
                    asyncHttpClient.get(url + String.valueOf(pageCount) + bool_rec, new ListParser(mHandler, mdList));
                    // 이미지 아이템이 없는 경우
                } else if (msg.what == -2) {
                    pageCount++;
                    asyncHttpClient.get(url + String.valueOf(pageCount) + bool_rec, new ListParser(mHandler, mdList));
                    // 성공한 경우
                } else {
                    lastitem = mdList.size() - 1;
                    if (handlercount) {
                        handlercount = false;
                        if (pageCount > 1) {
                            if (!dtList.isEmpty() && itemCount < (lastitem + 1)) {
                                while (exlastitem <= Integer.parseInt(mdList.get(itemCount).getNotice())) {
                                    if (itemCount == lastitem) {
                                        itemCount++;
                                        break;
                                    }
                                    itemCount++;
                                }
                            } else {
                                itemCount = lastitem + 1;
                            }
                        }
                    }

                    // DetailParser Thread 2개 작업 시작
                    for (int i = 0; i < 2; i++) {
                        if (itemCount > lastitem) {
                            pageCount++;
                            handlercount = true;
                            exlastitem = Integer.parseInt(mdList.get(lastitem).getNotice());
                            asyncHttpClient.get(url + String.valueOf(pageCount) + bool_rec, new ListParser(mHandler, mdList));
                            break;
                        } else {
                            if (i == 0 && itemCount == lastitem) {
                                asyncHttpClient.get("http://gall.dcinside.com" + mdList.get(itemCount).gettUrl(), new DetailParser(mdList.get(itemCount), dHandler, 0));
                                itemCount++;
                            } else {
                                asyncHttpClient.get("http://gall.dcinside.com" + mdList.get(itemCount).gettUrl(), new DetailParser(mdList.get(itemCount), dHandler, 1));
                                itemCount++;
                            }
                        }
                    }
                }
            }
        };

        asyncHttpClient.get(url + String.valueOf(pageCount) + bool_rec, new ListParser(mHandler, mdList));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        final SmoothScroll linearLayoutManager = new SmoothScroll(getActivity());

        // Set padding for Tiles
        recyclerView.setLayoutManager(linearLayoutManager);

        // Endless scrollListener onLoadMore 호출
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (loadMore) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh_view);
        // 내려서 새로고침
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loadMore) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            loadMore = false;
                            dtList.clear();
                            mdList.clear();
                            rdList.clear();
                            itemCount = 0;
                            pageCount = 1;
                            lastitem = 0;
                            handlercount = true;
                            firstitem = 0;
                            scrollListener.resetState();
                            adapter.notifyDataSetChanged();
                            asyncHttpClient.get(url + String.valueOf(pageCount) + bool_rec, new ListParser(mHandler, mdList));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다", Toast.LENGTH_SHORT);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        return view;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    //프래그먼트가 화면에 보여질 때
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshLayout.removeAllViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dtList.clear();
        mdList.clear();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
