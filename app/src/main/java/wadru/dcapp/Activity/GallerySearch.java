package wadru.dcapp.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import wadru.dcapp.Database.GalleryDB;
import wadru.dcapp.Database.GalleryDBColumns;
import wadru.dcapp.Database.GalleryDBList;
import wadru.dcapp.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-07-07.
 */
public class GallerySearch extends GalleryDB {
    ArrayList<GalleryDBList> mLocationItems = new ArrayList<GalleryDBList>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_search);

        Cursor c1 = mDB.query(GalleryDBColumns.GalleryDBCol.TABLE_NAME, GalleryDBColumns.GalleryDBCol.ARRAY_COLUMN,null,null,null,null,null);
        int recordCount = c1.getCount();
        for(int i=0; i<recordCount; i++){
            c1.moveToNext();
            mLocationItems.add(new GalleryDBList(c1.getString(0),c1.getString(1)));
        }
        c1.close();
    }
}
