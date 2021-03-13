package com.rutershok.daily.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.rutershok.daily.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class ImageUtil {

    public static Bitmap getScaledBitmapWatermark(Activity activity, View view) {
        ImageView imageLogo = view.findViewById(R.id.image_logo);

        //Add logo if the user isn't premium
        if (!Premium.isPremium(activity) && imageLogo!=null) {
            imageLogo.setVisibility(View.VISIBLE);
        }

        Bitmap bitmap = Bitmap.createBitmap(view.getHeight(), view.getHeight(), Bitmap.Config.ARGB_8888);

        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(new Canvas(bitmap));

        if (imageLogo!=null) {
            imageLogo.setVisibility(View.INVISIBLE);
        }
        return Bitmap.createScaledBitmap(bitmap, view.getWidth(),view.getHeight(), false);
    }

    public static void saveImage(Activity activity, Bitmap bitmap) {
        if (EasyPermissions.hasPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File dir = new File(Constant.PARENT_PATH, activity.getString(R.string.app_name));
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Snackbar.showError(activity, R.string.failed_to_create_folder);
                    return;
                }
            }

            String fileName = "quote_" + System.currentTimeMillis() + Constant.PNG;

            try {
                FileOutputStream outputStream = new FileOutputStream(dir.getPath() + File.separator + fileName);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                outputStream.flush();
                outputStream.close();

                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }

                Snackbar.show(activity, R.string.image_saved);

                //Refresh gallery
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(dir.getAbsolutePath()))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Snackbar.showError(activity, R.string.file_not_found);
            } catch (IOException e) {
                e.printStackTrace();
                Snackbar.showError(activity, R.string.io_exception);
            }
        } else {
            EasyPermissions.requestPermissions(activity, activity.getString(R.string.rationale_ask), Constant.RC_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    public static Bitmap getScaledBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getHeight(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(new Canvas(bitmap));
        return Bitmap.createScaledBitmap(bitmap, 1080,1080, true);
    }

    private static final List<Drawable> imagesList = new ArrayList<>();

    public static List<Drawable> getImages(Context context) {
        AssetManager assetManager = context.getAssets();
        if (imagesList.isEmpty()) {
            try {
                String[] namesList = assetManager.list("images");
                if (namesList != null) {
                    for (String name : namesList) {
                        if (name.contains("img")) {
                            try {
                                InputStream inputStream = assetManager.open("images/" + name);
                                imagesList.add(Drawable.createFromStream(inputStream, null));
                                inputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imagesList;
    }
}