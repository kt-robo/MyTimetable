package jp.co.robo.kt.mytimetable;

import android.app.Activity;

/**
 * Preferenceを保存するときのSaver。
 * 他のSaverはComplexProgressDialog（バー形式のプログレスとメッセージとパーセントを表示する）を使うが、
 * Preferenceの保存はさほど時間がかからないと思われるので、SimpleProgressDialog（くるくるだけ）を使う。
 * Created by kouta on 15/06/10.
 */
public class PreferenceSaver extends SaverWithProgress<String, Void, Void> {

    public PreferenceSaver(Activity activity) {
        super(activity);
    }

    @Override
    protected Void doInBackground(String... values) {
        int appWidgetListSize = Integer.parseInt(values[0]);
        if (appWidgetListSize == 0) {
            appWidgetListSize = PreferencesAppWidget.getDefaultAppWidgetListSize();
        }
        new PreferencesAppWidget(getActivity()).setAppWidgetListSize(appWidgetListSize);

        PreferencesUrl preferencesUrl = new PreferencesUrl(getActivity());
        preferencesUrl.setUrlPrefix(values[1]);
        String[] urlSuffixes = new String[values.length - 2];
        System.arraycopy(values, 2, urlSuffixes, 0, urlSuffixes.length);
        preferencesUrl.setUrlSuffixes(urlSuffixes);
        return(null);
    }

    @Override
    protected SimpleProgressDialog createProgressDialog() {
        return(new SimpleProgressDialog());
    }

    @Override
    public String getFinishMessage() {
        return(Constants.Messages.DETAILS_SAVED);
    }
}
