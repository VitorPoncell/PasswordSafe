package com.poturno.vitor.passwordsafe.controler;

import android.util.Log;

import com.poturno.vitor.passwordsafe.database.LogDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vitor_f3o2ns6 on 21/11/2017.
 */

public class LogController {

    LogDatabase logDatabase;
    String mensage;

    public void addKey(String userId, String keyName){
        logDatabase = new LogDatabase();
        mensage = "addKey: "+keyName+getDate();
        logDatabase.addLog(userId,mensage);
    }

    public void removeKey(String userId, String keyName){
        logDatabase = new LogDatabase();
        mensage = "removeKey: "+keyName+getDate();
        logDatabase.addLog(userId,mensage);
    }

    public void addUser(String userId){
        logDatabase = new LogDatabase();
        mensage = "newUser: " + getDate();
        logDatabase.addLog(userId,mensage);
    }

    public void errorAddUser(String userId){
        logDatabase = new LogDatabase();
        mensage = "errorDuplicateSignup: " + getDate();
        logDatabase.addLog(userId,mensage);
    }

    public void loginSuccess(String userId){
        logDatabase = new LogDatabase();
        mensage = "loginSuccess: " + getDate();
        logDatabase.addLog(userId,mensage);
    }

    public void loginfailed(String userId){
        logDatabase = new LogDatabase();
        mensage = "loginFailed: " + getDate();
        logDatabase.addLog(userId,mensage);
    }

    public void loginEmailNotFound(String email){
        logDatabase = new LogDatabase();
        mensage = "loginEmailNotFound: " + email + getDate();
        logDatabase.addLog("notFound",mensage);
    }

    public void recoverSuccess(String userId){
        logDatabase = new LogDatabase();
        mensage = "recoverSuccess" + getDate();
        logDatabase.addLog(userId,mensage);
    }

    public void revoverFailed(String userId){
        logDatabase = new LogDatabase();
        mensage = "recoverFailed" + getDate();
        logDatabase.addLog(userId,mensage);
    }

    public void recoverEmailNotfound(String email){
        logDatabase = new LogDatabase();
        mensage = "recoverEmailNotFound: " + email + getDate();
        logDatabase.addLog("notFound",mensage);
    }

    private String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(" 'on' yyyy.MM.dd 'at' HH.mm.ss 'GTM 'Z");
        Date date = Calendar.getInstance().getTime();
        return dateFormat.format(date);
    }

}
