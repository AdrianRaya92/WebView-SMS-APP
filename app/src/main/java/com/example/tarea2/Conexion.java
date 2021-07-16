package com.example.tarea2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Conexion extends AppCompatActivity {
    //conexión red
    String strMessage;
    public String getStrNetInfo (NetworkInfo redInfo){
        strMessage = "Red " + redInfo.getTypeName();
        if (redInfo.isAvailable()) {
            strMessage += " está disponible ";
            if (redInfo.isConnected()) {
                strMessage += "y conectada.";
            } else {
                strMessage += "pero no conectada.";
            }
        } else {
            strMessage += "no está disponible.";
        }
        return strMessage;
    }

    public String conexion(){
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] redes = cm.getAllNetworks();
            for (Network red : redes) {
                NetworkInfo redInfo = cm.getNetworkInfo(red);
                this.strMessage = getStrNetInfo(redInfo);
            }
        } else {
            NetworkInfo[] redesInfo = cm.getAllNetworkInfo();
            for (NetworkInfo redInfo : redesInfo) {
                this.strMessage = getStrNetInfo(redInfo);
            }
        }
        return strMessage;
    }
}
