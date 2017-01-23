package com.qq.googleplay.utils.bitmap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qq.googleplay.common.io.IOUtils;
import com.qq.googleplay.utils.FastBlur;
import com.qq.googleplay.utils.MediaStoreUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class BitmapUtils {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;
    public static final int MAX_HEIGHT = 1600;
    public static final int MAX_WIDTH = 800;

    public static Bitmap clipRoundCornerBitmap(Bitmap bitmap, float radius, int borderColor) {
        if (bitmap == null) {
            return null;
        }
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, w, h);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(borderColor);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static void recycleImageView(ImageView view, Drawable exclude) {
        if (view != null) {
            Bitmap bm;
            if (exclude instanceof BitmapDrawable) {
                bm = ((BitmapDrawable) exclude).getBitmap();
            } else {
                bm = null;
            }
            Drawable d = view.getDrawable();
            view.setImageDrawable(null);
            if (d instanceof BitmapDrawable) {
                Bitmap recycleBitmap = ((BitmapDrawable) d).getBitmap();
                if (recycleBitmap != null && recycleBitmap != bm) {
                    recycleBitmap.recycle();
                }
            }
        }
    }

    public static Bitmap vectorDrawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap transferMode(Bitmap src, Bitmap mask, Xfermode xfermode) {
        int width = mask.getWidth();
        int height = mask.getHeight();
        Bitmap dst = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        canvas.save();
        if (width < srcWidth || height < srcHeight) {
            float radio;
            float radioX = ((float) width) / ((float) srcWidth);
            float radioY = ((float) height) / ((float) srcHeight);
            float dx = 0.0f;
            float dy = 0.0f;
            if (radioX > radioY) {
                radio = radioX;
                dy = ((((float) height) / radioX) - ((float) srcHeight)) / 2.0f;
            } else {
                radio = radioY;
                dx = ((((float) width) / radioX) - ((float) srcWidth)) / 2.0f;
            }
            canvas.scale(radio, radio);
            canvas.translate(dx, dy);
        }
        canvas.drawBitmap(src, 0.0f, 0.0f, paint);
        canvas.restore();
        if (xfermode != null) {
            paint.setXfermode(xfermode);
            canvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        }
        return dst;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static Bitmap decodeResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap getBmpByResId(Context context, int id) {
        return BitmapFactory.decodeStream(context.getResources().openRawResource(id));
    }

    public static byte[] compressBitmap(String path, int maxSize, int rqsW, int rqsH) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            WeakReference<Bitmap> bitmap = cprsBmpBySize(path, rqsW, rqsH);
            WeakReference<Bitmap> rotatedBmp = autoRotateBitmap(path, bitmap);
            if (!(bitmap == null || rotatedBmp == null || bitmap.get() == rotatedBmp.get())) {
                ((Bitmap) bitmap.get()).recycle();
                bitmap.clear();
            }
            return cprsBmpByQuality(rotatedBmp, maxSize);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] cprsBmpByQuality(WeakReference<Bitmap> bmp, int maxSize) {
        if (bmp == null || bmp.get() == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ((Bitmap) bmp.get()).compress(CompressFormat.JPEG, 70, baos);
        ((Bitmap) bmp.get()).recycle();
        return baos.toByteArray();
    }

    private static WeakReference<Bitmap> cprsBmpBySize(String path, int rqsW, int rqsH) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return new WeakReference(BitmapFactory.decodeFile(path, options));
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        String imageType = options.outMimeType;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            /*long totalPixels = width * height / inSampleSize;
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }*/
        }
        return inSampleSize;
    }


    public static int calculateSampleSize(Options options, int rqsW, int rqsH) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) {
            return 1;
        }
        if (height > rqsH || width > rqsW) {
            int heightRatio = Math.round(((float) height) / ((float) rqsH));
            int widthRatio = Math.round(((float) width) / ((float) rqsW));
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
        }
        return inSampleSize;
    }

    public static int MIN_WIDTH = 100;

    /**
     * 计算inSampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (width < MIN_WIDTH) {
            return inSampleSize;
        } else {
            int heightRatio;
            if (width > height && reqWidth < reqHeight || width < height && reqWidth > reqHeight) {
                heightRatio = reqWidth;
                reqWidth = reqHeight;
                reqHeight = heightRatio;
            }

            if (height > reqHeight || width > reqWidth) {
                heightRatio = Math.round((float) height / (float) reqHeight);
                int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
            }

            return inSampleSize;
        }
    }

    public static File cprsBmpToFile(Context context, String path, int maxSize, int rqsW, int rqsH) {
        byte[] bytes = compressBitmap(path, maxSize, rqsW, rqsH);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        File file = new File(getImageCacheDir(context) + new File(path).getName());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    private static String getImageCacheDir(Context context) {
        String dir = context.getCacheDir() + "cache" + File.separator;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public static Options getBitmapOptions(String path) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        return options;
    }

    public static int getDegress(String path) {
        try {
            switch (new ExifInterface(path).getAttributeInt("Orientation", 1)) {
                case 3:
                    return 180;
                case 6:
                    return 90;
                case 8:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static WeakReference<Bitmap> rotateBitmap(WeakReference<Bitmap> bitmap, int degress) {
        if (bitmap == null || bitmap.get() == null) {
            return null;
        }
        Matrix m = new Matrix();
        m.postRotate((float) degress);
        return new WeakReference(Bitmap.createBitmap((Bitmap) bitmap.get(), 0, 0, ((Bitmap) bitmap.get()).getWidth(), ((Bitmap) bitmap.get()).getHeight(), m, true));
    }

    public static WeakReference<Bitmap> autoRotateBitmap(String path, WeakReference<Bitmap> bmp) {
        if (TextUtils.isEmpty(path) || bmp == null || bmp.get() == null) {
            return bmp;
        }
        int degree = getDegress(path);
        if (degree != 0) {
            bmp = rotateBitmap(bmp, degree);
        }
        return bmp;
    }

    public static Bitmap getResizedBitmapWithPath(Context ctx, String filePath) {
        return getResizedBitmapWithPath(ctx, filePath, MAX_WIDTH, MAX_HEIGHT);
    }

    public static Bitmap getResizedBitmapWithPath(Context ctx, String filePath, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getResizedBitmapWithUri(Context ctx, Uri uri) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        MediaStoreUtil.getBitmapFromUri(ctx, uri, options);
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);
        options.inJustDecodeBounds = false;
        return MediaStoreUtil.getBitmapFromUri(ctx, uri, options);
    }

    public static void saveBitmap(String fileName, Bitmap bmp) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            bmp.compress(CompressFormat.PNG,70,out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static Bitmap getBitmapWithUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        if (!(uri == null || context == null)) {
            bitmap = null;
            new Options().inJustDecodeBounds = true;
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream
                        (uri), null, getBitmapOptions(context, uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Options getBitmapOptions(Context context, Uri uri) {
        ContentResolver resolver = context.getContentResolver();
        Options opt = new Options();
        opt.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(resolver.openInputStream(uri), null, opt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        opt.inSampleSize = calculateInSampleSize(opt, MAX_WIDTH, MAX_HEIGHT);
        opt.inJustDecodeBounds = false;
        return opt;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, 70, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null));
    }

    public static Uri newImageUri(Context context, Uri uri) {
        if (context == null || uri == null) {
            return null;
        }
        Bitmap bmp = getBitmapWithUri(context, uri);
        try {
            String fileName = context.getExternalCacheDir().toString() + File.separatorChar +
                    System.currentTimeMillis() + ".png";
            saveBitmap(fileName, bmp);
            return Uri.fromFile(new File(fileName));
        } catch (Exception e) {
            return null;
        }
    }

    private static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) w) / ((float) width), ((float) h) / ((float) height));
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    @SuppressLint({"NewApi"})
    public static Bitmap takeScreenShot(Activity pActivity) {
        View view = pActivity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int stautsHeight = frame.top;
        Point size = new Point();
        Display display = pActivity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT < 13) {
            size.set(display.getWidth(), display.getHeight());
        } else {
            pActivity.getWindowManager().getDefaultDisplay().getSize(size);
        }
        return Bitmap.createBitmap(bitmap, 0, stautsHeight, size.x, size.y - stautsHeight);
    }

    public static Bitmap viewToBitmap(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate((float) (-v.getScrollX()), (float) (-v.getScrollY()));
        v.draw(c);
        return screenshot;
    }

    public static Bitmap convertViewToBitmap(View view) {
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Bitmap loadBitmapFromView(View view) {
        if (view.getMeasuredHeight() > 0) {
            return null;
        }
        view.measure(-2, -2);
        Bitmap bmp = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bmp;
    }

    public static Bitmap getBitmapByView(LinearLayout linearLayout) {
        int h = 0;
        int i = 0;
        while (i < linearLayout.getChildCount()) {
            try {
                if (linearLayout.getChildAt(i).getVisibility() == View.VISIBLE) {
                    h += linearLayout.getChildAt(i).getHeight();
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getWidth(), h, Bitmap.Config.RGB_565);
        linearLayout.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static final float BACKBTN_LEFT_MARGIN = 20.0f;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void blur(Context context, Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate((float) (-view.getLeft()), (float) (-view.getTop()));
        canvas.drawBitmap(bkg, 0.0f, 0.0f, null);
        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(BACKBTN_LEFT_MARGIN);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(new BitmapDrawable(context.getResources(), overlay));
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), overlay));
        }
        rs.destroy();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void blurAdvanced(Context context, Bitmap bkg, View view) {
        if (bkg != null) {
            Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0.0f, 0.0f, paint);
            overlay = FastBlur.doBlur(overlay, (int) 12.0f, true);
            if (Build.VERSION.SDK_INT >= 16) {
                view.setBackground(new BitmapDrawable(context.getResources(), overlay));
            } else {
                view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), overlay));
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static void blur_2(Context context, Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
        Bitmap overlay = Bitmap.createBitmap(d.getWidth(), d.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate((float) (-view.getLeft()), (float) (-view.getTop()));
        canvas.scale(1.0f, 1.0f);
        new Paint().setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0.0f, 0.0f, null);
        overlay = FastBlur.doBlur(overlay, (int) 12.0f, true);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(new BitmapDrawable(context.getResources(), overlay));
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), overlay));
        }
    }

    @SuppressLint({"NewApi"})
    public static BitmapDrawable getBlur_2(Context context, Bitmap bkg) {
        Bitmap bitmap = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0.0f, 0.0f, paint);
        return new BitmapDrawable(context.getResources(), FastBlur.doBlur(bitmap, (int) 1.0f, true));
    }

    @SuppressLint({"NewApi"})
    public static void blur_3(Context context, Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
        Bitmap overlay = Bitmap.createBitmap((int) (((float) d.getWidth()) / 1.0f),
                (int) (((float) d.getHeight()) / 1.0f), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1.0f / 1.0f, 1.0f / 1.0f);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0.0f, 0.0f, paint);
        overlay = FastBlur.doBlur(overlay, (int) 100.0f, true);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(new BitmapDrawable(context.getResources(), overlay));
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), overlay));
        }
    }

    public static void star_blur(Context context, Bitmap bm, ImageView iv) {
        Bitmap overlay = Bitmap.createBitmap((int) (((float) iv.getMeasuredWidth()) / 6.0f),
                (int) (((float) iv.getMeasuredHeight()) / 6.0f), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(((float) (-iv.getLeft())) / 6.0f, ((float) (-iv.getTop())) / 6.0f);
        canvas.scale(1.0f / 6.0f, 1.0f / 6.0f);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bm, 0.0f, 0.0f, paint);
        ImageView imageView = iv;
        imageView.setImageDrawable(new BitmapDrawable(context.getResources(), FastBlur.doBlur(overlay,
                (int) 5.0f, true)));
    }

    /**
     * 按最大边按一定大小缩放图片
     *
     * @param resources
     * @param resId
     * @param maxSize 压缩后最大长度
     * @return
     */
    public static Bitmap scaleImage(Resources resources, int resId, int maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, resId, options);
    }

}
