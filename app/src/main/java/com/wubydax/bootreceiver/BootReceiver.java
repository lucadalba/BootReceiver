package com.wubydax.bootreceiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Wuby and Dax on 15/09/2015.
 * Updated for MM support
 * To be installed in /system/priv-app only!!!
 */
public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ContentResolver cr = context.getContentResolver();

        // *** READ STRINGS.XML FOR MORE EXPLINATIONS ***

        //All this commands will be executed on boot

        //This takes the Settings DB value of notification_panel_default_active_app_list
        String toggles2 = Settings.System.getString(cr, context.getString(R.string.tiles_default_key));
        final String toggles = String.valueOf(toggles2);
        //And put it into notification_panel_active_app_list
        Settings.System.putString(cr, context.getString(R.string.tiles_key), toggles);

        //This takes the value of all toggles we want in notificaion panel
        //And put it into notification_panel_active_app_list_for_reset
        Settings.System.putString(cr, context.getString(R.string.tiles_reset_key), context.getString(R.string.tiles_reset_value));

        //This verifies if the boot is the first rom boot
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sp.edit();
        boolean isFirstBoot = sp.getBoolean("isFirst", true);
        if (isFirstBoot) {
            //THIS HAPPENS AT FIRST ROM BOOT

            //This takes the value of all toggles we want in notificaion panel
            //And put it into notification_panel_active_app_list ONLY AT FIRST ROM BOOT
            Settings.System.putString(cr, context.getString(R.string.tiles_key), context.getString(R.string.tiles_reset_value));

            //This takes the value of all toggles we want in notificaion panel
            //And put it into notification_panel_active_app_list ONLY AT FIRST ROM BOOT
            Settings.System.putString(cr, context.getString(R.string.tiles_default_key), context.getString(R.string.tiles_reset_value));

            //Do NOT change this:
            ed.putBoolean("isFirst", false).apply();
        }

        //This shows a toast that tells the boot is completed and the toggles are set
        Toast.makeText(context, R.string.boot_complete_toast, Toast.LENGTH_SHORT).show();
    }
}