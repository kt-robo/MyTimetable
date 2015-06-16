package jp.co.robo.kt.mytimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 休日削除ダイアログ。
 * Created by kouta on 15/05/24.
 */
public class DeleteHolidayDialog extends DialogFragment {
    private int mSelected;

    public static DeleteHolidayDialog newInstance(int selected, HolidayInformation original) {
        DeleteHolidayDialog dialog = new DeleteHolidayDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.ArgumentNames.SELECTED, selected);
        args.putParcelable(Constants.ArgumentNames.HOLIDAY, original);
        dialog.setArguments(args);
        return(dialog);
    }

    public DeleteHolidayDialog() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.dialog_delete_holiday, null);

        mSelected = getArguments().getInt(Constants.ArgumentNames.SELECTED);
        HolidayInformation original = getArguments().getParcelable(Constants.ArgumentNames.HOLIDAY);

        ((TextView)rootView.findViewById(R.id.deleteHolidayMonthTextView)).setText(original.getMonthString());
        ((TextView)rootView.findViewById(R.id.deleteHolidayDateTextView)).setText(original.getDateString());
        ((TextView)rootView.findViewById(R.id.deleteHolidayNameTextView)).setText(original.getName());

        builder.setView(rootView);
        builder.setTitle(Constants.DialogTitles.DELETE_HOLIDAY);
        builder.setPositiveButton(Constants.CommonStrings.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((SettingHolidaysActivity)getActivity()).endDialog(mSelected, null);
            }
        }).setNegativeButton(Constants.CommonStrings.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return(builder.create());
    }
}
