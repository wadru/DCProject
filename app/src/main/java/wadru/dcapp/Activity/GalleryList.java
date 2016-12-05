package wadru.dcapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import wadru.dcapp.Database.GalleryDBList;
import wadru.dcapp.R;

import java.util.ArrayList;
import java.util.List;


public class GalleryList extends GallerySearch {
    LayoutInflater mLayoutInflater;

    private ListView listView;
    private EditText editText;
    private CustomAdapter customAdapter;
    private List<String> arrayListNames = new ArrayList<String>();
//    private ArrayList<GalleryDBList> arrayList = new ArrayList<GalleryDBList>();
    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_list);
        listView = (ListView) findViewById(R.id.location_list);

        editText = (EditText) findViewById(R.id.txt_search);

        for (GalleryDBList gd : mLocationItems) {
            arrayListNames.add(gd.getGid());
        }
        customAdapter = new CustomAdapter();


        listView.setAdapter(customAdapter);

        builder = new AlertDialog.Builder(getApplicationContext());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
//                GalleryList.this.customAdapter.getFilter().filter(cs);
               customAdapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
                String a = arrayListNames.get(position);
                int b = 0;
                for(int i=0; i<mLocationItems.size();i++){
                    if(a == mLocationItems.get(i).getGid()){
                        b = i;
                        break;
                    }
                }
                editor.putString("Main",  mLocationItems.get(b).getgUrl());
                editor.putString("Title", mLocationItems.get(b).getGid());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("restart",true);
                startActivity(intent);
                finish();
            }

        });


    }

    class CustomAdapter extends BaseAdapter implements Filterable {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.listitem, null);
                holder.textView = (TextView) convertView.findViewById(R.id.txt_location);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setTextColor(Color.LTGRAY);
            holder.textView.setText(arrayListNames.get(position));

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return arrayListNames.size();
        }

        public class ViewHolder {
            public TextView textView;
        }

        @Override
        public Object getItem(int position) {

            return arrayListNames.get(position);
        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    arrayListNames = (List<String>) results.values;
                    notifyDataSetChanged();

                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<String> Filtered = new ArrayList<String>();

                    // perform your search here using the searchConstraint String.

                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mLocationItems.size(); i++) {
                        String dataNames = mLocationItems.get(i).getGid();
                        if (dataNames.toLowerCase().startsWith(constraint.toString())) {
                            Filtered.add(dataNames);
                        }
                    }

                    results.count = Filtered.size();
                    results.values = Filtered;
//                    Log.e("VALUES", results.values.toString());

                    return results;
                }
            };
            return filter;
        }
    }
}
