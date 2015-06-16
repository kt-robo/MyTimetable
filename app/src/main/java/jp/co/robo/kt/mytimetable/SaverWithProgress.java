package jp.co.robo.kt.mytimetable;


import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

/**
 * AsyncTaskWithProgressのサブクラスで情報の保存を行う。
 * ComplexProgressDialog（バータイプのプログレスバー）を使用し、途中経過も表示する。
 * Created by kouta on 15/05/19.
 */
public abstract class SaverWithProgress<T1, T2, T3> extends AsyncTaskWithProgress<T1, T2, T3> {

    protected SaverWithProgress(Activity activity) {
        super(activity);
    }

    @Override
    protected void onPostExecute(T3 args) {
        Activity activity = getActivity();
        String message = getFinishMessage();
        if (message != null) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
        String action = getFinishAction();
        if (action != null) {
            getActivity().sendBroadcast(new Intent(action));
        }
        super.onPostExecute(args);
        activity.finish();
    }

    @Override
    protected SimpleProgressDialog createProgressDialog() {
        return(new ComplexProgressDialog());
    }

    public void setProgressMessage(String message) {
        ((ComplexProgressDialog)getProgressDialog()).setMessage(message);
    }

    public void setProgressMax(int max) {
        ((ComplexProgressDialog)getProgressDialog()).setMax(max);
    }

    public void setProgressProgress(int progress) {
        ((ComplexProgressDialog)getProgressDialog()).setProgress(progress);
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public void save(T1... args) {
        execute(args);
    }

    public String getFinishMessage() {
        return(null);
    }

    public String getFinishAction() {
        return(null);
    }
}
