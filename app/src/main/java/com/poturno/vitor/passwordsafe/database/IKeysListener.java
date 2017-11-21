package com.poturno.vitor.passwordsafe.database;

import com.poturno.vitor.passwordsafe.model.Key;

import java.util.ArrayList;

/**
 * Created by vitor on 20/11/2017.
 */

public interface IKeysListener {

    public void onSucces(ArrayList<Key> keys);
    public void onError();
}
