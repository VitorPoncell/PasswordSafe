package com.poturno.vitor.passwordsafe.controler;

import com.poturno.vitor.passwordsafe.database.IEventListener;
import com.poturno.vitor.passwordsafe.database.IKeysListener;
import com.poturno.vitor.passwordsafe.database.KeyDatabase;
import com.poturno.vitor.passwordsafe.helper.Base64Custom;
import com.poturno.vitor.passwordsafe.security.AES;
import com.poturno.vitor.passwordsafe.security.Hash;


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

    public void addKey(String userId, String keyName, String keyValue,String hash, IEventListener listener) throws Exception {
        keyDatabase = new KeyDatabase();
        logController = new LogController();

        byte[] encryptionKey = Hash.getAESKey(hash).getBytes();
        byte[] plainText = keyValue.getBytes();

        AES aes = new AES(encryptionKey);

        byte[] cipherText = null;

        cipherText = aes.encrypt(plainText);

        keyDatabase.setKey(userId,keyName, Base64Custom.encodeBase64fromBytes(cipherText),listener);
        logController.addKey(userId,keyName);
    }

    public void  removeKey(String userId,String keyName, IEventListener listener){
        keyDatabase = new KeyDatabase();
        logController = new LogController();
        keyDatabase.deleteKey(userId,keyName,listener);
        logController.removeKey(userId,keyName);
    }

    public String getKeyValue(String keyValue,String hash) throws Exception {
        byte[] encryptionKey = Hash.getAESKey(hash).getBytes();
        AES aes = new AES(encryptionKey);
        return new String(aes.decrypt(Base64Custom.decodeBase64toBytes(keyValue)));
    }


}
