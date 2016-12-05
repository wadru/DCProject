package wadru.dcapp.Dataset;

import android.content.SharedPreferences;

import wadru.dcapp.Activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by hodongkim on 2016. 11. 17..
 */

public class SharedPref {

    public static void shardDataset(SharedPreferences.Editor editor, ArrayList<String> rec_item, ArrayList<String> sel_item){
        editor = MainActivity.sharedPreferences.edit();

        for(int i=0; i<MainActivity.sharedPreferences.getInt("sel_size",0); i++) {
            editor.remove("sel_gall"+i);
        }
//        for(int i=0; i<MainActivity.sharedPreferences.getInt("rec_size",0); i++){
//            editor.remove("rec_gall"+i);
//        }
//
//        for(int i=0; i<rec_item.size(); i++){
//            editor.putString("rec_gall"+i,rec_item.get(i));
//        }
//        editor.putInt("rec_item",rec_item.size());

        for(int i=0; i<sel_item.size(); i++) {
            editor.putString("sel_gall" + i, sel_item.get(i));
        }
        editor.putInt("sel_size",sel_item.size());

        editor.commit();
    }

}
