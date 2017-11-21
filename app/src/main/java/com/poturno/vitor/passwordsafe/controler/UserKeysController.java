package com.poturno.vitor.passwordsafe.controler;

import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.poturno.vitor.passwordsafe.database.IEventListener;
import com.poturno.vitor.passwordsafe.database.IKeysListener;
import com.poturno.vitor.passwordsafe.database.KeyDatabase;
import com.poturno.vitor.passwordsafe.model.Key;

import java.util.ArrayList;

/**
 * Created by vitor on 20/11/2017.
 */

public class UserKeysController {

    private KeyDatabase keyDatabase;
    private LogController logController;

    public void getKeys(String userId, IKeysListener listener){
        keyDatabase = new KeyDatabase();
        keyDatabase.getKeys(userId,listener);

    }

    public void addKey(String userId, String keyName, String keyValue, IEventListener listener){
        keyDatabase = new KeyDatabase();
        logController = new LogController();
        keyDatabase.setKey(userId,keyName,keyValue,listener);
        logController.addKey(userId,keyName);

    }

    public void  removeKey(String userId,String keyName, IEventListener listener){
        keyDatabase = new KeyDatabase();
        logController = new LogController();
        keyDatabase.deleteKey(userId,keyName,listener);
        logController.removeKey(userId,keyName);
    }


}
