package wadru.dcapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import wadru.dcapp.Dataset.SharedPref;
import wadru.dcapp.Download.MediaScanning;
import wadru.dcapp.Extra.FgAdapter;
import wadru.dcapp.Extra.StaticChange;
import wadru.dcapp.Extra.TabIcon;
import wadru.dcapp.Fragment.DetailList;
import wadru.dcapp.Fragment.ListDown;
import wadru.dcapp.Navigation.ExpandNaviBarAdapter;
import wadru.dcapp.R;


public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static File storagePath;

    private DrawerLayout mDrawerLayout;
    private DetailList detailRec, detailAll;
    private ListDown downgall;
    private ArrayList<String> parentList, rec_item, sel_item;
    private HashMap<String, ArrayList<String>> childList;

    private ExpandableListView expandableListView;
    private ExpandNaviBarAdapter mExpandNaviBarAdapter;
    private TextView selectgall;
    private SharedPreferences.Editor editor;
    private boolean restart;
    private FgAdapter fgAdapter;
    private TabIcon tabIcon;
    private TabLayout tabs;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ActionBar supportActionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        checkDowndir();

        if(getIntent().getBooleanExtra("restart",false)){
            restart=true;
        }else{
            restart=false;
        }

        dcStart();
        init();
        navi_bar();

    }

    public void checkDowndir(){
        String path = Environment.getExternalStorageDirectory()+"/dcgallery";
        storagePath = new File(path);
        if(!storagePath.exists()){
            storagePath.mkdir();
        }else{

        }

    }

    // 선언
    private void init(){
        detailAll = new DetailList(); detailRec = new DetailList();
        downgall = new ListDown();

        Bundle bAll = new Bundle(); Bundle bRec = new Bundle();

        bAll.putBoolean("bundle",true); bRec.putBoolean("bundle",false);

        detailAll.setArguments(bAll); detailRec.setArguments(bRec);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabIcon = new TabIcon(this);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(viewPager);
        tabIcon.setTabIcon(tabs);



        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
//                Log.d("Log", String.valueOf(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
                    if (tab.getPosition() == 0) {
                        detailAll.getRecyclerView().smoothScrollToPosition(0);
                    } else if (tab.getPosition() == 1) {
                        detailRec.getRecyclerView().smoothScrollToPosition(0);
                    }else if(tab.getPosition() == 2){
                        downgall.getRecyclerView().smoothScrollToPosition(0);
                    }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            supportActionBar.setTitle(sharedPreferences.getString("Title", "디시인사이드") + " 갤러리");
        }

    }

    private void navi_bar(){
        selectgall = (TextView) findViewById(R.id.select_gall);

        parentList = new ArrayList<String>();
//        parentList.add("최근 방문 갤러리");
        parentList.add("즐겨찾는 갤러리");

        childList = new HashMap<String,ArrayList<String>>();

        sel_item = new ArrayList<String>(); rec_item = new ArrayList<String>();

//        rec_item.add(0,sharedPreferences.getString("Title","마마무"));
        editor = sharedPreferences.edit();
        editor.putString(sharedPreferences.getString("Title","마마무")+"rec",sharedPreferences.getString("Main","http://gall.dcinside.com/board/lists/?id=mamamoo"));
        editor.commit();

        for(int i=0; i<sharedPreferences.getInt("sel_size",0); i++){
            sel_item.add(sharedPreferences.getString("sel_gall"+i,"마마무"));
        }

//        for(int i=0; i<sharedPreferences.getInt("rec_size",0); i++){
//            rec_item.add(sharedPreferences.getString("rec_gall"+i,"마마무"));
//        }

//        childList.put(parentList.get(0),rec_item);
        childList.put(parentList.get(0),sel_item);

        expandableListView = (ExpandableListView)findViewById(R.id.expandableList);
        mExpandNaviBarAdapter = new ExpandNaviBarAdapter(this,parentList,childList,sel_item,rec_item);
        expandableListView.setAdapter(mExpandNaviBarAdapter);


        selectgall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gall_url = sharedPreferences.getString("Main","http://gall.dcinside.com/board/lists/?id=mamamoo");
                String gall_title = sharedPreferences.getString("Title","마마무");

                if(sel_item.contains(gall_title)){
                    Toast.makeText(getApplicationContext(),"이미 즐겨찾는 갤러리에 추가되어 있습니다", Toast.LENGTH_SHORT).show();
                } else{
                    editor = sharedPreferences.edit();
                    editor.putString(gall_title+"sel",gall_url);
                    editor.commit();
                    sel_item.add(gall_title);
//                    childList.put(parentList.get(0),sel_item);
                    mExpandNaviBarAdapter.notifyDataSetChanged();
                }
            }
        });


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                mExpandNaviBarAdapter.notifyDataSetChanged();
                SharedPref.shardDataset(editor,rec_item,sel_item);
                    gallChange(groupPosition,childPosition,"sel");
                return false;
            }
        });

    }

    private void gallChange(int groupPosition, int childPosition, String rs){
        editor = sharedPreferences.edit();
        String title = mExpandNaviBarAdapter.getChild(groupPosition,childPosition);
        String url = sharedPreferences.getString(title+rs,"http://gall.dcinside.com/board/lists/?id=mamamoo");
        editor.putString("Main",url);
        editor.putString("Title",title);
        editor.commit();

        Intent intent = new Intent(getApplicationContext(),MainActivity.this.getClass());
        intent.putExtra("restart",true);
        startActivity(intent);
        finish();
    }

    private void dcStart(){
        Intent intent = new Intent(this,SplashPage.class);

        if(sharedPreferences.contains("Main")&&restart==false){
            intent.putExtra("data",true);
            startActivity(intent);
        }else if(sharedPreferences.contains("Main")&&restart==true){

        }else{
            intent.putExtra("data",false);
            startActivity(intent);
            finish();
        }
    }

    // Fragment 설정
    private void setupViewPager(final ViewPager viewPager) {
        fgAdapter = new FgAdapter(getSupportFragmentManager());
        fgAdapter.addFragment(detailAll);
        fgAdapter.addFragment(detailRec);
        fgAdapter.addFragment(downgall);
        viewPager.setAdapter(fgAdapter);

    }


    // 메뉴 아이템 레이아웃 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 메뉴 아이템 기능 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }else if(id == R.id.gall_search){
            SharedPref.shardDataset(editor,rec_item,sel_item);
            startActivity(new Intent(getApplicationContext(),GalleryList.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPref.shardDataset(editor,rec_item,sel_item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StaticChange.clearApplicationCache(this.getApplicationContext(),null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MediaScanning.startFileMediaScan(getApplicationContext(), Uri.fromFile(storagePath));
    }
}
