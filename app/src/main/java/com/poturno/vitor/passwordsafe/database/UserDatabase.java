package com.poturno.vitor.passwordsafe.database;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.poturno.vitor.passwordsafe.model.User;

/**
 * Created by vitor on 18/11/2017.
 */

public class UserDatabase {

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private User user;

    public void getUser(String userId, final IUserListener listener){
        databaseReference = FirebaseConfig.getDatabaseReference().child("users").child(userId);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                listener.onSucces(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError();
            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    public void setUser(User user){
        databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("users").child(user.getId()).setValue(user);
    }
}
