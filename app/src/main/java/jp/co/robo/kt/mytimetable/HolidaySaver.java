package jp.co.robo.kt.mytimetable;

import android.app.Activity;


/**
 * 休日一覧を書き込むSaver。
 * Created by kouta on 15/05/20.
 */
public class HolidaySaver extends SaverWithProgress<HolidayInformation, Integer, Void> {

    public HolidaySaver(Activity activity) {
        super(activity);
    }

    @Override
    protected Void doInBackground(HolidayInformation... list) {
        setProgressMessage(Constants.Messages.SAVING_HOLIDAYS);
        setProgressMax(list.length);
        HolidayDbHelper.getInstance(getActivity()).renew(list, this);
        return(null);
    }

    @Override
    public void onProgressUpdate(Integer... args) {
        setProgressProgress(args[0]);
    }

    @Override
    public String getFinishMessage() {
        return(Constants.Messages.HOLIDAYS_SAVED);
    }

    public void setProgress(int progress) {
        publishProgress(progress);
    }
}
