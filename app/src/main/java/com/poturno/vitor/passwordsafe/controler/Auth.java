package com.poturno.vitor.passwordsafe.controler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.poturno.vitor.passwordsafe.R;
import com.poturno.vitor.passwordsafe.activity.LoginActivity;
import com.poturno.vitor.passwordsafe.activity.MainActivity;
import com.poturno.vitor.passwordsafe.database.IUserListener;
import com.poturno.vitor.passwordsafe.database.UserDatabase;
import com.poturno.vitor.passwordsafe.helper.Base64Custom;
import com.poturno.vitor.passwordsafe.model.User;

import java.util.Random;

/**
 * Created by vitor on 18/11/2017.
 */

public class Auth {

    private UserDatabase userDatabase;

    public void authenticate(String email, final String password, final Activity activity){
        userDatabase = new UserDatabase();
        userDatabase.getUser(Base64Custom.encodeBase64(email), new IUserListener() {
            @Override
            public void onSucces(User user) {
                if(user!=null && user.getPassword().equals(password)){
                    openMain(user.getId(),user.getName(),user.getPassword(),activity);
                }else {
                    Toast.makeText(activity,"Email ou senha invalidos",Toast.LENGTH_LONG).show();
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
                if(user==null){
                    newUser.setToken(Integer.toString(Math.abs(new Random().nextInt())));
                    userDatabase.setUser(newUser);
                    showToken(newUser.getToken(), activity);
                }else{
                    Toast.makeText(activity,"Usuario ja cadastrado",Toast.LENGTH_LONG).show();
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
                if(user!=null && user.getToken().equals(newUser.getToken())){
                    //recuperar keys
                    newUser.setName(user.getName());
                    userDatabase.setUser(newUser);
                    showRecoverConfirm(newUser.getToken(),activity);
                }else {
                    Toast.makeText(activity,"Email ou token invalidos",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(activity,"Erro no servidor",Toast.LENGTH_LONG).show();
            }
        });
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
        intent.putExtra("password",password);
        activity.startActivity(intent);
        activity.finish();
    }
}
