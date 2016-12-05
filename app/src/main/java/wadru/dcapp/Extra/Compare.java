package wadru.dcapp.Extra;


import wadru.dcapp.Dataset.MainData;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by user on 2016-07-08.
 */
public class Compare {
   public static class onDescCompare implements Comparator<MainData> {

        @Override
        public int compare(MainData lhs, MainData rhs) {
            return Integer.parseInt(lhs.getNotice()) > Integer.parseInt(rhs.getNotice()) ? -1 : Integer.parseInt(lhs.getNotice()) < Integer.parseInt(rhs.getNotice()) ? 1 : 0;
        }
    }
    public static class noDescCompare implements Comparator<MainData> {

        @Override
        public int compare(MainData lhs, MainData rhs) {
            return Integer.parseInt(lhs.getNotice()) < Integer.parseInt(rhs.getNotice()) ? -1 : Integer.parseInt(lhs.getNotice()) > Integer.parseInt(rhs.getNotice()) ? 1 : 0;
        }
    }

    public static void compareChange(ArrayList<MainData> datas){
            int last = datas.size() - 1;
            MainData m1 = datas.get(last);
            MainData m2 = datas.get(last - 1);
            if (Integer.parseInt(m1.getNotice()) > Integer.parseInt(m2.getNotice())) {
                datas.set(last - 1, m1);
                datas.set(last, m2);
            } else {
                datas.set(last - 1, m2);
                datas.set(last, m1);
            }
    }

}
