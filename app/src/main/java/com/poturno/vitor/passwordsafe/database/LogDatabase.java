package com.poturno.vitor.passwordsafe.database;

import com.google.firebase.database.DatabaseReference;


/**
 * Created by vitor on 21/11/2017.
 */

public class LogDatabase {

    DatabaseReference databaseReference;


    public void addLog(String path,String mensage){
        databaseReference = FirebaseConfig.getDatabaseReference().child("log");
        databaseReference.child(path).push().setValue(mensage);
    }
}
