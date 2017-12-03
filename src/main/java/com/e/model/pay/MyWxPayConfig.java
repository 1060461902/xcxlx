package com.e.model.pay;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 *
 * @author asus
 * @date 2017/11/6
 */
public class MyWxPayConfig implements WXPayConfig {
    ResourceBundle bundle = ResourceBundle.getBundle("weixin");
    private byte[] certData;

    public MyWxPayConfig() throws Exception {
        String certPath = bundle.getString("certPath");
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return bundle.getString("appid");
    }

    @Override
    public String getMchID() {
        return bundle.getString("mch_id");
    }

    @Override
    public String getKey() {
        return bundle.getString("key");
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 30000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 30000;
    }
}
