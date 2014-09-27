package com.klinker.android.twitter_l.manipulations.profile_popups;

import android.content.Context;
import android.view.View;
import com.klinker.android.twitter_l.R;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucasklinker on 9/20/14.
 */
public class ProfileFavoritesPopup extends ProfileListPopupLayout {

    public ProfileFavoritesPopup(Context context, View main, User user, boolean windowed) {
        super(context, main, user, windowed);
    }

    public ProfileFavoritesPopup(Context context, View main, User user) {
        super(context, main, user);
    }

    public String getTitle() {
        return getResources().getString(R.string.favorites);
    }

    @Override
    public boolean incrementQuery() {
        paging.setPage(paging.getPage() + 1);
        return true;
    }

    @Override
    public List<Status> getData(Twitter twitter) {
        try {
            ArrayList<Status> statuses = new ArrayList<Status>();
            for (Status s : twitter.getFavorites(user.getId(), paging)) {
                statuses.add(s);
            }
            return statuses;
        } catch (Exception e) {
            return null;
        }
    }
}