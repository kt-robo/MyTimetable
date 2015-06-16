package jp.co.robo.kt.mytimetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * 休日編集ダイアログ。新規追加と編集の両方で使われる。
 * newInstance()の第1引数は選択された休日を表す（選択されたListのposition）値で、'-1'ならば新規。
 * Created by kouta on 15/05/24.
 */
public class EditHolidayDialog extends DialogFragment {
    private NumberPicker mMonthPicker;
    private NumberPicker mDatePicker;
    private CheckBox mHappyMondayCheckBox;
    private EditText mNameEditText;
    private int mSelected;

    public static EditHolidayDialog newInstance(int selected, HolidayInformation original) {
        EditHolidayDialog dialog = new EditHolidayDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.ArgumentNames.SELECTED, selected);
        if (original != null) {
            args.putParcelable(Constants.ArgumentNames.HOLIDAY, original);
        }
        dialog.setArguments(args);
        return(dialog);
    }

    public EditHolidayDialog() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.dialog_edit_holiday, null);

        mSelected = getArguments().getInt(Constants.ArgumentNames.SELECTED);
        HolidayInformation original = getArguments().getParcelable(Constants.ArgumentNames.HOLIDAY);

        mHappyMondayCheckBox = (CheckBox)rootView.findViewById(R.id.checkBoxIsHappyMonday);
        // (mSelected == -1) -> false、(mSelected != -1) -> original.isHappyMonday()
        mHappyMondayCheckBox.setChecked((mSelected != -1) && original.isHappyMonday());
        mHappyMondayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.checkBoxIsHappyMonday) {
                    // チェックボックスが変化したら日付の表示を変更する。
                    setDatePicker();
                }
            }
        });

        mMonthPicker = (NumberPicker)rootView.findViewById(R.id.editHolidayMonthPicker);
        mMonthPicker.setMinValue(1);
        mMonthPicker.setMaxValue(12);
        mMonthPicker.setDisplayedValues(Constants.Holidays.MONTHS);
        mMonthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (picker.getId() == R.id.editHolidayMonthPicker) {
                    // 月が変わったら日付の表示（最大値）を変更する。
                    setDatePicker();
                }
            }
        });

        mDatePicker = (NumberPicker)rootView.findViewById(R.id.editHolidayDatePicker);
        setDatePicker();

        mNameEditText = (EditText)rootView.findViewById(R.id.editHolidayNameEditText);

        if (mSelected != -1) {
            mMonthPicker.setValue(original.getMonth());
            mDatePicker.setValue(original.getDate());
            mNameEditText.setText(original.getName());
        } else {
            mMonthPicker.setValue(1);
            mDatePicker.setValue(1);
        }

        builder.setView(rootView);
        builder.setTitle((mSelected == -1)? Constants.DialogTitles.ADD_HOLIDAY : Constants.DialogTitles.EDIT_HOLIDAY);
        builder.setPositiveButton(Constants.CommonStrings.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((SettingHolidaysActivity)getActivity()).endDialog(
                        mSelected,
                        new HolidayInformation(mMonthPicker.getValue(),
                                mDatePicker.getValue(),
                                mHappyMondayCheckBox.isChecked(),
                                mNameEditText.getText().toString().trim()));
            }
        }).setNegativeButton(Constants.CommonStrings.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return(builder.create());
    }

    // HappyMondayのチェックボックスと月のNumberPickerの状態から日のNumberPickerを設定する。
    private void setDatePicker() {
        mDatePicker.setMinValue(1);
        if (mHappyMondayCheckBox.isChecked()) {
            mDatePicker.setMaxValue(Constants.Holidays.MONDAYS.length);
            mDatePicker.setDisplayedValues(Constants.Holidays.MONDAYS);
        } else {
            mDatePicker.setDisplayedValues(Constants.Holidays.DATES);
            int month = mMonthPicker.getValue();
            int max_date;
            if (month == 2) {
                max_date = 29;
            } else if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
                max_date = 30;
            } else {
                max_date = 31;
            }
            mDatePicker.setMaxValue(max_date);
        }
    }
}
