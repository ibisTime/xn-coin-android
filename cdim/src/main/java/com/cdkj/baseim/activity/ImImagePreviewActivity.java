package com.cdkj.baseim.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.cdkj.baseim.R;
import com.cdkj.baseim.databinding.ActivityImagePreviewBinding;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.utils.ImgUtils;

import java.io.File;
import java.io.IOException;

/**
 * 聊天图片拍摄预览
 */
public class ImImagePreviewActivity extends AbsBaseActivity {

    private String path;

    private ActivityImagePreviewBinding mBinding;

    public static void open(Activity context, String path, int recode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ImImagePreviewActivity.class);
        intent.putExtra("path", path);
        context.startActivityForResult(intent, recode);
    }

    public static void open(Fragment fragment, String path, int recode) {
        if (fragment == null) {
            return;
        }
        Intent intent = new Intent(fragment.getActivity(), ImImagePreviewActivity.class);
        intent.putExtra("path", path);
        fragment.startActivityForResult(intent, recode);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_image_preview, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle("图片预览");
        setTopLineState(true);
        setSubRightTitleAndClick("发送", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("path", path);
                intent.putExtra("isOri", mBinding.isOri.isChecked());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        path = getIntent().getStringExtra("path");
        showImage();
    }

    private void showImage() {
        if (path.equals("")) return;

        ImgUtils.loadActImg(this, path, mBinding.image);

        File file = new File(path);
        if (file.exists()) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            if (file.length() == 0 && options.outWidth == 0) {
                finish();
                return;
            }
            long fileLength = file.length();
            if (fileLength == 0) {
                fileLength = options.outWidth * options.outHeight / 3;
            }
            int reqWidth, reqHeight, width = options.outWidth, height = options.outHeight;
            if (width > height) {
                reqWidth = getWindowManager().getDefaultDisplay().getWidth();
                reqHeight = (reqWidth * height) / width;
            } else {
                reqHeight = getWindowManager().getDefaultDisplay().getHeight();
                reqWidth = (width * reqHeight) / height;
            }
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            mBinding.isOri.setText("原图发送" + "(" + getFileSize(fileLength) + ")");
            try {
                options.inSampleSize = inSampleSize;
                options.inJustDecodeBounds = false;
                float scaleX = (float) reqWidth / (float) (width / inSampleSize);
                float scaleY = (float) reqHeight / (float) (height / inSampleSize);
                Matrix mat = new Matrix();
                mat.postScale(scaleX, scaleY);
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                ExifInterface ei = new ExifInterface(path);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        mat.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        mat.postRotate(180);
                        break;
                }
//                ImageView imageView = (ImageView) findViewById(R.id.image);
//                imageView.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true));
            } catch (IOException e) {
                Toast.makeText(this, "图片发送失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            finish();
        }
    }

    private String getFileSize(long size) {
        StringBuilder strSize = new StringBuilder();
        if (size < 1024) {
            strSize.append(size).append("B");
        } else if (size < 1024 * 1024) {
            strSize.append(size / 1024).append("K");
        } else {
            strSize.append(size / 1024 / 1024).append("M");
        }
        return strSize.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disMissLoading();
    }
}
