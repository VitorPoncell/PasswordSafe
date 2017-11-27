package com.poturno.vitor.passwordsafe.database;

import com.poturno.vitor.passwordsafe.model.User;

/**
 * Created by vitor on 18/11/2017.
 */

public interface IUserListener {

    public void onSucces(User user);
    public void onError();
}
