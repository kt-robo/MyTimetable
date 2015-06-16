package jp.co.robo.kt.mytimetable;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * SimpleProgressDialogのサブクラス。メッセージとバースタイルのプログレスバー、パーセントを表示する。
 * AsyncTaskWithProgressクラスから使用する。
 * Created by kouta on 15/05/13.
 */
public class ComplexProgressDialog extends SimpleProgressDialog {
    private ProgressBar mProgressBar;
    private TextView mMessageTextView;
    private TextView mPercentTextView;
    private String mMessage;
    private String mPercent;
    private int mMax;
    private int mProgress;

    public ComplexProgressDialog() {
        super();
        mProgressBar = null;
        mMessageTextView = null;
        mPercentTextView = null;
        mMessage = null;
        mPercent = null;
        mMax = -1;
        mProgress = -1;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_complex_progress);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mProgressBar = (ProgressBar)dialog.findViewById(R.id.progressBarHorizontal);
        // このメソッドが完了する前にsetMessage()、setMax()、setProgress()が呼ばれる場合があるようなので、
        // どちらが先に動作してもよいようにする。
        if (mMax >= 0) {
            mProgressBar.setMax(mMax);
        }
        if (mProgress >= 0) {
            mProgressBar.setProgress(mProgress);
        }
        mMessageTextView = (TextView)dialog.findViewById(R.id.progressDialogMessage);
        if (mMessage != null) {
            mMessageTextView.setText(mMessage);
        }
        mPercentTextView = (TextView)dialog.findViewById(R.id.progressDialogPercent);
        if (mPercent != null) {
            mPercentTextView.setText(mPercent);
        }
        return(dialog);
    }

    public void setMessage(String title) {
        if (mMessageTextView == null) {
            mMessage = title;
        } else {
            mMessageTextView.setText(title);
        }
    }

    public void setMax(int max) {
        if (mProgressBar != null) {
            mProgressBar.setMax(max);
        }
        mMax = max;
    }

    public void setProgress(int progress) {
        if (mProgressBar == null) {
            mProgress = progress;
        } else {
            mProgressBar.setProgress(progress);
        }
        int percent = progress * 100 / mMax;
        String text = Integer.toString(percent) + Constants.CommonStrings.PERCENT;
        if (mPercentTextView == null) {
            mPercent = text;
        } else {
            mPercentTextView.setText(text);
        }
    }

}
