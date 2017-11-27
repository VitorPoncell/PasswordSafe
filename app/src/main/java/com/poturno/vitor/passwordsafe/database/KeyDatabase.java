package com.poturno.vitor.passwordsafe.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.poturno.vitor.passwordsafe.model.Key;

import java.util.ArrayList;

/**
 * Created by vitor on 20/11/2017.
 */

public class KeyDatabase {

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private Key key;
    private ArrayList<Key> keys;

    public void getKeys(String userId, final IKeysListener listener){
        keys = new ArrayList<>();
        databaseReference = FirebaseConfig.getDatabaseReference().child("keys").child(userId);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    key = data.getValue(Key.class);
                    keys.add(key);
                }
                listener.onSucces(keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError();
            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }

    public void setKey(String userId, String keyName, String keyValue, final IEventListener listener){
        key = new Key();
        key.setKeyName(keyName);
        key.setKeyValue(keyValue);
        databaseReference = FirebaseConfig.getDatabaseReference().child("keys");
        databaseReference.child(userId).push().setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete();
            }
        });
    }

    public void deleteKey(String userId, final String keyName, final IEventListener listener){
        databaseReference = FirebaseConfig.getDatabaseReference().child("keys").child(userId);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    key = data.getValue(Key.class);
                    if(key.getKeyName().equals(keyName)){
                        Log.i("remove",keyName);
                        DatabaseReference removeReference = data.getRef();
                        removeReference.removeValue();
                        listener.onComplete();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }

}
