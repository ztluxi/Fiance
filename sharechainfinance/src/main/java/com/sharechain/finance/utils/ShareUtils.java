package com.sharechain.finance.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.sharechain.finance.SFApplication;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 分享相关
 * Created by Chu on 2017/12/19.
 */

public class ShareUtils {
    private Context context;
    private IWXAPI iwxapi;
    private static final int THUMB_SIZE = 150;

    public enum SHARE_TARGET_TYPE {
        TYPE_FRIEND, TYPE_CIRCLE
    }

    public ShareUtils(Context activity) {
        this.context = activity;
        iwxapi = WXAPIFactory.createWXAPI(activity, SFApplication.WX_APPID);
    }

    /**
     * 分享文字
     *
     * @param targetType
     * @param text
     */
    public void shareWithText(SHARE_TARGET_TYPE targetType, String text) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction�ֶ�����Ψһ��ʶһ������
        req.message = msg;
        req.scene = getShareScene(targetType);
        iwxapi.sendReq(req);
    }

    /**
     * 分享图片
     *
     * @param targetType
     * @param bitmap
     */
    public void shareWithImage(SHARE_TARGET_TYPE targetType, Bitmap bitmap) {
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = BaseUtils.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = getShareScene(targetType);
        iwxapi.sendReq(req);
    }

    /**
     * 分享网页
     *
     * @param targetType
     * @param bitmap
     * @param webpageUrl
     * @param title
     * @param description
     */
    public void shareWithWeb(SHARE_TARGET_TYPE targetType, Bitmap bitmap, String webpageUrl, String title, String description) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = BaseUtils.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = getShareScene(targetType);
        iwxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private int getShareScene(SHARE_TARGET_TYPE type) {
        if (type == SHARE_TARGET_TYPE.TYPE_FRIEND) {
            return SendMessageToWX.Req.WXSceneSession;
        } else if (type == SHARE_TARGET_TYPE.TYPE_CIRCLE) {
            return SendMessageToWX.Req.WXSceneTimeline;
        }
        return SendMessageToWX.Req.WXSceneSession;
    }

}
