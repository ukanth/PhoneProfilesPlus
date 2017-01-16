package sk.henrichg.phoneprofilesplus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmClockBroadcastReceiver extends BroadcastReceiver {
    public AlarmClockBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler());

        PPApplication.logE("##### AlarmClockBroadcastReceiver.onReceive", "xxx");

        Context appContext = context.getApplicationContext();

        if (!PPApplication.getApplicationStarted(context.getApplicationContext(), true))
            return;

        if (android.os.Build.VERSION.SDK_INT >= 21) {

            int zenMode = PPApplication.getSystemZenMode(context, ActivateProfileHelper.ZENMODE_ALL);
            //Log.e("AlarmClockBroadcastReceiver", "zen_mode="+zenMode);

            if (zenMode != ActivateProfileHelper.ZENMODE_ALL) {

                //Log.e("AlarmClockBroadcastReceiver", "zen_mode != ALL");

                DataWrapper dataWrapper = new DataWrapper(appContext, false, false, 0);

                Profile profile = dataWrapper.getActivatedProfile();
                profile = PPApplication.getMappedProfile(profile, appContext);

                if (profile != null) {

                    //Log.e("AlarmClockBroadcastReceiver", "profile is activated");

                    /*
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //System.out.println(e);
                    }

                    Log.e("AlarmClockBroadcastReceiver", "set interruption filter to ALL");

                    PPNotificationListenerService.requestInterruptionFilter(appContext,
                            NotificationListenerService.INTERRUPTION_FILTER_ALL);
                    */

/*
                    final AudioManager audioManager = (AudioManager)appContext.getSystemService(Context.AUDIO_SERVICE);
                    SettingsContentObserver.internalChange = true;
                    //audioManager.setStreamVolume(AudioManager.STREAM_ALARM,  1, 0);
                    //audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
                    audioManager.setStreamMute(AudioManager.STREAM_RING, false);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                    audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
*/
                }
            }
        }
    }
}
