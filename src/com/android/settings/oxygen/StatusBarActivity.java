/*
 * Copyright (C) 2011 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class StatusBarActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private static final String BATTERY_STYLE = "battery_style";

    private static final String HIDE_CLOCK_PREF = "pref_hide_clock";

    private static final String UI_EXP_WIDGET = "expanded_widget";

    private static final String UI_EXP_WIDGET_HIDE_ONCHANGE = "expanded_hide_onchange";

    private static final String UI_EXP_WIDGET_HIDE_INDICATOR = "expanded_hide_indicator";

    private static final String UI_EXP_WIDGET_HIDE_SCROLLBAR = "expanded_hide_scrollbar";

    private static final String UI_EXP_WIDGET_HAPTIC_FEEDBACK = "expanded_haptic_feedback";

    private static final String UI_EXP_WIDGET_COLOR = "expanded_color_mask";

    private static final String UI_EXP_WIDGET_PICKER = "widget_picker";

    private static final String UI_EXP_WIDGET_ORDER = "widget_order";

    private ListPreference mBatteryStyle;

    private CheckBoxPreference mHideClock;

    private CheckBoxPreference mPowerWidget;

    private CheckBoxPreference mPowerWidgetHideOnChange;

    private CheckBoxPreference mPowerWidgetIndicatorHide;

    private CheckBoxPreference mPowerWidgetHideScrollBar;

    private ListPreference mPowerWidgetHapticFeedback;

    private Preference mPowerWidgetColor;

    private PreferenceScreen mPowerPicker;

    private PreferenceScreen mPowerOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.statusbar_settings_title);
        addPreferencesFromResource(R.xml.statusbar_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mBatteryStyle = (ListPreference) prefSet
                .findPreference(BATTERY_STYLE);

        mHideClock = (CheckBoxPreference) prefSet
                .findPreference(HIDE_CLOCK_PREF);

        mHideClock.setChecked(Settings.System.getInt(getContentResolver(), 
                Settings.System.HIDE_CLOCK, 0) == 1);

        /* Expanded View Power Widget */
        mPowerWidget = (CheckBoxPreference) prefSet.findPreference(UI_EXP_WIDGET);
        mPowerWidgetHideOnChange = (CheckBoxPreference) prefSet
                .findPreference(UI_EXP_WIDGET_HIDE_ONCHANGE);
        mPowerWidgetHideScrollBar = (CheckBoxPreference) prefSet
                .findPreference(UI_EXP_WIDGET_HIDE_SCROLLBAR);
        mPowerWidgetIndicatorHide = (CheckBoxPreference) prefSet
                .findPreference(UI_EXP_WIDGET_HIDE_INDICATOR);
        mPowerWidgetHapticFeedback = (ListPreference) prefSet
                .findPreference(UI_EXP_WIDGET_HAPTIC_FEEDBACK);
        mPowerWidgetHapticFeedback.setOnPreferenceChangeListener(this);

        mPowerWidgetColor = prefSet.findPreference(UI_EXP_WIDGET_COLOR);
        mPowerPicker = (PreferenceScreen) prefSet.findPreference(UI_EXP_WIDGET_PICKER);
        mPowerOrder = (PreferenceScreen) prefSet.findPreference(UI_EXP_WIDGET_ORDER);

        mPowerWidget.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, 1) == 1));
        mPowerWidgetHideOnChange.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_HIDE_ONCHANGE, 0) == 1));
        mPowerWidgetHideScrollBar.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_HIDE_SCROLLBAR, 1) == 1));
        mPowerWidgetIndicatorHide.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_HIDE_INDICATOR, 1) == 1));
        mPowerWidgetHapticFeedback.setValue(Integer.toString(Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_HAPTIC_FEEDBACK, 2)));

        int statusBarBattery = Settings.System.getInt(getContentResolver(),
                Settings.System.BATTERY_STYLE, 0);
        mBatteryStyle.setValue(String.valueOf(statusBarBattery));
        mBatteryStyle.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryStyle) {
            int statusBarBattery = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.BATTERY_STYLE,
                    statusBarBattery);
            return true;
        }
        if (preference == mPowerWidgetHapticFeedback) {
            int intValue = Integer.parseInt((String)newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_HAPTIC_FEEDBACK, intValue);
            return true;
        }
        return false;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */

        if (preference == mHideClock) {
               value = mHideClock.isChecked();
               Settings.System.putInt(getContentResolver(), Settings.System.HIDE_CLOCK,
                    value ? 1 : 0);
        }

        if (preference == mPowerPicker) {
            startActivity(mPowerPicker.getIntent());
        }

        if (preference == mPowerOrder) {
            startActivity(mPowerOrder.getIntent());
        }

        if (preference == mPowerWidget) {
            value = mPowerWidget.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetHideOnChange) {
            value = mPowerWidgetHideOnChange.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_HIDE_ONCHANGE,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetHideScrollBar) {
            value = mPowerWidgetHideScrollBar.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_HIDE_SCROLLBAR,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetIndicatorHide) {
            value = mPowerWidgetIndicatorHide.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_HIDE_INDICATOR,
                    value ? 1 : 0);
        }

        if (preference == mPowerWidgetColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this, mWidgetColorListener,
                    readWidgetColor());
            cp.show();
        }

        return true;
    }

    private int readWidgetColor() {
        try {
            return Settings.System.getInt(getContentResolver(),
                    Settings.System.EXPANDED_VIEW_WIDGET_COLOR);
        } catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mWidgetColorListener = new ColorPickerDialog.OnColorChangedListener() {
        public void colorChanged(int color) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.EXPANDED_VIEW_WIDGET_COLOR, color);
        }

        public void colorUpdate(int color) {
        }
    };

}
