package com.poturno.vitor.passwordsafe.database;

import com.poturno.vitor.passwordsafe.model.User;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vitor on 18/11/2017.
 */

public interface IUserListener {

    public void onSucces(User user)throws InvalidAlgorithmParameterException, NoSuchAlgorithmException;
    public void onError();
}
