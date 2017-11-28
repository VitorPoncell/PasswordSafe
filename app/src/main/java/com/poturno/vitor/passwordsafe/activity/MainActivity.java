package com.poturno.vitor.passwordsafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.poturno.vitor.passwordsafe.R;
import com.poturno.vitor.passwordsafe.adapter.KeysAdapter;
import com.poturno.vitor.passwordsafe.controler.LogController;
import com.poturno.vitor.passwordsafe.controler.UserKeysController;
import com.poturno.vitor.passwordsafe.database.IEventListener;
import com.poturno.vitor.passwordsafe.database.IKeysListener;
import com.poturno.vitor.passwordsafe.model.Key;
import com.poturno.vitor.passwordsafe.security.RSA;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView name;
    private ListView listView;
    private ImageView back;
    private Button addKey;
    private ArrayAdapter adapter;
    private ArrayList<Key> keys;
    private UserKeysController userKeys;
    private String userId;
    private String hash;
    private String pubKey;
    private String pvtKey;
    private LogController logController;
    private RSA rsa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (TextView)findViewById(R.id.txt_name);
        listView = (ListView)findViewById(R.id.list_key);
        back = (ImageView)findViewById(R.id.btn_back);
        addKey = (Button)findViewById(R.id.btn_add_key);

        Bundle bundle = getIntent().getExtras();
        name.setText(bundle.getString("name"));
        userId = bundle.getString("id");
        hash = bundle.getString("hash");
        pubKey = bundle.getString("pubKey");
        pvtKey = bundle.getString("pvtKey");

        refreshKeys();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

        addKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addKey();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String keyNameValue = keys.get(position).getKeyName();
                String keyValueValue = keys.get(position).getKeyValue();
                String keySign = keys.get(position).getSign();
                userKeys = new UserKeysController();
                try {
                    keyValueValue = userKeys.getKeyValue(keyValueValue,hash);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
                aleBuilder.setTitle(keyNameValue+" -> "+keyValueValue);
                aleBuilder.setCancelable(false);

                aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                aleBuilder.create();
                aleBuilder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
                aleBuilder.setTitle("Exlcuir chave "+keys.get(position).getKeyName()+" ?");
                aleBuilder.setCancelable(false);

                aleBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userKeys = new UserKeysController();
                        userKeys.removeKey(userId, keys.get(position).getKeyName(), new IEventListener() {
                            @Override
                            public void onComplete() {
                                refreshKeys();
                            }
                        });
                    }
                });

                aleBuilder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                aleBuilder.create();
                aleBuilder.show();

                return true;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        logController = new LogController();
        logController.logout(userId);
    }

    private void refreshKeys(){
        keys = new ArrayList<>();

        userKeys = new UserKeysController();
        userKeys.getKeys(userId, new IKeysListener() {
            @Override
            public void onSucces(ArrayList<Key> objects) {
                keys = objects;
                adapter = new KeysAdapter(MainActivity.this,keys);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this,"Erro no servidor",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addKey(){
        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);

        aleBuilder.setTitle("Nova chave");
        aleBuilder.setCancelable(false);

        final EditText keyName = new EditText(MainActivity.this);
        final EditText keyValue = new EditText(MainActivity.this);
        keyName.setHint("Digite o nome da chave");
        keyName.setTextColor(getResources().getColor(R.color.colorWhite));
        keyName.setHintTextColor(getResources().getColor(R.color.colorWhite));
        keyValue.setHint("Digite a chave");
        keyValue.setTextColor(getResources().getColor(R.color.colorWhite));
        keyValue.setHintTextColor(getResources().getColor(R.color.colorWhite));
        keyValue.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(keyName);
        layout.addView(keyValue);

        aleBuilder.setView(layout);

        aleBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String keyNameValue = keyName.getText().toString();
                String keyValueValue = keyValue.getText().toString();

                if(keyNameValue.isEmpty()||keyValueValue.isEmpty()){
                    Toast.makeText(MainActivity.this,"Preencha ambos os campos", Toast.LENGTH_LONG).show();
                }else{
                    userKeys = new UserKeysController();
                    try {

                        userKeys.addKey(userId, keyNameValue, keyValueValue, hash, rsa.getPvtKey(pvtKey), new IEventListener() {
                            @Override
                            public void onComplete() {
                                refreshKeys();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        aleBuilder.setNegativeButton("Canceclar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        aleBuilder.create();
        aleBuilder.show();
    }

    public void openLogin(){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
