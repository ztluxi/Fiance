package com.sharechain.finance.module.mine;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.sharechain.finance.BaseActivity;
import com.sharechain.finance.R;
import com.sharechain.finance.utils.GlideUtils;
import com.sharechain.finance.view.dialog.ChoosePhotoDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${zhoutao} on 2017/12/19 0013.
 */

public class PersonalCenterActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private Dialog mPickPicDialog = null;
    TextView take_photo;
    TextView choose_photo;
    TextView cancel_photo;
    @BindView(R.id.image_title_left)
    ImageView back_Image;
    @BindView(R.id.icon_iv)
    ImageView user_icon_iv;

    @BindView(R.id.text_title)
    TextView text_title;
    private File tempFile;
    private TakePhoto takePhoto;
    public String mHeaderAbsolutePath;
    public final static int mMessageFlag = 0x1010;
    private InvokeParam invokeParam;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case mMessageFlag:
                    Glide.with(PersonalCenterActivity.this).load(((File) msg.obj)).apply(new RequestOptions().circleCrop()).into(user_icon_iv);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void takeOrPickPhoto(boolean isTakePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        TakePhoto takePhoto = getTakePhoto();

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);

        if (isTakePhoto) {//拍照
            if (true) {//是否裁剪
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
            } else {
                takePhoto.onPickFromCapture(imageUri);
            }
        } else {//选取图片
            int limit = 1;//选择图片的个数。
            if (limit > 1) {
                //当选择图片大于1个时是否裁剪
                if (true) {
                    takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
                } else {
                    takePhoto.onPickMultiple(limit);
                }
                return;
            }
            //是否从文件中选取图片
            if (false) {
                if (true) {//是否裁剪
                    takePhoto.onPickFromDocumentsWithCrop(imageUri, getCropOptions());
                } else {
                    takePhoto.onPickFromDocuments();
                }
                return;
            } else {
                if (true) {//是否裁剪
                    takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                } else {
                    takePhoto.onPickFromGallery();
                }
            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.personal_center));
        back_Image.setVisibility(View.VISIBLE);
        createPickPicDialog();
        RequestOptions options = new RequestOptions().circleCrop();
        options.placeholder(R.drawable.history);
        GlideUtils.getInstance().loadUserImage(this,"http://img4.duitang.com/uploads/item/201208/17/20120817123857_NnPNB.thumb.600_0.jpeg",user_icon_iv,options);

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.image_title_left, R.id.icon_rl, R.id.nick_name_tv, R.id.sex_tv, R.id.personal_introductions_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_title_left:
                finish();
                break;
            case R.id.icon_rl:
                mPickPicDialog.show();
                break;
            case R.id.nick_name_tv:

                break;
            case R.id.sex_tv:

                break;
            case R.id.personal_introductions_tv:

                break;
        }
    }


    /***
     * 创建底部弹出调用相机和相册对话框
     */
    protected void createPickPicDialog() {
        if (mPickPicDialog == null) {
            Dialog dialog = new ChoosePhotoDialog(this).createBottomDialog(this, R.layout.dialog_photo_pick_layout);
            take_photo = dialog.findViewById(R.id.dialog_take_photo);
            choose_photo = dialog.findViewById(R.id.dialog_choose_phote);
            cancel_photo = dialog.findViewById(R.id.dialog_cancel);
            mPickPicDialog = dialog;
        }
        initPhotoListen();
    }


    /**
     * 拍照相关的配置
     *
     * @param takePhoto
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //是否使用Takephoto自带的相册
        if (false) {
            builder.setWithOwnGallery(true);
        }
        //纠正拍照的旋转角度
        if (true) {
            builder.setCorrectImage(true);
        }
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * 配置 压缩选项
     *
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 102400;
        int width = 800;
        int height = 800;
        //是否显示 压缩进度条
        boolean showProgressBar = true;
        //压缩后是否保存原图
        boolean enableRawFile = true;
        CompressConfig config;
        if (false) {
            //使用自带的压缩工具
            config = new CompressConfig.Builder()
                    .setMaxSize(maxSize)
                    .setMaxPixel(width >= height ? width : height)
                    .enableReserveRaw(enableRawFile)
                    .create();
        } else {
            //使用开源的鲁班压缩工具
            LubanOptions option = new LubanOptions.Builder()
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(maxSize)
                    .create();
            config = CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);
        }
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    /**
     * 配置 裁剪选项
     *
     * @return
     */
    private CropOptions getCropOptions() {
        int height = 100;
        int width = 100;

        CropOptions.Builder builder = new CropOptions.Builder();

        //按照宽高比例裁剪
        builder.setAspectX(width).setAspectY(height);
        //是否使用Takephoto自带的裁剪工具
        builder.setWithOwnCrop(false);
        return builder.create();
    }


    private void initPhotoListen() {
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOrPickPhoto(true);
            }
        });
        choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOrPickPhoto(false);
            }
        });
        cancel_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPickPicDialog();
            }
        });
    }

    protected void dismissPickPicDialog() {
        if (mPickPicDialog != null && mPickPicDialog.isShowing()) {
            mPickPicDialog.dismiss();
        }
    }


    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //添加头像
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }


    @Override
    public void takeSuccess(TResult result) {
        dismissPickPicDialog();
        File file = new File(result.getImages().get(0).getCompressPath());
        mHeaderAbsolutePath = file.getAbsolutePath();
        //需要返回到UI线程 刷新头像
        Message msg = mHandler.obtainMessage();
        msg.what = mMessageFlag;
        msg.obj = file;
        mHandler.sendMessage(msg);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }
}
