package com.klinker.android.talon.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.klinker.android.talon.R;
import com.klinker.android.talon.settings.SettingsPagerActivity;
import com.klinker.android.talon.sq_lite.HomeDataSource;
import com.klinker.android.talon.ui.MainActivity;
import com.klinker.android.talon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TimelineRefreshService extends IntentService {

    SharedPreferences sharedPrefs;

    public TimelineRefreshService() {
        super("TimelineRefreshService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Context context = getApplicationContext();
        boolean update = false;
        int numberNew = 0;

        try {
            Log.v("background_refresh", "In try block");
            Twitter twitter = Utils.getTwitter(context);

            User user = twitter.verifyCredentials();
            long lastId = sharedPrefs.getLong("last_tweet_id", 0);
            Paging paging = new Paging(1, 50);
            List<Status> statuses = twitter.getHomeTimeline(paging);

            boolean broken = false;

            // first try to get the top 50 tweets
            for (int i = 0; i < statuses.size(); i++) {
                if (statuses.get(i).getId() == lastId) {
                    statuses = statuses.subList(0, i);
                    broken = true;
                    break;
                }
            }

            Log.v("background_refresh", "First check done");

            // if that doesn't work, then go for the top 150
            if (!broken) {
                Paging paging2 = new Paging(1, 150);
                List<twitter4j.Status> statuses2 = twitter.getHomeTimeline(paging2);

                for (int i = 0; i < statuses2.size(); i++) {
                    if (statuses2.get(i).getId() == lastId) {
                        statuses2 = statuses2.subList(0, i);
                        break;
                    }
                }

                statuses = statuses2;
            }

            Log.v("background_refresh", "2nd check done");

            if (statuses.size() != 0) {
                sharedPrefs.edit().putLong("last_tweet_id", statuses.get(0).getId()).commit();
                update = true;
                numberNew = statuses.size();
            } else {
                update = false;
                numberNew = 0;
            }

            Log.v("background_refresh", "writing to datasource");

            HomeDataSource dataSource = new HomeDataSource(context);
            dataSource.open();

            for (twitter4j.Status status : statuses) {
                try {
                    dataSource.createTweet(status);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

            dataSource.close();

            Log.v("background_refresh", "done writing");

            int mId = 1;

            RemoteViews remoteView = new RemoteViews("com.klinker.android.talon", R.layout.custom_notification);
            Intent popup = new Intent(context, SettingsPagerActivity.class);
            PendingIntent popupPending =
                    PendingIntent.getActivity(
                            this,
                            0,
                            popup,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            remoteView.setOnClickPendingIntent(R.id.popup_button, popupPending);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_action_accept_dark)
                            .setContent(remoteView);

            Intent resultIntent = new Intent(this, MainActivity.class);
            //resultIntent.putExtra("fromNotification", true);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(mId, mBuilder.build());

            Log.v("background_refresh", "made notification");

        } catch (TwitterException e) {
            // Error in updating status
            Log.d("Twitter Update Error", e.getMessage());
        }
    }
}