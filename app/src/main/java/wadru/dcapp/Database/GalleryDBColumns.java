package wadru.dcapp.Database;

import android.provider.BaseColumns;

/**
 * Created by user on 2016-07-05.
 */
public class GalleryDBColumns{

    public GalleryDBColumns() {
        super();
    }

    public static abstract class GalleryDBCol implements BaseColumns {
        public static final String TABLE_NAME = "gallerylist";
        public static final String COLUMN_NAME_URL = "gurl";
        public static final String COLUMN_NAME_GID = "gid";
        public static final String[] ARRAY_COLUMN = {COLUMN_NAME_URL,COLUMN_NAME_GID};
    }
}
