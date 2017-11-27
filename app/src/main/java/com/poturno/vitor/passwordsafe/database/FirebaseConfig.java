package com.poturno.vitor.passwordsafe.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vitor on 18/11/2017.
 */

public class FirebaseConfig {

    private static DatabaseReference databaseReference;

    public static DatabaseReference getDatabaseReference(){
        if(databaseReference==null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
}
