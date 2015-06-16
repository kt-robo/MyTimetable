package jp.co.robo.kt.mytimetable;

import android.app.Activity;
import android.os.AsyncTask;


/**
 * プログレスダイアログを表示させて非同期処理を行う。
 * サブクラスはdoInBackground()をオーバーライドして、非同期で行う処理を記述する。
 * 表示するプログレスダイアログは、デフォルトではSimpleProgressDialog。createProgressDialog()を
 * オーバーライドすることで、SimpleProgressDialogを継承したプログレスダイアログを使用することができる。
 * Created by kouta on 15/05/19.
 */
public abstract class AsyncTaskWithProgress<T1, T2, T3> extends AsyncTask<T1, T2, T3> {
    private SimpleProgressDialog mProgressDialog;
    private Activity mActivity;

    protected AsyncTaskWithProgress(Activity activity) {
        mActivity = activity;
    }

    public Activity getActivity () {
        return(mActivity);
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = createProgressDialog();
        mProgressDialog.show(mActivity.getFragmentManager());
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    protected void onProgressUpdate(T2... args) {
    }

    @Override
    protected void onPostExecute(T3 arg) {
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    protected SimpleProgressDialog createProgressDialog() {
        return(new SimpleProgressDialog());
    }

    protected SimpleProgressDialog getProgressDialog() {
        return(mProgressDialog);
    }
}
