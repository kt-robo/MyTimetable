package jp.co.robo.kt.mytimetable;


import android.app.Activity;

import java.util.ArrayList;

/**
 * AsyncTaskWithProgressを継承し、プログレスを表示しながら時刻表DBを読み込むLoader。
 * Created by kouta on 15/05/13.
 */
public class TimetableLoader extends LoaderWithProgress<Integer, Void, TrainInformation> {

    public TimetableLoader(Activity activity) {
        super(activity);
    }

    @Override
    protected ArrayList<TrainInformation> doInBackground(Integer... args) {
        return(TimetableDbHelper.getInstance(getActivity()).selectAll(args));
    }
}
