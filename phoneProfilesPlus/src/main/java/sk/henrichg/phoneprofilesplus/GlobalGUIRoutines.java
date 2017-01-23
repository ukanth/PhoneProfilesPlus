package sk.henrichg.phoneprofilesplus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GlobalGUIRoutines {

    static BrightnessView brightneesView = null;
    static BrightnessView keepScreenOnView = null;
    static Collator collator = null;

    // import/export
    static final String DB_FILEPATH = "/data/" + PPApplication.PACKAGE_NAME + "/databases";
    static final String REMOTE_EXPORT_PATH = "/PhoneProfiles";
    static final String EXPORT_APP_PREF_FILENAME = "ApplicationPreferences.backup";
    static final String EXPORT_DEF_PROFILE_PREF_FILENAME = "DefaultProfilePreferences.backup";

    // this string is from material-preferences binary (https://github.com/ferrannp/material-preferences)
    //public static final String MAIN_PREFERENCE_FRAGMENT_TAG = "com.fnp.materialpreferences.MainFragment";

    @SuppressWarnings("deprecation")
    public static void setLanguage(Context context)//, boolean restart)
    {
        if (android.os.Build.VERSION.SDK_INT < 24) {

            // jazyk na aky zmenit
            String lang = PPApplication.applicationLanguage;

            Locale appLocale;

            if (!lang.equals("system")) {
                String[] langSplit = lang.split("-");
                if (langSplit.length == 1)
                    appLocale = new Locale(lang);
                else
                    appLocale = new Locale(langSplit[0], langSplit[1]);
            } else {
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                //    appLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
                //else
                    appLocale = Resources.getSystem().getConfiguration().locale;
            }

            Locale.setDefault(appLocale);
            Configuration appConfig = new Configuration();
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            //    appConfig.setLocale(appLocale);
            //else
                appConfig.locale = appLocale;

            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            //    Context context  = context.createConfigurationContext(appConfig);
            //else
                context.getResources().updateConfiguration(appConfig, context.getResources().getDisplayMetrics());
        }

        // collator for application locale sorting
        collator = getCollator();

        //languageChanged = restart;
    }

    public static Collator getCollator()
    {
        if (android.os.Build.VERSION.SDK_INT < 24) {
            // get application Locale
            String lang = PPApplication.applicationLanguage;
            Locale appLocale;
            if (!lang.equals("system")) {
                String[] langSplit = lang.split("-");
                if (langSplit.length == 1)
                    appLocale = new Locale(lang);
                else
                    appLocale = new Locale(langSplit[0], langSplit[1]);
            } else {
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //    appLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
                //} else {
                    //noinspection deprecation
                    appLocale = Resources.getSystem().getConfiguration().locale;
                //}
            }
            // get collator for application locale
            return Collator.getInstance(appLocale);
        }
        else {
            //Log.d("GlobalGUIRoutines.getCollator", java.util.Locale.getDefault().toString());
            return Collator.getInstance();
        }
    }

    public static void setTheme(Activity activity, boolean forPopup, boolean withToolbar)
    {
        activity.setTheme(getTheme(forPopup, withToolbar));
    }

    static int getTheme(boolean forPopup, boolean withToolbar) {
        if (PPApplication.applicationTheme.equals("material"))
        {
            if (forPopup)
            {
                if (withToolbar)
                    return R.style.PopupTheme_withToolbar_material;
                else
                    return R.style.PopupTheme_material;
            }
            else
            {
                if (withToolbar)
                    return R.style.Theme_Phoneprofilestheme_withToolbar_material;
                else
                    return R.style.Theme_Phoneprofilestheme_material;
            }
        }
        else
        if (PPApplication.applicationTheme.equals("dark"))
        {
            if (forPopup)
            {
                if (withToolbar)
                    return R.style.PopupTheme_withToolbar_dark;
                else
                    return R.style.PopupTheme_dark;
            }
            else
            {
                if (withToolbar)
                    return R.style.Theme_Phoneprofilestheme_withToolbar_dark;
                else
                    return R.style.Theme_Phoneprofilestheme_dark;
            }
        }
        else
        if (PPApplication.applicationTheme.equals("dlight"))
        {
            if (forPopup)
            {
                if (withToolbar)
                    return R.style.PopupTheme_withToolbar_dlight;
                else
                    return R.style.PopupTheme_dlight;
            }
            else
            {
                if (withToolbar)
                    return R.style.Theme_Phoneprofilestheme_withToolbar_dlight;
                else
                    return R.style.Theme_Phoneprofilestheme_dlight;
            }
        }
        return 0;
    }

    /*
    public static int getDialogTheme(boolean forAlert) {
        if (PPApplication.applicationTheme.equals("material"))
        {
            if (forAlert)
                return R.style.AlertDialogStyle;
            else
                return R.style.DialogStyle;
        }
        else
        if (PPApplication.applicationTheme.equals("dark"))
        {
            if (forAlert)
                return R.style.AlertDialogStyleDark;
            else
                return R.style.DialogStyleDark;
        }
        else
        if (PPApplication.applicationTheme.equals("dlight"))
        {
            if (forAlert)
                return R.style.AlertDialogStyle;
            else
                return R.style.DialogStyle;
        }
        return 0;
    }
    */

    static void reloadActivity(Activity activity, boolean newIntent)
    {
        if (newIntent)
        {
            final Activity _activity = activity;
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    Intent intent = _activity.getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    _activity.overridePendingTransition(0, 0);
                    _activity.finish();

                    _activity.overridePendingTransition(0, 0);
                    _activity.startActivity(intent);
                }
            });
        }
        else
            activity.recreate();
    }

    public static void setPreferenceTitleStyle(Preference preference, boolean bold, boolean underline, boolean errorColor, boolean systemSettings)
    {
        if (preference != null) {
            CharSequence title = preference.getTitle();
            Spannable sbt = new SpannableString(title);
            Object spansToRemove[] = sbt.getSpans(0, title.length(), Object.class);
            for (Object span : spansToRemove) {
                if (span instanceof CharacterStyle)
                    sbt.removeSpan(span);
            }
            if (systemSettings) {
                String s = title.toString();
                if (!s.contains("(S)"))
                    title = "(S) " + title;
            }
            sbt = new SpannableString(title);
            if (bold || underline) {
                if (bold)
                    sbt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (underline)
                    sbt.setSpan(new UnderlineSpan(), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (errorColor)
                    sbt.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                preference.setTitle(sbt);
            } else {
                preference.setTitle(sbt);
            }
        }
    }

    static void registerOnActivityDestroyListener(Preference preference, PreferenceManager.OnActivityDestroyListener listener) {
        try {
            PreferenceManager pm = preference.getPreferenceManager();
            Method method = pm.getClass().getDeclaredMethod(
                    "registerOnActivityDestroyListener",
                    PreferenceManager.OnActivityDestroyListener.class);
            method.setAccessible(true);
            method.invoke(pm, listener);
        } catch (Exception ignored) {
        }
    }

    static void unregisterOnActivityDestroyListener(Preference preference, PreferenceManager.OnActivityDestroyListener listener) {
        try {
            PreferenceManager pm = preference.getPreferenceManager();
            Method method = pm.getClass().getDeclaredMethod(
                    "unregisterOnActivityDestroyListener",
                    PreferenceManager.OnActivityDestroyListener.class);
            method.setAccessible(true);
            method.invoke(pm, listener);
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets the specified image buttonto the given state, while modifying or
     * "graying-out" the icon as well
     *
     * @param enabled The state of the menu item
     * @param item The menu item to modify
     * @param iconResId The icon ID
     */
    static void setImageButtonEnabled(boolean enabled, AppCompatImageButton item, int iconResId, Context context) {
        item.setEnabled(enabled);
        Drawable originalIcon = ContextCompat.getDrawable(context, iconResId);
        Drawable icon = enabled ? originalIcon : convertDrawableToGrayScale(originalIcon);
        item.setImageDrawable(icon);
    }

    /**
     * Mutates and applies a filter that converts the given drawable to a Gray
     * image. This method may be used to simulate the color of disable icons in
     * Honeycomb's ActionBar.
     *
     * @return a mutated version of the given drawable with a color filter
     *         applied.
     */
    private static Drawable convertDrawableToGrayScale(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Drawable res = drawable.mutate();
        res.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        return res;
    }

    /*
    static float pixelsToSp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }
    */

    private static float spToPixels(Context context, float sp) {
        return sp * context.getResources().getDisplayMetrics().scaledDensity;
    }

    static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /*static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    } */

    /**
     * Uses reflection to access divider private attribute and override its color
     * Use Color.Transparent if you wish to hide them
     */
    static void setSeparatorColorForNumberPicker(NumberPicker picker, int separatorColor) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, new ColorDrawable(separatorColor));
                } catch (IllegalAccessException | IllegalArgumentException ignored) {
                }
                break;
            }
        }
    }

    static void updateTextAttributesForNumberPicker(NumberPicker picker, /*int textColor,*/ int textSizeSP) {
        for (int i = 0; i < picker.getChildCount(); i++){
            View child = picker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = NumberPicker.class.getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);

                    Paint wheelPaint = ((Paint)selectorWheelPaintField.get(picker));
                    //wheelPaint.setColor(textColor);
                    wheelPaint.setTextSize(spToPixels(picker.getContext(), textSizeSP));

                    EditText editText = ((EditText) child);
                    //editText.setTextColor(textColor);
                    editText.setTextSize(textSizeSP);

                    picker.invalidate();
                    break;
                }
                catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ignored) {
                }
            }
        }
    }

    static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                when += TimeZone.getDefault().getOffset(when);

                /*
                int flags = 0;
                flags |= DateUtils.FORMAT_SHOW_TIME;
                flags |= DateUtils.FORMAT_SHOW_DATE;
                flags |= DateUtils.FORMAT_NUMERIC_DATE;
                flags |= DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                        when, flags);

                finalDateTime = DateFormat.getDateFormat(context).format(when) +
                        " " + DateFormat.getTimeFormat(context).format(when);
                */

                /*
                SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy HH:mm:ss");
                finalDateTime = sdf.format(when);
                */

                finalDateTime = timeDateStringFromTimestamp(context, when);
            }
        }
        return finalDateTime;
    }

    @SuppressLint("SimpleDateFormat")
    static String timeDateStringFromTimestamp(Context applicationContext, long timestamp){
        String timeDate;
        String androidDateTime=android.text.format.DateFormat.getDateFormat(applicationContext).format(new Date(timestamp))+" "+
                android.text.format.DateFormat.getTimeFormat(applicationContext).format(new Date(timestamp));
        String javaDateTime = DateFormat.getDateTimeInstance().format(new Date(timestamp));
        String AmPm="";
        if(!Character.isDigit(androidDateTime.charAt(androidDateTime.length()-1))) {
            if(androidDateTime.contains(new SimpleDateFormat().getDateFormatSymbols().getAmPmStrings()[Calendar.AM])){
                AmPm=" "+new SimpleDateFormat().getDateFormatSymbols().getAmPmStrings()[Calendar.AM];
            }else{
                AmPm=" "+new SimpleDateFormat().getDateFormatSymbols().getAmPmStrings()[Calendar.PM];
            }
            androidDateTime=androidDateTime.replace(AmPm, "");
        }
        if(!Character.isDigit(javaDateTime.charAt(javaDateTime.length()-1))){
            javaDateTime=javaDateTime.replace(" "+new SimpleDateFormat().getDateFormatSymbols().getAmPmStrings()[Calendar.AM], "");
            javaDateTime=javaDateTime.replace(" "+new SimpleDateFormat().getDateFormatSymbols().getAmPmStrings()[Calendar.PM], "");
        }
        javaDateTime=javaDateTime.substring(javaDateTime.length()-3);
        timeDate=androidDateTime.concat(javaDateTime);
        return timeDate.concat(AmPm);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

}