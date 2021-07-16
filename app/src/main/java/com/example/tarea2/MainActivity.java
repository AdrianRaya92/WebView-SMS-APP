package com.example.tarea2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
        Button btConexion;
        Button btMensaje;
        EditText etSMS, etTelefono;
        int duration = Toast.LENGTH_SHORT;
        String strMessage;
        TextView txtNombre;

        WebView myWebView=null;

        //ID permiso de SMS
        static final int ID_PETICION_PERMISO_SEND_SMS = 1111;

        //ID permiso de INTERNET
        static final int ID_PETICION_PERMISO_INTERNET = 1113;

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Botones de la interfaz
        btConexion = (Button) findViewById(R.id.button);
        btMensaje = (Button) findViewById(R.id.button2);

        //Asociar botones
        btConexion.setOnClickListener(this);
        btMensaje.setOnClickListener(this);

        //EditText SMS
        etSMS= findViewById(R.id.etSMS);
        etTelefono= findViewById(R.id.etTelefono);

        //Control WebView
        WebView myWebView = findViewById(R.id.webView);

        //Cliente web por defecto
        myWebView.setWebViewClient(new WebViewClient());

        //Comprobar permiso
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED){
            //Muestra la web
            myWebView.loadUrl("https://www.google.es/");
        }
        //Pedir permiso
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, ID_PETICION_PERMISO_INTERNET);
        }

            //TextView nombre con SharedPreferences
            txtNombre= findViewById(R.id.txtNombre);

            SharedPreferences sp = getSharedPreferences("Almacenamiento", Context.MODE_PRIVATE);
            String nombre = sp.getString("nombre", "Adrián Raya Hernández");
            txtNombre.setText(nombre);
    }

    //Método para enviar SMS
    public void sendSMSbyCode(String number, String text){
        SmsManager smsManager = SmsManager.getDefault();
        //Comprobamos el permiso SEND SMS
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            smsManager.sendTextMessage(number, null, text, null, null);
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},ID_PETICION_PERMISO_SEND_SMS);
        }
    }

    // Leer los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Switch para ver que ID de peticion
        switch (requestCode){
            //Para SMS
            case ID_PETICION_PERMISO_SEND_SMS:
                //GrantResults es un array de permisos
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Si esta concedido realiza la funcion
                    if(!etTelefono.getText().toString().isEmpty() && !etSMS.getText().toString().isEmpty() ) {
                        sendSMSbyCode(etTelefono.getText().toString(), etSMS.getText().toString());
                    }
                    Toast.makeText(this,"Permiso concedido",duration).show();
                }
                else{
                    Toast.makeText(this,"Permiso NO concedido",duration).show();
                }

                break;
            //Para Internet
            case ID_PETICION_PERMISO_INTERNET:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    myWebView.loadUrl("https://www.google.es/");
                    Toast.makeText(this,"Permiso concedido",duration).show();
                }
                else{
                    Toast.makeText(this,"Permiso NO concedido",duration).show();
                }

                break;
        }
    }



        @Override
        public void onClick (View v){
        switch (v.getId()) {
            //boton conexion
            case R.id.button:
                ConnectivityManager cm = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Network[] redes = cm.getAllNetworks();
                    for (Network red : redes) {
                        NetworkInfo redInfo = cm.getNetworkInfo(red);
                        if(isConnected==true){
                            strMessage = "Red " + redInfo.getTypeName() + " está disponible y conectada";
                        }
                    }
                    if(isConnected==false){
                        strMessage = "Red no está disponible";
                    }
                }else {
                    NetworkInfo[] redesInfo = cm.getAllNetworkInfo();
                    for (NetworkInfo redInfo : redesInfo) {
                        if(isConnected==true){
                            strMessage = "Red " + redInfo.getTypeName() + " está disponible y conectada";
                        }
                    }
                    if(isConnected==false){
                        strMessage = "Red no está disponible";
                    }
                }

                Toast toast = Toast.makeText(getApplicationContext(),  strMessage, duration);
                toast.show();
                break;
            case R.id.button2:
                //Si los textos no están vacíos
                if(!etTelefono.getText().toString().isEmpty() && !etSMS.getText().toString().isEmpty() ) {
                    //Llamamos al la funcion de enviar SMS
                    sendSMSbyCode(etTelefono.getText().toString(), etSMS.getText().toString());
                }
                //Si están vacíos
                else{
                    Toast.makeText(this,"Error campos vacios",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}