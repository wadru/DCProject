package wadru.dcapp.Navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wadru.dcapp.Activity.MainActivity;
import wadru.dcapp.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by hodongkim on 2016. 11. 14..
 */

public class ExpandNaviBarAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<String> mParentList;
    private ArrayList<String> rec_item;
    private ArrayList<String> sel_item;
    private ChildViewHolder mChildViewHolder;
    private HashMap<String, ArrayList<String>> mChildHashMap;
    private ExpandNaviBarAdapter mExpandNaviBarAdapter;


    public ExpandNaviBarAdapter(Context context, ArrayList<String> parentList, HashMap<String, ArrayList<String>> childHashMap, ArrayList<String> sel_item, ArrayList<String> rec_item) {
        super();
        this.mContext = context;
        this.mParentList = parentList;
        this.mChildHashMap = childHashMap;
        this.sel_item = sel_item;
        this.rec_item = rec_item;
        this.mExpandNaviBarAdapter = ExpandNaviBarAdapter.this;
    }

    @Override
    public int getGroupCount() {
        return mParentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mChildHashMap.get(this.mParentList.get(groupPosition)).size();
//        return sel_item.size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mParentList.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        // groupPostion과 childPosition을 통해 childList의 원소를 얻어옴
        return this.mChildHashMap.get(this.mParentList.get(groupPosition)).get(childPosition);
//        return sel_item.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // stable ID인지 boolean 값으로 반환 ?? 왜 true
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater groupInfla = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // ParentList의 layout 연결. root로 argument 중 parent를 받으며 root로 고정하지는 않음
            convertView = groupInfla.inflate(R.layout.expand_parent,parent,false);
        }
        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView)convertView.findViewById(R.id.expand_parent_text);
        parentText.setText(getGroup(groupPosition));
        return convertView;
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder{
        public TextView child_text;
        public TextView child_item_remove;

        public ChildViewHolder(View itemView) {
            super(itemView);
            child_text = (TextView)itemView.findViewById(R.id.child_text);
            child_item_remove = (TextView)itemView.findViewById(R.id.child_item);

        }
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    // ChildList의 View. 위 ParentList의 View를 얻을 때와 비슷하게 Layout 연결 후, layout 내 TextView, ImageView를 연결
        final String childData = getChild(groupPosition,childPosition);

        if(convertView==null) {
            LayoutInflater childInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInfla.inflate(R.layout.expand_child, null,false);
            mChildViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(mChildViewHolder);
        }else {
            mChildViewHolder = (ChildViewHolder)convertView.getTag();
        }
            mChildViewHolder.child_text.setText(getChild(groupPosition, childPosition));

            mChildViewHolder.child_item_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
                        editor.remove(sel_item.get(childPosition) + "sel");
                        editor.commit();
//                        mChildHashMap.get(mParentList.get(groupPosition)).remove(childPosition);
                        sel_item.remove(childPosition);
                        mExpandNaviBarAdapter.notifyDataSetChanged();
                }
            });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);

        convertView.setLayoutParams(layoutParams);

//            mChildViewHolder.child_item_remove.setText(getChild(groupPosition, childPosition));
        // 위에 getChilde(groupPosition,childPosition).mChildItem ??
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // 선택여부를 boolean 값으로 반환 ?? 왜 트루
        return true;
    }

}
