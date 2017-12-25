package com.sharechain.finance.utils;

import android.os.Handler;

import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

/**
 * Created by ${zhoutao} on 2017/12/25 0025.
 */

public class TranslateUtils {

    private Translator translator;
    Handler handler = new Handler();

    private void query(String input) {
//        showLoadingView("正在查询");
        // 源语言或者目标语言其中之一必须为中文,目前只支持中文与其他几个语种的互译
        String to = "中文";
        String from = "英文";

        Language langFrom = LanguageUtils.getLangByName(from);
        // 若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        // Language langFrosm = LanguageUtils.getLangByName("自动");

        Language langTo = LanguageUtils.getLangByName(to);

        TranslateParameters tps = new TranslateParameters.Builder()
                .source("youdao").from(langFrom).to(langTo).timeout(3000).build();// appkey可以省略

        translator = Translator.getInstance(tps);
        translator.lookup(input, "requestId", new TranslateListener() {
            @Override
            public void onResult(final Translate result, String input, String requestId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

//                        TranslateData td = new TranslateData(System.currentTimeMillis(), result);
//                        list.add(td);
//                        trslist.add(result);
//                        adapter.notifyDataSetChanged();
//                        translateList.setSelection(list.size() - 1);
//                        dismissLoadingView();
//                        fanyiInputText.setText("");
//                        imm.hideSoftInputFromWindow(fanyiInputText.getWindowToken(), 0);
                    }
                });
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        ToastManager.showShort(,"查询错误:" + error.name());
//                        dismissLoadingView();
                    }
                });
            }
        });
    }
}
