package jp.co.robo.kt.mytimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * メッセージを表示するダイアログ。
 * Created by kouta on 15/06/03.
 */
public class MessageDialog extends DialogFragment {
    private static final String TAG = "MESSAGE_DIALOG";

    public static MessageDialog newInstance(String title, String message) {
        MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(Constants.ArgumentNames.TITLE, title);
        args.putString(Constants.ArgumentNames.MESSAGE, message);
        dialog.setArguments(args);
        return(dialog);
    }

    public MessageDialog() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString(Constants.ArgumentNames.TITLE));
        builder.setMessage(getArguments().getString(Constants.ArgumentNames.MESSAGE));
        builder.setPositiveButton(Constants.CommonStrings.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return(builder.create());
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }
}
