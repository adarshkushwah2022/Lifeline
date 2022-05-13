package com.example.mcproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReaderWriter {

    private final Context context;
    private final String TAG = "FileReadWriter";
    public FileReaderWriter(Context context) {
        this.context = context;
    }

    public boolean isFilePresent(String filename) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + filename;
        File file = new File(path);
        return file.exists();
    }

    public void saveToInternalStorage(Bitmap bitmapImage){
        FileOutputStream fos = null;
        try {
            String filename = "profile.jpg";
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public File loadImageFromStorage()
    {
        String path = context.getFilesDir().getAbsolutePath() + "/" + "profile.jpg";
        File file = new File(path);
        return file;
//        FileInputStream fis = null;
//        Bitmap b = null;
//        String filename = "profile.jpg";
//        try {
//            String path = context.getFilesDir().getAbsolutePath() + "/" + filename;
//            File file = new File(path);
//            b = BitmapFactory.decodeStream(new FileInputStream(file));
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        return b;
    }

    public Bitmap loadPicture(){
        Bitmap b = null;
        String filename = "profile.jpg";
        try {
            String path = context.getFilesDir().getAbsolutePath() + "/" + filename;
            File file = new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

}