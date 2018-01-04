package com.weilai.finance.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sharechain.finance.R;
import com.sharechain.finance.SFApplication;
import com.sharechain.finance.bean.BaseNotifyBean;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Chu on 2017/12/19.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, SFApplication.WX_APPID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        if (baseResp instanceof SendAuth.Resp) {
            //登录
            String code;
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    code = ((SendAuth.Resp) baseResp).code;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    code="";
                    break;
                default:
                    code="";
                    break;
            }
            BaseNotifyBean baseNotifyBean = new BaseNotifyBean();
            baseNotifyBean.setType(BaseNotifyBean.TYPE.TYPE_LOGIN_WEIXIN);
            baseNotifyBean.setMessage(code);
            EventBus.getDefault().post(baseNotifyBean);
        } else {
            //分享
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = getString(R.string.share_success);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = getString(R.string.share_cancel);
                    break;
                default:
                    result = getString(R.string.share_failed);
                    break;
            }
            BaseNotifyBean baseNotifyBean = new BaseNotifyBean();
            baseNotifyBean.setType(BaseNotifyBean.TYPE.TYPE_SHARE_RESULT);
            baseNotifyBean.setMessage(result);
            EventBus.getDefault().post(baseNotifyBean);
        }
        finish();
    }

}
