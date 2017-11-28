package com.poturno.vitor.passwordsafe.model;

/**
 * Created by vitor on 18/11/2017.
 */

public class Key {

    private String keyName;
    private String keyValue;
    private String sign;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
