package jp.co.robo.kt.mytimetable;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

/**
 * シンプルなプログレスダイアログ。くるくる回るやつが真ん中に出るだけ。テキスト表示なし。
 * Created by kouta on 15/05/13.
 */
public class SimpleProgressDialog extends DialogFragment {
    private static final String TAG = "SIMPLE_PROGRESS_DIALOG";

    public SimpleProgressDialog() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_simple_progress);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return(dialog);
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }
}
