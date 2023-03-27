package network_resurce;

import android.util.Log;

import com.example.remotecameracontroller.UrlList;

import java.net.HttpURLConnection;
import java.net.URL;

public class ModbusGetInfo {
    final String TAG = "NetworkConnection : ";

    public ModbusGetInfo() {
        try {
            URL url = new URL(new UrlList().modbusConnectionUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
                Log.i(TAG, "GET-MOD-INFO OK");
            else
                Log.i(TAG, "GET-MOD-INFO FAIL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
