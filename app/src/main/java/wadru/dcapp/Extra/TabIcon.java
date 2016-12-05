package wadru.dcapp.Extra;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;

import wadru.dcapp.R;


/**
 * Created by user on 2016-07-03.
 */
public class TabIcon {
    Context context;
    Drawable myDrawable;

    public TabIcon(Context context) {
        super();
        this.context = context;
    }

    public void setTabIcon(TabLayout tabLayout){
        for (int i= 0; i<tabLayout.getTabCount(); i++) {

            switch (i){
                case 0:
                    tabDraw(tabLayout,0, R.drawable.home);
                    break;
                case 1:
                    tabDraw(tabLayout,1,R.drawable.choice);
                    break;
                case 2:
                   tabDraw(tabLayout,2,R.drawable.down);
                    break;
            }
        }
    }

    public void tabDraw(TabLayout tab_lt,int n,int some){
        myDrawable = context.getResources().getDrawable(some);
        myDrawable.setBounds(0,0,myDrawable.getIntrinsicWidth()/2,myDrawable.getIntrinsicHeight()/2);
        tab_lt.getTabAt(n).setIcon(myDrawable);
    }
}
