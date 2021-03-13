package com.rutershok.daily.utils;

import static android.graphics.Color.WHITE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.kobakei.ratethisapp.RateThisApp;
import com.rutershok.daily.PremiumActivity;
import com.rutershok.daily.R;
import com.rutershok.daily.adapters.SocialsAdapter;
import com.rutershok.daily.database.Storage;
import com.rutershok.daily.model.Interval;
import com.rutershok.daily.model.Quote;
import com.xw.repo.BubbleSeekBar;

public class Dialog {

    public static void showRateThisApp(Context context) {
        RateThisApp.onCreate(context);
        RateThisApp.showRateDialogIfNeeded(context);
    }

    private static int currentIntervalId;

    public static void showSelectInterval(Context context) {
        currentIntervalId = Storage.getIntervalId(context);

        new AlertDialog.Builder(context).setTitle(context.getString(R.string.refresh_quotes_every))
                .setSingleChoiceItems(Interval.getNames(context), currentIntervalId, (dialog, which) -> currentIntervalId = which)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Storage.setIntervalId(context, currentIntervalId);
                    Storage.setIntervalName(context, context.getString(Interval.getNamesRes()[currentIntervalId]));
                })
                .setNegativeButton(android.R.string.cancel, null) //Just dismiss the dialog
                .setNeutralButton(R.string._default, (dialog, which) -> Storage.setIntervalId(context, Interval.DAY.ordinal()))
                .show();
    }

    public static void showEditQuote(Context context, final TextView textQuote) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_quote, null);
        final TextInputEditText editTextQuote = view.findViewById(R.id.edit_quote);
        editTextQuote.setText(textQuote.getText());
        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if (editTextQuote.getText() != null) {
                        textQuote.setText(editTextQuote.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    public static void showNotificationTimePicker(final Context context) {
        new TimePickerDialog(context, (view, hourOfDay, minute) -> {
            Storage.setNotificationHour(context, hourOfDay);
            Storage.setNotificationMinute(context, minute);
            Storage.setIsAlarmSetted(context, false);
            DailyNotification.initialize(context);
        }, Storage.getNotificationHour(context), Storage.getNotificationMinute(context), true).show();
    }

    public static void showExit(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.exit)
                .setMessage(R.string.do_you_really_want_to_exit)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> activity.finish())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NEW >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private static GradientDrawable gradientDrawable;

    private static int gradientColorTop = WHITE;
    private static int gradientColorBottom = WHITE;

    public static void showChangeTextColor(final Context context, final TextView textView) {
        int currentTextColor = textView.getCurrentTextColor();
        ColorPickerDialogBuilder
                .with(context)
                .setTitle(context.getString(R.string.choose_color))
                .setOnColorChangedListener(textView::setTextColor)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i, integers) -> {/*Don't remove this*/})
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> textView.setTextColor(currentTextColor))
                .build()
                .show();
    }

    public static void showChangeTextOpacity(Context context, final TextView textView) {
        final float opacity = textView.getAlpha();
        new SeekBarBottomSheet(context, (int) (opacity * 100), new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                textView.setAlpha(progress * 0.01f);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
            }
        });
    }

    public static void showChangeTextSpacing(Context context, final TextView textView) {
        new SeekBarBottomSheet(context, (int) (textView.getLineSpacingMultiplier() / 0.02), new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                textView.setLineSpacing(0, progress * 0.02f);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
            }
        });
    }

    public static void showChangeTextHighlight(final Context context, final TextView textView) {
        final Drawable background = textView.getBackground();
        ColorPickerDialogBuilder.with(context)
                .setTitle(context.getString(R.string.choose_color))
                .setOnColorChangedListener(selectedColor -> textView.setBackground(new ColorDrawable(selectedColor)))
                .setPositiveButton(android.R.string.yes, (dialogInterface, i, integers) -> {/*Don't remove this*/})
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> textView.setBackground(background))
                .build()
                .show();
    }

    public static void showChangeTextBorder(Context context, final TextView textView) {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        final int currentMargin = layoutParams.leftMargin;

        new SeekBarBottomSheet(context, currentMargin / 4, new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(textView.getLayoutParams());
                int margin = progress * 4;
                marginParams.setMargins(margin, 0, margin, 0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                textView.setLayoutParams(layoutParams);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
    }

    public static void showChangeBackgroundColor(final Context context, final ImageView imageView) {
        Drawable currentBackgroundDrawable = imageView.getDrawable();
        ColorPickerDialogBuilder
                .with(context)
                .setTitle(context.getString(R.string.choose_color))
                .setOnColorChangedListener(selectedColor -> Glide.with(context).load(new ColorDrawable(selectedColor)).into(imageView))
                .setPositiveButton(android.R.string.yes, (dialogInterface, i, integers) -> {/*Don't remove this*/})
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> Glide.with(context).load(currentBackgroundDrawable).into(imageView))
                .build()
                .show();
    }

    public static void showChangeBackgroundGradient(Context context, ImageView imageView) {
        final Drawable currentBackgroundDrawable = imageView.getDrawable();
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(0f);
        gradientDrawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
        try {
            //Bottom color
            ColorPickerDialogBuilder
                    .with(context)
                    .setTitle(context.getString(R.string.choose_color))
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(8)
                    .setOnColorChangedListener(selectedColor -> {
                        gradientColorBottom = selectedColor;
                        gradientDrawable.setColors(new int[]{gradientColorBottom, gradientColorTop});
                        Glide.with(context).load(gradientDrawable).into(imageView);
                    })
                    .setPositiveButton(android.R.string.yes, (dialog, selectedColor, allColors) -> {
                        //Top color
                        ColorPickerDialogBuilder
                                .with(context)
                                .setTitle(context.getString(R.string.choose_color))
                                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                                .density(8)
                                .setOnColorChangedListener(selectedColor1 -> {
                                    gradientColorTop = selectedColor1;
                                    gradientDrawable.setColors(new int[]{gradientColorBottom, gradientColorTop});
                                    Glide.with(context).load(gradientDrawable).into(imageView);
                                })
                                .setPositiveButton(android.R.string.yes, (dialogInterface, i, integers) -> {
                                })
                                .setNegativeButton(android.R.string.cancel, (dialog1, which) -> Glide.with(context).load(currentBackgroundDrawable).into(imageView))
                                .build()
                                .show();
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> Glide.with(context).load(currentBackgroundDrawable).into(imageView))
                    .build()
                    .show();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void showChangeBackgroundOpacity(Context context, View view) {
        float opacity = view.getAlpha();
        new SeekBarBottomSheet(context, (int) (opacity * 100), new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                view.setAlpha(progress * 0.01f);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        }).show();
    }

    public static void showChangeTextSize(Context context, TextView textView) {
        int currentSize = (int) (textView.getTextSize() / context.getResources().getDisplayMetrics().scaledDensity);
        new SeekBarBottomSheet(context, currentSize, new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                textView.setTextSize(progress);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
    }

    private static class SeekBarBottomSheet extends BottomSheetDialog {
        SeekBarBottomSheet(Context context, int progress, BubbleSeekBar.OnProgressChangedListener listener) {
            super(context);

            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_seek_bar, null);
            setContentView(view);

            BubbleSeekBar seekBar = view.findViewById(R.id.bubble_seek_bar);
            seekBar.setProgress(progress);
            seekBar.setOnProgressChangedListener(listener);

            show();
        }

    }

    public static class ShareBottomSheet extends BottomSheetDialog {

        final Activity mActivity;

        public ShareBottomSheet(@NonNull Activity activity, Quote quote, View viewQuote) {
            super(activity);
            this.mActivity = activity;
            initialize(quote, viewQuote);
        }

        private void initialize(Quote quote, View viewQuote) {
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_share, null);
            setContentView(view);
            setShareWithSocial(view, viewQuote);
            TabLayout tabLayout = view.findViewById(R.id.tab_layout_share);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            Share.withImage(mActivity, viewQuote);
                            break;
                        case 1:
                            Share.withText(mActivity, quote);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            Share.withImage(mActivity, viewQuote);
                            break;
                        case 1:
                            Share.withText(mActivity, quote);
                            break;
                    }
                }
            });
        }

        private void setShareWithSocial(View view, View viewQuote) {
            RecyclerView recyclerView = view.findViewById(R.id.recycler_socials);
            recyclerView.setAdapter(new SocialsAdapter(mActivity, viewQuote));
            recyclerView.setHasFixedSize(true);
        }
    }

    public static void showGdprConsent(final Context context) {
        try {
            new AlertDialog.Builder(context)
                    .setView(LayoutInflater.from(context).inflate(R.layout.dialog_gdpr_consent, null, false))
                    .setTitle(R.string.data_protection_consent)
                    .setIcon(R.drawable.ic_gdpr)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> ConsentInformation.getInstance(context).setConsentStatus(ConsentStatus.PERSONALIZED))
                    .setNegativeButton(android.R.string.no, (dialog, which) -> ConsentInformation.getInstance(context).setConsentStatus(ConsentStatus.NON_PERSONALIZED))
                    .show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    public static void showPremiumIsNeeded(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.premium_version_required))
                .setMessage(context.getString(R.string.support_our_work_by_purchasing))
                .setPositiveButton(R.string.premium, (dialog, which) -> context.startActivity(new Intent(context, PremiumActivity.class)))
                .setNeutralButton(android.R.string.cancel, null)
                .show();
    }
}