package mars.com.timepickerwithinterval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class DateDialog implements OnClickListener {

    private final String TAG = DateDialog.this.getClass().getSimpleName();

    private final static int TIME_PICKER_INTERVAL = 5;

    private Dialog mDateDialog;
    private Activity mActivity;

    private DateDialogListener dListener;
    private TimePicker timePicker;

    public interface DateDialogListener {
        void OnDateValidate(String time);
    }

    public DateDialog(Activity activity) {
        mActivity = activity;

        View rootView = mActivity.getLayoutInflater().inflate(R.layout.date_dialog, null, false);

        timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        Button validateButton = (Button) rootView.findViewById(R.id.validate_btn);
        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_btn);

        validateButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // Set timer picker
        timePicker.setIs24HourView(true);

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        setTimePickerInterval(timePicker);

        // Configure displayed time
        if (((minute % TIME_PICKER_INTERVAL) != 0)) {
            int minuteFloor = (minute + TIME_PICKER_INTERVAL) - (minute % TIME_PICKER_INTERVAL);
            minute = minuteFloor + (minute == (minuteFloor + 1) ? TIME_PICKER_INTERVAL : 0);
            if (minute >= 60) {
                minute = minute % 60;
                hour++;
            }

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute / TIME_PICKER_INTERVAL);
        }

        // Implement dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(rootView);
        mDateDialog = builder.create();

    }

    public void setDateDialogListener(DateDialogListener listener) {
        dListener = listener;
    }

    public void show() {
        mDateDialog.show();
    }

    /**
     * Set TimePicker interval by adding a custom minutes list
     *
     * @param timePicker
     */
    private void setTimePickerInterval(TimePicker timePicker) {
        try {

            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.validate_btn:

                String hour = String.format("%02d", timePicker.getCurrentHour());
                String minute = String.format("%02d", timePicker.getCurrentMinute() * 5);

                String dateTime = String.format(mActivity.getString(R.string.time_text), hour, minute);
                if (dListener != null) {
                    dListener.OnDateValidate(dateTime);
                }
                mDateDialog.cancel();

                break;

            case R.id.cancel_btn:
                mDateDialog.cancel();

                break;

            default:
                break;

        }

    }

}
