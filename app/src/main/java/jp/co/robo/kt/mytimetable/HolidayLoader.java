package jp.co.robo.kt.mytimetable;


import android.app.Activity;

import java.util.ArrayList;

/**
 * 休日一覧を非同期で読み込むLoader。
 * Created by kouta on 15/05/13.
 */
public class HolidayLoader extends LoaderWithProgress<Void, Void, HolidayInformation> {

    public HolidayLoader(Activity activity) {
        super(activity);
    }

    @Override
    protected ArrayList<HolidayInformation> doInBackground(Void... args) {
        return(HolidayDbHelper.getInstance(getActivity()).selectAll());
    }
}
