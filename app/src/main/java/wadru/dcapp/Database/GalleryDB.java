package wadru.dcapp.Database;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by user on 2016-07-05.
 */
public class GalleryDB extends AppCompatActivity {
  public  GalleryDBHelper mGalleryDB = null;
   public SQLiteDatabase mDB = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGalleryDB = new GalleryDBHelper(this.getApplicationContext());
        mDB = mGalleryDB.getWritableDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDB != null){
            mDB.close();
        }
        if(mGalleryDB != null){
            mGalleryDB.close();
        }
    }
}
