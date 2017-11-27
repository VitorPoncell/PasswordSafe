package com.poturno.vitor.passwordsafe.controler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.poturno.vitor.passwordsafe.R;
import com.poturno.vitor.passwordsafe.activity.LoginActivity;
import com.poturno.vitor.passwordsafe.activity.MainActivity;
import com.poturno.vitor.passwordsafe.database.IEventListener;
import com.poturno.vitor.passwordsafe.database.IKeysListener;
import com.poturno.vitor.passwordsafe.database.IUserListener;
import com.poturno.vitor.passwordsafe.database.UserDatabase;
import com.poturno.vitor.passwordsafe.helper.Base64Custom;
import com.poturno.vitor.passwordsafe.model.Key;
import com.poturno.vitor.passwordsafe.model.User;
import com.poturno.vitor.passwordsafe.security.AES;
import com.poturno.vitor.passwordsafe.security.Hash;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vitor on 18/11/2017.
 */

public class AuthController {

    private UserDatabase userDatabase;
    private LogController logController;
    private UserKeysController userKeysController;
    private ArrayList<Key> keysArray;
    private ArrayList<String> keysValues;
    private AES aes;

    public void authenticate(final String email, final String password, final Activity activity){
        userDatabase = new UserDatabase();
        userDatabase.getUser(Base64Custom.encodeBase64(email), new IUserListener() {
            @Override
            public void onSucces(User user) {
                logController = new LogController();
                if(user!=null && user.getPassword().equals(password)){
                    openMain(user.getId(),user.getName(),user.getPassword(),activity);
                    logController.loginSuccess(user.getId());
                }else {
                    Toast.makeText(activity,"Email ou senha invalidos",Toast.LENGTH_LONG).show();
                    if (user!=null){
                        logController.loginfailed(user.getId());
                    }else{
                        logController.loginEmailNotFound(email);
                    }
                }
            }

            @Override
            public void onError() {
                Toast.makeText(activity,"Erro no servidor",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void addUser(final User newUser, final Activity activity){
        userDatabase = new UserDatabase();
        userDatabase.getUser(newUser.getId(), new IUserListener() {
            @Override
            public void onSucces(User user) {
                logController = new LogController();
                if(user==null){
                    newUser.setToken(Long.toString(Math.abs(new Random().nextLong())));
                    userDatabase.setUser(newUser);
                    logController.addUser(newUser.getId());
                    showToken(newUser.getToken(), activity);
                }else{
                    Toast.makeText(activity,"Usuario ja cadastrado",Toast.LENGTH_LONG).show();
                    logController.errorAddUser(newUser.getId());
                }
            }

            @Override
            public void onError() {
                Toast.makeText(activity,"Erro no servidor",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void recover(final User newUser, final Activity activity){
        userDatabase = new UserDatabase();
        userDatabase.getUser(Base64Custom.encodeBase64(newUser.getEmail()), new IUserListener() {
            @Override
            public void onSucces(User user) {
                logController = new LogController();
                if(user!=null && user.getToken().equals(newUser.getToken())){

                    recoverKeys(user,newUser);
                    newUser.setName(user.getName());
                    userDatabase.setUser(newUser);
                    showRecoverConfirm(newUser.getToken(),activity);

                    logController.recoverSuccess(user.getId());
                }else {
                    Toast.makeText(activity,"Email ou token invalidos",Toast.LENGTH_LONG).show();
                    if(user!=null){
                        logController.revoverFailed(user.getId());
                    }else {
                        logController.recoverEmailNotfound(newUser.getEmail());
                    }

                }
            }

            @Override
            public void onError() {
                Toast.makeText(activity,"Erro no servidor",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recoverKeys(final User user, final User newUser){
        userKeysController = new UserKeysController();
        userKeysController.getKeys(user.getId(), new IKeysListener() {
            @Override
            public void onSucces(ArrayList<Key> keys){
                keysArray = keys;
                removeKeys(user,newUser);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void removeKeys(final User user, final User newUser){
        for (int i = 0;i<keysArray.size();i++) {
            if(i==keysArray.size()-1){
                userKeysController.removeKey(user.getId(), keysArray.get(i).getKeyName(), new IEventListener() {
                    @Override
                    public void onComplete() {
                        addKeysValues(user,newUser);
                    }
                });
            }else{
                userKeysController.removeKey(user.getId(), keysArray.get(i).getKeyName(), new IEventListener() {
                    @Override
                    public void onComplete() {
                    }
                });
            }
        }
    }

    private void addKeysValues(final User user, final User newUser){
        byte [] encryptionKey = Hash.getAESKey(user.getPassword()).getBytes();
        aes = new AES(encryptionKey);

        for (int i = 0;i<keysArray.size();i++){
            try {
                byte[] decryptedKeyValue = aes.decrypt(Base64Custom.decodeBase64toBytes(keysArray.get(i).getKeyValue()));
                userKeysController.addKey(user.getId(), keysArray.get(i).getKeyName(),new String(decryptedKeyValue), newUser.getPassword(), new IEventListener() {
                    @Override
                    public void onComplete() {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void showToken(String token, final Activity activity){
        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(activity, R.style.AlertDialog);
        aleBuilder.setTitle("Cadastro realizado com sucesso");
        aleBuilder.setMessage("Guarde seu token de recuperacao: " + token);
        aleBuilder.setCancelable(false);
        aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openLogin(activity);
            }
        });

        aleBuilder.create();
        aleBuilder.show();
    }

    private void showRecoverConfirm(String token, final Activity activity){
        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(activity, R.style.AlertDialog);
        aleBuilder.setTitle("Recuperacao eralizada com sucesso");
        aleBuilder.setMessage("Guarde seu token de recuperacao: " + token);
        aleBuilder.setCancelable(false);
        aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openLogin(activity);
            }
        });
        aleBuilder.create();
        aleBuilder.show();
    }

    private void openLogin(Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void openMain(String id,String name,String password,Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        intent.putExtra("hash",password);
        activity.startActivity(intent);
        activity.finish();
    }
}
