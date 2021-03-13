package com.rutershok.daily.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.rutershok.daily.R;
import com.rutershok.daily.model.Quote;
import com.rutershok.daily.model.Social;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Share {

    public static void copyToClipboard(final Activity activity, final String quote) {
        StringBuilder stringBuilder = new StringBuilder(quote);

        if (!Premium.isPremium(activity)) {
            stringBuilder.append(" ").append(activity.getString(R.string.download_this_app));
        }

        ((ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE))
                .setPrimaryClip(ClipData.newPlainText(activity.getString(R.string.quotes), stringBuilder));
        Snackbar.show(activity, R.string.copied_to_clipboard);
    }

    public static void withText(final Context context, Quote quote) {
        if (quote != null) {
            StringBuilder stringBuilder = new StringBuilder(quote.getQuote());
            stringBuilder.append("\n- ").append(quote.getAuthor());
            if (!Premium.isPremium(context)) {
                stringBuilder.append("\n\n").append(context.getString(R.string.download_this_app));
            }
            context.startActivity(Intent.createChooser(new Intent().setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, stringBuilder.toString()).setType("text/plain"), context.getString(R.string.share)));
        }
    }

    public static void withImage(final Activity activity, View view) {
        final Bitmap bitmap = ImageUtil.getScaledBitmapWatermark(activity, view);
        try {
            File cachePath = new File(activity.getCacheDir(), "images");
            if (!cachePath.exists()) {
                cachePath.mkdirs();
            } // don't forget to make the directory
            FileOutputStream outputStream = new FileOutputStream(cachePath + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            File newFile = new File(new File(activity.getCacheDir(), "images"), "image.png");
            Uri uri = FileProvider.getUriForFile(activity, Constant.AUTHORITY, newFile);
            if (uri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        .setDataAndType(uri, activity.getContentResolver().getType(uri))
                        .putExtra(Intent.EXTRA_STREAM, uri);
                activity.startActivity(Intent.createChooser(shareIntent, ""));
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void withSocial(Activity activity, View view, Social social) {
        try {
            File cachePath = new File(activity.getCacheDir(), "images");
            if (!cachePath.exists()) {
                cachePath.mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(cachePath + "/image.png");
            ImageUtil.getScaledBitmapWatermark(activity, view).compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            outputStream.close();

            File newFile = new File(new File(activity.getCacheDir(), "images"), "image.png");
            Uri uri = FileProvider.getUriForFile(activity, Constant.AUTHORITY, newFile);
            if (uri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_STREAM, uri)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        .setType("image/png");
                if (activity.getPackageManager().getLaunchIntentForPackage(social.getPackageName()) != null) {
                    shareIntent.setPackage(social.getPackageName());
                    activity.startActivity(Intent.createChooser(shareIntent, ""));
                } else {
                    Toast.makeText(activity, R.string.app_not_installed, Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}