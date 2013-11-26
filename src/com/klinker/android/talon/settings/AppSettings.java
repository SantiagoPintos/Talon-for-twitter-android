package com.klinker.android.talon.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created with IntelliJ IDEA.
 * User: luke
 * Date: 11/9/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppSettings {

    public SharedPreferences sharedPrefs;

    public static String TWITTER_CONSUMER_KEY = "l1RXEJCfdU7q1CRYkTmeaw";
    public static String TWITTER_CONSUMER_SECRET = "uVsk5H5umoLcYdcVSa6rWFQMN0kFOoTBxAnBid4OAkM";

    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_BLACK = 2;

    public String authenticationToken;
    public String authenticationTokenSecret;
    public String myScreenName;
    public String myName;
    public String myBackgroundUrl;
    public String myProfilePicUrl;

    public boolean isTwitterLoggedIn;
    public boolean reverseClickActions;
    public boolean advanceWindowed;
    public boolean notifications;
    public boolean refreshOnStart;
    public boolean autoTrim;

    public int theme;
    public int textSize;
    public int maxTweetsRefresh;
    public int timelineSize;
    public int mentionsSize;
    public int dmSize;

    public long timelineRefresh;
    public long mentionsRefresh;
    public long dmRefresh;

    public AppSettings(Context context) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Strings
        authenticationToken = sharedPrefs.getString("authentication_token", "none");
        authenticationTokenSecret = sharedPrefs.getString("authentication_token_secret", "none");
        myScreenName = sharedPrefs.getString("twitter_screen_name", "");
        myName = sharedPrefs.getString("twitter_users_name", "");
        myBackgroundUrl = sharedPrefs.getString("twitter_background_url", "");
        myProfilePicUrl = sharedPrefs.getString("profile_pic_url", "");

        // Booleans
        isTwitterLoggedIn = sharedPrefs.getBoolean("is_logged_in", false);
        reverseClickActions = sharedPrefs.getBoolean("reverse_click_option", false);
        advanceWindowed = sharedPrefs.getBoolean("advance_windowed", true);
        notifications = sharedPrefs.getBoolean("notifications", true);
        refreshOnStart = sharedPrefs.getBoolean("refresh_on_start", true);
        autoTrim = sharedPrefs.getBoolean("auto_trim", true);

        // Integers
        theme = Integer.parseInt(sharedPrefs.getString("theme", "1"));
        textSize = Integer.parseInt(sharedPrefs.getString("text_size", "14"));
        maxTweetsRefresh = Integer.parseInt(sharedPrefs.getString("max_tweets", "1"));
        timelineSize = Integer.parseInt(sharedPrefs.getString("timeline_size", "1000"));
        mentionsSize = Integer.parseInt(sharedPrefs.getString("mentions_size", "100"));
        dmSize = Integer.parseInt(sharedPrefs.getString("dm_size", "100"));

        // Longs
        timelineRefresh = Long.parseLong(sharedPrefs.getString("timeline_sync_interval", "1800000"));
        mentionsRefresh = Long.parseLong(sharedPrefs.getString("mentions_sync_interval", "1800000"));
        dmRefresh = Long.parseLong(sharedPrefs.getString("dm_sync_interval", "1800000"));
    }
}
