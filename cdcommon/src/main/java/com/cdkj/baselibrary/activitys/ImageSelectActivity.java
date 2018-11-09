package com.cdkj.baselibrary.activitys;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.CapturePhotoHelper;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ImageSelectActivity extends Activity implements View.OnClickListener {

    private TextView tv_take_capture;// 拍照
    private TextView tv_alumb;// 相册选取
    private TextView tv_cancle;// 取消
    private View empty_view;// 取消

    public  final static String staticPath="ylq_pic";
    private boolean isSplit = false;//是否裁剪

    private static final String CACHDIR = "ylqpicimgcach";
    public static final String IMAGE_URL = "/bky/bcoin/";
    //private final static int RUNTIME_PERMISSION_REQUEST_CODE = 0x1;

    private CapturePhotoHelper mCapturePhotoHelper;

    public static void launch(Activity activity, int photoid) {
        if (activity == null) {
            return;
        }
        activity.startActivityForResult(new Intent(activity, ImageSelectActivity.class),photoid);
    }

    public static void launch(Activity activity, boolean isSplit, int photoid) {
        if (activity == null) {
            return;
        }
        activity.startActivityForResult(new Intent(activity, ImageSelectActivity.class)
                        .putExtra("isSplit", isSplit)
                , photoid);
    }

    public static void launch(Fragment fragment, boolean isSplit, int photoid) {
        if (fragment == null) {
            return;
        }
        fragment.startActivityForResult(new Intent(fragment.getActivity(), ImageSelectActivity.class)
                        .putExtra("isSplit", isSplit)
                , photoid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void init() {
        isSplit = getIntent().getBooleanExtra("isSplit", isSplit);

        tv_take_capture = (TextView) findViewById(R.id.tv_take_capture);
        tv_alumb = (TextView) findViewById(R.id.tv_alumb);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        empty_view = findViewById(R.id.empty_view);


        tv_take_capture.setOnClickListener(this);
        tv_alumb.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        empty_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        try {
            int i = v.getId();
            if (i == R.id.tv_take_capture) {
                PermissionCheck(0); //6.0系统申请相机权限

            } else if (i == R.id.tv_alumb) {
                PermissionCheck(1);

            } else if (i == R.id.empty_view || i == R.id.tv_cancle) {
                finish();

            } else {
            }
        } catch (Exception e) {
            Toast.makeText(ImageSelectActivity.this, getString(R.string.activity_image_unknow_error), Toast.LENGTH_SHORT);
            finish();
        }
    }

    /**
     * 启动相机
     *
     * @return
     */
    private boolean startCamera() {
        if (hasCamera())  //判读有没有可用相机
        {
            getImageFromCamera();
        } else {
            if (isFinishing()) {
                return true;
            }
            new CommonDialog(this).builder()
                    .setTitle(getString(R.string.activity_image_tip)).setContentMsg(getString(R.string.activity_image_no_camera))
                    .setNegativeBtn(getString(R.string.activity_image_confirm), new CommonDialog.OnNegativeListener() {
                        @Override
                        public void onNegative(View view) {
                            ImageSelectActivity.this.finish();
                        }
                    }, false).show();
        }
        return false;
    }

    /**
     * 开启相机
     */
    private void turnOnCamera() {
        if (mCapturePhotoHelper == null) {
            mCapturePhotoHelper = new CapturePhotoHelper(this, getCacheDir());
        }
        mCapturePhotoHelper.capture();
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, 3);
    }

    // 调相册图片
    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
         */
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/jpeg");

        startActivityForResult(intent, 1);
    }

    private Uri imageUrl;

    // 调相机拍照
    private void getImageFromCamera() {
        String SDState = Environment.getExternalStorageState();
//        isSplit = true;
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

            imageUrl = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "camera.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intent, CapturePhotoHelper.CAPTURE_PHOTO_REQUEST_CODE);
        } else {
            ToastUtil.show(this, getString(R.string.activity_image_no_cdcard));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            /**
             * 1.选择图片 2.拍照 3.选择完前两项后进行剪切
             */
            if (resultCode != Activity.RESULT_OK) {
                return;
            } else {
                switch (requestCode) {
                    case 1:// 相册
                        Uri imageUri = data.getData();

                        if ("Xiaomi".equals(Build.MANUFACTURER) || SystemUtils.isMIUI())   //小米相册兼容代码
                        {
                            String imgP = setPhotoForMiuiSystem(data);

                            if (!TextUtils.isEmpty(imgP)) {
                                setResult(Activity.RESULT_OK, new Intent().putExtra(staticPath, imgP));
                                finish();
                                return;
                            }

                            if (imageUri == null) {
                                Toast.makeText(ImageSelectActivity.this, getString(R.string.activity_image_getimg_failure), Toast.LENGTH_SHORT);
                                finish();
                                return;
                            }
                            startPhotoZoom(imageUri);
                            return;
                        }
                        if (imageUri == null) {
                            Toast.makeText(ImageSelectActivity.this, getString(R.string.activity_image_getimg_failure), Toast.LENGTH_SHORT);
                            finish();
                            return;
                        }

                        if (isSplit) {
                            startPhotoZoom(imageUri);
                        } else {
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            cursor.close();
                            setResult(Activity.RESULT_OK, new Intent().putExtra(staticPath, picturePath));
                            finish();
                        }
                        break;
                    case CapturePhotoHelper.CAPTURE_PHOTO_REQUEST_CODE:// 拍照

                        if (isSplit) {
                            startPhotoZoom(imageUrl);
                        } else {


                            Bitmap bitmap = decodeBitmapFromFile(imageUrl.getPath(), 150, 150);
                            String path = amendRotatePhoto(imageUrl.getPath(), bitmap, this);
//                            String path = saveFile(bitmap, staticPath);
                            setResult(Activity.RESULT_OK, new Intent().putExtra(staticPath, path));
                            finish();
                        }
                        break;
                    case 3:  //图片裁剪
                        Bundle extras = data.getExtras();
                        Bitmap photo = extras.getParcelable("data");
                        String path = saveFile(photo, "verfy_head" + System.currentTimeMillis());  //图片名称

                        setResult(Activity.RESULT_OK, new Intent().putExtra(staticPath, path));
                        finish();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(ImageSelectActivity.this, getString(R.string.activity_image_getimg_failure), Toast.LENGTH_SHORT);
            finish();
        }
    }

    public void saveMyBitmap(Bitmap mBitmap, String bitName) {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return;
        }
        File f = new File(Environment.getExternalStorageDirectory() + "/"
                + bitName + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
    }

    /**
     * 判断是否存在可用相机
     *
     * @return
     */
    public boolean hasCamera() {
        PackageManager packageManager = ImageSelectActivity.this.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 压缩Bitmap的大小
     *
     * @param imagePath     图片文件路径
     * @param requestWidth  压缩到想要的宽度
     * @param requestHeight 压缩到想要的高度
     * @return Bitmap
     */
    public static Bitmap decodeBitmapFromFile(String imagePath, int requestWidth, int requestHeight) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (requestWidth <= 0 || requestHeight <= 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                return bitmap;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
            BitmapFactory.decodeFile(imagePath, options);
            if (options.outHeight == -1 || options.outWidth == -1) {
                try {
                    ExifInterface exifInterface = new ExifInterface(imagePath);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                    options.outWidth = width;
                    options.outHeight = height;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); //计算获取新的采样率
            options.inSampleSize = Math.min(options.outWidth / requestWidth, options.outHeight / requestHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(imagePath, options);
        } else {
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;

            for (int halfWidth = width / 2; halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth; inSampleSize *= 2) {
            }

            long totalPixels = (long) (width * height / inSampleSize);

            for (long totalReqPixelsCap = (long) (reqWidth * reqHeight * 2); totalPixels > totalReqPixelsCap; totalPixels /= 2L) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



	/*
*
     * 相机权限申请
     * @param poto
*/

    @TargetApi(Build.VERSION_CODES.M)
    private void PermissionCheck(int requestcode) {
        if (AppUtils.getAndroidVersion(Build.VERSION_CODES.M))  //如果运行环境是6.0
        {
            //判断是否有相机权限
            if (ContextCompat.checkSelfPermission(ImageSelectActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(ImageSelectActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(ImageSelectActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //没有权限
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);  //申请相机权限

                return;
            }
        }
        if (requestcode == 0) {
            //判断是否有可用相机
            startCamera();
        } else {
            getImageFromAlbum();
        }

    }

    //权限申请回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean isgetPermissions = true;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isgetPermissions = false;
                break;
            }
        }


        if (!isgetPermissions) {

            if (isFinishing()) {
                return;
            }
//            new CommonDialog(this).builder()
//                    .setTitle("提示").setContentMsg("权限获取失败,无法进行下一步操作.请重新获取权限或打开设置界面授予应用相机权限.")
//                    .setMiddleBtn("设置", new CommonDialog.OnMiddleListener() {
//                        @Override
//                        public void onMiddle(View view) {
//                            // 根据包名跳转到系统自带的应用程序信息界面
//                            AppUtils.startDetailsSetting(ImageSelectActivity.this);
//                            ImageSelectActivity.this.finish();
//                        }
//                    })
//                    .setPositiveBtn("重新获取权限", new CommonDialog.OnPositiveListener() {
//                        @Override
//                        public void onPositive(View view) {
//                            PermissionCheck(0);
//                        }
//                    })
//                    .setNegativeBtn("取消", new CommonDialog.OnNegativeListener() {
//                        @Override
//                        public void onNegative(View view) {
//                            ImageSelectActivity.this.finish();
//                        }
//                    }, false).show();

            new CommonDialog(this).builder()
                    .setTitle(getString(R.string.activity_image_system_tip)).setContentMsg(getString(R.string.activity_image_system_content))

                    .setPositiveBtn(getString(R.string.activity_image_system_open), new CommonDialog.OnPositiveListener() {
                        @Override
                        public void onPositive(View view) {
                            // 根据包名跳转到系统自带的应用程序信息界面
                            AppUtils.startDetailsSetting(ImageSelectActivity.this);
                        }
                    })
                    .setNegativeBtn(getString(R.string.activity_image_system_cancel), new CommonDialog.OnNegativeListener() {
                        @Override
                        public void onNegative(View view) {
                            ImageSelectActivity.this.finish();
                        }
                    }, false).show();

        } else {
            if (requestCode == 0) {
                startCamera();  //启动相机
            } else {
                getImageFromAlbum(); //启动相册
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // 质量压缩方法
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

        return bitmap;
    }


    public static String saveFile(Bitmap bitmap, String imageName) {
        File file1 = new File(getDirectory());
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String imagename = System.currentTimeMillis() + imageName + ".jpg";
        File file = new File(file1, imagename);
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getPath();
    }


    /**
     * 获得缓存目录
     **/
    public static String getDirectory() {
        String dir = getSDPath() + "/" + CACHDIR;
        return dir;
    }

    /**
     * 取SD卡路径
     **/
    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }


    /**
     * MIUI系统的相册选择
     *
     * @param data
     */
    private String setPhotoForMiuiSystem(Intent data) {
        Uri localUri = data.getData();
        String scheme = localUri.getScheme();
        String imagePath = "";
        if ("content".equals(scheme)) {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            c.close();
        } else if ("file".equals(scheme)) {//小米4选择云相册中的图片是根据此方法获得路径
            imagePath = localUri.getPath();
        }
        return imagePath;
    }

    /**
     * 保存到系统相册
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(final Context context, final Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), IMAGE_URL);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            ToastUtil.show(context ,"图片已保存至"+ IMAGE_URL);
        } catch (FileNotFoundException e) {
            ToastUtil.show(context ,"保存失败");
            e.printStackTrace();
        }

        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }


    /**
     * 处理旋转后的图片
     * @param originpath 原图路径
     * @param context 上下文
     * @return 返回修复完毕后的图片路径
     */
    public String amendRotatePhoto(String originpath, Bitmap bmp,Context context) {

        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);

        // 把原图压缩后得到Bitmap对象
//        Bitmap bmp = getCompressPhoto(originpath);;

        // 修复图片被旋转的角度
        Bitmap bitmap = rotaingImageView(angle, bmp);

        // 保存修复后的图片并返回保存后的图片路径
        return savePhotoToSD(bitmap, context);
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public String savePhotoToSD(Bitmap mbitmap, Context context) {
        FileOutputStream outStream = null;
//        String fileName = getPhotoFileName(context);
        try {
            outStream = new FileOutputStream(imageUrl.getPath());
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            return imageUrl.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }



}
