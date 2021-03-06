package com.example.OnlineMarket.viewmodel;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.example.OnlineMarket.view.fragment.NotificationTimePickerFragment;
import com.example.OnlineMarket.view.fragment.SettingsFragment;
import com.example.OnlineMarket.R;
import com.example.OnlineMarket.services.PollJobService;
import com.example.OnlineMarket.worker.PollWorker;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SettingViewModel extends AndroidViewModel {
    public static final String TAG = "Setting";
    private boolean notificationOn = PollWorker.isWorkScheduled(getApplication());
    private boolean exactNotificationOn = PollJobService.isJobScheduled(getApplication());
    private long minutes = 3;
    private long delayMilliSecond = -1;

    public SettingViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDelayMilliSecond(long delayMilliSecond) {
        this.delayMilliSecond = delayMilliSecond;
    }

    public boolean getNotificationState() {
        return PollWorker.isWorkScheduled(getApplication());
    }

    public void onSplitTypeChanged(RadioGroup radioGroup, int id) {
        Log.d(TAG, "onSplitTypeChanged: " + radioGroup.getCheckedRadioButtonId() + " id " + id);
        switch (id) {
            case (R.id.five_radio_button):
                minutes = 300;
                break;
            case (R.id.eight_radio_button):
                minutes = 480;
                break;
            case (R.id.twelve_radio_button):
                minutes = 720;
                break;
            default:
                minutes = 3;
                break;
        }
        Log.d(TAG, "onSplitTypeChanged: " + minutes);
    }

    public void onCheckedChangedPeriodic(boolean checked) {
        notificationOn = checked;
        Log.d(TAG, "onCheckedChangedPeriodic: " + checked);
    }

    public boolean setSettings() {
        PollWorker.scheduleWork(getApplication(), notificationOn, minutes);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (delayMilliSecond < 0 && exactNotificationOn)
                return true;
            PollJobService.scheduleJob(getApplication(), exactNotificationOn, delayMilliSecond);
        }
        return true;
    }

    public boolean getStateExactNotification() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            return PollJobService.isJobScheduled(getApplication());
        return false;
    }

    public void onCheckedChangedExact(boolean checked) {
        exactNotificationOn = checked;
        Log.d(TAG, "onCheckedChangedExact: " + checked);
    }

    public void onClickExactTimeSelect(Fragment fragment) {
        NotificationTimePickerFragment timePickerFragment = NotificationTimePickerFragment.newInstance();
        timePickerFragment.setTargetFragment(fragment, SettingsFragment.REQUEST_CODE_TIME_PICKER);
        timePickerFragment.show(fragment.getParentFragmentManager(), SettingsFragment.DIALOG_FRAGMENT_TAG);
    }
}
