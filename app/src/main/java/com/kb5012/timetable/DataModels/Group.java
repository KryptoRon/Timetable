package com.kb5012.timetable.DataModels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Ronald on 14-12-2015.
 */
@ParseClassName("Group")
public class Group extends ParseObject {
    private ArrayList<User> members;
    private ArrayList<Task> tasks;

    public Group() { }

    public String getName() {
        return getString("group_name");
    }

    public void setName(String group_name) {
        put("group_name", group_name);
    }

//    TODO: Figure out images on parse
//    public Drawable getImage(Context context) {
//        ParseFile file = getParseFile("group_image");
//        Bitmap bmp = BitmapFactory.decodeByteArray(file, 0, file.);
//        Drawable d = new BitmapDrawable(context.getResources(), bmp);
//
//        return d;
//    }
//
//    public void setImage(String imageName, Drawable image) {
//        Bitmap bitmap = drawableToBitmap(image);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//        byte[] file = stream.toByteArray();
//        ParseFile image_file = new ParseFile(imageName, file);
//        put("group_image", image_file); }

    public ArrayList<User> getGroupUsers() {
        ArrayList<User> users = new DBHelper().findAllUsersByGroup(this.getObjectId());
        return users;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
