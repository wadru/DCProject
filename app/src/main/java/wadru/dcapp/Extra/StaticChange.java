package wadru.dcapp.Extra;

import android.content.Context;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.util.TypedValue;

import java.io.File;

/**
 * Created by user on 2016-07-07.
 */
public class StaticChange {

      public static String calculString (String title){
            String titleshit;

            if(title.length()>23){
                String mo = title.substring(0,23);
                titleshit = mo+"...";
            }else {
                titleshit = title;
            }
            return titleshit;
        }

    public static int dpToPixel(Context context, int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static void clearApplicationCache(Context context, File file) {

        File dir = null;

        if (file == null) {
            dir = context.getCacheDir();
        } else {
            dir = file;
        }

        if (dir == null)
            return;

        File[] children = dir.listFiles();
        try {
            for (int i = 0; i < children.length; i++)
                if (children[i].isDirectory())
                    clearApplicationCache(context, children[i]);
                else
                    children[i].delete();
        } catch (Exception e) {
        }
    };

}
