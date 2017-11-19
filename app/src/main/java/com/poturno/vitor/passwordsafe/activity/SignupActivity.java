package com.poturno.vitor.passwordsafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.poturno.vitor.passwordsafe.R;
import com.poturno.vitor.passwordsafe.controler.Auth;
import com.poturno.vitor.passwordsafe.helper.Base64Custom;
import com.poturno.vitor.passwordsafe.model.User;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password0;
    private EditText password1;
    private Button signup;
    private ImageView back;
    private User user;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText)findViewById(R.id.edit_signup_name);
        email = (EditText)findViewById(R.id.edit_signup_email);
        password0 = (EditText)findViewById(R.id.edit_signup_password0);
        password1 = (EditText)findViewById(R.id.edit_signup_password1);
        signup = (Button)findViewById(R.id.btn_signup);
        back = (ImageView)findViewById(R.id.btn_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameValue = name.getText().toString();
                String emailValue = email.getText().toString();
                String password0Value = password0.getText().toString();
                String password1Value = password1.getText().toString();
                if(nameValue.isEmpty()||
                        emailValue.isEmpty()||
                        password0Value.isEmpty()||
                        password1Value.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Todos os campos sao obrigatorios",Toast.LENGTH_LONG).show();
                }else {
                    if (password0Value.equals(password1Value)){
                        auth = new Auth();
                        user = new User();
                        user.setId(Base64Custom.encodeBase64(emailValue));
                        user.setName(nameValue);
                        user.setEmail(emailValue);
                        user.setPassword(password0Value);
                        auth.addUser(user, SignupActivity.this);
                    }else {
                        Toast.makeText(SignupActivity.this,"As senhas devem ser iguais",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    private void openLogin(){
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
