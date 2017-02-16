package com.qq.googleplay.common.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：GooglePlay
 * Package_Name：com.qq.googleplay
 * Version：1.0
 * time：2016/2/16 13:33
 * des ：${TODO}
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
/**
 * Created by Sanjay on 2/2/2015.
 */
public class DownloadFile extends AsyncTask<String, String, String> {


    private final String strurl;
    private final Context mContext;
    private final String filename;
    private final boolean download;
    float filesize;
    private ProgressDialog pDialog;

    DownloadFile(Context context, String url, String filename, boolean download) {
        strurl = url;
        mContext = context;
        this.filename = filename;
        this.download = download;
    }

    private static File getOutputMediaFile(String filename) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Common.IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Common.IMAGE_DIRECTORY_NAME, "Oops! Failed create " + Common.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + filename);

        return mediaFile;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait..");
        pDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(strurl);
            System.out.println("5555 get size for -" + url);

            URLConnection urlConnection;
            urlConnection = url.openConnection();
            urlConnection.connect();
            filesize = urlConnection.getContentLength() / 1024f;
            filesize = filesize / 1024f;
            System.out.println("size of file:- " + filesize);


            if (download) {
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(getOutputMediaFile(filename));
                byte data[] = new byte[1024];
                long total = 0;
                int count = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (!download)
            Common.showAlertDialog(mContext, "File Size", "Size of file at " + strurl + "  is " + filesize + " MB", false);
        else
            Common.showAlertDialog(mContext, "File Path", "File stored at " + getOutputMediaFile(filename).getPath().toString(), false);
    }

}
