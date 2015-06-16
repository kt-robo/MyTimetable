package jp.co.robo.kt.mytimetable;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * 非同期でロードを行い、結果をArrayAdapter設定する。
 * Created by kouta on 15/05/19.
 */
public abstract class LoaderWithProgress<T1, T2, T3> extends AsyncTaskWithProgress<T1, T2, ArrayList<T3>> {
    private ArrayAdapter<T3> mAdapter;

    protected LoaderWithProgress(Activity activity) {
        super(activity);
    }

    @Override
    protected void onPostExecute(ArrayList<T3> list) {
        mAdapter.addAll(list);
        super.onPostExecute(list);
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public void load(ArrayAdapter<T3> adapter, T1... args) {
        mAdapter = adapter;
        execute(args);
    }
}
