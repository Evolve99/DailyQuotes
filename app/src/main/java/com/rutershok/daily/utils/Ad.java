package com.rutershok.daily.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.facebook.ads.AudienceNetworkAds;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.pollfish.main.PollFish;
import com.rutershok.daily.R;
import com.rutershok.daily.database.Storage;

public class Ad {
    
    public static void initialize(final Context context) {
        if (!Premium.isPremium(context) && !Premium.isPurchased() && Premium.isBillingInitialized()) {
            if (ConsentInformation.getInstance(context).getConsentStatus().equals(ConsentStatus.UNKNOWN)) {
                Dialog.showGdprConsent(context);
            }
            MobileAds.initialize(context);
            AudienceNetworkAds.initialize(context);
        }
    }

    private static void showInterstitial(final Activity activity) {
        if (!Premium.isPremium(activity) && !Premium.isPurchased()) {
            InterstitialAd interstitialAd = new InterstitialAd(activity);
            interstitialAd.setAdUnitId(Constant.ADMOB_INTERSTITIAL_ID);
            interstitialAd.loadAd(new AdRequest.Builder().build());
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    interstitialAd.show();
                }
            });
        }
    }

    public static void showBanner(Activity activity) {
        AdView adView = activity.findViewById(R.id.banner);
        if (!Premium.isPremium(activity) && !Premium.isPurchased() && Premium.isBillingInitialized()) {
            adView.loadAd(new AdRequest.Builder().build());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    adView.setVisibility(View.GONE);
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    adView.setVisibility(View.GONE);
                    Log.e("Ads", "Close");
                }
            });
        } else {
            adView.setVisibility(View.GONE);
        }
    }

    //To unlock premium version
    public static void showSurvey(Activity activity) {
        if (Premium.isPurchased()) {
            Snackbar.show(activity, R.string.you_already_have_premium_version);
            return;
        }
        if (Network.isConnected(activity)) {
            Snackbar.show(activity, R.string.loading);
            PollFish.initWith(activity, new PollFish.ParamsBuilder(Constant.POLL_FISH_API_KEY)
                    .releaseMode(true)
                    .customMode(true)
                    .pollfishSurveyNotAvailableListener(() -> Snackbar.show(activity, R.string.survey_not_available))
                    .pollfishCompletedSurveyListener(surveyInfo -> Storage.updatePremiumExpirationMillis(activity, Constant.DAY_MILLIS))
                    .build());
        } else {
            Snackbar.showError(activity, R.string.no_internet_connection);
        }
    }

    public static void showRewardedVideo(Activity activity) {
        if (Premium.isPurchased()) {
            Snackbar.show(activity, R.string.you_already_have_premium_version);
            return;
        }
        if (Network.isConnected(activity)) {
            Snackbar.show(activity, R.string.loading);
            RewardedVideoAd rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
            rewardedVideoAd.loadAd(Constant.ADMOB_REWARDED_VIDEO_ID, new AdRequest.Builder().build());
            rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    rewardedVideoAd.show();
                }

                @Override
                public void onRewardedVideoAdOpened() {

                }

                @Override
                public void onRewardedVideoStarted() {

                }

                @Override
                public void onRewardedVideoAdClosed() {

                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    Storage.updatePremiumExpirationMillis(activity, Constant.HOUR_MILLIS);
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    Snackbar.show(activity, R.string.failed_to_load_video);
                }

                @Override
                public void onRewardedVideoCompleted() {

                }
            });
        } else {
            Snackbar.showError(activity, R.string.no_internet_connection);
        }
    }

    public static void showInterstitialFrequency(final Activity activity) {
        if (!Premium.isPremium(activity) && !Premium.isPurchased()) {
            if (Storage.getInterstitialCount(activity) % Constant.INTERSTITIAL_FREQUENCY == 0 &&
                    !activity.isDestroyed()) {
                showInterstitial(activity);
            } else {
                Storage.updateInterstitialCount(activity);
            }
        }
    }
}