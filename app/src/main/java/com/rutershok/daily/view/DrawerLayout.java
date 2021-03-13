package com.rutershok.daily.view;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.rutershok.daily.CategoriesActivity;
import com.rutershok.daily.EditorActivity;
import com.rutershok.daily.FavoritesActivity;
import com.rutershok.daily.MainActivity;
import com.rutershok.daily.PremiumActivity;
import com.rutershok.daily.R;
import com.rutershok.daily.RandomActivity;
import com.rutershok.daily.SearchActivity;
import com.rutershok.daily.SettingsActivity;
import com.rutershok.daily.WeekActivity;
import com.rutershok.daily.utils.Dialog;
import com.rutershok.daily.utils.Premium;
import com.rutershok.daily.utils.Setting;

public class DrawerLayout {

    public DrawerLayout(final Activity activity, Toolbar toolbar) {
        new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(new AccountHeaderBuilder()
                        .withActivity(activity)
                        .withHeaderBackground(R.drawable.header)
                        .build())
                .addDrawerItems(
                        getPrimaryDrawerItem(R.string.daily_quotes, R.drawable.ic_quotes),
                        getPrimaryDrawerItem(R.string.categories, R.drawable.ic_categories),
                        getPrimaryDrawerItem(R.string.premium, R.drawable.ic_premium),
                        getPrimaryDrawerItem(R.string.favorites, R.drawable.ic_favorites),
                        getPrimaryDrawerItem(R.string.editor, R.drawable.ic_editor),
                        getPrimaryDrawerItem(R.string.search, R.drawable.ic_search),
                        getPrimaryDrawerItem(R.string.week, R.drawable.ic_last_week),
                        getPrimaryDrawerItem(R.string.random, R.drawable.ic_random),
                        getPrimaryDrawerItem(R.string.settings, R.drawable.ic_settings),
                        new ExpandableDrawerItem().withName(R.string.contacts).withTextColor(ContextCompat.getColor(activity, R.color.colorAccent)).withSelectable(false).withSubItems(
                                getPrimaryDrawerItem(R.string.instagram, R.drawable.ic_instagram),
                                getPrimaryDrawerItem(R.string.facebook, R.drawable.ic_facebook),
                                getPrimaryDrawerItem(R.string.twitter, R.drawable.ic_twitter),
                                getPrimaryDrawerItem(R.string.email, R.drawable.ic_email)))
                .withSelectedItem(-1) //First item is not selected by default
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    switch ((int) drawerItem.getIdentifier()) {
                        case R.string.daily_quotes:
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                            break;
                        case R.string.categories:
                            activity.startActivity(new Intent(activity, CategoriesActivity.class));
                            break;
                        case R.string.premium:
                            activity.startActivity(new Intent(activity, PremiumActivity.class));
                            break;
                        case R.string.favorites:
                            activity.startActivity(new Intent(activity, FavoritesActivity.class));
                            break;
                        case R.string.editor:
                            activity.startActivity(new Intent(activity, EditorActivity.class));
                            break;
                        case R.string.search:
                            if (Premium.isPremium(activity)) {
                                activity.startActivity(new Intent(activity, SearchActivity.class));
                            } else {
                                Dialog.showPremiumIsNeeded(activity);
                            }
                            break;
                        case R.string.random:
                            activity.startActivity(new Intent(activity, RandomActivity.class));
                            break;
                        case R.string.week:
                            activity.startActivity(new Intent(activity, WeekActivity.class));
                            break;
                        case R.string.settings:
                            activity.startActivity(new Intent(activity, SettingsActivity.class));
                            break;
                        case R.string.instagram:
                            Setting.openInstagram(activity);
                            break;
                        case R.string.facebook:
                            Setting.openFacebook(activity);
                            break;
                        case R.string.twitter:
                            Setting.openTwitter(activity);
                            break;
                        case R.string.email:
                            Setting.contactUs(activity);
                            break;
                    }
                    return true;
                })
                .build();
    }

    private PrimaryDrawerItem getPrimaryDrawerItem(int nameRes, int iconRes) {
        return new PrimaryDrawerItem()
                .withIdentifier(nameRes)
                .withName(nameRes)
                .withIcon(iconRes)
                .withSelectable(false);
    }
}
